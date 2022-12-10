package com.example.invertiblebloomfilter.ibf;

import java.util.Collection;
import java.util.Set;

/** Manages and provides OracleTableInfo and OracleTableState for each selected table */
public interface OracleTableInfoProvider {
    Set<TableRef> getSelectedTables();

    OracleTableState getOracleTableState(TableRef tableRef);

    Collection<OracleTableInfo> getAllOracleTableInfo();

    OracleTableInfo getOracleTableInfo(TableRef tableRef);
}
