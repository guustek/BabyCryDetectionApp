package com.example.babycrydetectionapp.contacts

import android.annotation.SuppressLint
import android.content.Context
import android.content.DialogInterface
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Filter
import android.widget.Filterable
import androidx.appcompat.app.AlertDialog
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.RecyclerView
import com.example.babycrydetectionapp.R
import com.example.babycrydetectionapp.databinding.ContactsItemViewBinding
import com.hbb20.CountryCodePicker

class ContactsAdapter(private val contactsViewModel: ContactsViewModel, val context: Context) :
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
//            binding.root.setOnClickListener {
////                val dialog = AlertDialog.Builder(adapter.context)
////                    .setTitle(adapter.context.getString(R.string.edit_contact))
////                    .setCancelable(true)
////                    .setView(R.layout.contact_dialog)
////                    .create()
////                dialog.setButton(
////                    AlertDialog.BUTTON_POSITIVE,
////                    adapter.context.getString(R.string.ok)
////                ) { _: DialogInterface, _: Int -> }
////                dialog.setButton(
////                    AlertDialog.BUTTON_NEGATIVE,
////                    adapter.context.getString(R.string.cancel)
////                ) { _: DialogInterface, _: Int ->
////                    dialog.dismiss()
////                }
////                dialog.show()
////                val nameInput = dialog.findViewById<EditText>(R.id.name_input)!!
////                nameInput.setText(currentItem.name)
////                val numberInput = dialog.findViewById<EditText>(R.id.number_input)!!
////                val firstSpaceIndex = currentItem.number.indexOf(" ")
////                val number = currentItem.number.substring(firstSpaceIndex + 1)
////                numberInput.setText(number)
////                val countryInput = dialog.findViewById<CountryCodePicker>(R.id.country_input)!!
////                val countryCode = currentItem.number.substring(1, firstSpaceIndex).toInt()
////                countryInput.setCountryForPhoneCode(countryCode)
////                countryInput.registerCarrierNumberEditText(numberInput)
////                nameInput.addTextChangedListener {
////                    nameInput.error = null
////                    if (nameInput.text.isEmpty())
////                        nameInput.error = adapter.context.getString(R.string.name_is_empty)
////                }
////                numberInput.addTextChangedListener {
////                    numberInput.error = null
////                    if (!countryInput.isValidFullNumber)
////                        numberInput.error = adapter.context.getString(R.string.invalid_number)
////                }
////
////                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
////                    if (nameInput.error == null && numberInput.error == null) {
////                        val updatedContact = Contact(
////                            nameInput.text.toString(),
////                            countryInput.formattedFullNumber
////                        )
////                        ContactDatabase.getDatabase(adapter.context).contactDao().editContact(updatedContact)
////                        Log.d("XD", contactsViewModel.contacts.value.toString())
////                        adapter.notifyItemChanged(position)
////                        adapter.refresh()
////                        dialog.dismiss()
////                    }
////                }
////            }
            binding.contactName.text = currentItem.name
            binding.contactNumber.text = currentItem.number
            binding.deleteButton.setOnClickListener {
                ContactDatabase.getDatabase(adapter.context).contactDao().deleteContact(currentItem)
                contactsViewModel.contacts.value = contactsViewModel.contacts.value!!.minus(currentItem)
                adapter.notifyItemRemoved(position)
                adapter.refresh()
            }
        }


    }


}