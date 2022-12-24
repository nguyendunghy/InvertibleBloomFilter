package com.example.invertiblebloomfilter.velocity;

import com.example.invertiblebloomfilter.ibf.*;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public class VelocityUtilsTest {
    @Test
    void testGenerateRetrieveQuery() {
        String retrieveDataQuery = VelocityUtils.generateIBFQuery(
                "retrieve_data_template.vm",
                "IBF_DATA",
                new String[]{"STRING_COLUMN", "NUMBER_COLUMN", "DATE_COLUMN", "CLOB_COLUMN"},
                "selectChangedData"
        );
        System.out.println(retrieveDataQuery);
        Assert.assertNotNull(retrieveDataQuery);
    }

    @Test
    void testGenerateRetrieveQueryWithOracleColumnInfo() {
        TableRef tableRef = new TableRef("JOHN", "IBF_DATA");
        List<OracleColumnInfo> columns = buildColumns(tableRef);
        String retrieveDataQuery = VelocityUtils.generateIBFQuery(
                "retrieve_data_template.vm",
                "IBF_DATA",
                columns.toArray(new OracleColumnInfo[]{}),
                "selectChangedData"
        );
        System.out.println(retrieveDataQuery);
        Assert.assertNotNull(retrieveDataQuery);
    }


    @Test
    void testGenerateIbfQuery() {
        String ibfQuery = VelocityUtils.generateIBFQuery(
                "invertible_bloom_filter.vm",
                "IBF_DATA",
                new String[]{"STRING_COLUMN", "NUMBER_COLUMN", "DATE_COLUMN", "CLOB_COLUMN"},
                "numberizeHashTableData"
        );
        System.out.println(ibfQuery);
        Assert.assertNotNull(ibfQuery);
    }

    @Test
    void testGenerateIbfQueryWithOracleColumnInfo() {
        TableRef tableRef = new TableRef("JOHN", "IBF_DATA");
        List<OracleColumnInfo> columns = buildColumns(tableRef);

        String ibfQuery = VelocityUtils.generateIBFQuery(
                "invertible_bloom_filter.vm",
                "IBF_DATA",
                columns.toArray(new OracleColumnInfo[]{}),
                "numberizeHashTableData"
        );
        System.out.println(ibfQuery);
        Assert.assertNotNull(ibfQuery);
    }

    @Test
    void testOracleIbfSqlGeneration() {
        TableRef tableRef = new TableRef("JOHN", "IBF_DATA");
        OracleColumnInfo[] columns = buildColumns(tableRef).toArray(new OracleColumnInfo[]{});
        String ibfQuery = VelocityUtils.generateIBFQuery("oracle_ibf.sql.vm", tableRef,
                columns, new long[]{37, 41, 47},"invertibleBloomFilter");

        System.out.println(ibfQuery);
        Assert.assertNotNull(ibfQuery);
    }


    @Test
    void testOracleRetrieveSqlGeneration() {
        TableRef tableRef = new TableRef("JOHN", "IBF_DATA");
        OracleColumnInfo[] columns = buildColumns(tableRef).toArray(new OracleColumnInfo[]{});
        String ibfQuery = VelocityUtils.generateIBFQuery("oracle_ibf.sql.vm", tableRef,
                columns, new long[]{37, 41, 47},"retrieveData");

        System.out.println(ibfQuery);
        Assert.assertNotNull(ibfQuery);
    }

    private List<OracleColumnInfo> buildColumns(TableRef tableRef) {

        Column stringColumn = new Column("STRING_COLUMN", DataType.String, true);
        Column numberColumn = new Column("NUMBER_COLUMN", DataType.Long, true);
        Column dateColumn = new Column("DATE_COLUMN", DataType.LocalDate, true);
        Column clobColumn = new Column("CLOB_COLUMN", DataType.String, true);

        OracleColumn oracleStringColumn = new OracleColumn("STRING_COLUMN", OracleType.create("VARCHAR"), true, tableRef, Optional.empty());
        oracleStringColumn.setDataDefault(Optional.of("|"));
        OracleColumn oracleNumberColumn = new OracleColumn("NUMBER_COLUMN", OracleType.create("NUMBER"), true, tableRef, Optional.empty());
        oracleNumberColumn.setDataDefault(Optional.of("0"));
        OracleColumn oracleDateColumn = new OracleColumn("DATE_COLUMN", OracleType.create("DATE"), true, tableRef, Optional.empty());
        oracleDateColumn.setDataDefault(Optional.of("15-12-2022"));
        OracleColumn oracleClobColumn = new OracleColumn("CLOB_COLUMN", OracleType.create("CLOB", true), true, tableRef, Optional.empty());
        oracleDateColumn.setDataDefault(Optional.of("|"));

        OracleColumnInfo stringOracleColumnInfo = new OracleColumnInfo(oracleStringColumn, stringColumn);
        stringOracleColumnInfo.parseAndSetDataDefaultExpression();
        stringOracleColumnInfo.setAddedSinceLastSync(false);

        OracleColumnInfo numberOracleColumnInfo = new OracleColumnInfo(oracleNumberColumn, numberColumn);
        numberOracleColumnInfo.setAddedSinceLastSync(false);
        numberOracleColumnInfo.parseAndSetDataDefaultExpression();

        OracleColumnInfo dateOracleColumnInfo = new OracleColumnInfo(oracleDateColumn, dateColumn);
        dateOracleColumnInfo.setAddedSinceLastSync(false);
        dateOracleColumnInfo.parseAndSetDataDefaultExpression();

        OracleColumnInfo clobOracleColumnInfo = new OracleColumnInfo(oracleClobColumn, clobColumn);
        clobOracleColumnInfo.setAddedSinceLastSync(false);
        clobOracleColumnInfo.parseAndSetDataDefaultExpression();

        return Arrays.asList(stringOracleColumnInfo, numberOracleColumnInfo, dateOracleColumnInfo, clobOracleColumnInfo);

    }
}
