package br.com.app5m.skipitrestaurantes.controller


import android.content.Context
import android.util.Log
import br.com.app5m.skipitrestaurantes.config.RetrofitInitializer
import br.com.app5m.skipitrestaurantes.controller.webservice.WSConstants
import br.com.app5m.skipitrestaurantes.controller.webservice.WSResult
import br.com.app5m.skipitrestaurantes.controller.webservice.WebService
import br.com.app5m.skipitrestaurantes.helper.Preferences
import br.com.app5m.skipitrestaurantes.helper.Useful
import br.com.app5m.skipitrestaurantes.model.restaurant.Restaurant
import br.com.app5m.skipitrestaurantes.model.restaurant.RestaurantSubList
import br.com.app5m.skipitrestaurantes.model.restaurant.RestaurantSubListItem

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BuscaEstabControl2(context: Context, private val result: WSResult) : Callback<RestaurantSubList> {
    val context = context
    private val useful: Useful = Useful(context)
    private val preferences: Preferences = Preferences(context)

    private val service = RetrofitInitializer().retrofit(
        true
    ).create(WebService::class.java)
    private var type = ""

    override fun onResponse(call: Call<RestaurantSubList>, response: Response<RestaurantSubList>) {
        useful.closeLoading()
        if (response.isSuccessful) {

            if (response.body() != null) {
                result.responseRestaurant(response.body()!!, type)

            }
        } else {
//                    result.error("fsfsfsfsf"+response.toString())

            Log.d("erro", "Erro não esperado.")
        }
    }

    override fun onFailure(call: Call<RestaurantSubList>, t: Throwable) {
        useful.closeLoading()
        //Toast.makeText(context, "Não foi possível contatar o servidor.", Toast.LENGTH_SHORT).show();
        Log.d("erro", "Não foi possível contatar o servidor.")
    }





    fun buscaPorCategoria(restaurent: RestaurantSubListItem) {
        useful.openLoading()

        restaurent.id_de = 857
        type = "busca"
        restaurent.token = WSConstants.token

        val param: Call<RestaurantSubList> = service.buscaPorCategoria(restaurent)

        param.enqueue(object : Callback<RestaurantSubList> {
            override fun onResponse(call: Call<RestaurantSubList>, response: Response<RestaurantSubList>) {
                useful.closeLoading()

                if (response.isSuccessful) {
                    if (response.body() != null) {
                        result.responseRestaurant(response.body()!!, type)

                    }
                } else {
                    result.error(response.errorBody().toString())


                    Log.d("erro", "Erro não esperado.")
                }
            }

            override fun onFailure(call: Call<RestaurantSubList>, t: Throwable) {
                result.error(t.localizedMessage.toString())

                useful.closeLoading()

                //Toast.makeText(context, "Não foi possível contatar o servidor.", Toast.LENGTH_SHORT).show();
                Log.d("erro", t.message.toString())
                Log.d("erro2", t.cause.toString())
                Log.d("erro3", t.localizedMessage.toString())
                Log.d("erro4", t.suppressed.toString())
                Log.d("erro5", t.suppressedExceptions.toString())
            }
        })
    }



}

