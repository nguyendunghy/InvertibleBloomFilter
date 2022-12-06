package com.example.invertiblebloomfilter.velocity;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;


@SpringBootTest
public class VelocityUtilsTest {

    @Test
    void testGenerateIBFQuery(){
        String sql = VelocityUtils.generateIBFQuery(
                "invertible_bloom_filter.vm",
                "IBF_DATA",
                new String[]{"STRING_COLUMN","NUMBER_COLUMN", "DATE_COLUMN", "CLOB_COLUMN"},
                "numberizeHashTableData"
        );
        System.out.println(sql);
        Assert.notNull(sql);
    }


    @Test
    void testRetrieveData(){
        String sql = VelocityUtils.generateIBFQuery(
                "retrieve_data_template.vm",
                "IBF_DATA",
                new String[]{"STRING_COLUMN","NUMBER_COLUMN", "DATE_COLUMN", "CLOB_COLUMN"},
                "selectChangedData"
        );
        System.out.println(sql);
        Assert.notNull(sql);
    }

    @Test
    void testRetrieveHistoryData(){
        String sql = VelocityUtils.generateIBFQuery(
                "retrieve_data_template.vm",
                "IBF_DATA_HISTORY",
                new String[]{"STRING_COLUMN","NUMBER_COLUMN", "DATE_COLUMN", "CLOB_COLUMN"},
                "selectChangedData"
        );
        System.out.println(sql);
        Assert.notNull(sql);
    }
}
