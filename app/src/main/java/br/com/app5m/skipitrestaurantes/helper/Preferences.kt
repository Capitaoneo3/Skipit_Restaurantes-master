package br.com.app5m.skipitrestaurantes.helper

import android.content.Context

import br.com.app5m.skipitrestaurantes.model.Address
import br.com.app5m.skipitrestaurantes.model.user.User
import br.com.app5m.skipitrestaurantes.model.user.UserItem
import com.google.gson.Gson

class Preferences(context: Context?) {

    /**
     * Preferences
     *
     * @author Android version: Wesley Costa
     * @since Version 1.0.3
     * @Created  06/2020 - 02/2021
     */

    private var preferences = context?.getSharedPreferences("high.preference", 0)
    private var editor = preferences?.edit()

    fun setUserData(user: UserItem?) {
        val data = Gson().toJson(user)
        editor?.putString("getUserData", data)
        editor?.commit()
    }
    fun clearUserLocation(){
        editor?.remove("location")
        editor?.commit()
    }
    fun setUserLocation(location: Address?) {
        val dados = Gson().toJson(location)
        editor?.putString("location", dados)
        editor?.commit()
    }
    fun getUserLocation(): Address? {
        val location: Address
        val gson = Gson()
        val data = preferences?.getString("location", "")
        return if (data!!.isNotEmpty()) {
            location = gson.fromJson(data, Address::class.java)
            location
        } else null
    }


    fun getUserData(): UserItem? {
        val user: UserItem
        val gson = Gson()
        val data = preferences?.getString("getUserData", "")
        return if (data != null && data.isNotEmpty()) {
            user = gson.fromJson(data, UserItem::class.java)
            user
        } else null
    }



    fun setLogin(enable: Boolean){
        editor?.putBoolean("login", enable)
        editor?.commit()
    }


    fun getLogin(): Boolean? {
        return preferences?.getBoolean("login", false)
    }




    fun clearUserData(){
        editor?.remove("getUserData")
        editor?.remove("login")
        editor?.remove("location")
        editor?.remove("tokenFcm")
        editor?.remove("currentLocationEnabled")
        editor?.commit()
    }



    fun setLocationAux(num: String) {
        editor?.putString("locationAux", num)
        editor?.commit()
    }
    fun getLocationAux(): String? {
        val data = preferences?.getString("locationAux", "")
        return data
    }
    fun clearUserLocationAux() {
        editor?.remove("locationAux")?.commit()
    }
    fun firstUser(): String? {
        return preferences?.getString("user", "0")
    }



    fun getInstanceTokenFcm(): String {
        return preferences?.getString("tokenFcm", "")!!
    }
    fun saveInstanceTokenFcm(key: String?, value: String) {
        editor?.putString(key, value)
        editor!!.commit()
    }






}