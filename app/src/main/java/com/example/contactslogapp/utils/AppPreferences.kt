package com.example.contactslogapp.utils

import android.content.Context
import android.content.Context.MODE_PRIVATE

class AppPreferences (var context: Context){
    private val preferences = context.getSharedPreferences("app_pref",MODE_PRIVATE)

    fun getData(key:String):String?{
        return preferences.getString(key,"0")
    }

    fun setData(key:String, value:String){
        preferences.edit().putString(key,value).apply()
    }

    fun clearData(key:String){
        preferences.edit().remove(key).apply()
    }

    fun clearAllData(){
        preferences.edit().clear().apply()
    }

    fun getLength():Int{
        return preferences.all.size
    }
}