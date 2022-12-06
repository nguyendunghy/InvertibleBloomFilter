package com.example.invertiblebloomfilter.ibf;

import java.util.Optional;

public interface IbfTableInspector {
    /** Get the TableRef of the associated table */
    TableRef tableRef();

    /** Get the estimated row count of the associated table */
    Optional<Long> estimatedRowCount();
}
