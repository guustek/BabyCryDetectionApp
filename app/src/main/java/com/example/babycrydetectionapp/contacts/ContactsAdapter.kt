package com.example.babycrydetectionapp.contacts

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.example.babycrydetectionapp.databinding.ContactsItemViewBinding

class ContactsAdapter(private val contactsViewModel: ContactsViewModel) :
    RecyclerView.Adapter<ContactsAdapter.ContactHolder>(), Filterable {


    var contactsNumbers: MutableList<Contact> = ArrayList(contactsViewModel.contacts.value!!)
    var contactNumbersFull: List<Contact> = contactsViewModel.contacts.value!!
    private var lastFilter: CharSequence = ""

    private lateinit var binding: ContactsItemViewBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactHolder {
        binding = ContactsItemViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ContactHolder(binding, contactsViewModel, this)
    }

    override fun onBindViewHolder(holder: ContactHolder, position: Int) {
        holder.bind(contactsNumbers[position], position)
    }

    override fun getItemCount(): Int {
        return contactsNumbers.size
    }

    override fun getFilter(): Filter {
        return contactFilter
    }

    fun refresh() {
        contactNumbersFull = contactsViewModel.contacts.value!!
        contactsNumbers.clear()
        contactsNumbers.addAll(contactNumbersFull)
        filter.filter(lastFilter)
    }


    private val contactFilter = object : Filter() {
        override fun performFiltering(constraint: CharSequence?): FilterResults {
            val filterPatter = constraint.toString().lowercase().trim()
            val filteredList = contactNumbersFull.filter { contact: Contact ->
                contact.number.lowercase().contains(filterPatter) or contact.name.lowercase().contains(filterPatter)
            }
            val result = FilterResults()
            result.values = filteredList
            return result
        }

        @SuppressLint("NotifyDataSetChanged")
        @Suppress("UNCHECKED_CAST")
        override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
            lastFilter = constraint ?: ""
            contactsNumbers.clear()
            contactsNumbers.addAll(results!!.values as Collection<Contact>)
            notifyDataSetChanged()
        }

    }

    class ContactHolder(
        private val binding: ContactsItemViewBinding,
        private val contactsViewModel: ContactsViewModel,
        private val adapter: ContactsAdapter
    ) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(currentItem: Contact, position: Int) {
            binding.contactName.text = currentItem.name
            binding.contactNumber.text = currentItem.number
            binding.deleteButton.setOnClickListener {
                contactsViewModel.contacts.value = contactsViewModel.contacts.value!!.minus(currentItem)
                JakisGownoSingletonDoPrzekazaniaNumerowDoSerwisuBoNieChceMiSieRobicBazyDanych.data =  contactsViewModel.contacts.value!!
                adapter.notifyItemRemoved(position)
                adapter.refresh()
            }
        }


    }


}