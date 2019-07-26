package com.pericle.guessthecar.levels

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pericle.guessthecar.database.Level
import com.pericle.guessthecar.databinding.ListItemLevelBinding

class LevelsAdapter(val clickListener: LevelListener) : RecyclerView.Adapter<LevelsAdapter.ViewHolder>() {

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
        holder.bind(clickListener, item)
    }

    class ViewHolder private constructor(val binding: ListItemLevelBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(clickListener: LevelListener, item: Level) {
            binding.level = item
            binding.clickListener = clickListener
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

class LevelListener(val clickListener: (level: Level) -> Unit) {
    fun onClick(level: Level) = clickListener(level)
}