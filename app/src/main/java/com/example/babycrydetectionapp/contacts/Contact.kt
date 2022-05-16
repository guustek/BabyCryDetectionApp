package com.example.babycrydetectionapp.contacts

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "contact_table")
data class Contact(
    val name: String,
    val number: String,
    @PrimaryKey(autoGenerate = true) val id: Int = 0
) {

    override fun toString(): String {
        return "Contact(name='$name', number='$number')"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Contact

        if (number != other.number) return false

        return true
    }

    override fun hashCode(): Int {
        return number.hashCode()
    }


}