package com.example.a520.news

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
                            private var itemSelectedList: MutableList<Int>,
                            private var alreadySelected: MutableList<String>,
                            private val comparisonAction: (String) -> Unit
    ) : RecyclerView.Adapter<CustomRecyclerAdapter.MyViewHolder>() {

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
        if (position in itemSelectedList){
            holder.itemView.setBackgroundResource(R.drawable.bordered_select)
        }
        else {
            holder.itemView.setBackgroundResource(R.drawable.bordered)
        }

        holder.itemView.setOnLongClickListener {
            comparisonAction(item.link)
            true
        }

        holder.itemView.setOnClickListener {
            if (itemSelectedList.contains(position)){
                itemSelectedList.remove(position)
                holder.itemView.setBackgroundResource(R.drawable.bordered)
                item.selected = false
                if (item.link in alreadySelected){
                    alreadySelected.remove(item.link)
                }
                comparisonAction("off")
            } else {
                selectItem(holder, item, position)
            }
            // Log.d(TAG, "selected positions: $itemSelectedList")
        }

    }

    private fun selectItem(holder: MyViewHolder, item: Dataset, position: Int) {
        if (alreadySelected.size < 2) {
            itemSelectedList.add(position)
            alreadySelected.add(item.link)
            holder.itemView.setBackgroundResource(R.drawable.bordered_select)
            item.selected = true
            if (alreadySelected.size == 2){
                comparisonAction("on")
            }
        }
        else {
            comparisonAction("on")
        }
        // Log.d(TAG, "selected links: $alreadySelected")
    }

    override fun getItemCount() = dataset.size

}