package com.example.contactslogapp.utils

import android.content.Context
import android.content.Context.MODE_PRIVATE

class AppPreferences (var context: Context){
    private val preferences = context.getSharedPreferences("app_pref",MODE_PRIVATE)

    fun setData(key:String, value:String){
        preferences.edit().putString(key,value).apply()
    }

    fun clearAllData(){
        preferences.edit().clear().apply()
    }

    fun deleteContact(key: String) {
        preferences.edit().remove(key).apply()
    }

    fun getLength():Int{
        return preferences.all.size
    }

    fun getData(key: String): String? {
        return preferences.getString(key, null)
    }

    fun getKey(index: Int): String {
        return preferences.all.keys.elementAt(index)
    }

    fun saveUpdatedData(key: String, contact: String) {
        preferences.edit().putString(key, contact).apply()
    }

}