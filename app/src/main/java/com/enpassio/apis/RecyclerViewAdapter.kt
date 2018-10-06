package com.enpassio.apis

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

class RecyclerViewAdapter(
        private val messageIdList: ArrayList<MessageWithIdAndThreadId>, private val context: Context)
    : RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder>() {

    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var id: TextView
        var threadId: TextView

        init {
            id = view.findViewById(R.id.id)
            threadId = view.findViewById(R.id.thread_id)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.recycler_row_item, parent, false)

        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val message = messageIdList[position]
        holder.id.setText(message.id)
        holder.threadId.setText(message.threadId)


    }

    override fun getItemCount(): Int {
        return messageIdList.size
    }
}