package cat.montilivi.appclient.dades

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
class Article(
    var IdArticle:Int?,
    var NomArticle:String,
    var DescripcioArticle:String,
    var PreuVenta:Double,
    var TipusUnitat:String,
    var Stock:Double,
    var MinimStock:Double,
    var AutoStock:Double,
    var IdCategoria:Int?,
    var IdProveidorHabitual:Int?,
    var FotoArticle:String
):Parcelable {
}