package com.example.invertiblebloomfilter.service;

import com.example.invertiblebloomfilter.entity.CellEntity;
import com.example.invertiblebloomfilter.ibf.Cell;
import com.example.invertiblebloomfilter.ibf.InvertibleBloomFilter;
import com.example.invertiblebloomfilter.repo.CellRepo;
import org.springframework.beans.factory.annotation.Autowired;

public class CellService {

    @Autowired
    private CellRepo cellRepo;
    public void save(InvertibleBloomFilter ibf){
        Long maxId = cellRepo.getMaxId();
        int count = 0;
        for(Cell cell : ibf.getCells()){
            count++;
            CellEntity cellEntity = new CellEntity();
            cellEntity.setId(maxId + count);


        }
    }

}
