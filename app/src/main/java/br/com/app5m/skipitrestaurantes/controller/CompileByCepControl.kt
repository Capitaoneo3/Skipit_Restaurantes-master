package br.com.app5m.skipitrestaurantes.controller


import br.com.app5m.skipitrestaurantes.controller.webservice.WSConstants
import br.com.app5m.skipitrestaurantes.controller.webservice.WebService
import br.com.app5m.skipitrestaurantes.model.Address
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


/**
 *
 */
object CompileByCepControl {

    fun compile(cep: String?, resultCep: CepResult) {
        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BODY)

        val httpClient = OkHttpClient.Builder()
//        httpClient.addInterceptor(logging)
        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(WSConstants.VIACEP)
            .addConverterFactory(GsonConverterFactory.create())
            .client(httpClient.build())
            .build()
        val webService: WebService = retrofit.create(WebService::class.java)
        val callCEPByCEP: Call<Address> = webService.getAddressByCEP(cep)

        callCEPByCEP.enqueue(object : Callback<Address> {

            override fun onResponse(call: Call<Address>, response: Response<Address>) {
                if (response.isSuccessful) response.body()?.let { resultCep.resultCep(it) }
            }

            override fun onFailure(call: Call<Address>, t: Throwable) {}
        }
        )
    }

    interface CepResult {
        fun resultCep(cep: Address)
    }
}