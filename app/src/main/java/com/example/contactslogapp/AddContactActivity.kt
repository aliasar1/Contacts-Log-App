package com.example.contactslogapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import com.example.contactslogapp.models.Contact
import com.example.contactslogapp.utils.AppPreferences
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import java.util.*

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

        findViewById<Button>(R.id.cancel_btn).setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

    private fun addContact(){
        try {
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
                else if(!isNumeric(phone)){
                    showMsg("Phone number can have numeric digits only.")
                }
                else{
                    val id = UUID.randomUUID().toString()
                    val contact = Contact(id, fName, lName, email, phone)
                    val gsonContact = gson.toJson(contact, Contact::class.java)
                    preferences.setData(id, gsonContact)
                    showMsg("New Contact added successfully.")
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                }
            }
        }
        catch  (e: Exception) {
            showMsg(e.toString())
        }

    }

    private fun isNumeric(str: String): Boolean {
        return str.matches("-?\\d+(\\.\\d+)?".toRegex())
    }


    private fun isEmailValid(email: String): Boolean {
        val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
        return email.matches(emailPattern.toRegex())
    }


    private fun showMsg(msg: String) {
        val snackbar = Snackbar.make(findViewById(android.R.id.content), msg, Snackbar.LENGTH_SHORT)
        snackbar.setBackgroundTint(resources.getColor(R.color.white))
        snackbar.setTextColor(resources.getColor(R.color.teal_700))
        snackbar.show()
    }

}