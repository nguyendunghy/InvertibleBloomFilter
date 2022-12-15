package com.example.invertiblebloomfilter;

import com.example.invertiblebloomfilter.ibf.OracleColumn;
import com.example.invertiblebloomfilter.ibf.OracleIBFQueryBuilder;
import com.example.invertiblebloomfilter.ibf.SyncMode;
import com.example.invertiblebloomfilter.ibf.TableRef;
import org.aspectj.weaver.ast.Or;
import org.junit.Test;

import java.util.List;
import java.util.function.Function;

import static org.junit.Assert.assertTrue;

public class OracleIBFTest {

//    private OracleIBFQueryBuilder queryBuilder(
//            TableRef tableRef, SyncMode syncMode, Function<TableRef, List<OracleColumn>> columnF) {
//        return testContext
//                .getOracleIbfContext()
//                .getOracleIBFQueryBuilder(newOracleTableInfo(tableRef, syncMode, columnF.apply(tableRef)));
//    }

    @Test
    public void testQueryTemplate_FixedSizeIBF() {
//        OracleIBFQueryBuilder ibfQueryBuilder =
//                queryBuilder(getDefaultTestTable(), SyncMode.Legacy, OracleIbfTestUtil::columnsForTestTable1)
//                        .setFixedSize(true)
//                        .setCellCount(300);

        OracleIBFQueryBuilder ibfQueryBuilder = new OracleIBFQueryBuilder();

        // Assert that the cell count was converted into expected hash bucket index ranges
        innerAsserts1(ibfQueryBuilder.buildQuery());
    }

    private void innerAsserts1(String query) {
        assertTrue(query.contains("101))"));
        assertTrue(query.contains("103)) + 101"));
        assertTrue(query.contains("107)) + 204"));

        // Assert that expected  columns are present in query
        assertTrue(query.contains("PRIMARY_KEY"));
        assertTrue(query.contains("UNSIGNED_SMALLINT_TEST"));
        assertTrue(query.contains("SIGNED_MEDIUMINT_TEST"));
        assertTrue(query.contains("UNSIGNED_MEDIUMINT_TEST"));
        assertTrue(query.contains("YEAR_TEST"));
        assertTrue(query.contains("SIGNED_INT_TEST"));
    }

    private TableRef getDefaultTestTable() {
        return new TableRef("default_schema", "default_table");
    }
}
