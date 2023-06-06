package cat.montilivi.appclient.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import cat.montilivi.appclient.ClientManager
import cat.montilivi.appclient.dades.ComandaVenda
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import java.io.IOException
import java.security.cert.X509Certificate
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

class ComandesViewModel : ViewModel() {
    private var WEB_SERVER = "https://10.0.2.2:7151"
    private var servidor: OkHttpClient = ConexioServidor()


    private var _llistaComandes: MutableLiveData<MutableList<ComandaVenda>> = MutableLiveData<MutableList<ComandaVenda>>(arrayListOf())
    private var _stringLog:MutableLiveData<String> = MutableLiveData<String>()



    public var llistaComandes: LiveData<MutableList<ComandaVenda>> = _llistaComandes
    public var stringLog:LiveData<String> = _stringLog

    public fun GetComandes(){
        val thread = Thread{
         try{
             var gson = Gson()

             var idClient = ClientManager.client!!.IdClient

             var url = "$WEB_SERVER/comandaVenda/comandesClient".toHttpUrl().newBuilder()
                 .addQueryParameter("idClient",idClient.toString())
                 .build()

             val request = Request.Builder()
                 .url(url)
                 .header("Content-Type", "application/json")
                 .build()

             servidor.newCall(request).execute().use { resposta ->
                 if(!resposta.isSuccessful)throw IOException("Unexpected code $resposta")

                 var respostaJson = JSONObject(resposta.body?.string())

                 var status:Int = respostaJson.get("status").toString().toInt()

                 if(status == 200){
                    var jsonComanda = respostaJson.getJSONArray("comandes").toString()
                     var tipoLlista = object:TypeToken<List<ComandaVenda>>(){}.type
                     var comandes = gson.fromJson<List<ComandaVenda>>(jsonComanda,tipoLlista)


                     var llista:MutableList<ComandaVenda> = arrayListOf()
                     for(comanda in comandes){
                         llista.add(comanda)
                     }
                     _llistaComandes.postValue(llista)

                 }
                 else{
                     _stringLog.postValue("NO HI HA COMANES PENDENTS")
                 }

             }


         }catch(e:Exception){
             Log.e("ComandesViewModel","Problemas en la funcio GetComandes:\n ${e.message}")
         }
        }

        thread.start()
    }




    //FUNCIO PER FER LA CONEXIO AL SERVIDOR API
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