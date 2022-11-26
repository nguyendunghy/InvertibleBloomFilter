package com.example.statrystesting.repo;

import com.example.statrystesting.entity.DataTable;
import com.example.statrystesting.entity.IbfData;
import com.example.statrystesting.ibf.InvertibleBloomFilter;

import java.util.List;

public interface IbfDataRepo {

    void streamIbfData(InvertibleBloomFilter invertibleBloomFilter) throws Exception;

    List<IbfData> findAll();

    List<DataTable> retrieveAllData(String rowHash);

    List<DataTable> retrieveAllHistoryData(String rowHash);

}
