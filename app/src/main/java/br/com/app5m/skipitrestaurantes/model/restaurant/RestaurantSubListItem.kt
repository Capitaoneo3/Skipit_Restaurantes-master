package br.com.app5m.skipitrestaurantes.model.restaurant

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
@Parcelize


data class RestaurantSubListItem(
    val id: Int?=null,
    var nome: String?=null,
    val nome_fantasia: String?=null,
    val razao_social: String?=null,
    val rows: Int?=null,
    var token: String?=null,
    var documento: String?=null,
    var cep: String?=null,
    var telefone: String?=null,
    var celular: String?=null,
    var avatar: String?=null,
    var uf: String?=null,
    var cidade: String?=null,
    var endereco: String?=null,
    var bairro: String?=null,
    var numero: String?=null,
    var complemento: String?=null,
    var latDe: String?=null,
    var latPara: String?=null,
    var LonPara: String?=null,
    var distancia: String?=null,
    var duracao: String?=null,
    var id_de: Int?=null,
    val email: String?=null,
    val lonDe: String?=null,

    var lon_de: String?=null,
    var lat_de: String?=null,
    var id_categoria: Int?=null


    ): Parcelable {


}
