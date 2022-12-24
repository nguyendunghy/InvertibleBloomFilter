package com.example.invertiblebloomfilter.repo.impl;

import com.example.invertiblebloomfilter.entity.DBAggIbfData;
import com.example.invertiblebloomfilter.entity.DataTable;
import com.example.invertiblebloomfilter.entity.IbfData;
import com.example.invertiblebloomfilter.ibf.*;
import com.example.invertiblebloomfilter.repo.IbfDataRepo;
import com.example.invertiblebloomfilter.velocity.VelocityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static com.example.invertiblebloomfilter.repo.Sql.*;

@Repository
public class IbfDataRepoImpl implements IbfDataRepo {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public IbfDataRepoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void streamIbfData(final InvertibleBloomFilter invertibleBloomFilter, String... query) throws Exception {
        try {
            String ibfQuery;
            if (query != null && query.length != 0) {
                ibfQuery = query[0];
            } else {
                ibfQuery = VelocityUtils.generateIBFQuery(
                        "invertible_bloom_filter.vm",
                        "IBF_DATA",
                        new String[]{"STRING_COLUMN", "NUMBER_COLUMN", "DATE_COLUMN", "CLOB_COLUMN"},
                        "hashTableData"
                );
            }

//            System.out.println("=====> IBF_QUERY <===== " + ibfQuery);
            jdbcTemplate.query(ibfQuery, resultSet -> {

                do {
                    IbfData row = new IbfData(
                            resultSet.getString("ROWHASH")

                    );
                    invertibleBloomFilter.insert(row);
                } while (resultSet.next());

                System.out.println(invertibleBloomFilter);
            });

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void streamDbAggIbfData(InvertibleBloomFilter invertibleBloomFilter, String... query) throws Exception {
        try {
            String ibfQuery;
            if (query != null && query.length != 0) {
                ibfQuery = query[0];
            } else {
                TableRef tableRef = new TableRef("JOHN", "IBF_DATA");
                OracleColumnInfo[] columns = buildColumns(tableRef).toArray(new OracleColumnInfo[]{});
                ibfQuery = VelocityUtils.generateIBFQuery("oracle_ibf.sql.vm", tableRef, columns,
                        invertibleBloomFilter.getDivisors(), "invertibleBloomFilter");
            }

            jdbcTemplate.query(ibfQuery, resultSet -> {

                do {
                    DBAggIbfData row = new DBAggIbfData(
                            resultSet.getInt("_ibf_cell_index"),
                            resultSet.getLong("_ibf_row_hash_number"),
                            resultSet.getLong("count")
                    );
                    invertibleBloomFilter.insert(row);
                } while (resultSet.next());

                System.out.println(invertibleBloomFilter);
            });

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public List<IbfData> findAll() {
        return jdbcTemplate.query(
                IBF_QUERY,
                (rs, rowNum) ->
                        new IbfData(
                                rs.getString("ROWHASH"))
        );
    }

    @Override
    public List<DataTable> retrieveAllData(String rowHash, String... query) {
        String retrieveDataQuery;
        if (query != null && query.length != 0) {
            retrieveDataQuery = query[0];
        } else {
            retrieveDataQuery = VelocityUtils.generateIBFQuery(
                    "retrieve_data_template.vm",
                    "IBF_DATA",
                    new String[]{"STRING_COLUMN", "NUMBER_COLUMN", "DATE_COLUMN", "CLOB_COLUMN"},
                    "selectChangedData"
            );
        }
        return jdbcTemplate.query(retrieveDataQuery, new Object[]{rowHash}, (rs, rowNum) ->
                new DataTable(
                        rs.getString("ROWHASH"),
                        rs.getString("STRING_COLUMN"),
                        rs.getString("NUMBER_COLUMN"),
                        rs.getString("DATE_COLUMN"),
                        rs.getString("CLOB_COLUMN")
                ));
    }

    @Override
    public List<DataTable> retrieveDbAggAllData(String rowHash, String... query) {
        String retrieveDataQuery;
        if (query != null && query.length != 0) {
            retrieveDataQuery = query[0];
        } else {
            TableRef tableRef = new TableRef("JOHN", "IBF_DATA");
            OracleColumnInfo[] columns = buildColumns(tableRef).toArray(new OracleColumnInfo[]{});
            retrieveDataQuery = VelocityUtils.generateIBFQuery("oracle_ibf.sql.vm", tableRef,
                    columns, new long[]{}, "retrieveData");
        }
        return jdbcTemplate.query(retrieveDataQuery, new Object[]{rowHash}, (rs, rowNum) ->
                new DataTable(
                        rs.getString("_ibf_row_hash"),
                        rs.getString("_ibf_column0"),
                        rs.getString("_ibf_column1"),
                        rs.getString("_ibf_column2"),
                        rs.getString("_ibf_column3")
                ));
    }

    @Override
    public List<DataTable> retrieveAllHistoryData(String rowHash, String... query) {
        String retrieveHistoryDataQuery;

        if (query != null && query.length != 0) {
            retrieveHistoryDataQuery = query[0];
        } else {
            retrieveHistoryDataQuery = VelocityUtils.generateIBFQuery(
                    "retrieve_data_template.vm",
                    "IBF_DATA_HISTORY",
                    new String[]{"STRING_COLUMN", "NUMBER_COLUMN", "DATE_COLUMN", "CLOB_COLUMN"},
                    "selectChangedData"
            );
        }

        return jdbcTemplate.query(retrieveHistoryDataQuery, new Object[]{rowHash}, (rs, rowNum) ->
                new DataTable(
                        rs.getString("ROWHASH"),
                        rs.getString("STRING_COLUMN"),
                        rs.getString("NUMBER_COLUMN"),
                        rs.getString("DATE_COLUMN"),
                        rs.getString("CLOB_COLUMN")
                ));
    }

    @Override
    public List<DataTable> retrieveDbAggAllHistoryData(String rowHash, String... query) {
        String retrieveHistoryDataQuery;

        if (query != null && query.length != 0) {
            retrieveHistoryDataQuery = query[0];
        } else {
            TableRef tableRef = new TableRef("JOHN", "IBF_DATA_HISTORY");
            OracleColumnInfo[] columns = buildColumns(tableRef).toArray(new OracleColumnInfo[]{});
            retrieveHistoryDataQuery = VelocityUtils.generateIBFQuery("oracle_ibf.sql.vm", tableRef,
                    columns, new long[]{}, "retrieveData");
        }

        return jdbcTemplate.query(retrieveHistoryDataQuery, new Object[]{rowHash}, (rs, rowNum) ->
                new DataTable(
                        rs.getString("_ibf_row_hash"),
                        rs.getString("_ibf_column0"),
                        rs.getString("_ibf_column1"),
                        rs.getString("_ibf_column2"),
                        rs.getString("_ibf_column3")
                ));
    }

    @Override
    public void save(DataTable dataTable) {
        jdbcTemplate.update(SAVE_IBF_DATA,
                dataTable.getStringColumn(), dataTable.getNumberColumn(), dataTable.getDateColumn(), dataTable.getClobColumn());
    }

    @Override
    public void delete(DataTable dataTable) {
        jdbcTemplate.update(DELETE_IBF_DATA,
                dataTable.getStringColumn(), dataTable.getNumberColumn(), dataTable.getDateColumn(), dataTable.getClobColumn());
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

