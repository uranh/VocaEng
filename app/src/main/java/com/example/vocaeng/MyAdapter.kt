package com.example.vocaeng

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MyAdapter(val items: ArrayList<Voca>,val color:Int) : RecyclerView.Adapter<MyAdapter.ViewHolder>() {

    interface OnItemClickListener{
        fun OnItemClick(holder:ViewHolder, view: View, data:Voca, position:Int)
        fun OnStarClick(holder:ViewHolder, view: View, data:Voca, position:Int)
        fun OnItemLongClick(holder:ViewHolder, view: View, data:Voca, position:Int):Boolean
    }
    var itemClickListener:OnItemClickListener?=null


    fun removeItem(pos:Int){
        items.removeAt(pos)
        notifyItemRemoved(pos)
    }
    fun editItem(pos:Int,newmeaing:String){
        items[pos].meaning=newmeaing
        notifyItemChanged(pos)
    }


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val word: TextView =itemView.findViewById(R.id.word)
        val meaning: TextView =itemView.findViewById(R.id.meaning)
        val star: ImageView =itemView.findViewById(R.id.star)
        val category: TextView =itemView.findViewById(R.id.category)
        init{
            itemView.findViewById<LinearLayout>(R.id.wordlinear).setBackgroundResource(color)
            category.setBackgroundResource(color)
            word.setOnClickListener{
                itemClickListener?.OnItemClick(this,it,items[adapterPosition],adapterPosition)
            }
            word.setOnLongClickListener {
                itemClickListener?.OnItemLongClick(this,it,items[adapterPosition],adapterPosition) == true
            }
            star.setOnClickListener {
                itemClickListener?.OnStarClick(this,it,items[adapterPosition],adapterPosition)
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view= LayoutInflater.from(parent.context).inflate(R.layout.row,parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.word.text=items[position].word
        holder.meaning.text=items[position].meaning
        holder.meaning.visibility= View.GONE
        if(items[position].isChecked==0){
            holder.star.isSelected=false
        }
        else if(items[position].isChecked==1){
            holder.star.isSelected=true
        }
        else if(items[position].isChecked==-1){ //myword나 review인 경우
            holder.star.visibility=View.INVISIBLE
            holder.category.text=items[position].category
            holder.category.visibility=View.VISIBLE
        }
    }
}