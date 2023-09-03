package hm.bytefy.imagecompress.lib

import android.content.Context

class Lib(private val context: Context) {
    //get and set cache
    fun setCache(key: String, value: String) {
        val pref = context.getSharedPreferences(context.packageName, Context.MODE_PRIVATE)
        val editor = pref.edit()
        editor.putString(key, value)
        editor.apply()
    }

    fun getCache(key: String): String {
        val pref = context.getSharedPreferences(context.packageName, Context.MODE_PRIVATE)
        val defaultValue = "0"
        return pref.getString(key, defaultValue).toString()
    }
}