package com.example.invertiblebloomfilter.service;

import com.example.invertiblebloomfilter.entity.DataTable;
import com.example.invertiblebloomfilter.entity.IbfData;
import com.example.invertiblebloomfilter.ibf.InvertibleBloomFilter;
import com.example.invertiblebloomfilter.repo.IbfDataRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IbfService {
    @Autowired
    private IbfDataRepo ibfDataRepo;
    public void streamIbfData(InvertibleBloomFilter invertibleBloomFilter)  {
        try{
            ibfDataRepo.streamIbfData(invertibleBloomFilter);
            Thread.sleep(3000);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public List<IbfData> findAll(){
        return ibfDataRepo.findAll();
    }

    public List<DataTable> retrieveAllData(String rowHash){
        return ibfDataRepo.retrieveAllData( rowHash);
    }

    public List<DataTable> retrieveAllHistoryData(String rowHash){
        return ibfDataRepo.retrieveAllHistoryData( rowHash);
    }

}
