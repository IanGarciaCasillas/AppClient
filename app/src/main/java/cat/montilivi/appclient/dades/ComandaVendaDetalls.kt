package cat.montilivi.appclient.dades

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class ComandaVendaDetalls(
    var IdComandaVenda: Int?,
    var IdArticle: Int,
    var QuantitatDemanada:Double,
    var QuantitatServida: Double?
):Parcelable {
}