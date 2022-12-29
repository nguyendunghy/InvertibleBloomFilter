package com.example.invertiblebloomfilter.repo;

import com.example.invertiblebloomfilter.entity.DBAggIbfData;
import com.example.invertiblebloomfilter.ibf.InvertibleBloomFilter;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Component("DatabaseRowCallBackHandler")
public class DatabaseRowCallBackHandler implements RowCallbackHandler {
    private int numberOfColumns;
    private InvertibleBloomFilter invertibleBloomFilter;

    @Override
    public void processRow(ResultSet resultSet) throws SQLException {
        do {
            int ibfCellIndex = resultSet.getInt("_ibf_cell_index");
            long[] rowHash = new long[numberOfColumns];
            for (int i = 0; i < numberOfColumns; i++) {
                rowHash[i] = resultSet.getLong("_ibf_row_hash_number${i}_xor".replace("${i}", i + ""));
            }

            Long count = resultSet.getLong("count");
            DBAggIbfData row = new DBAggIbfData(ibfCellIndex, rowHash, count);
            invertibleBloomFilter.insert(row);
        } while (resultSet.next());

        System.out.println(invertibleBloomFilter);
    }
}
