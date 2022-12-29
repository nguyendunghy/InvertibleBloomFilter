package com.example.invertiblebloomfilter.repo.impl;

import com.example.invertiblebloomfilter.entity.IbfEntity;
import com.example.invertiblebloomfilter.ibf.InvertibleBloomFilter;
import com.example.invertiblebloomfilter.repo.IbfRepo;
import com.example.invertiblebloomfilter.utils.StringUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.nio.charset.Charset;

@Repository
@Qualifier("IbfRepoFileImpl")
public class IbfRepoFileImpl implements IbfRepo {
    private static ObjectMapper mapper = new ObjectMapper();

    private static String filePath = "IbfLocal/ibf.txt";
    @Override
    public void save(IbfEntity ibf) {
        try {
            String json = mapper.writeValueAsString(ibf);
            FileUtils.write(new File(filePath), json, Charset.defaultCharset(),false);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public IbfEntity get(Long id) {
        try {
            String json = FileUtils.readFileToString(new File(filePath), Charset.defaultCharset());
            IbfEntity ibfEntity = mapper.readValue(json, IbfEntity.class);
            return ibfEntity;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public Long getMaxId() {
        return 1L;
    }
}
