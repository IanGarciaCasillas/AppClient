package cat.montilivi.appclient.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import cat.montilivi.appclient.dades.Client
import cat.montilivi.appclient.dades.Ticket
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.internal.canParseAsIpAddress
import org.json.JSONObject
import java.io.IOException
import java.security.cert.X509Certificate
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

class LliurarFacturaViewModel : ViewModel() {

    private var WEB_SERVER = "https://10.0.2.2:7151"
    private var servidor: OkHttpClient = ConexioServidor()


    private var _llistaHistorialTicket: MutableLiveData<MutableList<Ticket>> = MutableLiveData<MutableList<Ticket>>(arrayListOf())
    private var _txtLog:MutableLiveData<String> = MutableLiveData<String>()
    private var _numTicket:MutableLiveData<String> = MutableLiveData<String>()
    private var _numDocument:MutableLiveData<String> = MutableLiveData<String>()




    public var llistaHistorialTicket: LiveData<MutableList<Ticket>> = _llistaHistorialTicket
    public var txtLog:LiveData<String> = _txtLog
    public var numTicket:LiveData<String> = _numTicket
    public var numDocument:LiveData<String> = _numDocument



    fun LliurarFactura(numTicket:Int, numDocument:Int, client:Client)
    {
        val thread = Thread{
            try{

                var gson = Gson()

                var url = "$WEB_SERVER/tickets/LliurarFactura".toHttpUrl().newBuilder()
                    .addQueryParameter("numTicket",numTicket.toString())
                    .addQueryParameter("numDocument",numDocument.toString())
                    .build()

                var jsonBody = gson.toJson(client)
                var body = RequestBody.create("application/json; charset=utf-8".toMediaType(),jsonBody)


                var request = Request.Builder()
                    .url(url)
                    .header("Content-Type","application/json")
                    .post(body)
                    .build()

                servidor.newCall(request).execute().use { resposta ->
                    if(!resposta.isSuccessful)throw IOException("Unexpected code $resposta")

                    var respostaJson = JSONObject(resposta.body?.string())
                    var status:Int = respostaJson.get("status").toString().toInt()

                    if(status == 200){
                        _txtLog.postValue("Ticket assignat al client")
                    }
                    else if(status == 300){
                        _txtLog.postValue("No existeix el ticket")
                    }
                    else if(status == 301){
                        _txtLog.postValue("El ticket ja esta assignat a un client")
                    }

                }


            }catch(e:Exception){
                Log.e("LliurarFacturaViewModel","Problemas amb la funcio LliurarFactura")
            }
        }

        thread.start()
    }


    fun CarregarHistorialTicket(idClient:Int) {
        val thread = Thread{
            try{
                var gson=Gson()

                var url = "$WEB_SERVER/tickets/GetTicketClient".toHttpUrl().newBuilder()
                    .addQueryParameter("idClient",idClient.toString())
                    .build()

                var request = Request.Builder()
                    .url(url)
                    .header("Content-Type", "application/json")
                    .build()

                servidor.newCall(request).execute().use { resposta ->
                    if(!resposta.isSuccessful)throw IOException("Unexpected code $resposta")

                    var respostaJson = JSONObject(resposta.body?.string())

                    var status:Int = respostaJson.get("status").toString().toInt()
                    if(status == 200){
                        var jsonTickets = respostaJson.getJSONArray("tickets").toString()
                        var tipoLlista = object: TypeToken<List<Ticket>>(){}.type
                        var tickets = gson.fromJson<List<Ticket>>(jsonTickets,tipoLlista)

                        var llista:MutableList<Ticket> = arrayListOf()
                        for(item in tickets){
                            llista.add(item)
                        }

                        _llistaHistorialTicket.postValue(llista)
                        var algo=1

                    }
                    else if(status == 201){
                        _txtLog.postValue("NO HI HA TICKETS REGISTRAT")
                    }
                }
            }catch(e:Exception){
                Log.e("LliurarFacturaViewModel","Problemas en la funcio CarregarHistorialTicket")
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