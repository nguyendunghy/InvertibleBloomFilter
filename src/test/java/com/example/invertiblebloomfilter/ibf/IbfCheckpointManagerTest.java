package com.example.invertiblebloomfilter.ibf;

import com.example.invertiblebloomfilter.entity.DataTable;
import com.example.invertiblebloomfilter.repo.IbfDataRepo;
import com.example.invertiblebloomfilter.repo.Sql;
import com.example.invertiblebloomfilter.repo.impl.IbfDataRepoImpl;
import com.example.invertiblebloomfilter.utils.DataSourceUtils;
import com.example.invertiblebloomfilter.utils.FileUtils;
import com.example.invertiblebloomfilter.utils.JdbcTemplateUtils;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.springframework.boot.autoconfigure.jdbc.JdbcProperties;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.crypto.SecretKey;
import javax.sql.DataSource;
import java.io.File;
import java.nio.file.Path;
import java.time.Duration;
import java.util.*;
import static com.example.invertiblebloomfilter.utils.DataSourceUtils.buildDataSource;

@Log4j2
@RunWith(JUnit4.class)
public class IbfCheckpointManagerTest {

    private static final Duration CHECKPOINT_PERIOD = Duration.ofMinutes(15);
    //Local file name
    public static final String OBJECT_ID = "165c56bd-d573-4bc2-9cc9-e36d3ed82fd5";
    private static final String IBF_LOCAL_DIR = "IbfLocal";


    @BeforeClass
    public static void initTest() {
        genIbfSyncResultAndUpdateLocalFile();
    }

    @AfterClass
    public static void afterTest() {
        try {
            DataSourceUtils.buildDataSource().getConnection().close();
        } catch (Exception e) {
            log.error("error",e);
        }
    }
    @Test
    public void testUpsert() {
        String stringColumn = RandomStringUtils.randomAlphabetic(3).toUpperCase();
        String numberColumn = RandomUtils.nextLong(1,1000000) + "";
        String date = RandomUtils.nextInt(10, 30) + "-" + RandomUtils.nextInt(10, 12) + "-" + RandomUtils.nextInt(2000, 2023);
        String clobColumn = RandomStringUtils.randomAlphabetic(5).toUpperCase();

        insertRecord(stringColumn, numberColumn, date, clobColumn);
        IbfSyncResult ibfSyncResult = genIbfSyncResultAndUpdateLocalFile();

        List<List<DataTable>> lists = ibfSyncResult.upserts();
        log.info("UPSERT record: " + lists);

        Assert.assertEquals(stringColumn, lists.get(0).get(0).getStringColumn());
        Assert.assertEquals(numberColumn, lists.get(0).get(0).getNumberColumn());
        Assert.assertEquals(date, lists.get(0).get(0).getDateColumn());
        Assert.assertEquals(clobColumn, lists.get(0).get(0).getClobColumn());

    }

    @Test
    public void testDelete() {
        String stringColumn = RandomStringUtils.randomAlphabetic(3).toUpperCase();
        String numberColumn = RandomUtils.nextLong(1,1000000) + "";
        String date = RandomUtils.nextInt(10, 30) + "-" + RandomUtils.nextInt(10, 12) + "-" + RandomUtils.nextInt(2000, 2023);
        String clobColumn = RandomStringUtils.randomAlphabetic(5).toUpperCase();

        insertRecord(stringColumn, numberColumn, date, clobColumn);
        genIbfSyncResultAndUpdateLocalFile();

        createIbfDataHistory();

        deleteRecord(stringColumn, numberColumn, date, clobColumn);

        IbfSyncResult ibfSyncResult = genIbfSyncResultAndUpdateLocalFile();
        List<List<DataTable>> lists = new ArrayList<>(ibfSyncResult.deletes()) ;
        log.info("DELETE record: " + lists);

        Assert.assertEquals(stringColumn, lists.get(0).get(0).getStringColumn());
        Assert.assertEquals(numberColumn, lists.get(0).get(0).getNumberColumn());
        Assert.assertEquals(date, lists.get(0).get(0).getDateColumn());
        Assert.assertEquals(clobColumn, lists.get(0).get(0).getClobColumn());
    }


    private static IbfSyncResult genIbfSyncResultAndUpdateLocalFile() {
        try {
            OracleIbfAdapter oracleIbfAdapter = buildOracleIbfAdapter();
            IbfPersistentStorage ibfPersistentStorage = buildIbfPersistentStorage();
            IbfCheckpointManager ibfCheckpointManager = new IbfCheckpointManager(oracleIbfAdapter,
                    ibfPersistentStorage, OBJECT_ID);

            if (!checkLocalStorageFileHaveData(IBF_LOCAL_DIR + "/" + OBJECT_ID)) {
                ibfCheckpointManager.simulateResetWithEmptyIBF();
            }
            IbfSyncResult ibfSyncResult = ibfCheckpointManager.diff();
            ibfCheckpointManager.update();
            return ibfSyncResult;
        } catch (Exception ex) {
            log.error("error", ex);
        }
        return null;
    }

