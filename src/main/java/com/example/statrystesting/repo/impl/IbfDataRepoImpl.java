package com.example.statrystesting.repo.impl;

import com.example.statrystesting.entity.IbfData;
import com.example.statrystesting.ibf.InvertibleBloomFilter;
import com.example.statrystesting.repo.IbfDataRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.example.statrystesting.repo.Sql.IBF_QUERY;

@Repository
public class IbfDataRepoImpl implements IbfDataRepo {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    private static InvertibleBloomFilter invertibleBloomFilter = new InvertibleBloomFilter(4, 100);

    @Override
    public void streamIbfData() throws Exception {
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

    public InvertibleBloomFilter getInvertibleBloomFilter() {
        return this.invertibleBloomFilter;
    }

}

