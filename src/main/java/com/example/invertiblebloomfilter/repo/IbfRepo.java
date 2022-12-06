package com.example.invertiblebloomfilter.repo;

import com.example.invertiblebloomfilter.entity.Ibf;

public interface IbfRepo {
    void save(Ibf ibf);

    Ibf get(Long id);

    Long getMaxId();
}
