package com.ar.meditation.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "signup_table")
data class SignupModel(
    @PrimaryKey(autoGenerate = true) var id: Int = 0,
    var name: String = "",
    var age: String = "",
    var cnic: String = "",
    var diseases: String = "",
    var pass: String = ""
)
