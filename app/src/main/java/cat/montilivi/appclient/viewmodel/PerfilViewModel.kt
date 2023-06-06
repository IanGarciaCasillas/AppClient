package cat.montilivi.appclient.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import cat.montilivi.appclient.dades.Client
import com.google.gson.Gson
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import org.json.JSONObject
import java.io.IOException
import java.security.cert.X509Certificate
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

class PerfilViewModel : ViewModel() {

    private var WEB_SERVER = "https://10.0.2.2:7151"
    private var servidor: OkHttpClient = ConexioServidor()




    private var _txtLog:MutableLiveData<String> = MutableLiveData<String>()



    public var txtLog: LiveData<String> = _txtLog



    fun UpdateClient(client: Client) {
        val thread = Thread{
            try{
                var gson = Gson()
                var jsonBody = gson.toJson(client)

                var body = RequestBody.create("application/json; charset=utf-8".toMediaType(),jsonBody)

                var url = "$WEB_SERVER/client/${client.IdClient}".toHttpUrl().newBuilder()
                    .build()

                var request = Request.Builder()
                    .url(url)
                    .addHeader("Content-Type", "application/json")
                    .put(body)
                    .build()

                servidor.newCall(request).execute().use { resposta->
                    if(!resposta.isSuccessful)throw IOException("Unexpected code $resposta")

                    var respostaJson = JSONObject(resposta.body?.string())

                    var status:Int = respostaJson.get("status").toString().toInt()

                    if(status == 200){
                        _txtLog.postValue("Informacio actualitzada")
                    }

                }




            }catch(e:Exception){
                Log.e("PerfilViewModel","Porblemas amb la funcio UpdateClient: \n ${e.message}")
            }
        }

        thread.start()

    }




    //Funcio per poder fer la conexio al Servidor Web API
    private fun ConexioServidor():OkHttpClient{
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
}