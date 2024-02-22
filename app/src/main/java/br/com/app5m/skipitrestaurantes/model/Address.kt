package br.com.app5m.skipitrestaurantes.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize

data class Address(
    var id: String? = null,

    @field:SerializedName("id_endereco")
    var id_endereco: String? = null,

    @field:SerializedName("latitude")
var latitude: String? = null,

    @field:SerializedName("longitude")
var longitude: String? = null,

    @field:SerializedName("pt_referencia")
    var pt_referencia: String? = null,


    @field:SerializedName("cep")
var cep: String? = null,

    @field:SerializedName("uf")
var state: String? = null,

    @field:SerializedName("cidade")
var city: String? = null,

    @field:SerializedName("bairro")
var neighborhood: String? = null,

    @field:SerializedName("numero")
var number: String? = null,

    @field:SerializedName("país")
var country: String? = null,

    @field:SerializedName("complemento")
var complement: String? = null,

    @field:SerializedName("endereco")
var address: String? = null,
    var endereco_completo: String? = null,
    @field:SerializedName("token")
var token: String? = null,

    @field:SerializedName("status")
var status: String? = null,

    @field:SerializedName("msg")
var msg: String? = null,

    @field:SerializedName("rows")
var rows: String? = null,

    @field:SerializedName("distancia")
var distance: String? = null,



    @field:SerializedName("logradouro")
var logradouro: String? = null,

    @field:SerializedName("localidade")
var place: String? = null,

    @field:SerializedName("id_user")
    var id_user: String? = null,

    ): Parcelable {

}
