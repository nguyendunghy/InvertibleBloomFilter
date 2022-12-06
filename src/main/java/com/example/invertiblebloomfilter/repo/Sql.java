package com.example.invertiblebloomfilter.repo;

public class Sql {

    public static final String IBF_QUERY = "select TO_NUMBER(SUBSTR(ROWHASH, 1, 15), 'XXXXXXXXXXXXXXXX')    as ROW_HASH_NUMBER,\n" +
            "       TO_NUMBER(SUBSTR(STRINGHASH, 1, 15), 'XXXXXXXXXXXXXXXX') as STRING_HASH_NUMBER,\n" +
            "       TO_NUMBER(SUBSTR(NUMBERHASH, 1, 15), 'XXXXXXXXXXXXXXXX') as NUMBER_HASH_NUMBER,\n" +
            "       TO_NUMBER(SUBSTR(DATEHASH, 1, 15), 'XXXXXXXXXXXXXXXX')   as DATE_HASH_NUMBER,\n" +
            "       TO_NUMBER(SUBSTR(CLOBHASH, 1, 15), 'XXXXXXXXXXXXXXXX')   as CLOB_HASH_NUMBER\n" +
            "     from (select STANDARD_HASH(COALESCE(STRING_COLUMN, '') || COALESCE(TO_CHAR(NUMBER_COLUMN), '') ||\n" +
            "                           COALESCE(TO_CHAR(DATE_COLUMN), '') || COALESCE(TO_CHAR(CLOB_COLUMN), ''), 'MD5') as ROWHASH,\n" +
            "             STANDARD_HASH(COALESCE(STRING_COLUMN, ''), 'MD5')                                              as STRINGHASH,\n" +
            "             STANDARD_HASH(COALESCE(TO_CHAR(NUMBER_COLUMN), ''), 'MD5')                                     as NUMBERHASH,\n" +
            "             STANDARD_HASH(COALESCE(TO_CHAR(DATE_COLUMN), ''), 'MD5')                                       as DATEHASH,\n" +
            "             STANDARD_HASH(COALESCE(TO_CHAR(CLOB_COLUMN), ''), 'MD5')                                       as CLOBHASH\n" +
            "      from (select distinct STRING_COLUMN, NUMBER_COLUMN, DATE_COLUMN, TO_CHAR(CLOB_COLUMN) AS CLOB_COLUMN\n" +
            "            from IBF_DATA))";


    public static final String RETRIEVE_DATA = "select *\n" +
            "from (select TO_NUMBER(SUBSTR(ROWHASH, 1, 15), 'XXXXXXXXXXXXXXXX') as ROW_HASH_NUMBER,\n" +
            "             STRING_COLUMN,\n" +
            "             NUMBER_COLUMN,\n" +
            "             DATE_COLUMN,\n" +
            "             CLOB_COLUMN\n" +
            "      from (select STANDARD_HASH(COALESCE(STRING_COLUMN, '') || COALESCE(TO_CHAR(NUMBER_COLUMN), '') ||\n" +
            "                                 COALESCE(TO_CHAR(DATE_COLUMN), '') || COALESCE(TO_CHAR(CLOB_COLUMN), ''),\n" +
            "                                 'MD5') as ROWHASH,\n" +
            "                   STRING_COLUMN,\n" +
            "                   NUMBER_COLUMN,\n" +
            "                   DATE_COLUMN,\n" +
            "                   CLOB_COLUMN\n" +
            "            from (select distinct STRING_COLUMN, NUMBER_COLUMN, DATE_COLUMN, TO_CHAR(CLOB_COLUMN) AS CLOB_COLUMN\n" +
            "                  from IBF_DATA)))\n" +
            "where ROW_HASH_NUMBER = ?";

    public static final String RETRIEVE_DATA_HISTORY = "select *\n" +
            "from (select TO_NUMBER(SUBSTR(ROWHASH, 1, 15), 'XXXXXXXXXXXXXXXX') as ROW_HASH_NUMBER,\n" +
            "             STRING_COLUMN,\n" +
            "             NUMBER_COLUMN,\n" +
            "             DATE_COLUMN,\n" +
            "             CLOB_COLUMN\n" +
            "      from (select STANDARD_HASH(COALESCE(STRING_COLUMN, '') || COALESCE(TO_CHAR(NUMBER_COLUMN), '') ||\n" +
            "                                 COALESCE(TO_CHAR(DATE_COLUMN), '') || COALESCE(TO_CHAR(CLOB_COLUMN), ''),\n" +
            "                                 'MD5') as ROWHASH,\n" +
            "                   STRING_COLUMN,\n" +
            "                   NUMBER_COLUMN,\n" +
            "                   DATE_COLUMN,\n" +
            "                   CLOB_COLUMN\n" +
            "            from (select distinct STRING_COLUMN, NUMBER_COLUMN, DATE_COLUMN, TO_CHAR(CLOB_COLUMN) AS CLOB_COLUMN\n" +
            "                  from IBF_DATA_HISTORY)))\n" +
            "where ROW_HASH_NUMBER = ?";

}
