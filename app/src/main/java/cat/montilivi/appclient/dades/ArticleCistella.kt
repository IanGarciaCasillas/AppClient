package cat.montilivi.appclient.dades

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class ArticleCistella(
    var Article:Article?,
    var Quantitat:Int?
):Parcelable {
}