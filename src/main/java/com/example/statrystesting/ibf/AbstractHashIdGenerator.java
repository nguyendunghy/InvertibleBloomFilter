package com.example.statrystesting.ibf;

import java.util.List;
import java.util.Map;

/** Abstraction for hashId generation that allows for future changes. */
public interface AbstractHashIdGenerator {
    String hashId(Map<String, ?> row, List<Column> knownTypes);
}