    private void deleteRecord(String stringCol, String numberCol, String dateCol, String clobCol) {
        log.info("DELETED RECORD: " + stringCol + "|" + numberCol + "|" + dateCol + "|" + clobCol);
        JdbcTemplate jdbcTemplate = JdbcTemplateUtils.buildJdbcTemplate(DataSourceUtils.buildDataSource(), new JdbcProperties());
        IbfDataRepo ibfDataRepo = new IbfDataRepoImpl(jdbcTemplate);
        ibfDataRepo.delete(DataTable.builder()
                .stringColumn(stringCol).numberColumn(numberCol)
                .dateColumn(dateCol).clobColumn(clobCol)
                .build()
        );
    }
    private void insertRecord(String stringCol, String numberCol, String dateCol, String clobCol) {
        log.info("INSERTED RECORD: " + stringCol + "|" + numberCol + "|" + dateCol + "|" + clobCol);
        JdbcTemplate jdbcTemplate = JdbcTemplateUtils.buildJdbcTemplate(DataSourceUtils.buildDataSource(), new JdbcProperties());
        IbfDataRepo ibfDataRepo = new IbfDataRepoImpl(jdbcTemplate);
        ibfDataRepo.save(DataTable.builder().stringColumn(stringCol).numberColumn(numberCol).dateColumn(dateCol).clobColumn(clobCol).build());
    }

    private void createIbfDataHistory(){
        JdbcTemplate jdbcTemplate = JdbcTemplateUtils.buildJdbcTemplate(DataSourceUtils.buildDataSource(), new JdbcProperties());
        try {
            jdbcTemplate.update(Sql.DROP_IBF_DATA_HISTORY);
        }catch (Exception ex){
            log.error("error ", ex);
        }

        jdbcTemplate.update(Sql.CREATE_IBF_DATA_HISTORY);


    }

    private static boolean checkLocalStorageFileHaveData(String filePath) {
        try {
            String fileContent = FileUtils.readFile(filePath);
            new IbfSyncData(fileContent);
            return true;
        } catch (Exception ex) {
            log.error("error", ex);
        }
        return false;
    }

    public static OracleIbfAdapter buildOracleIbfAdapter() {
        DataSource dataSource = buildDataSource();

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

     static List<OracleColumnInfo> buildColumns(TableRef tableRef) {

        Column stringColumn = new Column("STRING_COLUMN", DataType.String, true);
        Column numberColumn = new Column("NUMBER_COLUMN", DataType.Long, true);
        Column dateColumn = new Column("DATE_COLUMN", DataType.LocalDate, true);
        Column clobColumn = new Column("CLOB_COLUMN", DataType.String, true);

        OracleColumn oracleStringColumn = new OracleColumn("STRING_COLUMN", OracleType.create("VARCHAR"), true, tableRef, Optional.empty());
        OracleColumn oracleNumberColumn = new OracleColumn("NUMBER_COLUMN", OracleType.create("NUMBER"), true, tableRef, Optional.empty());
        OracleColumn oracleDateColumn = new OracleColumn("DATE_COLUMN", OracleType.create("DATE"), true, tableRef, Optional.empty());
        OracleColumn oracleClobColumn = new OracleColumn("CLOB_COLUMN", OracleType.create("CLOB", true), true, tableRef, Optional.empty());

        OracleColumnInfo stringOracleColumnInfo = new OracleColumnInfo(oracleStringColumn, stringColumn);
        stringOracleColumnInfo.setAddedSinceLastSync(false);
        OracleColumnInfo numberOracleColumnInfo = new OracleColumnInfo(oracleNumberColumn, numberColumn);
        numberOracleColumnInfo.setAddedSinceLastSync(false);
        OracleColumnInfo dateOracleColumnInfo = new OracleColumnInfo(oracleDateColumn, dateColumn);
        dateOracleColumnInfo.setAddedSinceLastSync(false);
        OracleColumnInfo clobOracleColumnInfo = new OracleColumnInfo(oracleClobColumn, clobColumn);
        clobOracleColumnInfo.setAddedSinceLastSync(false);

        return Arrays.asList(stringOracleColumnInfo, numberOracleColumnInfo, dateOracleColumnInfo, clobOracleColumnInfo);

    }

    public static IbfPersistentStorage buildIbfPersistentStorage() {
        File file = new File(IBF_LOCAL_DIR);
        Path path = file.toPath();
        log.info("Using local ibf storage location " + path);
        LocalDiskStorageAddressSpecification localDiskStorageAddressSpecification = new LocalDiskStorageAddressSpecification(path);
        LocalDiskIbfStorageClient localDiskIbfStorageClient = new LocalDiskIbfStorageClient(localDiskStorageAddressSpecification);
        SecretKey secretKey = Encrypt.newDataEncryptionKey();
        IbfPersistentStorage ibfPersistentStorage = new IbfPersistentStorage(localDiskIbfStorageClient, secretKey);
        return ibfPersistentStorage;

    }


}
