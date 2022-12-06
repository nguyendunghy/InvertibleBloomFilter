package com.example.invertiblebloomfilter.repo.impl;

import com.example.invertiblebloomfilter.entity.Ibf;
import com.example.invertiblebloomfilter.repo.IbfRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.example.invertiblebloomfilter.repo.Sql.*;

@Repository
public class IbfRepoImpl implements IbfRepo {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Override
    public void save(Ibf ibf) {
        jdbcTemplate.update(SAVE_IBF_QUERY, ibf.getId(), ibf.getDivisors(),ibf.getKeyLengthSum());
    }

    @Override
    public Ibf get(Long id) {
        List<Ibf> list = jdbcTemplate.query(
                IBF_QUERY,
                (rs, rowNum) ->
                        new Ibf(
                                rs.getLong("ID"),
                                rs.getString("STRING_HASH_NUMBER"),
                                rs.getLong("KEY_LENGTH_SUM")

                        )
        );

        if(list == null || list.isEmpty()){
            return null;
        }

        return list.get(0);
    }

    @Override
    public Long getMaxId() {
        return jdbcTemplate.queryForObject(GET_MAX_IBF_ID_QUERY,Long.class);
    }
}
