package com.example.babycrydetectionapp

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.babycrydetectionapp.databinding.ProbabilitiesListItemBinding
import org.tensorflow.lite.support.label.Category

class ProbabilitiesAdapter : RecyclerView.Adapter<ProbabilitiesAdapter.ViewHolder>() {
    var categoryList: List<Category> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ProbabilitiesListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val category = categoryList[position]
        holder.bind(category.label, category.score)
    }

    override fun getItemCount() = categoryList.size

    class ViewHolder(private val binding: ProbabilitiesListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(label: String, score: Float) {
            with(binding) {
                labelTextView.text = label
                val newValue = (score * 100).toInt()
                progressBar.progress = newValue
            }
        }
    }
}