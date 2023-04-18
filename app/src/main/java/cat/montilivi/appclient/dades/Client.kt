package cat.montilivi.appclient.dades

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
class Client(
    @SerializedName("id")
    var Id:Int ,
    @SerializedName("correu")
    var Correu:String,
    @SerializedName("contrasenya")
    var Contrasenya:String,
    @SerializedName("nom")
    var Nom:String,
    @SerializedName("cognom")
    var Cognom:String,
    @SerializedName("token")
    var Token:String
) :Parcelable{

    override fun toString(): String {
        return "$Nom $Cognom: ID:$Id"
    }
}