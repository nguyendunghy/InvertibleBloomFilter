package com.example.statrystesting.ibf;

import oracle.jdbc.pool.OracleDataSource;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;

import javax.crypto.SecretKey;
import javax.sql.DataSource;
import java.io.File;
import java.nio.file.Path;
import java.sql.SQLException;
import java.util.UUID;

public class IbfCheckpointManagerTest {

    private String url = "jdbc:oracle:thin:@localhost:49161:XE";
    private String username = "john";
    private String password = "abcd1234";

    @Test
    void testDiff() {
        try {
            OracleIbfAdapter oracleIbfAdapter = buildOracleIbfAdapter();

            IbfPersistentStorage ibfPersistentStorage = buildIbfPersistentStorage();

            String objectId = UUID.randomUUID().toString();

            IbfCheckpointManager ibfCheckpointManager = new IbfCheckpointManager(oracleIbfAdapter, ibfPersistentStorage, objectId);
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


        OracleIbfAdapter oracleIbfAdapter = new OracleIbfAdapter(dataSource, null, null, null, null);
        return oracleIbfAdapter;
    }

    private IbfPersistentStorage buildIbfPersistentStorage() {
        File file = new File("IbfLocal.txt");
        Path path = file.toPath();
        System.out.println("Using local ibf storage location " + path);
        LocalDiskStorageAddressSpecification localDiskStorageAddressSpecification = new LocalDiskStorageAddressSpecification(path);
        LocalDiskIbfStorageClient localDiskIbfStorageClient = new LocalDiskIbfStorageClient(localDiskStorageAddressSpecification);
        SecretKey secretKey = Encrypt.newDataEncryptionKey();
        IbfPersistentStorage ibfPersistentStorage = new IbfPersistentStorage(localDiskIbfStorageClient, secretKey);
        return ibfPersistentStorage;

    }

    private DataSource createDataSource(String url, String user, String password) {
        try {
            OracleDataSource dataSource = new OracleDataSource();
            dataSource.setURL(url);
            dataSource.setUser(user);
            dataSource.setPassword(password);
            return dataSource;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return null;
    }
}
