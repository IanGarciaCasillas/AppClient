package cat.montilivi.appclient.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import cat.montilivi.appclient.dades.Client
import com.google.gson.Gson
import okhttp3.MediaType
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

class RegistreViewModel : ViewModel() {
    private var WEB_SERVER = "https://10.0.2.2:7151/client"
    private var servidor: OkHttpClient = ConexioServidor()



    private var _clientAccept:MutableLiveData<Boolean> = MutableLiveData<Boolean>()
    private var _textRegistre:MutableLiveData<String> = MutableLiveData<String>()
    private var _client:MutableLiveData<Client> = MutableLiveData<Client>()

    public var clientAccept:LiveData<Boolean> = _clientAccept
    public var textRegistre:LiveData<String> = _textRegistre
    public var client:LiveData<Client> = _client

    //Funcio per Registrar CLIENT
    public fun CrearCompte(client:Client){
        val thread = Thread {
            try
            {

                var gson = Gson()
                var clientJson = gson.toJson(client)


                val body = RequestBody.create("application/json; charset=utf-8".toMediaType(), clientJson.toString())

                val request = Request.Builder()
                    .url(WEB_SERVER)
                    .post(body)
                    .build()

                servidor.newCall(request).execute().use { resposta ->
                    if (!resposta.isSuccessful) throw IOException("Unexpected code $resposta")
                    //Imprimir el nombre d'elements
                    var resultData = resposta.body?.string()
                    var jsonResult = JSONObject(resultData)

                    var resultCode = jsonResult.get("status").toString().toInt()
                    if(resultCode == 200){
                        var jsonNewClient= jsonResult.get("newClient").toString()
                        var nada = 1
                        var clientSelect:Client = gson.fromJson(jsonNewClient,Client::class.java)

                        _client.postValue(clientSelect)
                        _textRegistre.postValue("REGISTRE CORRECTE")
                        _clientAccept.postValue(true)

                    }
                    else if(resultCode == 201){
                        _textRegistre.postValue("Correu ja existent")
                        _clientAccept.postValue(false)
                    }


                    var espera = 1
                }
            } catch (e: Exception) {
                e.printStackTrace()
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