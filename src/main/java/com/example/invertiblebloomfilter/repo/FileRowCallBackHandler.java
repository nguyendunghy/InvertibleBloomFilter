package com.example.invertiblebloomfilter.repo;

import com.example.invertiblebloomfilter.entity.DBAggIbfData;
import com.example.invertiblebloomfilter.ibf.Cell;
import com.example.invertiblebloomfilter.ibf.InvertibleBloomFilter;
import com.example.invertiblebloomfilter.utils.FileUtils;
import com.example.invertiblebloomfilter.utils.PropertyUtils;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.stereotype.Component;

import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;

import static com.example.invertiblebloomfilter.utils.Constant.SPRING_DATA_FILE_PATH;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Component("FileRowCallBackHandler")
public class FileRowCallBackHandler implements RowCallbackHandler {
    private InvertibleBloomFilter invertibleBloomFilter;

    @Override
    public void processRow(ResultSet rs) throws SQLException {
        File file = new File(SPRING_DATA_FILE_PATH);
        String fileContent = FileUtils.readFile(file.getPath());
        String[] rows = fileContent.split("\n");
        int index = 0;
        for (String row : rows) {
            if (index++ == 0) {
                continue;
            }

            String[] rowData = row.split(",");
            int ibfCellIndex = Integer.parseInt(rowData[0]);
            long[] rowHash = new long[rowData.length - 2];
            for (int i = 1; i < rowData.length - 1; i++) {
                rowHash[i - 1] = Long.parseLong(rowData[i]);
            }
            long count = Long.parseLong(rowData[rowData.length - 1]);
            DBAggIbfData dbAggIbfData = new DBAggIbfData(ibfCellIndex, rowHash, count);
            invertibleBloomFilter.insert(dbAggIbfData);

        }
        System.out.println(invertibleBloomFilter);

    }
}
