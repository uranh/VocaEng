package com.example.vocaeng

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MyReviewAdapter(val items: ArrayList<Voca>, val color:Int) : RecyclerView.Adapter<MyReviewAdapter.ViewHolder>() {

    interface OnItemClickListener{
        fun OnItemClick(holder:ViewHolder, view: View, data:Voca, position:Int)
    }
    var itemClickListener:OnItemClickListener?=null


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val word: TextView =itemView.findViewById(R.id.word)
        val category: TextView =itemView.findViewById(R.id.category)
        init{
            word.setBackgroundResource(color)
            category.setBackgroundResource(color)
            word.setOnClickListener{
                itemClickListener?.OnItemClick(this,it,items[adapterPosition],adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view= LayoutInflater.from(parent.context).inflate(R.layout.reviewrow,parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.word.text=items[position].word
        holder.category.text=items[position].category
    }
}