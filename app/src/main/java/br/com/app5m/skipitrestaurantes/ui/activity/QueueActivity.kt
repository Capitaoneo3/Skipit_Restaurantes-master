package br.com.app5m.skipitrestaurantes.ui.activity

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.navigation.fragment.findNavController
import br.com.app5m.skipitrestaurantes.R
import br.com.app5m.skipitrestaurantes.controller.QueueControl
import br.com.app5m.skipitrestaurantes.controller.webservice.WSResult
import br.com.app5m.skipitrestaurantes.helper.Useful
import br.com.app5m.skipitrestaurantes.model.fila.Queue
import br.com.app5m.skipitrestaurantes.model.fila.QueueItem
import br.com.app5m.skipitrestaurantes.ui.dialog.AtentionMessageDialog
import br.com.app5m.skipitrestaurantes.ui.dialog.RightMessageDialog
import br.com.app5m.skipitrestaurantes.ui.dialog.TanksForPurchaseDialog
import com.google.android.gms.maps.model.LatLng
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_queue.*
import kotlinx.android.synthetic.main.activity_queue.name
import java.lang.NullPointerException


class QueueActivity : AppCompatActivity() {
    var id_filas_c: Int? = null
    var id_de: Int? = null
    var id_para: Int? = null
    var id_horario: Int? = null


    enum class MainScreenStage {
        ENTER_QUEUE,
        UPDATE_POSITION,
        EXIT_QUEUE
    }

    val _screenStageLiveData = MutableLiveData<MainScreenStage>()
    val screenStageLiveData: LiveData<MainScreenStage> = _screenStageLiveData

    private val myReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {

            if (intent.extras != null) {

                var screenStage: MainScreenStage? =
                    intent.getSerializableExtra("notifyScreen") as MainScreenStage?

                notifyScreenStageChanged(screenStage!!)

            }
        }
    }

    fun notifyScreenStageChanged(newStage: MainScreenStage) {

        try {

            when (newStage) {
                MainScreenStage.ENTER_QUEUE -> {
                    entrar()
                }
                MainScreenStage.UPDATE_POSITION -> {
                    updatePosition()
                }
                MainScreenStage.EXIT_QUEUE -> {
                    sairDafila()

                }
            }

        } catch (eNull: NullPointerException) {

        }


    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_queue)
        LocalBroadcastManager.getInstance(this).registerReceiver(
            myReceiver,
            IntentFilter("Notification")
        )

        if (getIntent().getExtras() != null) {
            val args = ((intent.getParcelableExtra("bundleQueue") as? QueueItem)!!)

            countDownText.text = args.posicao.toString()
            name.text = args.nome_fantasia.toString()

            id_filas_c = args?.id_filas_c
            id_de = args?.id_de
            id_para = args?.id_para
            id_horario = args?.id_horario
        }



        animationView.playAnimation()

        closeBt.setOnClickListener {
            sairDafilaNaApi()
//            sairDafila()
        }

    }

    fun entrar() {
        tanksForPurchaseDialog("A espera acabou. Você completou a fila!")
    }

    fun updatePosition() {
        /*    {
                  "id_de": 864,
                  "id_para": 874,
                  "id_horario": 2,
                  "status": 1,
                  "token": "xvCAmyyPGh"
              }*/
        var queueModel = QueueItem()
        queueModel.id_de = id_de
        queueModel.id_para = id_para
        queueModel.id_horario = id_horario
        queueModel.status = 1

        QueueControl(this, object : WSResult {
            override fun responseQueue(queue: Queue, type: String) {
                super.responseQueue(queue, type)
                if (!queue.isNullOrEmpty()){

                    countDownText.text = queue[0].posicao.toString()
                }
            }

            override fun error(error: String) {
                super.error(error)
                dialogshowAtention(error)
            }
        }).posicaonafila(queueModel)

    }

    fun sairDafilaNaApi() {
         val useful: Useful = Useful(this)
        /*       {
           "id_filas_c": 9,
           "status": 2,
           "token": "xvCAmyyPGh"
       }*/
        var queueModel = QueueItem()
        queueModel.id_filas_c = id_filas_c
        queueModel.status = 3
        QueueControl(this, object : WSResult {
            override fun responseQueue(queue: Queue, type: String) {
                super.responseQueue(queue, type)
                useful.closeLoading()

            }

            override fun error(error: String) {
                super.error(error)
                useful.closeLoading()

                dialogshowAtention(error)
            }
        }).sairDaUserfila(queueModel)

        val myIntent = Intent(this, HomeActivity::class.java)
        finishAffinity()
        this.startActivity(myIntent)
    }

    fun sairDafila() {


        val myIntent = Intent(this, HomeActivity::class.java)
        finishAffinity()
        this.startActivity(myIntent)
    }

    override fun onRestart() {
        super.onRestart()
        updatePosition()
    }

    fun tanksForPurchaseDialog(message: String) {
        val dialog = TanksForPurchaseDialog(this, message)

        supportFragmentManager?.let { it1 -> dialog.show(it1, "BadMessageDialog") }
    }


    fun dialogshowAtention(message: String) {
        val dialog = AtentionMessageDialog(message)

        supportFragmentManager?.let { it1 -> dialog.show(it1, "BadMessageDialog") }
    }

    fun dialogshowRight(message: String) {
        val dialog = RightMessageDialog(message)

        supportFragmentManager?.let { it1 -> dialog.show(it1, "BadMessageDialog") }
    }
}