package com.example.statrystesting.ibf;

import java.util.Optional;

public interface ColumnInfoService {

    Optional<String> getDataDefault(OracleColumnInfo columnInfo) throws Exception;
}
