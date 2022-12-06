package com.example.invertiblebloomfilter.repo.impl;

import com.example.invertiblebloomfilter.entity.DataTable;
import com.example.invertiblebloomfilter.entity.IbfData;
import com.example.invertiblebloomfilter.ibf.InvertibleBloomFilter;
import com.example.invertiblebloomfilter.repo.IbfDataRepo;
import com.example.invertiblebloomfilter.velocity.VelocityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.example.invertiblebloomfilter.repo.Sql.*;

@Repository
public class IbfDataRepoImpl implements IbfDataRepo {
    @Autowired
    private JdbcTemplate jdbcTemplate;


    @Override
    public void streamIbfData(final InvertibleBloomFilter invertibleBloomFilter) throws Exception {
        try {
            String ibfQuery = VelocityUtils.generateIBFQuery(
                    "invertible_bloom_filter.vm",
                    "IBF_DATA",
                    new String[]{"STRING_COLUMN", "NUMBER_COLUMN", "DATE_COLUMN", "CLOB_COLUMN"},
                    "numberizeHashTableData"
            );
            System.out.println("=====> IBF_QUERY <===== " + ibfQuery);
            jdbcTemplate.query(ibfQuery, resultSet -> {

                while (resultSet.next()) {
                    IbfData row = new IbfData(
                            resultSet.getLong("ROW_HASH_NUMBER"),
                            resultSet.getLong("STRING_COLUMN_HASH_NUMBER"),
                            resultSet.getLong("NUMBER_COLUMN_HASH_NUMBER"),
                            resultSet.getLong("DATE_COLUMN_HASH_NUMBER"),
                            resultSet.getLong("CLOB_COLUMN_HASH_NUMBER")

                    );
                    invertibleBloomFilter.insert(row);
                }

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
                                rs.getLong("ROW_HASH_NUMBER"),
                                rs.getLong("STRING_HASH_NUMBER"),
                                rs.getLong("NUMBER_HASH_NUMBER"),
                                rs.getLong("DATE_HASH_NUMBER"),
                                rs.getLong("CLOB_HASH_NUMBER")

                        )
        );
    }

    @Override
    public List<DataTable> retrieveAllData(String rowHash) {
        String retrieveDataQuery = VelocityUtils.generateIBFQuery(
                "retrieve_data_template.vm",
                "IBF_DATA",
                new String[]{"STRING_COLUMN","NUMBER_COLUMN", "DATE_COLUMN", "CLOB_COLUMN"},
                "selectChangedData"
        );
        System.out.println(retrieveDataQuery);
        return jdbcTemplate.query(retrieveDataQuery, new Object[]{rowHash}, (rs, rowNum) ->
                new DataTable(
                        rs.getLong("ROW_HASH_NUMBER"),
                        rs.getString("STRING_COLUMN"),
                        rs.getLong("NUMBER_COLUMN"),
                        rs.getString("DATE_COLUMN"),
                        rs.getString("CLOB_COLUMN")
                ));
    }

    @Override
    public List<DataTable> retrieveAllHistoryData(String rowHash) {
        String retrieveHistoryDataQuery = VelocityUtils.generateIBFQuery(
                "retrieve_data_template.vm",
                "IBF_DATA_HISTORY",
                new String[]{"STRING_COLUMN","NUMBER_COLUMN", "DATE_COLUMN", "CLOB_COLUMN"},
                "selectChangedData"
        );
        System.out.println(retrieveHistoryDataQuery);

        return jdbcTemplate.query(retrieveHistoryDataQuery, new Object[]{rowHash}, (rs, rowNum) ->
                new DataTable(
                        rs.getLong("ROW_HASH_NUMBER"),
                        rs.getString("STRING_COLUMN"),
                        rs.getLong("NUMBER_COLUMN"),
                        rs.getString("DATE_COLUMN"),
                        rs.getString("CLOB_COLUMN")
                ));
    }

}

