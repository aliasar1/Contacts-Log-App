package com.example.contactslogapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.contactslogapp.R
import com.example.contactslogapp.models.Contact

class ContactAdapter(var contactList: MutableList<Contact>):RecyclerView.Adapter<ContactAdapter.ContactViewHolder>() {

    private var onDeleteClickListener: ((Contact) -> Unit)? = null
    private var onEditClickListener: ((Contact) -> Unit)? = null
    private var onCallClickListener: ((Contact) -> Unit)? = null

    inner class ContactViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val txName = view.findViewById<TextView>(R.id.txName)
        val ivDelete = view.findViewById<ImageView>(R.id.ivDelete)
        val ivEdit = view.findViewById<ImageView>(R.id.ivEdit)
        val ivPhone = view.findViewById<ImageView>(R.id.ivPhone)

        init {
            ivDelete.setOnClickListener {
                onDeleteClickListener?.invoke(contactList[adapterPosition])
            }

            ivEdit.setOnClickListener {
                onEditClickListener?.invoke(contactList[adapterPosition])
            }

            ivPhone.setOnClickListener {
                onCallClickListener?.invoke(contactList[adapterPosition])
            }
        }
    }

    fun removeItem(contact: Contact) {
        val position = contactList.indexOf(contact)
        if (position != -1) {
            contactList.removeAt(position)
            notifyItemRemoved(position)
        }
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

        holder.ivDelete.setOnClickListener {
            onDeleteClickListener?.invoke(contact)
        }

        holder.ivEdit.setOnClickListener {
            onEditClickListener?.invoke(contact)
        }

        holder.ivPhone.setOnClickListener {
            onCallClickListener?.invoke(contact)
        }
    }

    fun getItem(position: Int): Contact {
        return contactList[position]
    }

    fun setOnDeleteClickListener(listener: (Contact) -> Unit) {
        onDeleteClickListener = listener
    }

    fun setOnEditClickListener(listener: (Contact) -> Unit) {
        onEditClickListener = listener
    }

    fun setOnCallClickListener(listener: (Contact) -> Unit) {
        onCallClickListener = listener
    }
}

