package com.example.a520.agents

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.a520.R

class RuMediaAdapter(private val mediaList: MutableList<RuMedia>) :
    RecyclerView.Adapter<RuMediaAdapter.MediaViewHolder>() {

    class MediaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameTextView: TextView = itemView.findViewById(R.id.media_name)
        val dateTextView: TextView = itemView.findViewById(R.id.media_date)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MediaViewHolder {
        val itemView =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.media_item, parent, false)
        return RuMediaAdapter.MediaViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MediaViewHolder, position: Int) {
        val item = mediaList[position]
        holder.nameTextView.text = item.name
        holder.dateTextView.text = item.date
    }

    override fun getItemCount() = mediaList.size

}
