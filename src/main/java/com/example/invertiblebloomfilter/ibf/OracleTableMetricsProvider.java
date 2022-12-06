package com.example.invertiblebloomfilter.ibf;

import java.util.OptionalLong;

public interface OracleTableMetricsProvider {
    OptionalLong getEstimatedTableSizeInBytes(TableRef tableRef);

    OptionalLong getTableRowCount(TableRef tableRef);
}
