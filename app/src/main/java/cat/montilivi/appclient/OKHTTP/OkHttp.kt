package cat.montilivi.appclient.OKHTTP

import android.app.Application
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONArray
import java.io.IOException
import java.security.cert.X509Certificate
import java.util.concurrent.LinkedBlockingQueue
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager




class OkHttp {
    private var WEB_SERVER = "https://10.0.2.2:7151/client"
    private var client: OkHttpClient = CrearClient()




    fun CrearClient():OkHttpClient
    {
        //set self sign certificate
        val trustAllCerts = arrayOf<TrustManager>(object : X509TrustManager {
            override fun checkClientTrusted(chain: Array<out X509Certificate>?, authType: String?) {
            }

            override fun checkServerTrusted(chain: Array<out X509Certificate>?, authType: String?) {
            }

            override fun getAcceptedIssuers() = arrayOf<X509Certificate>()
        })
        val sslContext = SSLContext.getInstance("SSL")
        sslContext.init(null, trustAllCerts, java.security.SecureRandom())

        // Create an ssl socket factory with our all-trusting manager
        val sslSocketFactory = sslContext.socketFactory

        // connect to server
        return OkHttpClient.Builder().sslSocketFactory(sslSocketFactory, trustAllCerts[0] as X509TrustManager).hostnameVerifier{ _, _ -> true }.build()
    }


    fun TestGet(): String {
        val queue = LinkedBlockingQueue<String>()
        val thread = Thread{
            try {
                val request = Request.Builder()
                    .url(WEB_SERVER)
                    .build()

                var arr: JSONArray? = null

                client.newCall(request).execute().use { resposta ->
                    if (!resposta.isSuccessful) throw IOException("Unexpected code $resposta")

                    arr = JSONArray(resposta.body()!!.string())
                    println(arr?.length())
                }

                queue.add(arr.toString())
            }
            catch (e: Exception) {
                e.printStackTrace()
                queue.add("Problemas amb la funcio")
            }
        }
        thread.start()
        return queue.take()
    }

}
