package com.example.invertiblebloomfilter.ibf;

import java.util.List;
import java.util.Map;

public interface OracleIncrementalUpdaterFactory {
    OracleAbstractIncrementalUpdater newIncrementalUpdater(Map<TableRef, List<OracleColumn>> selected);
}
