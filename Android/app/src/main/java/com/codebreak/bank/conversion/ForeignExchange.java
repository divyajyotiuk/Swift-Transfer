package com.codebreak.bank.conversion;

import com.codebreak.bank.model.ExchangeRate;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ForeignExchange {

    @GET("latest")
    Call<ExchangeRate> getExchangeRate(@Query("base") String base,@Query("symbols") String symbols);
}
