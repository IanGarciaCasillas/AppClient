package cat.montilivi.appclient.dades

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
class Client(
    var IdClient:Int?,
    var Dni:String,
    var NomClient:String,
    var Cognom1Client:String,
    var Cognom2Client:String,
    var CorreuClient:String,
    var ContrasenyaClient:String,
    var TelefonClient:String,
    var DireccioClient:String,
    var CodicPostal:String,
    var Token:String
) :Parcelable{

    override fun toString(): String {
        return "$NomClient $Cognom1Client: ID:$IdClient"
    }
}