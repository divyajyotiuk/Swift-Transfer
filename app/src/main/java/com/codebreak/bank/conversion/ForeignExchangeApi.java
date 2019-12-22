package com.codebreak.bank.conversion;

import com.codebreak.bank.model.ExchangeRate;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ForeignExchangeApi {


    private static OkHttpClient client;
    private static ForeignExchange api;
    private Call<ExchangeRate> call;

    private static OkHttpClient buildClient()
    {
        if(client==null)
            client = new OkHttpClient.Builder()
                    .connectTimeout(100, TimeUnit.SECONDS)
                    .readTimeout(100,TimeUnit.SECONDS).build();

        return client;
    }

    private static ForeignExchange buildApi()
    {
        if (api==null){ api = new Retrofit
                .Builder().baseUrl("https://api.exchangeratesapi.io/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ForeignExchange.class);
        }
        return api;
    }

    public void getExchangeRate(String base, String symbols, ExchangeRateListener listener )
    {
        call = buildApi().getExchangeRate(base, symbols);
        call.enqueue(new Callback<ExchangeRate>() {
            @Override
            public void onResponse(Call<ExchangeRate> call, Response<ExchangeRate> response) {
                if(response.code()==200 && response.body()!=null)
                {
                    listener.onExchangeRate(response.body());
                }
            }

            @Override
            public void onFailure(Call<ExchangeRate> call, Throwable t) {
                listener.onFailed(t.getMessage());

            }
        });
    }


    public void cancelCall()
    {
        if(call!=null)
            call.cancel();
    }

    public interface ExchangeRateListener
    {
        void onExchangeRate(ExchangeRate exchangeRate);
        void onFailed(String message);
    }
}
