package cat.montilivi.appclient.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import cat.montilivi.appclient.dades.Article
import cat.montilivi.appclient.dades.ComandaVenda
import cat.montilivi.appclient.dades.ComandaVendaDetalls
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.android.awaitFrame
import kotlinx.coroutines.awaitAll
import okhttp3.FormBody
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException
import java.security.cert.X509Certificate
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

class ComandesDetallsViewModel : ViewModel() {
    private var WEB_SERVER = "https://10.0.2.2:7151"
    private var servidor: OkHttpClient = ConexioServidor()


    private var _nomArticle:MutableLiveData<String> = MutableLiveData<String>()
    private var _llistaDetalls:MutableLiveData<MutableList<ComandaVendaDetalls>> = MutableLiveData<MutableList<ComandaVendaDetalls>>(arrayListOf())
    private var _llistaArticles:MutableLiveData<MutableList<Article>> = MutableLiveData<MutableList<Article>>(arrayListOf())
    private var _llistaIdArticles:MutableLiveData<MutableList<Int>> = MutableLiveData<MutableList<Int>>(arrayListOf())
    private var _estatComanda:MutableLiveData<Boolean> = MutableLiveData<Boolean>()
    private var _totalArticles:MutableLiveData<Int> = MutableLiveData<Int>(0)
    private var _totalPreu:MutableLiveData<Double> = MutableLiveData<Double>(0.0)
    private var _totOK:MutableLiveData<Boolean> = MutableLiveData<Boolean>()
    private var _totOK2:MutableLiveData<Boolean> = MutableLiveData<Boolean>()




    public var nomArticle: LiveData<String> = _nomArticle
    public var llistaDetalls:LiveData<MutableList<ComandaVendaDetalls>> = _llistaDetalls
    public var llistaArticles:LiveData<MutableList<Article>> = _llistaArticles
    public var llistaIdArticles:LiveData<MutableList<Int>> = _llistaIdArticles
    public var totalArticles:MutableLiveData<Int> = _totalArticles
    public var totalPreu:MutableLiveData<Double> = _totalPreu
    public var totOk:MutableLiveData<Boolean> = _totOK
    public var totOk2:MutableLiveData<Boolean> = _totOK2

    public fun CarregarDetalls(comanda:ComandaVenda){
        var thread = Thread{
            try{

                var gson = Gson()

                var url = "$WEB_SERVER/comandaVendaDetalls/GetDetalls".toHttpUrl().newBuilder()
                    .addQueryParameter("idComanda",comanda.IdComanda.toString())
                    .build()


                var request = Request.Builder()
                    .url(url)
                    .header("Content-Type", "application/json")
                    .build()

                servidor.newCall(request).execute().use { resposta->
                    if(!resposta.isSuccessful)throw IOException("unexpected code $resposta")

                    var respostaJson = JSONObject(resposta.body?.string())

                    var jsonDetalls = respostaJson.getJSONArray("detalls").toString()


                    var tipoLlista = object: TypeToken<List<ComandaVendaDetalls>>(){}.type
                    var detalls = gson.fromJson<List<ComandaVendaDetalls>>(jsonDetalls,tipoLlista)


                    var llista:MutableList<ComandaVendaDetalls> = arrayListOf()
                    var llistaIds:MutableList<Int> = arrayListOf()

                    for(detall in detalls ){
                        llista.add(detall)
                        llistaIds.add(detall.IdArticle)
                    }

                    if(comanda.EstatComandaVenda == "Pendent")
                        _estatComanda.postValue(true)
                    else
                        _estatComanda.postValue(false)


                    _llistaDetalls.postValue(llista)
                    _llistaIdArticles.postValue(llistaIds)

                   _totOK.postValue(true)

                }

            }catch (e:Exception){
                Log.e("ComandesDetallsViewModel","Problemas en la funcio CarregarDetalls:\n ${e.message}")
            }
        }

        thread.start()

    }

    public fun GetArticlesDetalls(){
        var thread = Thread{
            try{
                var gson = Gson()

                var url = "$WEB_SERVER/articles/GetArticlesDetalls".toHttpUrl().newBuilder()
                    .build()

                var jsonBody = gson.toJson(_llistaIdArticles.value)

                var body = RequestBody.create("application/json; charset=utf-8".toMediaType(),jsonBody)

                var request = Request.Builder()
                    .url(url)
                    .header("Content-Type", "application/json")
                    .post(body)
                    .build()


                servidor.newCall(request).execute().use { resposta ->
                    if(!resposta.isSuccessful)throw IOException("Unexpected code $resposta")

                    var respostaJson = JSONObject(resposta.body?.string())

                    var jsonArticles = respostaJson.getJSONArray("listArticles").toString()
                    var tipoList = object:TypeToken<List<Article>>(){}.type
                    var articles = gson.fromJson<List<Article>>(jsonArticles,tipoList)

                    var llistaDetalls = _llistaDetalls.value!!

                    var subTotalPreu=0.0
                    var subArticlesTotal=0.0


                    if(_estatComanda.value!!){
                        for(idx in 0 until articles.size){
                            subTotalPreu += articles[idx].PreuVenta.toDouble() * llistaDetalls[idx].QuantitatDemanada!!.toDouble()
                            subArticlesTotal += llistaDetalls[idx].QuantitatDemanada!!.toDouble()

                        }
                    }
                    else{
                        for(idx in 0 until articles.size){
                            subTotalPreu += articles[idx].PreuVenta.toDouble() * llistaDetalls[idx].QuantitatServida!!.toDouble()
                            subArticlesTotal += llistaDetalls[idx].QuantitatServida!!.toDouble()

                        }
                    }

                    _llistaArticles.postValue(articles.toMutableList())
                    _totalPreu.postValue(subTotalPreu)
                    _totalArticles.postValue(subArticlesTotal.toInt())

                    totOk2.postValue(true)
                }


            }catch (e:Exception){
                Log.e("ComandesDetallsViewModel","Problemas amb la funcio GetArticlesDetalls:\n ${e.message}")
            }
        }

        thread.start()
    }

    public fun NomArticle(idArticle:Int){
        var thread = Thread{
            try{
                var gson = Gson()

                var url = "$WEB_SERVER/articles/$idArticle".toHttpUrl().newBuilder()
                    .build()

                var request = Request.Builder()
                    .url(url)
                    .header("Content-Type", "application/json")
                    .build()


                servidor.newCall(request).execute().use { resposta ->
                    if(!resposta.isSuccessful)throw IOException("Unexpected code $resposta")

                    var resultData = resposta.body?.string()
                    var jsonResult = JSONObject(resultData)

                    var article:Article = gson.fromJson((jsonResult.toString()),Article::class.java)

                    //_nomArticle.postValue(article.NomArticle)
                   // var llista = _llistaNomArticles.value!!
                   // llista.add(article.NomArticle)
                    var llistaDetalls = _llistaDetalls.value!!
                    //_llistaNomArticles.postValue(llista)
                }


            }catch (e:Exception){
             Log.e("ComandesDetallsViewModel","Problemas amb la funcio NomArticle:\n ${e.message}")
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