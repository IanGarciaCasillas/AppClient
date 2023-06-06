package cat.montilivi.appclient.dades

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import java.text.SimpleDateFormat
import java.util.*


@Parcelize
class ComandaVenda(
    var IdComanda:Int,
    @SerializedName("DataComanda")
    var dataComandaString: String,
    var EstatComandaVenda:String,
    var IdClient:Int

) :Parcelable{
    val dataComanda: Date
        get() {
            val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
            val outputFormat = SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault())

            val date: Date = inputFormat.parse(dataComandaString)
            val outputString: String = outputFormat.format(date)
            return outputFormat.parse(outputString)
        }

}