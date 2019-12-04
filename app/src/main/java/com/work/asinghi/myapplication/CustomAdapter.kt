package com.work.asinghi.myapplication

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView

class CustomAdapter(val searchList: List<Search>, val listener: (Int) -> Unit) :
    RecyclerView.Adapter<CustomAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.list_layout, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return searchList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val list = searchList[position]

        holder.title.text = list.title
        holder.date.text = list.created_at

        holder.bind(searchList[position], position, listener)


    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var card = itemView.findViewById<CardView>(R.id.card_View)
        val title = itemView.findViewById<TextView>(R.id.title)
        val date = itemView.findViewById<TextView>(R.id.date)

        fun bind(item: Search, pos: Int, listener: (Int) -> Unit) = with(itemView) {
            val cvItem = findViewById<CardView>(R.id.card_View)
            cvItem.setOnClickListener {
                listener(pos)
            }
        }


    }
}
