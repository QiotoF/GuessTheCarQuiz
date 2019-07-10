package com.pericle.guessthecar.levels

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pericle.guessthecar.databinding.ListItemLevelBinding

class LevelsAdapter : RecyclerView.Adapter<LevelsAdapter.ViewHolder>() {

    var data = listOf<Level>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = data[position]
        holder.bind(item)
    }

    class ViewHolder private constructor(val binding: ListItemLevelBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Level) {
            binding.level = item
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemLevelBinding.inflate(layoutInflater, parent, false)

                return ViewHolder(binding)
            }
        }
    }
}