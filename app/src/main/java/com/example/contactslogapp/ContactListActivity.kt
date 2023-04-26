package com.example.contactslogapp

import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.contactslogapp.adapter.ContactAdapter
import com.example.contactslogapp.models.Contact
import com.example.contactslogapp.utils.AppPreferences
import com.google.android.material.snackbar.Snackbar
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
            showEditContactDialog(contact)
        }

        contactAdapter.setOnCallClickListener { contact ->
            callPerson(contact.phone)
        }
    }

    private fun showEditContactDialog(contact: Contact) {
        setContentView(R.layout.activity_add_contact)

        val fName: EditText = findViewById(R.id.fName_txt)
        val lName: EditText = findViewById(R.id.lName_txt)
        val email: EditText = findViewById(R.id.email_txt)
        val phone: EditText = findViewById(R.id.phone_txt)
        val btnSave: Button = findViewById(R.id.add_btn)
        val btnCancel : Button = findViewById(R.id.cancel_btn)
        val layoutType : TextView = findViewById(R.id.layout_name_txt)

        layoutType.text = "Edit Contact"
        btnSave.text = "EDIT"

        fName.setText(contact.firstName)
        lName.setText(contact.lastName)
        email.setText(contact.email)
        phone.setText(contact.phone)

        btnSave.setOnClickListener {
            val updatedFirstName = fName.text.toString().trim()
            val updatedLastName = lName.text.toString().trim()
            val updatedEmail = email.text.toString().trim()
            val updatedPhone = phone.text.toString().trim()

            if (updatedFirstName.isNotEmpty() && updatedLastName.isNotEmpty() && updatedEmail.isNotEmpty() && updatedPhone.isNotEmpty()) {
                contact.firstName = updatedFirstName
                contact.lastName = updatedLastName
                contact.email = updatedEmail
                contact.phone = updatedPhone
                updateContact(contact)
                showMsg("Contact edited successfully!")
                val intent = Intent(this, ContactListActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            }
        }

        btnCancel.setOnClickListener {
            val intent = Intent(this, ContactListActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
            finish()
        }

    }

    override fun onBackPressed() {
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
        finish()
    }

    private fun showMsg(msg: String) {
        val snackbar = Snackbar.make(findViewById(android.R.id.content), msg, Snackbar.LENGTH_SHORT)
        snackbar.setBackgroundTint(resources.getColor(R.color.white))
        snackbar.setTextColor(resources.getColor(R.color.teal_700))
        snackbar.show()
    }

    private fun updateContact(contact: Contact) {
        preferences.saveUpdatedData(contact.id, gson.toJson(contact))
        contactAdapter.notifyDataSetChanged()
    }


    private fun onDeleteClick(contact: Contact) {
        val alertDialog = AlertDialog.Builder(this)
        alertDialog.setTitle("Delete Contact")

        alertDialog.setMessage("Are you sure you want to delete this contact?")
        alertDialog.setPositiveButton("Yes") { _, _ ->
            deleteContact(contact)
        }
        alertDialog.setNegativeButton("No") { dialogInterface, _ ->
            dialogInterface.dismiss()
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

    private fun callPerson(phone: String){
        val intent = Intent(Intent.ACTION_DIAL);
        intent.data = Uri.parse("tel:${phone}");
        startActivity(intent);
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