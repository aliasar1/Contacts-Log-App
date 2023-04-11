package com.example.contactslogapp

import android.app.Dialog
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
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

        // Set up RecyclerView with LinearLayoutManager
        val rvContacts = findViewById<RecyclerView>(R.id.rvContacts)
        val layoutManager = LinearLayoutManager(this)
        rvContacts.layoutManager = layoutManager

        // Inflate activity_contact_item layout
        val inflater = layoutInflater
        val itemView = inflater.inflate(R.layout.activity_contact_item, null, false)

        preferences = AppPreferences(this)

        val rvContacts1 = findViewById<RecyclerView>(R.id.rvContacts)
        rvContacts1.adapter = ContactAdapter(getContacts())

        rvContacts.addOnItemClickListener { position ->
            val clickedContact = (rvContacts1.adapter as ContactAdapter).getItem(position)
            showContactDetailsDialog(clickedContact)
        }

        val ivDel = itemView.findViewById<ImageView>(R.id.ivDelete)
        val txEdit = itemView.findViewById<TextView>(R.id.ivEdit)

        ivDel.setOnClickListener { view ->
            val position = rvContacts.getChildAdapterPosition(view)
            val clickedContact = (rvContacts.adapter as ContactAdapter).getItem(position)
            deleteContact(clickedContact)
        }
    }

    private fun deleteContact(contact: Contact) {
        val contactKey = contact.id
        preferences.deleteContact(contactKey)
    }

    private fun getContacts(): MutableList<Contact> {
        val contacts = mutableListOf<Contact>()
        val total = preferences.getLength()

        for (i in 0 until total) {
            val id = preferences.getKey(i)
            val gsonContact = preferences.getData(id)
            val contact = gson.fromJson(gsonContact, Contact::class.java)
            contacts.add(contact)
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