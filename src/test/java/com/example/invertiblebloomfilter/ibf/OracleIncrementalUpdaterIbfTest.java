package com.example.invertiblebloomfilter.ibf;

import org.junit.jupiter.api.Test;

import javax.sql.DataSource;
import java.util.Arrays;

import static com.example.invertiblebloomfilter.utils.DataSourceUtils.buildDataSource;

public class OracleIncrementalUpdaterIbfTest extends IbfCheckpointManagerTest{

    @Test
    public void testFullFlow() {


        IbfTableWorker ibfTableWorker = buildIbfTableWorker();


        OracleIncrementalUpdaterIbf oracleIncrementalUpdaterIbf = new OracleIncrementalUpdaterIbf(
                null,
                null,
                null,
                null,
                null,
                Arrays.asList(ibfTableWorker)
        );


    }


    private IbfTableWorker buildIbfTableWorker() {
        DataSource dataSource = buildDataSource();

        IbfCheckpointManager ibfCheckpointManager = buildIbfCheckpointManager();

        IbfTableWorker oracleTableSyncer = new OracleIbfTableSyncer(
                null,
                null,
                null,
                null,
                ibfCheckpointManager,
                dataSource,
                null,
                null);
        return oracleTableSyncer;
    }




    private IbfCheckpointManager buildIbfCheckpointManager(){
        OracleIbfAdapter oracleIbfAdapter = buildOracleIbfAdapter();
        IbfPersistentStorage ibfPersistentStorage = buildIbfPersistentStorage();
        IbfCheckpointManager ibfCheckpointManager = new IbfCheckpointManager(oracleIbfAdapter,
                ibfPersistentStorage, OBJECT_ID);
        return ibfCheckpointManager;
    }


}
