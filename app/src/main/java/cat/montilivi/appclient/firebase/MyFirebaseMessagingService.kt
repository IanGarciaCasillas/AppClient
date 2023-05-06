package cat.montilivi.appclient.firebase

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import cat.montilivi.appclient.MainActivity
import cat.montilivi.appclient.R
import cat.montilivi.appclient.fragments.MainFragment
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import okhttp3.internal.notify


const val CHANNEL_ID = "NOTIFICATION_CHANNEL"
const val CHANNEL_NAME = "com.example.testnotific"

public class MyFirebaseMessagingService:FirebaseMessagingService() {


    private fun generarNotificaction(title:String, message:String){
        val intent = Intent(this,MainFragment::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

        var pendingIntent:PendingIntent

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.S){
            pendingIntent = PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_MUTABLE)
        }
        else{
            pendingIntent = PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_ONE_SHOT)
        }

        var builder: NotificationCompat.Builder = NotificationCompat.Builder(applicationContext,CHANNEL_ID)
            .setSmallIcon(R.drawable.icon_app)
            .setAutoCancel(true)
            .setVibrate(longArrayOf(1000,1000,1000,1000))
            .setOnlyAlertOnce(true)
            .setContentIntent(pendingIntent)

        builder = builder.setContent(getRemoteView(title,message))

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val notificationChannel = NotificationChannel(CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH)
            notificationManager.createNotificationChannel(notificationChannel)
        }
        notificationManager.notify(0,builder.build())
    }
    private fun getRemoteView(title:String, message:String):RemoteViews?{
        val remoteView = RemoteViews("cat.montilivi.appclient",R.layout.notification)

        remoteView.setTextViewText(R.id.title,title)
        remoteView.setTextViewText(R.id.message,message)

        return remoteView
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        var titol = remoteMessage.notification!!.title.toString()
        var body = remoteMessage.notification!!.body.toString()
        generarNotificaction(titol,body)
    }


    override fun onNewToken(token: String) {
        Log.d("MyFireBaseMS", "Refreshed token: $token")
        
    }
}