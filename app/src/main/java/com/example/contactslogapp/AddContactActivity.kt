package com.example.contactslogapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.contactslogapp.models.Contact
import com.example.contactslogapp.utils.AppPreferences
import com.google.gson.Gson

class AddContactActivity : AppCompatActivity() {
    private lateinit var preferences: AppPreferences
    private val gson = Gson()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_contact)

        preferences = AppPreferences(this)

        findViewById<Button>(R.id.add_btn).setOnClickListener {
            addContact()
        }
    }

    private fun addContact(){
        val fName = findViewById<EditText>(R.id.fName_txt).text.trim().toString()
        val lName = findViewById<EditText>(R.id.lName_txt).text.trim().toString()
        val email = findViewById<EditText>(R.id.email_txt).text.trim().toString()
        val phone = findViewById<EditText>(R.id.phone_txt).text.trim().toString()

        if(fName.isEmpty()){
            showMsg("Please enter contact first name.")
        }
        else if(lName.isEmpty()){
            showMsg("Please enter contact last name.")
        }
        else if(email.isEmpty()){
            showMsg("Please enter contact email.")
        }
        else if(phone.isEmpty()){
            showMsg("Please enter contact phone number.")
        }
        else{
            if(!isEmailValid(email)){
                showMsg("Email is badly formatted.")
            }
            else if(phone.length != 11){
                showMsg("Phone number can have 11 digits only.")
            }
            else{
                val contact = Contact(fName, lName, email, phone)
                val key = preferences.getLength()
                val gsonContact = gson.toJson(contact, Contact::class.java)
                preferences.setData(key.toString(), gsonContact)
                showMsg("New Contact added successfully.")
                finish()
            }
        }
    }

    private fun isEmailValid(email: String): Boolean {
        val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
        return email.matches(emailPattern.toRegex())
    }

    private fun showMsg(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }
}