package com.example.statrystesting.repo;

import com.example.statrystesting.entity.IbfData;
import com.example.statrystesting.ibf.InvertibleBloomFilter;

import java.sql.Statement;
import java.util.List;

public interface IbfDataRepo {

    void streamIbfData() throws Exception;

    InvertibleBloomFilter getInvertibleBloomFilter();
    List<IbfData> findAll();
}
