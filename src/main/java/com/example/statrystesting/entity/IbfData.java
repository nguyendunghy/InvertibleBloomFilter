package com.example.statrystesting.entity;

public class IbfData {
    private long rowHashNumber;
    private long stringHashNumber;
    private long numberHashNumber;
    private long dateHashNumber;
    private long clobHashNumber;

    public IbfData() {
    }

    public IbfData(long rowHashNumber, long stringHashNumber, long numberHashNumber, long dateHashNumber, long clobHashNumber) {
        this.rowHashNumber = rowHashNumber;
        this.stringHashNumber = stringHashNumber;
        this.numberHashNumber = numberHashNumber;
        this.dateHashNumber = dateHashNumber;
        this.clobHashNumber = clobHashNumber;
    }


    public long getRowHashNumber() {
        return rowHashNumber;
    }

    public void setRowHashNumber(long rowHashNumber) {
        this.rowHashNumber = rowHashNumber;
    }

    public long getStringHashNumber() {
        return stringHashNumber;
    }

    public void setStringHashNumber(long stringHashNumber) {
        this.stringHashNumber = stringHashNumber;
    }

    public long getNumberHashNumber() {
        return numberHashNumber;
    }

    public void setNumberHashNumber(long numberHashNumber) {
        this.numberHashNumber = numberHashNumber;
    }

    public long getDateHashNumber() {
        return dateHashNumber;
    }

    public void setDateHashNumber(long dateHashNumber) {
        this.dateHashNumber = dateHashNumber;
    }

    public long getClobHashNumber() {
        return clobHashNumber;
    }

    public void setClobHashNumber(long clobHashNumber) {
        this.clobHashNumber = clobHashNumber;
    }
}
