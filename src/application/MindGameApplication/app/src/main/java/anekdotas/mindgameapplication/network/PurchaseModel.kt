package anekdotas.mindgameapplication.network

import com.squareup.moshi.Json

data class PurchaseModel(
    @Json(name="ids")
    var purchases : MutableList<Int> = mutableListOf(0)
)