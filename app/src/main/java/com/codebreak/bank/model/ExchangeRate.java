package com.codebreak.bank.model;

import java.util.Map;

public class ExchangeRate {

    Map<String, Float> rates;
    private String base;
    private String date;


    // Getter Methods

    public Map<String ,Float> getRates() {
        return rates;
    }

    public String getBase() {
        return base;
    }

    public String getDate() {
        return date;
    }

    // Setter Methods

    public void setRates(Map<String,Float> rates) {
        this.rates = rates;
    }

    public void setBase(String base) {
        this.base = base;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public class Rates {
        private float INR;


        // Getter Methods

        public float getINR() {
            return INR;
        }

        // Setter Methods

        public void setINR(float INR) {
            this.INR = INR;
        }
    }
}