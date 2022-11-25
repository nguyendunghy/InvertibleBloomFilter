package com.example.statrystesting.service;

import com.example.statrystesting.entity.DataTable;
import com.example.statrystesting.entity.IbfData;
import com.example.statrystesting.ibf.InvertibleBloomFilter;
import com.example.statrystesting.repo.IbfDataRepo;
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

}
