package com.example.invertiblebloomfilter;

import com.example.invertiblebloomfilter.ibf.*;
import com.google.common.collect.ImmutableSet;

import javax.crypto.spec.SecretKeySpec;
import javax.sql.DataSource;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.*;
import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.mock;

public class OracleIbfTestUtil {
    public static OracleType NUMBER = OracleType.create("NUMBER", null, 9, 0);
    public static OracleType INTEGER = OracleType.create("INTEGER");
    public static OracleType RAW = OracleType.create("RAW");
    public static OracleType VARCHAR2_128 = OracleType.create("VARCHAR2", 128, null, null);
    public static OracleType NVARCHAR2_128 = OracleType.create("NVARCHAR2", 128, null, null);
    public static OracleType TIMESTAMP = OracleType.create("TIMESTAMP", null, null, null);
    private static final Lazy<Path> STORAGE_DIR =
            new Lazy<>(() -> Files.createTempDirectory("local_disk_ibf_storage_client-"));

    public static List<OracleColumn> columnsForTestTable1(TableRef tableRef) {
        List<OracleColumn> columns = new ArrayList<>();
        columns.add(newPrimaryKeyColumn("PRIMARY_KEY", INTEGER, tableRef));
        columns.add(newSimpleColumn("UNSIGNED_SMALLINT_TEST", INTEGER, tableRef));
        columns.add(newSimpleColumn("SIGNED_MEDIUMINT_TEST", INTEGER, tableRef));
        columns.add(newSimpleColumn("UNSIGNED_MEDIUMINT_TEST", INTEGER, tableRef));
        columns.add(newSimpleColumn("YEAR_TEST", INTEGER, tableRef));
        columns.add(newSimpleColumn("SIGNED_INT_TEST", INTEGER, tableRef));
        return columns;
    }

    public static List<OracleColumn> columnsForTestTable2(TableRef tableRef) {
        List<OracleColumn> columns = new ArrayList<>();
        columns.add(newPrimaryKeyColumn("PRIMARY_KEY", INTEGER, tableRef));
        columns.add(newSimpleColumn("UPDATABLE_COLUMN", VARCHAR2_128, tableRef));
        return columns;
    }

    public static List<OracleColumn> columnsForTestTable3(TableRef tableRef) {
        List<OracleColumn> columns = new ArrayList<>();
        columns.add(newPrimaryKeyColumn("PRIMARY_KEY", VARCHAR2_128, tableRef));
        columns.add(newSimpleColumn("UPDATABLE_COLUMN", INTEGER, tableRef));
        return columns;
    }

    /**
     * Two primary keys
     *
     * @param tableRef
     * @return
     */
    public static List<OracleColumn> columnsForTestTable4(TableRef tableRef) {
        List<OracleColumn> columns = new ArrayList<>();
        columns.add(newPrimaryKeyColumn("PRIMARY_KEYA", INTEGER, tableRef));
        columns.add(newPrimaryKeyColumn("PRIMARY_KEYB", INTEGER, tableRef));
        columns.add(newSimpleColumn("UPDATABLE_COLUMN", VARCHAR2_128, tableRef));
        return columns;
    }

    public static List<OracleColumn> columnsForTestTable5(TableRef tableRef) {
        List<OracleColumn> columns = new ArrayList<>();
        columns.add(newPrimaryKeyColumn("PRIMARY_KEYA", INTEGER, tableRef));
        columns.add(newPrimaryKeyColumn("PRIMARY_KEYB", VARCHAR2_128, tableRef));
        columns.add(newSimpleColumn("UPDATABLE_COLUMN", VARCHAR2_128, tableRef));
        return columns;
    }

    public static List<OracleColumn> columnsForTestTableIllegalPkType(TableRef tableRef) {
        List<OracleColumn> columns = new ArrayList<>();
        columns.add(newPrimaryKeyColumn("ID", TIMESTAMP, tableRef));
        columns.add(newSimpleColumn("UPDATABLE_COLUMN", VARCHAR2_128, tableRef));
        return columns;
    }

    public static List<OracleColumn> columnsForTestTableNoPk(TableRef tableRef) {
        List<OracleColumn> columns = new ArrayList<>();
        columns.add(newSimpleColumn("ID", NUMBER, tableRef));
        columns.add(newSimpleColumn("UPDATABLE_COLUMN", VARCHAR2_128, tableRef));
        return columns;
    }

    public static List<OracleColumn> columnsForAllDatatypes(TableRef tableRef) {
        List<OracleColumn> columns = new ArrayList<>();
        columns.add(newPrimaryKeyColumn("ID", INTEGER, tableRef));
        columns.add(newSimpleColumn("VARCHAR2_COL", VARCHAR2_128, tableRef));
        columns.add(newSimpleColumn("NVARCHAR2_COL", NVARCHAR2_128, tableRef));
        columns.add(newSimpleColumn("RAW_COL", RAW, tableRef));
        columns.add(newSimpleColumn("TIMESTAMP_COL", TIMESTAMP, tableRef));
        return columns;
    }

