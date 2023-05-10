package cat.montilivi.appclient.viewmodel

import android.text.BoringLayout
import android.util.Base64
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import cat.montilivi.appclient.dades.Article
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import com.google.gson.reflect.TypeToken
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException
import java.security.cert.X509Certificate
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

class ArticlesViewModel : ViewModel() {
    private var WEB_SERVER = "https://10.0.2.2:7151/articles"
    private var servidor: OkHttpClient = ConexioServidor()



    private var _llistaArticles:MutableLiveData<MutableList<Article>> = MutableLiveData<MutableList<Article>>(arrayListOf())
    private var _llistaCarregada:MutableLiveData<Boolean> = MutableLiveData<Boolean>()
    private var _textLog:MutableLiveData<String> = MutableLiveData<String>()
    private var _llistaCompra:MutableLiveData<MutableList<Article>> = MutableLiveData<MutableList<Article>>(arrayListOf())
    private var _articleAdd:MutableLiveData<String> = MutableLiveData<String>()

    public var llista: LiveData<MutableList<Article>> = _llistaArticles
    public var llistaCarregada:LiveData<Boolean> =_llistaCarregada
    public var textLog:LiveData<String> = _textLog
    public var llistaCompra:LiveData<MutableList<Article>> = _llistaCompra
    public var articleAdd:LiveData<String> = _articleAdd





    public fun AfegirArticle(article:Article){
        _llistaArticles.value!!.add(article)
        _articleAdd.postValue(article.NomArticle)
    }


    public fun BuscarArticle(nomArticle:String) {
        val thread = Thread{
            try{
                var url = "$WEB_SERVER/BuscarArticle".toHttpUrl().newBuilder()
                    .addQueryParameter("nomArticle",nomArticle)
                    .build()
                var request = Request.Builder()
                    .url(url)
                    .header("Content-Type","application/json")
                    .build()

                var resposta:Response = servidor.newCall(request).execute()
                if(!resposta.isSuccessful) throw IOException("Unexpected code $resposta")

                else{
                    var resultData = resposta.body?.string()
                    var jsonResult = JSONObject(resultData)

                    var resultCode = jsonResult.get("status").toString().toInt()

                    if(resultCode == 200){
                        var jsonArticles = jsonResult.get("llista").toString()
                        var gson = Gson()

                        var tipoLlista = object:TypeToken<List<Article>>(){}.type
                        var articles = gson.fromJson<List<Article>>(jsonArticles,tipoLlista)

                        _llistaArticles.value!!.clear()
                        for(article in articles){
                            _llistaArticles.value!!.add(article)
                        }
                        _llistaCarregada.postValue(true)
                    }
                    else if(resultCode == 201){
                        _textLog.postValue("NO HI HA CAP ARTICLE QUE CONTINGUI AQUESTA PARAULA")
                    }
                }
            }catch (e:Exception){
                Log.e("ArticlesViewModel", "Problemas en al funcio GetArticles:\n ${e.message}")
            }
        }

        thread.start()
    }



    public fun GetArticles(){
        val thread = Thread{
            try{
                var url = "$WEB_SERVER".toHttpUrl().newBuilder()
                    .build()
                var request = Request.Builder()
                    .url(url)
                    .header("Content-Type","application/json")
                    .build()

                var resposta:Response = servidor.newCall(request).execute()
                if(!resposta.isSuccessful) throw IOException("Unexpected code $resposta")

                else{
                    var json = resposta.body?.string()
                    var gson =Gson()

                    var tipoLlista = object:TypeToken<List<Article>>(){}.type
                    var articles = gson.fromJson<List<Article>>(json,tipoLlista)

                    _llistaArticles.value!!.clear()
                    for(article in articles)
                    {
                        _llistaArticles.value!!.add(article)
                    }
                    _llistaCarregada.postValue(true)

                }
            }catch (e:Exception){
                Log.e("ArticlesViewModel", "Problemas en al funcio GetArticles:\n ${e.message}")
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


