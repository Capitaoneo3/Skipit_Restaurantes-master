package br.com.app5m.skipitrestaurantes.helper

import android.content.Context

object IntroHelper {
    const val ENTERING_FIRST_TIME = "EnteringFirstTime"
    fun storeInt(context: Context, key: String?, value: Int) {
        val prefs = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val editor = prefs.edit()
        editor.putInt(key, value)
        editor.commit()
    }

    fun getInt(context: Context, key: String?, defaultValue: Int): Int {
        return context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
            .getInt(key, defaultValue)
    }
}