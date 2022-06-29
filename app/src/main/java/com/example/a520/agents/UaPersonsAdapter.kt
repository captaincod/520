package com.example.a520.agents

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.a520.R

class UaPersonsAdapter(private val personsList: MutableList<String>) :
    RecyclerView.Adapter<UaPersonsAdapter.PersonsViewHolder>() {

    class PersonsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameTextView: TextView = itemView.findViewById(R.id.person_name)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PersonsViewHolder {
        val itemView =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.ua_person_item, parent, false)
        return UaPersonsAdapter.PersonsViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: PersonsViewHolder, position: Int) {
        val item = personsList[position]
        holder.nameTextView.text = item
    }

    override fun getItemCount() = personsList.size

}