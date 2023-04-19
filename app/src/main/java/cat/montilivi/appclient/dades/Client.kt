package cat.montilivi.appclient.dades

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
class Client(
    @SerializedName("idClient")
    var IdClient:Int ,
    @SerializedName("dni")
    var Dni:String,
    @SerializedName("nomClient")
    var NomClient:String,
    @SerializedName("cognom1Client")
    var Cognom1Client:String,
    @SerializedName("cognom2Client")
    var Cognom2Client:String,
    @SerializedName("correuClient")
    var CorreuClient:String,
    @SerializedName("contrasenyaClient")
    var ContrasenyaClient:String,
    @SerializedName("telefonClient")
    var TelefonClient:String,
    @SerializedName("direccioClient")
    var DireccioClient:String,
    @SerializedName("codicPostal")
    var CodicPostal:String,
    @SerializedName("token")
    var Token:String
) :Parcelable{

    override fun toString(): String {
        return "$NomClient $Cognom1Client: ID:$IdClient"
    }
}