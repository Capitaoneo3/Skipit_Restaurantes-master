package br.com.app5m.skipitrestaurantes.model.fila

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize

data class QueueItem(
    var rows: Int?=null,
    var id_para: Int?=null,
    var status: Int?=null,
    var token: String?=null,
    var app_users_id: Int?=null,
    var day: Int?=null,
    var day_name: String?=null,
    var horario_in: String?=null,
    var horario_out: String?=null,
    var id_fila: Int?=null,
    var id_horario: Int?=null,
    var id_de: Int?=null,
    var posicao: Int?=null,
    var nome_fantasia: String?=null,
    var id: Int?=null,
    var id_filas_c: Int?=null,
    var msg: String?=null,
    var data: String?=null,
    var tipo: String?=null,



): Parcelable {

}