package com.example.invertiblebloomfilter.repo;

import com.example.invertiblebloomfilter.entity.IbfEntity;

public interface IbfRepo {
    void save(IbfEntity ibf);

    IbfEntity get(Long id);

    Long getMaxId();
}
