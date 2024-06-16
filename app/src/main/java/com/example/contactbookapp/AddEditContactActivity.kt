package com.example.contactbookapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity

class AddEditContactActivity : AppCompatActivity() {

    private lateinit var contactNameInput: EditText
    private lateinit var contactPhoneInput: EditText
    private lateinit var saveContactButton: Button
    private var contact: Contact? = null
    private var position: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_edit_contact)

        contactNameInput = findViewById(R.id.contact_name_input)
        contactPhoneInput = findViewById(R.id.contact_phone_input)
        saveContactButton = findViewById(R.id.save_contact_button)

        contact = intent.getSerializableExtra("contact") as Contact?
        position = intent.getIntExtra("position", -1)

        contact?.let {
            contactNameInput.setText(it.name)
            contactPhoneInput.setText(it.phone)
        }

        saveContactButton.setOnClickListener {
            val name = contactNameInput.text.toString()
            val phone = contactPhoneInput.text.toString()

            val resultIntent = Intent()
            resultIntent.putExtra("contact", Contact(name, phone))
            resultIntent.putExtra("position", position)

            setResult(RESULT_OK, resultIntent)
            finish()
        }
    }
}
