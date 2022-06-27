package com.example.a520.recyclerview

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.a520.R
import com.squareup.picasso.Picasso

class CustomRecyclerAdapter(private val context: Context,
                            private val dataset: MutableList<Dataset>,
                            private val comparisonAction: (String) -> Unit
    ) : RecyclerView.Adapter<CustomRecyclerAdapter.MyViewHolder>() {
    //private var isEnable = false
    private var itemSelectedList = mutableListOf<Int>()

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTextView: TextView = itemView.findViewById(R.id.textViewLarge)
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
        val item = dataset[position]
        Picasso.with(context)
            .load(item.image)
            .placeholder(R.drawable.world_news)
            .error(R.drawable.world_news)
            .fit()
            .into(holder.imageView)
        holder.titleTextView.text = item.title
        holder.sourceTextView.text = item.source
        holder.dateTextView.text = item.date

        holder.titleTextView.setOnLongClickListener {
            comparisonAction(item.link)
            true
        }

        holder.titleTextView.setOnClickListener {
            if (itemSelectedList.contains(position)){
                itemSelectedList.remove(position)
                holder.itemView.setBackgroundResource(R.drawable.bordered)
                item.selected = false
                if (itemSelectedList.isEmpty()){
                    comparisonAction("off")
                    //isEnable = false
                }
            } else {
                selectItem(holder, item, position)
            }
        }

    }

    private fun selectItem(holder: CustomRecyclerAdapter.MyViewHolder, item: Dataset, position: Int) {
        //isEnable = true
        itemSelectedList.add(position)
        holder.itemView.setBackgroundResource(R.drawable.bordered_select)
        item.selected = true
        comparisonAction("on")
    }

    override fun getItemCount() = dataset.size

}