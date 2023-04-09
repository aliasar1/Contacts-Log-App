package com.example.contactslogapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.contactslogapp.R
import com.example.contactslogapp.models.Contact

class ContactAdapter(var contactList: MutableList<Contact>):RecyclerView.Adapter<ContactAdapter.ContactViewHolder>() {

    class ContactViewHolder(view: View): RecyclerView.ViewHolder(view){
        val txName = view.findViewById<TextView>(R.id.txName)
        val txEmail = view.findViewById<TextView>(R.id.txEmail)
        val txPhone = view.findViewById<TextView>(R.id.txPhone)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.activity_contact_item, parent, false)
        return ContactViewHolder(view)
    }

    override fun getItemCount(): Int {
        return contactList.size
    }

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        val contact = contactList[position]
        holder.txName.text = "${contact.firstName} ${contact.lastName}"
        holder.txEmail.text = contact.email
        holder.txPhone.text = contact.phone
    }

    fun getItem(position: Int): Contact {
        return contactList[position]
    }
}