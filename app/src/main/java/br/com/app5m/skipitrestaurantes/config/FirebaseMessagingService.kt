package br.com.app5m.skipitrestaurantes.config

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import br.com.app5m.skipitrestaurantes.R
import br.com.app5m.skipitrestaurantes.ui.activity.HomeActivity
import br.com.app5m.skipitrestaurantes.ui.activity.QueueActivity
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class FirebaseMessagingService: FirebaseMessagingService() {


    private val TAG = "notifica"
    private var intent: Intent? = null

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        //log para ver os dados que estao vindo
        Log.i(TAG, "onMessageReceived: " + remoteMessage.data)

        if (remoteMessage.data.isNotEmpty()) {
            val title = remoteMessage.data["titulo"]
            val body = remoteMessage.data["descricao"]
            val type = remoteMessage.data["type"]

            setMessage(title, body,type)
        }
    }

    override fun onNewToken(s: String) {
        super.onNewToken(s)
        Log.i(TAG, "onNewToken: $s")
    }

    @SuppressLint("UnspecifiedImmutableFlag", "LaunchActivityFromNotification")
    private fun setMessage(title: String?, body: String? , type: String?) {

        val channel = getString(R.string.default_notification_channel_id)
        val uriSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        var notifyScreenValue: QueueActivity.MainScreenStage? = null


        intent = Intent(this, HomeActivity::class.java)

        val broadcastManager = LocalBroadcastManager.getInstance(this)
        val intentBroadcast = Intent("Notification")
/*        1 - entrou na fila
        2 - atualizacao de posicao
        3 - saiu da fila*/
        when (type) {
            "1" -> {
                notifyScreenValue = QueueActivity.MainScreenStage.ENTER_QUEUE
            }
            "2"->{
                notifyScreenValue = QueueActivity.MainScreenStage.UPDATE_POSITION
            }
            "3" ->{
                notifyScreenValue = QueueActivity.MainScreenStage.EXIT_QUEUE
            }
            //DEFAULT NOTIFICATIONS
            else -> {

            }
        }


        if (notifyScreenValue != null) {

            intent!!.putExtra("notifyScreen", notifyScreenValue)
            intentBroadcast.putExtra("notifyScreen", notifyScreenValue)
        }


        val pendingIntent: PendingIntent = PendingIntent.getBroadcast(
            baseContext,
            0, intent!!,
            PendingIntent.FLAG_IMMUTABLE
        )

        broadcastManager.sendBroadcast(intentBroadcast)


        val notification = NotificationCompat.Builder(this, channel)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentTitle(title)
            .setContentText(body)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setSound(uriSound)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val nChannel =
                NotificationChannel(channel, "channel", NotificationManager.IMPORTANCE_HIGH)
            notificationManager.createNotificationChannel(nChannel)
        }
        notificationManager.notify(0, notification.build())

    }

}
