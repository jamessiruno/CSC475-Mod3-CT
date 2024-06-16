package com.example.contactbookapp

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.TextView

class ContactAdapter(context: Context, private val contacts: ArrayList<Contact>) :
    ArrayAdapter<Contact>(context, 0, contacts) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var view = convertView
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.contact_item, parent, false)
        }

        val contact = contacts[position]

        val nameTextView = view?.findViewById<TextView>(R.id.contact_name)
        val phoneTextView = view?.findViewById<TextView>(R.id.contact_phone)
        val editButton = view?.findViewById<Button>(R.id.edit_contact_button)
        val deleteButton = view?.findViewById<Button>(R.id.delete_contact_button)

        nameTextView?.text = contact.name
        phoneTextView?.text = contact.phone

        editButton?.setOnClickListener {
            val intent = Intent(context, AddEditContactActivity::class.java)
            intent.putExtra("contact", contact)
            intent.putExtra("position", position)
            (context as MainActivity).startActivityForResult(intent, 2)
        }

        deleteButton?.setOnClickListener {
            contacts.removeAt(position)
            notifyDataSetChanged()
            (context as MainActivity).saveContacts()
        }

        return view!!
    }
}
