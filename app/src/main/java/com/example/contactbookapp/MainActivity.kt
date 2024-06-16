package com.example.contactbookapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class MainActivity : AppCompatActivity() {

    companion object {
        private const val PREFS_NAME = "ContactBookPrefs"
        private const val CONTACTS_KEY = "Contacts"
    }

    private lateinit var contactListView: ListView
    private lateinit var addContactButton: Button
    private val contactList: ArrayList<Contact> = ArrayList()
    private lateinit var contactAdapter: ContactAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        contactListView = findViewById(R.id.contact_list_view)
        addContactButton = findViewById(R.id.add_contact_button)

        loadContacts()

        contactAdapter = ContactAdapter(this, contactList)
        contactListView.adapter = contactAdapter

        addContactButton.setOnClickListener {
            val intent = Intent(this@MainActivity, AddEditContactActivity::class.java)
            startActivityForResult(intent, 1)
        }

        contactListView.setOnItemClickListener { _, _, position, _ ->
            val contact = contactList[position]
            val intent = Intent(this@MainActivity, AddEditContactActivity::class.java)
            intent.putExtra("contact", contact)
            intent.putExtra("position", position)
            startActivityForResult(intent, 2)
        }
    }

    private fun loadContacts() {
        val contactsJson = getSharedPreferences(PREFS_NAME, MODE_PRIVATE).getString(CONTACTS_KEY, null)
        if (contactsJson != null) {
            try {
                val jsonArray = JSONArray(contactsJson)
                for (i in 0 until jsonArray.length()) {
                    val jsonObject = jsonArray.getJSONObject(i)
                    val name = jsonObject.getString("name")
                    val phone = jsonObject.getString("phone")
                    contactList.add(Contact(name, phone))
                }
            } catch (e: JSONException) {
                e.printStackTrace()
            }
        }
    }

    fun saveContacts() {
        val jsonArray = JSONArray()
        for (contact in contactList) {
            val jsonObject = JSONObject()
            try {
                jsonObject.put("name", contact.name)
                jsonObject.put("phone", contact.phone)
                jsonArray.put(jsonObject)
            } catch (e: JSONException) {
                e.printStackTrace()
            }
        }
        getSharedPreferences(PREFS_NAME, MODE_PRIVATE).edit().putString(CONTACTS_KEY, jsonArray.toString()).apply()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && data != null) {
            val contact = data.getSerializableExtra("contact") as Contact?
            if (contact != null) {
                if (requestCode == 1) { // Add
                    contactList.add(contact)
                } else if (requestCode == 2) { // Edit
                    val position = data.getIntExtra("position", -1)
                    if (position != -1) {
                        contactList[position] = contact
                    }
                }
                contactAdapter.notifyDataSetChanged()
                saveContacts()
            }
        }
    }
}
