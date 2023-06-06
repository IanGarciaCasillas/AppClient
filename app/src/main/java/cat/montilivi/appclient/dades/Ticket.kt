package cat.montilivi.appclient.dades

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import java.text.SimpleDateFormat
import java.util.*

@Parcelize
class Ticket(
    var IdTicket:Int,
    var NumDocument:Int,
    @SerializedName("DataTicket")
    var dataTicketString: String,
    var IdClient:Int?,
    var IdComanda:Int?,
    var IdAlbara:Int?

):Parcelable {

    val DataTicket: Date
        get() {
            val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
            val outputFormat = SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault())

            val date: Date = inputFormat.parse(dataTicketString)
            val outputString: String = outputFormat.format(date)
            return outputFormat.parse(outputString)
        }
}