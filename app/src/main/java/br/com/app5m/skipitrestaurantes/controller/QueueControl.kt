package br.com.app5m.skipitrestaurantes.controller


import android.content.Context
import android.util.Log
import br.com.app5m.skipitrestaurantes.config.RetrofitInitializer
import br.com.app5m.skipitrestaurantes.controller.webservice.WSConstants
import br.com.app5m.skipitrestaurantes.controller.webservice.WSResult
import br.com.app5m.skipitrestaurantes.controller.webservice.WebService
import br.com.app5m.skipitrestaurantes.helper.Preferences
import br.com.app5m.skipitrestaurantes.helper.Useful
import br.com.app5m.skipitrestaurantes.model.Category.Category
import br.com.app5m.skipitrestaurantes.model.fila.Queue
import br.com.app5m.skipitrestaurantes.model.fila.QueueItem

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class QueueControl(context: Context, private val result: WSResult) : Callback<Queue> {
    val context = context
    private val useful: Useful = Useful(context)
    private val preferences: Preferences = Preferences(context)

    private val service = RetrofitInitializer().retrofit(
        true
    ).create(WebService::class.java)
    private var type = ""

    override fun onResponse(call: Call<Queue>, response: Response<Queue>) {
        useful.closeLoading()
        if (response.isSuccessful) {
            if (response.body() != null) {
                result.responseQueue(response.body()!!, type)

            }
        } else {
//                    result.error("fsfsfsfsf"+response.toString())
            Log.d("erro", "Erro não esperado.")
        }
    }

    override fun onFailure(call: Call<Queue>, t: Throwable) {
        useful.closeLoading()

        //Toast.makeText(context, "Não foi possível contatar o servidor.", Toast.LENGTH_SHORT).show();
        Log.d("erro", "Não foi possível contatar o servidor.")
    }

//    listhorariosfiladia
    fun pessoasnafilaqnt(queueBody:QueueItem) {
        useful.openLoading()
        type = "QueueBody"
        queueBody.token = WSConstants.token

        val param: Call<Queue> = service.pessoasnafilaqnt(queueBody)

        param.enqueue(object : Callback<Queue> {
            override fun onResponse(call: Call<Queue>, response: Response<Queue>) {
                useful.closeLoading()

                if (response.isSuccessful) {
                    if (response.body() != null) {
                        result.responseQueue(response.body()!!, type)
                                            }
                }else {
                    result.error(response.errorBody().toString())


                    Log.d("erro", "Erro não esperado.")
                }
            }

            override fun onFailure(call: Call<Queue>, t: Throwable) {
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


    fun listhorariosfila(queueBody:QueueItem) {
        useful.openLoading()
        type = "QueueBody"
         queueBody
        queueBody.token = WSConstants.token

        val param: Call<Queue> = service.listhorariosfila(queueBody)

        param.enqueue(object : Callback<Queue> {
            override fun onResponse(call: Call<Queue>, response: Response<Queue>) {
                useful.closeLoading()

                if (response.isSuccessful) {
                    if (response.body() != null) {
                        result.responseQueue(response.body()!!, type)
                    }
                }else {
                    result.error(response.errorBody().toString())


                    Log.d("erro", "Erro não esperado.")
                }
            }

            override fun onFailure(call: Call<Queue>, t: Throwable) {
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


    fun listhorariosfiladia(queueBody:QueueItem) {
        useful.openLoading()
        type = "QueueBody"
         queueBody
        queueBody.token = WSConstants.token

        val param: Call<Queue> = service.listhorariosfiladia(queueBody)

        param.enqueue(object : Callback<Queue> {
            override fun onResponse(call: Call<Queue>, response: Response<Queue>) {
                useful.closeLoading()

                if (response.isSuccessful) {
                    if (response.body() != null) {
                        result.responseQueue(response.body()!!, type)
                    }
                }else {
                    result.error(response.errorBody().toString())


                    Log.d("erro", "Erro não esperado.")
                }
            }

            override fun onFailure(call: Call<Queue>, t: Throwable) {
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


    fun inserirUserfila(queueBody:QueueItem) {
        useful.openLoading()
        type = "QueueBody"
         queueBody
        queueBody.token = WSConstants.token

        val param: Call<Queue> = service.inserirUserfila(queueBody)

        param.enqueue(object : Callback<Queue> {
            override fun onResponse(call: Call<Queue>, response: Response<Queue>) {
                useful.closeLoading()

                if (response.isSuccessful) {
                    if (response.body() != null) {
                        result.responseQueue(response.body()!!, type)
                    }
                }else {
                    result.error(response.errorBody().toString())


                    Log.d("erro", "Erro não esperado.")
                }
            }

            override fun onFailure(call: Call<Queue>, t: Throwable) {
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

    fun sairDaUserfila(queueBody:QueueItem) {

        useful.openLoading()
        type = "QueueBody"
        queueBody
        queueBody.token = WSConstants.token

        val param: Call<Queue> = service.atualizarUsuarioNaFila(queueBody)

        param.enqueue(object : Callback<Queue> {
            override fun onResponse(call: Call<Queue>, response: Response<Queue>) {


                if (response.isSuccessful) {
                    if (response.body() != null) {
                        result.responseQueue(response.body()!!, type)
                    }
                }else {
                    result.error(response.errorBody().toString())


                    Log.d("erro", "Erro não esperado.")
                }
            }

            override fun onFailure(call: Call<Queue>, t: Throwable) {
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


    fun posicaonafila(queueBody:QueueItem) {
    /*    {
            "id_de": 864,
            "id_para": 874,
            "id_horario": 2,
            "status": 1,
            "token": "xvCAmyyPGh"
        }*/
        useful.openLoading()
        type = "QueueBody"
        queueBody
        queueBody.token = WSConstants.token

        val param: Call<Queue> = service.posicaonafila(queueBody)

        param.enqueue(object : Callback<Queue> {
            override fun onResponse(call: Call<Queue>, response: Response<Queue>) {
                useful.closeLoading()

                if (response.isSuccessful) {
                    if (response.body() != null) {
                        result.responseQueue(response.body()!!, type)
                    }
                }else {
                    result.error(response.errorBody().toString())


                    Log.d("erro", "Erro não esperado.")
                }
            }

            override fun onFailure(call: Call<Queue>, t: Throwable) {
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

    fun historiconafila(queueBody:QueueItem) {
        /*    {
                 "id_de": 906,
    "token": "xvCAmyyPGh"
            }*/
        useful.openLoading()
        type = "QueueBody"
        queueBody
        queueBody.token = WSConstants.token

        val param: Call<Queue> = service.historiconafila(queueBody)

        param.enqueue(object : Callback<Queue> {
            override fun onResponse(call: Call<Queue>, response: Response<Queue>) {
                useful.closeLoading()

                if (response.isSuccessful) {
                    if (response.body() != null) {
                        result.responseQueue(response.body()!!, type)
                    }
                }else {
                    result.error(response.errorBody().toString())


                    Log.d("erro", "Erro não esperado.")
                }
            }

            override fun onFailure(call: Call<Queue>, t: Throwable) {
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

