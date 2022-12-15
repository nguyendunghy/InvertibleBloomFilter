package com.example.invertiblebloomfilter.ibf;

import com.example.invertiblebloomfilter.velocity.VelocityUtils;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.ImmutableMap;
import com.google.common.primitives.Ints;
import org.apache.commons.io.IOUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;

import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class OracleIBFQueryBuilder {
    private static final String TEMPLATE_FILENAME = "/integrations/oracle/resources/oracle_ibf.sql.vm";
    private static final String IMPORT_TEMPLATE_FILENAME = "/integrations/oracle/resources/oracle_ibf_importer.sql.vm";


    private static final String FT_BITXOR_IMPL_TYPE_SQL = "/integrations/oracle/resources/FT_BITXOR_IMPL_TYPE.sql";
    private static final String FT_BITXOR_IMPL_BODY_SQL = "/integrations/oracle/resources/FT_BITXOR_IMPL_BODY.sql";
    private static final String FT_BITXOR_FUNCTION_SQL = "/integrations/oracle/resources/FT_BITXOR_FUNCTION.sql";

    private static final String TEST_BITXOR_FUNCTION = "SELECT FT_BITXOR(0) FROM DUAL";

    private static final int RAW_KEY_SIZE = 4;

    private static final String ORACLE_DATE_TIME_FORMAT = "yyyymmddhh24miss";

    public enum IBFType {
        REGULAR, // all columns
        TRANSITIONAL, // all columns minus modified columns
        REPLACEMENT // all columns including modified columns w/default values
    }

    private IBFType ibfType = IBFType.REGULAR;

    private int cellCount;
    private boolean fixedSize;

    private boolean isOracleVersionBelow12;

    private ResizableInvertibleBloomFilter.Sizes ibfSizes;
    private OracleIbfTableInfo ibfTableInfo;

    private Lazy<String> bitXorTypeExpression = new Lazy<>(this::loadCreateBitXorTypeStatement);
    private Lazy<String> bitXorBodyExpression = new Lazy<>(this::loadCreateBitXorBodyStatement);
    private Lazy<String> bitXorFunctionExpression = new Lazy<>(this::loadCreateBitXorFunctionStatement);

    private List<Integer> keyLengths;
    private int sumKeyLengths;
    private OracleColumnInfo[] arrayOfPrimaryKeys;

    public OracleIBFQueryBuilder() {
    }

    public OracleIBFQueryBuilder(OracleIbfTableInfo ibfTableInfo) {
        if (ibfTableInfo.getOracleTableInfo().getPrimaryKeys().isEmpty()) {
            throw new RuntimeException(ibfTableInfo.getTableRef() + " does not have a primary key");
        }

        this.arrayOfPrimaryKeys = new OracleColumnInfo[ibfTableInfo.getOracleTableInfo().getPrimaryKeys().size()];

        int index = 0;
        for (OracleColumnInfo pkColInfo : ibfTableInfo.getOracleTableInfo().getPrimaryKeys()) {
            arrayOfPrimaryKeys[index++] = pkColInfo;
        }

        this.ibfTableInfo = ibfTableInfo;

        keyLengths =
                ibfTableInfo
                        .getOracleTableInfo()
                        .getPrimaryKeys()
                        .stream()
                        .map(colInfo -> OracleIBFQueryBuilder.numberOfColumnsForKey(colInfo))
                        .collect(Collectors.toList());

        sumKeyLengths = keyLengths.stream().reduce(0, Integer::sum).intValue();
    }

    public OracleIBFQueryBuilder setIbfType(IBFType ibfType) {
        this.ibfType = ibfType;
        return this;
    }

    public OracleIBFQueryBuilder setIbfSizes(ResizableInvertibleBloomFilter.Sizes ibfSizes) {
        this.ibfSizes = ibfSizes;
        return this;
    }

    public OracleIBFQueryBuilder setFixedSize(boolean val) {
        fixedSize = val;
        return this;
    }

    public OracleIBFQueryBuilder setIsOracleVerisonBelow12(boolean isOracleVersionBelow12) {
        this.isOracleVersionBelow12 = isOracleVersionBelow12;
        return this;
    }

    public OracleIBFQueryBuilder setCellCount(int cellCount) {
        this.cellCount = cellCount;
        return this;
    }

    public List<Integer> getKeyLengths() {
        return keyLengths;
    }

    public String getCreateBitXorTypeStatement() {
        return bitXorTypeExpression.get();
    }

    public String getTestBitXorFunction() {
        return TEST_BITXOR_FUNCTION;
    }

    private String loadCreateBitXorTypeStatement() throws IOException {
        return resourceToString(FT_BITXOR_IMPL_TYPE_SQL);
    }

    public String getCreateBitXorBodyStatement() {
        return bitXorBodyExpression.get();
    }

    private String loadCreateBitXorBodyStatement() throws IOException {
        return resourceToString(FT_BITXOR_IMPL_BODY_SQL);
    }

    public String getCreateBitXorFunctionStatement() {
        return bitXorFunctionExpression.get();
    }

    private String loadCreateBitXorFunctionStatement() throws IOException {
        return resourceToString(FT_BITXOR_FUNCTION_SQL);
    }

    private String resourceToString(String path) throws IOException {
        return IOUtils.toString(getClass().getResource(path));
    }

    public String buildQuery() {
        return doBuildQuery(templateParameters());
    }

    private String doBuildQuery(Map<String, Object> templateParameters) {
//        return fixedSize
//                ? IbfDbUtils.generateInvertibleBloomFilterQuery(TEMPLATE_FILENAME, cellCount, templateParameters)
//                : IbfDbUtils.generateResizableInvertibleBloomFilterQuery(
//                TEMPLATE_FILENAME,
//                cellCount,
//                Objects.requireNonNull(ibfSizes, "Call setIbfSizes(...) first"),
//                templateParameters);


        String ibfQuery = VelocityUtils.generateIBFQuery(
                "invertible_bloom_filter.vm",
                ibfTableInfo.getTableRef().name,
                this.arrayOfPrimaryKeys,
                "numberizeHashTableData"
        );

        return ibfQuery;
    }

    public String retrieveAllDataQuery() {
        String retrieveDataQuery = VelocityUtils.generateIBFQuery(
                "retrieve_data_template.vm",
                ibfTableInfo.getTableRef().name,
                this.arrayOfPrimaryKeys,
                "selectChangedData"
        );
        return retrieveDataQuery;
    }

    public String retrieveAllHistoryDataQuery() {
        String retrieveDataQuery = VelocityUtils.generateIBFQuery(
                "retrieve_data_template.vm",
                "IBF_DATA_HISTORY",
                this.arrayOfPrimaryKeys,
                "selectChangedData"
        );
        return retrieveDataQuery;
    }

    public static class TemplateHelper {
        public boolean isBinary(OracleColumnInfo columnInfo) {
            return columnInfo.getType() == OracleType.Type.RAW;
        }

        public boolean isDate(OracleColumnInfo columnInfo) {
            return columnInfo.getType() == OracleType.Type.DATE;
        }

        public boolean isDateTime(OracleColumnInfo columnInfo) {
            return columnInfo.getOracleType().isDateTimeLike();
        }

        public boolean isNumber(OracleColumnInfo columnInfo) {
            return columnInfo.getOracleType().isNumber();
        }

        public boolean isString(OracleColumnInfo columnInfo) {
            return columnInfo.getOracleType().isCharacterString();
        }

        public boolean isUnicode(OracleColumnInfo columnInfo) {
            return columnInfo.getOracleType().isUnicodeString();
        }

        public Object escapeDefaultValue(Object entity) {
            if (entity instanceof String) return "\'" + entity + "\'";
            return entity;
        }
    }

    @VisibleForTesting
    Map<String, Object> templateParameters() {
        List<OracleColumnInfo> columns = null;

        switch (ibfType) {
            case REGULAR:
            case REPLACEMENT:
                columns = ibfTableInfo.getOracleTableInfo().getIncomingColumns();
                break;
            case TRANSITIONAL:
                columns = getColumnsWithoutModifiedColumns();
                break;
        }

        ImmutableMap.Builder<String, Object> builder =
                new ImmutableMap.Builder<String, Object>()
                        .put("primaryKeys", arrayOfPrimaryKeys)
                        .put("keyCount", arrayOfPrimaryKeys.length)
                        .put("columns", columns)
                        .put("table", SqlStatementUtils.ORACLE.quote(ibfTableInfo.getTableRef()))
                        .put("keyLength", sumKeyLengths)
                        .put("keyLengths", Ints.toArray(keyLengths))
                        .put("dateNumberFormat", ORACLE_DATE_TIME_FORMAT)
//                        .put("useXOR", FlagName.OracleIbfXOR.check())
//                        .put("fastIbfQuery", FlagName.OracleIbfFastIBFQuery.check())
//                        .put("useConnectorAggregation", FlagName.OracleIbfUseConnectorAggregation.check())
//                        .put(
//                                "useLegacyRowHash",
//                                FlagName.OracleIbfLegacyRowHashing.check() || isOracleVersionBelow12)

                        .put("useXOR", true)
                        .put("fastIbfQuery", true)
                        .put("useConnectorAggregation", true)
                        .put(
                                "useLegacyRowHash",
                                true || isOracleVersionBelow12)
                        .put("moduloDivisor", Long.MAX_VALUE)
                        .put("helper", new TemplateHelper());

        return builder.build();
    }

    public static int numberOfColumnsForKey(OracleColumnInfo keyColumn) {
        if (OracleType.Type.RAW.equals(keyColumn.getType())) {
            return computeKeyLengthBinary(keyColumn.getLength().orElse(IbfTableEncoder.DEFAULT_KEY_LENGTH));
        }
        return IbfDbUtils.computeKeyLength(keyColumn.getLength().orElse(IbfTableEncoder.DEFAULT_KEY_LENGTH));
    }

    public static int computeKeyLengthBinary(long typeParameter) {
        if (typeParameter <= RAW_KEY_SIZE) {
            return IbfTableEncoder.DEFAULT_KEY_LENGTH;
        }
        int keyLength = (int) (typeParameter / RAW_KEY_SIZE);
        if (typeParameter % RAW_KEY_SIZE > 0) keyLength += 1;
        return keyLength;
    }

    @VisibleForTesting
    List<OracleColumnInfo> getColumnsWithoutModifiedColumns() {
        return ibfTableInfo
                .getOracleTableInfo()
                .getIncomingColumns()
                .stream()
                .filter(columnInfo -> !columnInfo.isAddedSinceLastSync())
                .collect(Collectors.toList());
    }

    public int getSumKeyLengths() {
        return sumKeyLengths;
    }

    public static String generate(String vmFilePath, HashMap<String, Object> hashMap) {
        VelocityEngine velocityEngine = new VelocityEngine();
        velocityEngine.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath");
        velocityEngine.setProperty("classpath.resource.loader.class", ClasspathResourceLoader.class.getName());
        velocityEngine.init();

        Template t = velocityEngine.getTemplate(vmFilePath);
        VelocityContext context = new VelocityContext();
        for (String key : hashMap.keySet()) {
            context.put(key, hashMap.get(key));
        }
        StringWriter writer = new StringWriter();
        t.merge(context, writer);

        return writer.toString();
    }
}