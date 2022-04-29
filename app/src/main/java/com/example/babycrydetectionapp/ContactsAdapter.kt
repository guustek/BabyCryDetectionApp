package com.example.babycrydetectionapp

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.example.babycrydetectionapp.databinding.ContactsItemViewBinding

class ContactsAdapter(data: MutableList<Contact>) : RecyclerView.Adapter<ContactsAdapter.ContactHolder>(), Filterable {


    private var contactsNumbers: MutableList<Contact>
    private var contactNumbersFull: MutableList<Contact>
    private var lastFilter: CharSequence = ""

    init {
        contactNumbersFull = data
        contactsNumbers = ArrayList(data)
    }

    private lateinit var binding: ContactsItemViewBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactHolder {
        binding = ContactsItemViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ContactHolder(binding)
    }

    override fun onBindViewHolder(holder: ContactHolder, position: Int) {
        holder.bind(contactsNumbers[position])
    }

    override fun getItemCount(): Int {
        return contactsNumbers.size
    }

    override fun getFilter(): Filter {
        return contactFilter
    }

    fun notifyNewContactAdded() {
        contactsNumbers.clear()
        contactsNumbers.addAll(contactNumbersFull)
        filter.filter(lastFilter)
        notifyItemInserted(contactNumbersFull.size)
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

    class ContactHolder(private val binding: ContactsItemViewBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(currentItem: Contact) {
            binding.contactName.text = currentItem.name
            binding.contactNumber.text = currentItem.number
        }

    }


}