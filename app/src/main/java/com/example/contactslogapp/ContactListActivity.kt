package com.example.contactslogapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.RecyclerView
import com.example.contactslogapp.adapter.ContactAdapter
import com.example.contactslogapp.models.Contact

class ContactListActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contacts_list)

        val rvContacts = findViewById<RecyclerView>(R.id.rvContacts)
        rvContacts.adapter = ContactAdapter(getContacts())
    }

    private fun getContacts():MutableList<Contact>{
        val contacts = mutableListOf<Contact>()
        contacts.add(Contact("Ali", "Asar", "ali@gmail.com", "03312311221"))
        contacts.add(Contact("Usama", "Khan", "usama@gmail.com", "03312312311"))
        contacts.add(Contact("Ajmal", "Ali", "ajmal@gmail.com", "033123114441"))
        contacts.add(Contact("Shafiq", "Mir", "shafiq@gmail.com", "0331236661"))
        contacts.add(Contact("Aman", "Nawaaz", "aman@gmail.com", "03312316631"))
        contacts.add(Contact("Sarim", "Ali", "sarim@gmail.com", "03312310001"))
        contacts.add(Contact("Moosa", "Akber", "moosa@gmail.com", "03312556221"))
        return contacts
    }
}