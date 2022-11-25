package com.example.statrystesting.repo.impl;

import com.example.statrystesting.entity.DataTable;
import com.example.statrystesting.entity.IbfData;
import com.example.statrystesting.ibf.InvertibleBloomFilter;
import com.example.statrystesting.repo.IbfDataRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.example.statrystesting.repo.Sql.IBF_QUERY;
import static com.example.statrystesting.repo.Sql.RETRIEVE_DATA;

@Repository
public class IbfDataRepoImpl implements IbfDataRepo {
    @Autowired
    private JdbcTemplate jdbcTemplate;


    @Override
    public void streamIbfData(final InvertibleBloomFilter invertibleBloomFilter) throws Exception {
        try {
            jdbcTemplate.query(IBF_QUERY, resultSet -> {

                while (resultSet.next()) {
                    IbfData row = new IbfData(
                            resultSet.getLong("ROW_HASH_NUMBER"),
                            resultSet.getLong("STRING_HASH_NUMBER"),
                            resultSet.getLong("NUMBER_HASH_NUMBER"),
                            resultSet.getLong("DATE_HASH_NUMBER"),
                            resultSet.getLong("CLOB_HASH_NUMBER")

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
        return jdbcTemplate.query(RETRIEVE_DATA, new Object[]{rowHash}, (rs, rowNum) ->
                new DataTable(
                        rs.getLong("ROW_HASH_NUMBER"),
                        rs.getString("STRING_COLUMN"),
                        rs.getLong("NUMBER_COLUMN"),
                        rs.getDate("DATE_COLUMN"),
                        rs.getString("CLOB_COLUMN")
                ));
    }


}

