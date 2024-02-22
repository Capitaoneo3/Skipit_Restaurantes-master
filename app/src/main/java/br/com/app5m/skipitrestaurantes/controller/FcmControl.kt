package br.com.app5m.skipitrestaurantes.controller

import android.content.Context
import android.util.Log
import android.widget.Toast

import br.com.app5m.skipitrestaurantes.config.RetrofitInitializer
import br.com.app5m.skipitrestaurantes.controller.webservice.WSConstants
import br.com.app5m.skipitrestaurantes.controller.webservice.WSResult
import br.com.app5m.skipitrestaurantes.controller.webservice.WebService
import br.com.app5m.skipitrestaurantes.helper.Preferences
import br.com.app5m.skipitrestaurantes.helper.Useful
import br.com.app5m.skipitrestaurantes.model.Fcm
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FcmControl(private val context: Context, private val result: WSResult) :
    Callback<List<Fcm?>> {
    private val useful: Useful
    private val fcm: Fcm = Fcm()
    private val list = false
    private var type = ""
    private val preferences: Preferences
    private val service= RetrofitInitializer().retrofit(
        true
    ).create(WebService::class.java)

 /*   private val serviceCadastro = RetrofitInitializer(WSConstants.wsUrlSort).retrofit(
        true
    ).create(WebService::class.java)*/

    override fun onResponse(call: Call<List<Fcm?>>, response: Response<List<Fcm?>>) {
        useful.closeLoading()
        if (response.isSuccessful) {
            result.resultFcm(response.body()!![0], type)
        } else {
            Toast.makeText(context, "Erro não esperado", Toast.LENGTH_SHORT).show()
        }
    }


    override fun onFailure(call:Call<List<Fcm?>>, t: Throwable) {
        useful.closeLoading()

        result.error(t.localizedMessage.toString())


        //Toast.makeText(context, "Não foi possível contatar o servidor.", Toast.LENGTH_SHORT).show();
        Log.d("erro", t.message.toString())
        Log.d("erro2", t.cause.toString())
        Log.d("erro3", t.localizedMessage.toString())
        Log.d("erro4", t.suppressed.toString())
        Log.d("erro5", t.suppressedExceptions.toString())
    }




    fun saveFcm(fcm: Fcm) {
        useful.openLoading()
        fcm.id_user = preferences.getUserData()?.id.toString()
        fcm.type = "1"
        fcm.token = WSConstants.token

        val param: Call<Fcm?> = service.saveFcm(fcm)

        param?.enqueue(object : Callback<Fcm?> {
            override fun onResponse(call: Call<Fcm?>, response: Response<Fcm?>) {
                useful.closeLoading()

                if (response.isSuccessful) {
                    if (response.body() != null) {
                        result.resultFcm(response.body()!!, type)

                    }
                }
                else if (response.code() == 401) {
//                    result.error("Email ou senha incorretos. Tente outros dados. ")
                    result.error("erro: "+response.code())
                }

                else {
                    result.error("erro: "+response.code())


                    Log.d("erro", "Erro não esperado.")
                }
            }
            override fun onFailure(call: Call<Fcm?>, t: Throwable) {
                useful.closeLoading()

                result.error(t.localizedMessage.toString())


                //Toast.makeText(context, "Não foi possível contatar o servidor.", Toast.LENGTH_SHORT).show();
                Log.d("erro", t.message.toString())
                Log.d("erro2", t.cause.toString())
                Log.d("erro3", t.localizedMessage.toString())
                Log.d("erro4", t.suppressed.toString())
                Log.d("erro5", t.suppressedExceptions.toString())
            }
        })
    }

    init {
        useful = Useful(context)
        preferences = Preferences(context)
    }
}