    public static List<OracleColumn> columnsForIbfReplacementTest(TableRef tableRef) {
        List<OracleColumn> columns = new ArrayList<>();
        columns.add(newPrimaryKeyColumn("ID", INTEGER, tableRef));
        columns.add(newSimpleColumn("COL1", INTEGER, tableRef));
        columns.add(newSimpleColumn("COL2", INTEGER, tableRef));
        return columns;
    }

    public static DataSource mockDataSource(Connection connection) throws SQLException {
        DataSource mockDs = mock(DataSource.class);
        when(mockDs.getConnection()).thenReturn(connection);
        return mockDs;
    }

    public static Connection mockConnection() {
        return mock(Connection.class);
    }

    public static Connection mockConnection(PreparedStatement ps) throws SQLException {
        Connection mockConnection = mockConnection();
        when(mockConnection.prepareStatement(anyString())).thenReturn(ps);
        return mockConnection;
    }

    public static Connection mockCreateStatement(Connection conn, Statement statement) throws SQLException {
        when(conn.createStatement()).thenReturn(statement);
        return conn;
    }

    public static Connection mockPrepareStatement(Connection conn, PreparedStatement statement) throws SQLException {
        when(conn.prepareStatement(anyString())).thenReturn(statement);
        return conn;
    }

    public static PreparedStatement mockPreparedStatement() {
        return mock(PreparedStatement.class);
    }

    public static Statement mockStatement() {
        return mock(Statement.class);
    }

    public static PreparedStatement mockPreparedStatement(SQLException exception) throws SQLException {
        PreparedStatement ps = mockPreparedStatement();
        when(ps.executeQuery()).thenThrow(exception);
        return ps;
    }

    public static Statement mockStatementExecute(Statement stmt) throws SQLException {
        when(stmt.execute(anyString())).thenReturn(true);
        return stmt;
    }

    public static PreparedStatement mockExecuteQuery(PreparedStatement stmt, SQLException exception)
            throws SQLException {
        when(stmt.executeQuery()).thenThrow(exception);
        return stmt;
    }

    public static PreparedStatement mockPreparedStatementExecuteQuery(PreparedStatement stmt, ResultSet rs)
            throws SQLException {
        when(stmt.executeQuery()).thenReturn(rs);
        return stmt;
    }

    public static Map<TableRef, SyncMode> getSyncModesForTable(TableRef tableRef, SyncMode syncMode) {
        Map<TableRef, SyncMode> syncModesByTable = new HashMap<>();
        syncModesByTable.put(tableRef, syncMode);
        return syncModesByTable;
    }

    public static ResultSet mockResultSetEmpty() {
        ResultSet mock = mock(ResultSet.class);
        try {
            when(mock.next()).thenReturn(false);
        } catch (SQLException ignore) {
        }
        return mock;
    }

    public static ResultSet mockResultSetNotEmpty() {
        ResultSet mock = mock(ResultSet.class);
        try {
            when(mock.next()).thenReturn(true);
        } catch (SQLException ignore) {
        }
        return mock;
    }

    public static OracleColumn newPrimaryKeyColumn(String name, OracleType oracleType, TableRef sourceTable) {
        return new OracleColumn(name, oracleType, true, sourceTable, Optional.empty());
    }

    public static OracleColumn newSimpleColumn(String name, OracleType oracleType, TableRef sourceTable) {
        return new OracleColumn(name, oracleType, false, sourceTable, Optional.empty());
    }

    public static StandardConfig mockStandardConfig(TableRef tableRef, SyncMode syncMode) {
        StandardConfig standardConfig = mock(StandardConfig.class);
        doReturn(ImmutableSet.of(tableRef.toTableName())).when(standardConfig).includedTables();
        doReturn(getSyncModesForTable(tableRef, syncMode)).when(standardConfig).syncModes();
        return standardConfig;
    }

    public static StandardConfig mockStandardConfig(TableRef tableRef, SyncMode syncMode, boolean included) {
        StandardConfig standardConfig = mock(StandardConfig.class);
        if (included) {
            doReturn(ImmutableSet.of(tableRef.toTableName())).when(standardConfig).includedTables();
        } else {
            Map<TableName, String> excluded = new HashMap<>();
            excluded.put(tableRef.toTableName(), "no reason");
            when(standardConfig.excludedTables()).thenReturn(excluded);
        }

        doReturn(getSyncModesForTable(tableRef, syncMode)).when(standardConfig).syncModes();
        return standardConfig;
    }

    public static Map<TableRef, List<OracleColumn>> selectedTables(TableRef tableRef, List<OracleColumn> columns) {
        Map<TableRef, List<OracleColumn>> map = new HashMap<>();
        map.put(tableRef, columns);
        return map;
    }

    public static Map<TableRef, List<OracleColumn>> toMap(TableRef tableRef, List<OracleColumn> columns) {
        Map<TableRef, List<OracleColumn>> selectedTables = new HashMap<>();
        selectedTables.put(tableRef, columns);

        return selectedTables;
    }

    public static OracleTableMetricsProvider newOracleTableMetricsProvider() {
        return new OracleTableMetricsProvider() {
            @Override
            public OptionalLong getEstimatedTableSizeInBytes(TableRef tableRef) {
                return OptionalLong.empty();
            }

            @Override
            public OptionalLong getTableRowCount(TableRef tableRef) {
                return OptionalLong.empty();
            }
        };
    }


}