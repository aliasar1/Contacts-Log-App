package com.example.contactslogapp

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
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
    private lateinit var contactAdapter: ContactAdapter
    private lateinit var preferences: AppPreferences
    private val gson = Gson()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contacts_list)

        preferences = AppPreferences(this)

        val rvContacts = findViewById<RecyclerView>(R.id.rvContacts)
        contactAdapter = ContactAdapter(getContacts())
        rvContacts.adapter = contactAdapter

        rvContacts.addOnItemClickListener { position ->
            val clickedContact = contactAdapter.getItem(position)
            showContactDetailsDialog(clickedContact)
        }

        contactAdapter.setOnDeleteClickListener { contact ->
            onDeleteClick(contact)
        }

        contactAdapter.setOnEditClickListener { contact ->
        }
    }

    private fun onDeleteClick(contact: Contact) {
        val alertDialog = AlertDialog.Builder(this)
        alertDialog.setTitle("Delete Contact")
        alertDialog.setMessage("Are you sure you want to delete this contact?")
        alertDialog.setPositiveButton("Yes") { dialog, which ->
            deleteContact(contact)
        }
        alertDialog.setNegativeButton("No") { dialog, which ->
            dialog.dismiss()
        }
        alertDialog.show()
    }

    private fun deleteContact(contact: Contact) {
        val contactKey = contact.id
        preferences.deleteContact(contactKey)
        contactAdapter.removeItem(contact)
        contactAdapter.notifyDataSetChanged()
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
            override fun onChildViewAttachedToWindow(view: View) {
                view.setOnClickListener {
                    val holder = getChildViewHolder(view)
                    onClickListener.invoke(holder.adapterPosition)
                }
            }
            override fun onChildViewDetachedFromWindow(view: View) {
                view.setOnClickListener(null)
            }
        })
    }
}