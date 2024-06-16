package com.example.contactbookapp

import java.io.Serializable

data class Contact(
    var name: String,
    var phone: String
) : Serializable
