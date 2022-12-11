package com.example.invertiblebloomfilter.repo.impl;

import com.example.invertiblebloomfilter.entity.CellEntity;
import com.example.invertiblebloomfilter.repo.CellRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.example.invertiblebloomfilter.repo.Sql.*;

@Repository
public class CellRepoImpl implements CellRepo {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public void save(CellEntity cell) {
        jdbcTemplate.update(SAVE_CELL_QUERY,
                cell.getId(), cell.getIbfId(),
                cell.getCellIndex(), cell.getRowHashSum(),
                cell.getKeySums(), cell.getCount());
    }

    @Override
    public List<CellEntity> findAll(Long ibfId) {
        return jdbcTemplate.query(
                RETRIEVE_CELL_BY_IBF_ID_QUERY, new Object[]{ibfId},
                (rs, rowNum) ->
                        new CellEntity(
                                rs.getLong("ID"),
                                rs.getLong("IBF_ID"),
                                rs.getLong("CELL_INDEX"),
                                rs.getLong("ROW_HASH_SUM"),
                                rs.getString("KEY_SUMS"),
                                rs.getLong("COUNT")

                        )
        );
    }

    @Override
    public Long getMaxId() {
        Long temp = jdbcTemplate.queryForObject(GET_MAX_CELL_ID_QUERY, Long.class);
        return temp == null ? 0L : temp;
    }
}
