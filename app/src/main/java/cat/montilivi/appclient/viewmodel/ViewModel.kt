package cat.montilivi.appclient.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException
import java.security.cert.X509Certificate
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

public class ViewModel : ViewModel() {
    private var WEB_SERVER = "https://10.0.2.2:7151/client"
    private var servidor:OkHttpClient = ConexioServidor()
    //PARIDAS
    private val _testJson:MutableLiveData<String> = MutableLiveData<String>()
    public val testJson:LiveData<String> = _testJson
    ///---------------------------------

    //PRIVADES
    private val _login:MutableLiveData<Boolean> = MutableLiveData<Boolean>()

    //PUBLIQUES
    public val login:LiveData<Boolean> = _login


    public fun LoginClient(correuClient:String, password:String):Boolean{

        val thread = Thread{
            try{
                var json = JSONObject()
                json.put("correuClient",correuClient)
                json.put("passwordClient",password)


                val request = Request.Builder()
                    .url(WEB_SERVER)
                    .build()
                var arr:JSONArray?=null

                val parameters = "{\"$correuClient\": }"
                servidor.newCall(request).execute().use { resposta ->
                    if(!resposta.isSuccessful){
                        throw IOException("Unexpected code $resposta")
                    }
                    arr = JSONArray(resposta.body()!!.string())
                    var espera = 1
                }
            }
            catch (e:Exception){
                Log.e("ViewModelError", "Problemas en la funcio LoginClient: \n ${e.message}")
            }
        }


        thread.start()
        return false
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

    fun TestGet(){

        val thread = Thread{
            try{
                val request = Request.Builder()
                    .url(WEB_SERVER)
                    .build()
                var arr:JSONArray?=null

                servidor.newCall(request).execute().use { resposta ->
                    if(!resposta.isSuccessful){
                        throw IOException("Unexpected code $resposta")
                    }
                    arr = JSONArray(resposta.body()!!.string())
                    _testJson.postValue(arr.toString())
                }
            }
            catch (e:Exception){
                _testJson.value = "Problemas amb la funcio \"TestGet\" \n ${e.message}"
            }
        }


        thread.start()
    }
}