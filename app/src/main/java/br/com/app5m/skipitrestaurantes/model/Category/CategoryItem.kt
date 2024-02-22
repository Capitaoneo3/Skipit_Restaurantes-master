package br.com.app5m.skipitrestaurantes.model.Category

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize

data class CategoryItem(
    val id: Int?=null,
    val nome: String?=null,
    val rows: Int?=null,
    val status: Int?=null,
    val url: String?=null,
    var token: String?=null,

    ): Parcelable {

}