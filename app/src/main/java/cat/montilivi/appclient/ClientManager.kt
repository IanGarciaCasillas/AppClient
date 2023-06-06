package cat.montilivi.appclient

import cat.montilivi.appclient.dades.Article
import cat.montilivi.appclient.dades.ArticleCistella
import cat.montilivi.appclient.dades.Client

object ClientManager{
    var client: Client?=null
    var token:String?=null
    var cistella:MutableList<ArticleCistella> = arrayListOf()

}