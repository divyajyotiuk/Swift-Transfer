package com.codebreak.bank.model;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class TxnList {

    private String to,from,date,time,txnID,currency, senderName, receiverName;
    private Double amount;
    private boolean moneyAdded;

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTxnID() {
        return txnID;
    }

    public void setTxnID(String txnID) {
        this.txnID = txnID;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();

        result.put("to",to);
        result.put("from",from);
        result.put("currency",currency);
        result.put("txnID",txnID);
        result.put("date",date);
        result.put("time",time);
        result.put("amount",amount);

        return result;
    }

    public boolean isMoneyAdded() {
        return moneyAdded;
    }

    public void setMoneyAdded(boolean moneyAdded) {
        this.moneyAdded = moneyAdded;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }
}
