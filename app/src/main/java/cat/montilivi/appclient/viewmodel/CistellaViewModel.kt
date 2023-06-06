package cat.montilivi.appclient.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import cat.montilivi.appclient.ClientManager
import cat.montilivi.appclient.dades.Article
import cat.montilivi.appclient.dades.ArticleCistella
import cat.montilivi.appclient.dades.Client
import cat.montilivi.appclient.dades.ComandaVendaDetalls
import com.google.gson.Gson
import com.google.gson.JsonObject
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

class CistellaViewModel : ViewModel() {
    private var WEB_SERVER = "https://10.0.2.2:7151"
    private var servidor: OkHttpClient = ConexioServidor()



    private var _llistaCistella:MutableLiveData<MutableList<ArticleCistella>> = MutableLiveData<MutableList<ArticleCistella>>(arrayListOf())
    private var _countTotalArticles:MutableLiveData<Int> = MutableLiveData<Int>()
    private var _countTotalPagar:MutableLiveData<Double> = MutableLiveData<Double>()
    private var _update:MutableLiveData<Boolean> = MutableLiveData<Boolean>()
    private var _canPagar:MutableLiveData<Boolean> = MutableLiveData<Boolean>()
    private var _canBuidarCistella:MutableLiveData<Boolean> = MutableLiveData<Boolean>()
    private var _stringStat:MutableLiveData<String> = MutableLiveData<String>()


    public var llistaCistella:LiveData<MutableList<ArticleCistella>> = _llistaCistella
    public var countTotalArticles:LiveData<Int> = _countTotalArticles
    public var countTotalPagar:LiveData<Double> = _countTotalPagar
    public var update:LiveData<Boolean> = _update
    public var canPagar:LiveData<Boolean> = _canPagar
    public var canBuidarCistella:LiveData<Boolean> = _canBuidarCistella
    public var stringStat:LiveData<String> = _stringStat

    public fun GetLlista(){
        if(_llistaCistella.value!!.size == 0)
            _llistaCistella.value = ClientManager.cistella!!
        Update()
    }

    fun DeleteItem(item: ArticleCistella) {
        _llistaCistella.value!!.remove(item)
        item.Article!!.Stock += item.Quantitat!!.toDouble()
        UpdateStock(item.Article!!)
        Update()
    }

    fun UpdateItem(item: ArticleCistella) {
        var idx = _llistaCistella.value!!.indexOf(item)
        _llistaCistella.value!![idx] = item
        UpdateStock(item.Article!!)
        Update()
    }

    private fun UpdateStock(article: Article) {
        val thread = Thread{
            try{
                var gson = Gson()
                var jsonBody = gson.toJson(article)

                var body = RequestBody.create("application/json; charset=utf-8".toMediaType(),jsonBody)

                var url = "$WEB_SERVER/articles/${article.IdArticle}".toHttpUrl().newBuilder()
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
                    var algo=1
                }


            }catch(e:Exception){
                Log.e("CistellaViewModel","Problemas amb la funcion UpdateStock:\n ${e.message}")
            }
        }

        thread.start()

    }

    fun ClearLlista(){
        _llistaCistella.value!!.clear()
        Update()
        _canPagar.value = false
        _canBuidarCistella.value = false
    }

    fun Update()
    {
        if(_llistaCistella.value!!.size>=1){
            _canBuidarCistella.value = true
            _canPagar.value = true


            _countTotalArticles.value = 0
            _countTotalPagar.value = 0.0

            for(item in _llistaCistella.value!!){
                _countTotalArticles.value = _countTotalArticles.value!! + item.Quantitat!!
                _countTotalPagar.value = _countTotalPagar.value!! + (item.Article!!.PreuVenta * item.Quantitat!!)
            }
        }
        else
        {
            _countTotalArticles.value = 0
            _countTotalPagar.value = 0.0
            _canBuidarCistella.value = false
            _canPagar.value = false
        }

        ClientManager.cistella = _llistaCistella.value!!
        _llistaCistella.value = _llistaCistella.value
    }

    fun EnviarComanda() {
        val thread = Thread{
            try{

                var gson = Gson()

                var llistaComandaDetalls = Detalls()
                if(llistaComandaDetalls.size==0){
                    _stringStat.postValue("NO HI HA ARTICLES EN LA CISTELLA")

                }
                else{
                    var idClient = ClientManager.client!!.IdClient

                    var jsonBody = gson.toJson(llistaComandaDetalls)

                    val body=RequestBody.create("application/json; charset=utf-8".toMediaType(),jsonBody)

                    var url = "$WEB_SERVER/comandaVenda/newComanda".toHttpUrl().newBuilder()
                        .addQueryParameter("idClient",idClient.toString())
                        .build()

                    val request = Request.Builder()
                        .url(url)
                        .addHeader("Content-Type", "application/json")
                        .post(body)
                        .build()

                    servidor.newCall(request).execute().use { resposta->
                        if(!resposta.isSuccessful)throw IOException("Unexpected code $resposta")

                        var respostaJson = JSONObject(resposta.body?.string())
                        var status:Int = respostaJson.get("statusCode").toString().toInt()

                        if(status == 200){
                            _stringStat.postValue("Comanda enviada. Gracies")

                        }
                        else{
                            _stringStat.postValue("Problemas al enviar la comanda")
                        }
                    }
                }
            }catch (e:Exception){
                Log.e("CistellaViewModel","Problemas amb la funcion EnviarComanda:\n ${e.message}")
            }
        }

        thread.start()
    }

    private fun Detalls():MutableList<ComandaVendaDetalls>{
        var llistaArticles = _llistaCistella.value!!
        var detalls:MutableList<ComandaVendaDetalls> = arrayListOf()
        for(item in llistaArticles){
            detalls.add(ComandaVendaDetalls(null,item.Article!!.IdArticle!!,item.Quantitat!!.toDouble(),null))
        }

        return detalls
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