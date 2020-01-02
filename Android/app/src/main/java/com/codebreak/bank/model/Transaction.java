package com.codebreak.bank.model;

public class Transaction {
    private float amount;
    private String currency;
    private String date;
    private String from;
    private String time;
    private String to;
    private String txnID;


    // Getter Methods

    public float getAmount() {
        return amount;
    }

    public String getCurrency() {
        return currency;
    }

    public String getDate() {
        return date;
    }

    public String getFrom() {
        return from;
    }

    public String getTime() {
        return time;
    }

    public String getTo() {
        return to;
    }

    public String getTxnID() {
        return txnID;
    }

    // Setter Methods

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public void setTxnID(String txnID) {
        this.txnID = txnID;
    }
}
