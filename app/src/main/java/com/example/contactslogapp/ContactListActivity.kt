package com.example.contactslogapp

import android.app.Dialog
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.contactslogapp.adapter.ContactAdapter
import com.example.contactslogapp.models.Contact
import com.example.contactslogapp.utils.AppPreferences
import com.google.gson.Gson

class ContactListActivity : AppCompatActivity() {
    private lateinit var preferences: AppPreferences
    private val gson = Gson()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contacts_list)

        preferences = AppPreferences(this)

        val rvContacts = findViewById<RecyclerView>(R.id.rvContacts)
        rvContacts.adapter = ContactAdapter(getContacts())

        rvContacts.addOnItemClickListener { position ->
            val clickedContact = (rvContacts.adapter as ContactAdapter).getItem(position)
            showContactDetailsDialog(clickedContact)
        }
    }

    private fun getContacts(): MutableList<Contact> {
        val contacts = mutableListOf<Contact>()
        val total = preferences.getLength()
        for (i in 0 until total) {
            val data = gson.fromJson(preferences.getData(i.toString()), Contact::class.java)
            contacts.add(data)
        }
        return contacts
    }

    private fun showContactDetailsDialog(contact: Contact) {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.activity_show_details)

        dialog.setTitle("Contact Details")

        val txName: TextView = dialog.findViewById(R.id.txName)
        val txEmail: TextView = dialog.findViewById(R.id.txEmail)
        val txPhone: TextView = dialog.findViewById(R.id.txPhone)

        txName.text = "${contact.firstName} ${contact.lastName}"
        txEmail.text = "${contact.email}"
        txPhone.text = "${contact.phone}"
        dialog.show()
    }

    private fun RecyclerView.addOnItemClickListener(onClickListener: (Int) -> Unit) {
        this.addOnChildAttachStateChangeListener(object : RecyclerView.OnChildAttachStateChangeListener {
            override fun onChildViewAttachedToWindow(view: android.view.View) {
                view.setOnClickListener {
                    val holder = getChildViewHolder(view)
                    onClickListener.invoke(holder.adapterPosition)
                }
            }
            override fun onChildViewDetachedFromWindow(view: android.view.View) {
                view.setOnClickListener(null)
            }
        })
    }
}