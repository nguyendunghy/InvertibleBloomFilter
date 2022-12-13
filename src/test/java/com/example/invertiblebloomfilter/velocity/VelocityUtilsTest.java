package com.example.invertiblebloomfilter.velocity;

import org.junit.Assert;
import org.junit.jupiter.api.Test;

public class VelocityUtilsTest {
    @Test
    void testGenerateRetrieveQuery(){
        String retrieveDataQuery = VelocityUtils.generateIBFQuery(
                "retrieve_data_template.vm",
                "IBF_DATA",
                new String[]{"STRING_COLUMN","NUMBER_COLUMN", "DATE_COLUMN", "CLOB_COLUMN"},
                "selectChangedData"
        );
        System.out.println(retrieveDataQuery);
        Assert.assertNotNull(retrieveDataQuery);
    }


    @Test
    void testGenerateIbfQuery(){
        String ibfQuery = VelocityUtils.generateIBFQuery(
                "invertible_bloom_filter.vm",
                "IBF_DATA",
                new String[]{"STRING_COLUMN", "NUMBER_COLUMN", "DATE_COLUMN", "CLOB_COLUMN"},
                "numberizeHashTableData"
        );
        System.out.println(ibfQuery);
        Assert.assertNotNull(ibfQuery);
    }
}
