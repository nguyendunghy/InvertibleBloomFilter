package com.example.invertiblebloomfilter.velocity;

import com.example.invertiblebloomfilter.ibf.*;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;

import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

public class VelocityUtils {

    public static String generateIBFQuery(String templateFilename,TableRef tableRef, OracleColumnInfo[] columns){
        HashMap<String,Object> hashMap = new HashMap<>();
        hashMap.put("tableColumns", columns);
        hashMap.put("table", tableRef.name);
        hashMap.put("helper", new OracleIBFQueryBuilder.TemplateHelper());
        long[] divisors = OneHashingBloomFilterUtils.primeDivisors(100);
        hashMap.put("primeDivisors", divisors);
        hashMap.put("partitionOffsets", OneHashingBloomFilterUtils.partitionOffsets(divisors));
        hashMap.put("dateNumberFormat", "DD-MM-YYYY");
        hashMap.put("useConnectorAggregation", false);
        hashMap.put("useXOR", false);
        hashMap.put("useLegacyRowHash", true);
        hashMap.put("fastIbfQuery", true);
        hashMap.put("oracleVersion", 12);
        hashMap.put("output", "#invertibleBloomFilter()");

        return VelocityUtils.generate(templateFilename, hashMap);
    }

    public static String generateIBFQuery(String templateFilename, String tableName, OracleColumnInfo[] columnNames,
                                          String outputFunction) {
        HashMap<String,Object> hashMap = new HashMap<>();
        hashMap.put("dataTableName",tableName);
        hashMap.put("columnNames",columnNames);
        hashMap.put("helper", new OracleIBFQueryBuilder.TemplateHelper());
        hashMap.put("dateNumberFormat","DD-MM-YYYY");
        hashMap.put("output", "#" + outputFunction + "()");

        return VelocityUtils.generate(templateFilename, hashMap);
    }

    public static String generateIBFQuery(String templateFilename, String tableName, String[] columnNames, String outputFunction) {
        HashMap<String,Object> hashMap = new HashMap<>();
        hashMap.put("dataTableName",tableName);
        hashMap.put("columnNames",columnNames);
        hashMap.put("output", "#" + outputFunction + "()");

        return VelocityUtils.generate(templateFilename, hashMap);
    }
    public static String getInvertibleBloomFilterTemplate(String templateFilename, long[] divisors,
                                                           String macroName, Map<String, Object> templateParams) {
        HashMap<String,Object> hashMap = new HashMap<>();
        hashMap.putAll(templateParams);
        hashMap.put("cellsCount", OneHashingBloomFilterUtils.totalCellCount(divisors));
        hashMap.put("primeDivisors", divisors);
        hashMap.put("partitionOffsets", OneHashingBloomFilterUtils.partitionOffsets(divisors));
        hashMap.put("moduloDivisor",11);//TODO: understand this variable
        hashMap.put("table","ibf_data");
        hashMap.put("useLegacyRowHash",true);
        hashMap.put("keyLength",3);
        hashMap.put("keyCount",3);
        hashMap.put("output", "#" + macroName + "()");

        return generate(templateFilename, hashMap);
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

    public class TemplateHelper {
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
}
