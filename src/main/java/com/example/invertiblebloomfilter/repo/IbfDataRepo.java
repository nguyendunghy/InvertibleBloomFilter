package com.example.invertiblebloomfilter.repo;

import com.example.invertiblebloomfilter.entity.DataTable;
import com.example.invertiblebloomfilter.entity.IbfData;
import com.example.invertiblebloomfilter.ibf.InvertibleBloomFilter;

import java.util.List;

public interface IbfDataRepo {

    void streamIbfData(InvertibleBloomFilter invertibleBloomFilter, String... query) throws Exception;

    List<IbfData> findAll();

    List<DataTable> retrieveAllData(String rowHash, String... query);

    List<DataTable> retrieveAllHistoryData(String rowHash, String... query);

}
