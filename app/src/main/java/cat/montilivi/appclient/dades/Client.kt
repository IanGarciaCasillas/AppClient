package cat.montilivi.appclient.dades

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class Client(
    var Id:Int=0 ,
    var Correu:String?=null,
    var Contrasenya:String?=null,
    var Nom:String?=null,
    var Cognom:String?=null,
    var Token:String?=null
) :Parcelable{

    override fun toString(): String {
        return "$Nom $Cognom: ID:$Id"
    }
}