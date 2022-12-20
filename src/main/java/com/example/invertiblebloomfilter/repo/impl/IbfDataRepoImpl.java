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
                        "numberizeHashTableData"
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
                        rs.getLong("ROW_HASH_NUMBER"),
                        rs.getString("STRING_COLUMN"),
                        rs.getString("NUMBER_COLUMN"),
                        rs.getString("DATE_COLUMN"),
                        rs.getString("CLOB_COLUMN")
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
                        rs.getLong("ROW_HASH_NUMBER"),
                        rs.getString("STRING_COLUMN"),
                        rs.getString("NUMBER_COLUMN"),
                        rs.getString("DATE_COLUMN"),
                        rs.getString("CLOB_COLUMN")
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

}

