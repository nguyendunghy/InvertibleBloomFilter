package com.example.invertiblebloomfilter.ibf;

import org.junit.Assert;
import org.junit.jupiter.api.Test;

import javax.crypto.SecretKey;
import javax.sql.DataSource;
import java.io.File;
import java.nio.file.Path;
import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static com.example.invertiblebloomfilter.utils.DataSourceUtils.createDataSource;

public class IbfCheckpointManagerTest {

    private String url = "jdbc:oracle:thin:@localhost:49161:XE";
    private String username = "john";
    private String password = "abcd1234";
    private final Duration CHECKPOINT_PERIOD = Duration.ofMinutes(15);

    @Test
    void testDiff() {
        try {
            OracleIbfAdapter oracleIbfAdapter = buildOracleIbfAdapter();

            IbfPersistentStorage ibfPersistentStorage = buildIbfPersistentStorage();

            String objectId = "165c56bd-d573-4bc2-9cc9-e36d3ed82fd5";

            IbfCheckpointManager ibfCheckpointManager = new IbfCheckpointManager(oracleIbfAdapter, ibfPersistentStorage, objectId);

            ibfCheckpointManager.simulateResetWithEmptyIBF();

            IbfSyncResult ibfSyncResult = ibfCheckpointManager.diff();


            System.out.println("UPSERT :" + ibfSyncResult.upserts());
            System.out.println("DELETE :" + ibfSyncResult.deletes());


            Assert.assertTrue(true);
        } catch (Exception ex) {
            ex.printStackTrace();
            Assert.assertTrue(false);
        }


    }

    private OracleIbfAdapter buildOracleIbfAdapter() {
        DataSource dataSource = createDataSource(url, username, password);

        TableRef tableRef = new TableRef("JOHN", "IBF_DATA");
        StandardConfig standardConfig = StandardConfig.createModifiable("JOHN", Arrays.asList("IBF_DATA"));
        List<OracleColumnInfo> columns = buildColumns(tableRef);
        OracleTableInfo oracleTableInfo = new OracleTableInfo(
                tableRef,
                standardConfig,
                columns,
                null
        );
        OracleIbfTableInfo oracleIbfTableInfo = new OracleIbfTableInfo(oracleTableInfo);
        OracleIBFQueryBuilder oracleIBFQueryBuilder = new OracleIBFQueryBuilder(oracleIbfTableInfo);
        PeriodicCheckpointer periodicCheckpointer = new PeriodicCheckpointer(new Output<String>(), "state", CHECKPOINT_PERIOD);

        OracleIbfAdapter oracleIbfAdapter = new OracleIbfAdapter(dataSource, oracleTableInfo, oracleIbfTableInfo, oracleIBFQueryBuilder, periodicCheckpointer);
        return oracleIbfAdapter;
    }

    private List<OracleColumnInfo> buildColumns(TableRef tableRef) {

        Column stringColumn = new Column("STRING_COLUMN", DataType.String, true);
        Column numberColumn = new Column("NUMBER_COLUMN", DataType.Long, true);
        Column dateColumn = new Column("DATE_COLUMN", DataType.LocalDate, true);
        Column clobColumn = new Column("CLOB_COLUMN", DataType.String, true);

        OracleColumn oracleStringColumn = new OracleColumn("STRING_COLUMN", OracleType.create("VARCHAR"), true, tableRef, Optional.empty());
        OracleColumn oracleNumberColumn = new OracleColumn("NUMBER_COLUMN", OracleType.create("NUMBER"), true, tableRef, Optional.empty());
        OracleColumn oracleDateColumn = new OracleColumn("DATE_COLUMN", OracleType.create("DATE"), true, tableRef, Optional.empty());
        OracleColumn oracleClobColumn = new OracleColumn("CLOB_COLUMN", OracleType.create("CLOB",true), true, tableRef, Optional.empty());

        OracleColumnInfo stringOracleColumnInfo = new OracleColumnInfo(oracleStringColumn, stringColumn);
        OracleColumnInfo numberOracleColumnInfo = new OracleColumnInfo(oracleNumberColumn, numberColumn);
        OracleColumnInfo dateOracleColumnInfo = new OracleColumnInfo(oracleDateColumn, dateColumn);
        OracleColumnInfo clobOracleColumnInfo = new OracleColumnInfo(oracleClobColumn, clobColumn);

        return Arrays.asList(stringOracleColumnInfo, numberOracleColumnInfo, dateOracleColumnInfo, clobOracleColumnInfo);

    }

    private IbfPersistentStorage buildIbfPersistentStorage() {
        File file = new File("IbfLocal");
        Path path = file.toPath();
        System.out.println("Using local ibf storage location " + path);
        LocalDiskStorageAddressSpecification localDiskStorageAddressSpecification = new LocalDiskStorageAddressSpecification(path);
        LocalDiskIbfStorageClient localDiskIbfStorageClient = new LocalDiskIbfStorageClient(localDiskStorageAddressSpecification);
        SecretKey secretKey = Encrypt.newDataEncryptionKey();
        IbfPersistentStorage ibfPersistentStorage = new IbfPersistentStorage(localDiskIbfStorageClient, secretKey);
        return ibfPersistentStorage;

    }


}
