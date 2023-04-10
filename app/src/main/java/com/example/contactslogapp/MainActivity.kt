package com.example.contactslogapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.example.contactslogapp.AddContactActivity
import com.example.contactslogapp.ContactListActivity
import com.example.contactslogapp.R
import com.example.contactslogapp.utils.AppPreferences

class MainActivity : AppCompatActivity() {

    private lateinit var preferences: AppPreferences
    private lateinit var countView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        preferences = AppPreferences(this)

        countView = findViewById(R.id.countTxt)
        updateCountView()

        findViewById<Button>(R.id.add_btn).setOnClickListener{
            val intent = Intent(this, AddContactActivity::class.java)
            startActivity(intent)
        }

        findViewById<Button>(R.id.view_all_btn).setOnClickListener{
            val intent = Intent(this, ContactListActivity::class.java)
            startActivity(intent)
        }
    }

    private fun updateCountView() {
        val totalContacts = preferences.getLength()
        countView.text = totalContacts.toString()
    }

}
