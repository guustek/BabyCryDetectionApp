package com.example.babycrydetectionapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.babycrydetectionapp.R

class ContactsAdapter: RecyclerView.Adapter<ContactsAdapter.ViewHolder>() {

    private var contactsNumbers = arrayOf("jebac", "disa")

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactsAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.contact_card_view, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.text.text = contactsNumbers[position]
    }

    override fun getItemCount(): Int {
        return contactsNumbers.size
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        var text: TextView

        init {
            text = itemView.findViewById(R.id.contact_number)
        }
    }


}