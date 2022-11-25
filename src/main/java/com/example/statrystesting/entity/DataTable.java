package com.example.statrystesting.entity;

import java.util.Date;

public class DataTable {
    private long rowHashNumber;
    private String stringColumn;
    private Long numberColumn;
    private Date dateColumn;
    private String clobColumn;

    public DataTable(long rowHashNumber, String stringColumn, Long numberColumn, Date dateColumn, String clobColumn) {
        this.rowHashNumber = rowHashNumber;
        this.stringColumn = stringColumn;
        this.numberColumn = numberColumn;
        this.dateColumn = dateColumn;
        this.clobColumn = clobColumn;
    }

    public DataTable() {
    }

    public long getRowHashNumber() {
        return rowHashNumber;
    }

    public void setRowHashNumber(long rowHashNumber) {
        this.rowHashNumber = rowHashNumber;
    }

    public String getStringColumn() {
        return stringColumn;
    }

    public void setStringColumn(String stringColumn) {
        this.stringColumn = stringColumn;
    }

    public Long getNumberColumn() {
        return numberColumn;
    }

    public void setNumberColumn(Long numberColumn) {
        this.numberColumn = numberColumn;
    }

    public Date getDateColumn() {
        return dateColumn;
    }

    public void setDateColumn(Date dateColumn) {
        this.dateColumn = dateColumn;
    }

    public String getClobColumn() {
        return clobColumn;
    }

    public void setClobColumn(String clobColumn) {
        this.clobColumn = clobColumn;
    }
}
