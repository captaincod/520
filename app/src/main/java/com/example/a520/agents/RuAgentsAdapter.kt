package com.example.a520.agents

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.a520.R

class RuAgentsAdapter(private val agents: MutableList<RuAgent>) :
    RecyclerView.Adapter<RuAgentsAdapter.AgentsViewHolder>() {

    class AgentsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameTextView: TextView = itemView.findViewById(R.id.agent_name)
        val dateTextView: TextView = itemView.findViewById(R.id.agent_date)
        val sourcesTextView: TextView = itemView.findViewById(R.id.sources)
        val infoTextView: TextView = itemView.findViewById(R.id.info)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AgentsViewHolder {
        val itemView =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.agent_item, parent, false)
        return RuAgentsAdapter.AgentsViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: AgentsViewHolder, position: Int) {
        val item = agents[position]
        holder.nameTextView.text = item.name
        holder.dateTextView.text = item.inclusionDate
        holder.sourcesTextView.text = item.foreignSource
        holder.infoTextView.text = item.info
    }

    override fun getItemCount() = agents.size

}
