package com.example.a520

import android.content.Context
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

class CustomRecyclerAdapter(private val context: Context,
                            private val titles: List<String>,
                            private val sources: List<String>,
                            private val dates: List<String>,
                            private val images: List<String>) :
    RecyclerView.Adapter<CustomRecyclerAdapter.MyViewHolder>() {

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val largeTextView: TextView = itemView.findViewById(R.id.textViewLarge)
        val sourceTextView: TextView = itemView.findViewById(R.id.source)
        val dateTextView: TextView = itemView.findViewById(R.id.date)
        val imageView: ImageView = itemView.findViewById(R.id.imageView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.recyclerview_item, parent, false)
        return MyViewHolder(itemView)
    }

    // TODO: заменить placeholder

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        Picasso.with(context)
            .load(images[position])
            .placeholder(R.drawable.world_news)
            .error(R.drawable.world_news)
            .fit()
            .into(holder.imageView)
        holder.largeTextView.text = titles[position]
        holder.largeTextView.movementMethod = LinkMovementMethod.getInstance()
        holder.sourceTextView.text = sources[position]
        holder.dateTextView.text = dates[position]
    }

    override fun getItemCount() = titles.size
}