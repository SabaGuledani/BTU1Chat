package com.saba.messagingapp

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.view.menu.ActionMenuItemView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.firebase.auth.FirebaseAuth

class MessageAdapter(val context:Context,val messageList:ArrayList<Message>):
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    val ITEM_RECEIVE = 1
    val ITEM_SENT = 2
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            if (viewType == 1){
                val view:View = LayoutInflater.from(context).inflate(R.layout.receive,parent,false)
                return ReceiveViewHolder(view)
            }else{
                val view:View = LayoutInflater.from(context).inflate(R.layout.sent,parent,false)
                return SentViewHolder(view)
            }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val currentMessage = messageList[position]
        if(holder.javaClass == SentViewHolder::class.java){

            val holder = holder as SentViewHolder
            holder.sentMessage.text = currentMessage.message


        }else{
            val holder = holder as ReceiveViewHolder
            holder.receiveMessage.text = currentMessage.message
            holder.name.text = currentMessage.sendername.toString()

            Glide.with(context)
                .load(currentMessage.senderImgUrl.toString())
                .fitCenter()
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(holder.senderImg)
        }
    }

    override fun getItemViewType(position: Int): Int {
        val currentMessage = messageList[position]
        if(FirebaseAuth.getInstance().currentUser?.uid.equals(currentMessage.senderId)){
            return ITEM_SENT
        }else{
            return ITEM_RECEIVE
        }
    }

    override fun getItemCount(): Int {
        return messageList.size
    }
    class SentViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        val sentMessage = itemView.findViewById<TextView>(R.id.txt_sent)
    }
    class ReceiveViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        val receiveMessage = itemView.findViewById<TextView>(R.id.txt_recceived)
        val name = itemView.findViewById<TextView>(R.id.name)
        var senderImg = itemView.findViewById<ImageView>(R.id.senderImg)
    }
}