package br.com.app5m.skipitrestaurantes.config


import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


/**
 * RetrofitInitializer
 *
 * @author Android version: Wesley Costa
 * @since Version 1.0.2
 * @Created  01/2020
 */
class RetrofitInitializer {

    var gson = GsonBuilder()
        .setLenient()
        .create()

    private val wsUrl = "https://"

    fun retrofit(log_enable: Boolean): Retrofit{
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY

        val client = if (log_enable) OkHttpClient.Builder().connectTimeout(20, TimeUnit.SECONDS)
                .writeTimeout(20, TimeUnit.SECONDS).readTimeout(20, TimeUnit.SECONDS).addInterceptor(logging).build()
        else
            OkHttpClient.Builder().connectTimeout(20, TimeUnit.SECONDS)
                    .writeTimeout(20, TimeUnit.SECONDS).readTimeout(20, TimeUnit.SECONDS).build()

        return Retrofit.Builder().baseUrl(wsUrl)
                .addConverterFactory(GsonConverterFactory.create(gson)).client(client).build()
    }


}

