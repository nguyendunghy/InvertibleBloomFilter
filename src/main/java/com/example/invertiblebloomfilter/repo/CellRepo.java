package com.example.invertiblebloomfilter.repo;

import com.example.invertiblebloomfilter.entity.CellEntity;

import java.util.List;

public interface CellRepo {

   void save(CellEntity cell);

   void save(List<CellEntity> cells);

   List<CellEntity> findAll(Long ibfId);

   Long getMaxId();

}
