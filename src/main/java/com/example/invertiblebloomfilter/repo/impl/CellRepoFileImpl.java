package com.example.invertiblebloomfilter.repo.impl;

import com.example.invertiblebloomfilter.entity.CellEntity;
import com.example.invertiblebloomfilter.repo.CellRepo;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

@Repository
@Qualifier("CellRepoFileImpl")
public class CellRepoFileImpl implements CellRepo {
    private String filePath = "IbfLocal/cell.txt";
    private static ObjectMapper mapper = new ObjectMapper();

    @Override
    public void save(CellEntity cell) {
        try {
            String json = mapper.writeValueAsString(cell);
            File file = new File(filePath);
            FileUtils.write(file, json + "\n", Charset.defaultCharset(), true);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void save(List<CellEntity> cells) {
        try {
            StringBuilder json = new StringBuilder();
            for (CellEntity cell : cells) {
                json.append(mapper.writeValueAsString(cell)).append("\n");
            }
            File file = new File(filePath);
            FileUtils.write(file, json.toString(), Charset.defaultCharset(), false);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public List<CellEntity> findAll(Long ibfId) {
        try {
            File file = new File(filePath);
            String content = FileUtils.readFileToString(file);
            String[] array = content.split("\n");
            List<CellEntity> list = new ArrayList<>();
            for (String json : array) {
                CellEntity cellEntity = mapper.readValue(json, CellEntity.class);
                list.add(cellEntity);
            }
            return list;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public Long getMaxId() {
        return 0L;
    }
}
