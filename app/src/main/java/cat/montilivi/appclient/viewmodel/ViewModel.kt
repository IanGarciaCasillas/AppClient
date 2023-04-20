package cat.montilivi.appclient.viewmodel

import android.util.Log
import androidx.lifecycle.*
import androidx.lifecycle.ViewModel
import cat.montilivi.appclient.dades.Client
import com.google.gson.Gson
import okhttp3.*
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
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
    private val _clientActual:MutableLiveData<Client> = MutableLiveData<Client>()
    private val _txtLogLogin:MutableLiveData<String> = MutableLiveData<String>()
    //PUBLIQUES
    public val login:LiveData<Boolean> = _login
    public val clientActual:LiveData<Client> = _clientActual
    public val txtLogLogin:LiveData<String> = _txtLogLogin


    //FUNCIONS


    public fun LoginClient(correuClient:String, password:String){

        val thread = Thread{
            try {

                val url = "$WEB_SERVER/login".toHttpUrlOrNull()!!.newBuilder()
                    .addQueryParameter("correuClient", correuClient)
                    .addQueryParameter("passwordClient", password)
                    .build()

                var request = Request.Builder()
                    .url(url)
                    .header("Content-Type","application/json")
                    .build()


                var resposta :Response = servidor.newCall(request).execute()

                if(resposta.isSuccessful){

                    var respostaData = JSONObject(resposta.body?.string())

                    var statusResposta = respostaData.get("status").toString()

                    if(statusResposta == "Registrat"){

                        var jsonObjectClientString = respostaData.get("client").toString()
                        var gson = Gson()
                        var client:Client = gson.fromJson(jsonObjectClientString,Client::class.java)

                        _txtLogLogin.postValue("USUARI CORRECTE")
                        _clientActual.postValue(client)
                        _login.postValue(true)
                    }
                    else if (statusResposta == "InCorrect"){
                        //PASSWORD O CORREU INCORRECTE MIRA
                        _txtLogLogin.postValue("La contrasenya o el correu son erronis. TORNA")
                        _login.postValue(false)
                    }
                    else if(statusResposta == "SenseRegistre"){
                        _txtLogLogin.postValue("NO TENS CAP COMPTA REGISTRADA")
                        _login.postValue(false)
                    }
                }
                else{
                        throw IOException("Unexpected code $resposta")
                }
            }
            catch (e:Exception){
                Log.e("ViewModelError", "Problemas en la funcio LoginClient: \n ${e.message}")
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
                    var espera = 1
                    arr = JSONArray(resposta.body!!.string())
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