package com.example.invertiblebloomfilter.ibf;

import java.util.List;

// TODO: Merge this interface into IbfTableInspector once all implementors have added compound PK support
public interface IbfTableEncoderWithCompoundPK extends IbfTableEncoder {
    /** get key lengths * */
    List<Integer> keyLengths();

    /** get data types of primary keys * */
    List<DataType> keyTypes();
}
