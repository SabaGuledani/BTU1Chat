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
    val ITEM_RESPONSE = 3
    val ITEM_RESPONSE_RECEIVE =4
    //click listeners
    private lateinit var mlistener:onItemClickListener
    interface onItemClickListener{
        fun onItemClick(position: Int):String?
    }
    fun setOnItemClickListener(listener:onItemClickListener){
        mlistener = listener
    }


    //recyclerview
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            if (viewType == 1){
                val view:View = LayoutInflater.from(context).inflate(R.layout.receive,parent,false)
                return ReceiveViewHolder(view,mlistener)
            }else if(viewType == 2){
                val view:View = LayoutInflater.from(context).inflate(R.layout.sent,parent,false)
                return SentViewHolder(view,mlistener)
            }else if (viewType == 3){
                val view:View = LayoutInflater.from(context).inflate(R.layout.response,parent,false)
                return SentResponseViewHolder(view,mlistener)
            }else{
                val view:View = LayoutInflater.from(context).inflate(R.layout.response_received,parent,false)
                return ReceiveResponseViewHolder(view,mlistener)
            }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val currentMessage = messageList[position]
        if(holder.javaClass == SentViewHolder::class.java){
            val holder = holder as SentViewHolder
            holder.sentMessage.text = currentMessage.message

        }else if (holder.javaClass == ReceiveViewHolder::class.java){
            val holder = holder as ReceiveViewHolder
            holder.receiveMessage.text = currentMessage.message
            holder.name.text = currentMessage.sendername.toString()
            Glide.with(context)
                .load(currentMessage.senderImgUrl.toString())
                .fitCenter()
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(holder.senderImg)

        }else if (holder.javaClass == SentResponseViewHolder::class.java){
            val holder = holder as SentResponseViewHolder
            holder.sentMessage.text = currentMessage.message
            holder.responseToMsg.text = currentMessage.responsetext
        }else{
            val holder = holder as ReceiveResponseViewHolder
            holder.receiveMessage.text = currentMessage.message
            holder.name.text = currentMessage.sendername.toString()
            holder.respondingToMsg.text = currentMessage.responsetext
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
            if(currentMessage.responsetext == ""){
                return ITEM_SENT
            }else{
                return ITEM_RESPONSE
            }
        }else{
            if(currentMessage.responsetext == ""){
                return ITEM_RECEIVE
            }else{
                return  ITEM_RESPONSE_RECEIVE
            }

        }
    }

    override fun getItemCount(): Int {
        return messageList.size
    }
    class SentViewHolder(itemView: View,listener:onItemClickListener):RecyclerView.ViewHolder(itemView){
        val sentMessage = itemView.findViewById<TextView>(R.id.txt_sent)
        init {
            itemView.setOnClickListener {
                listener.onItemClick(adapterPosition)
            }
        }
    }
    class SentResponseViewHolder(itemView: View,listener:onItemClickListener):RecyclerView.ViewHolder(itemView){
        val sentMessage = itemView.findViewById<TextView>(R.id.txt_sent_response)
        val responseToMsg = itemView.findViewById<TextView>(R.id.txt_recceived_response)
        init {
            itemView.setOnClickListener {
                listener.onItemClick(adapterPosition)
            }
        }
    }
    class ReceiveViewHolder(itemView: View,listener:onItemClickListener):RecyclerView.ViewHolder(itemView){
        val receiveMessage = itemView.findViewById<TextView>(R.id.txt_recceived)
        val name = itemView.findViewById<TextView>(R.id.name)
        var senderImg = itemView.findViewById<ImageView>(R.id.senderImg)

        init {
            itemView.setOnClickListener {
                listener.onItemClick(adapterPosition)
            }
        }
    }

    class ReceiveResponseViewHolder(itemView: View,listener:onItemClickListener):RecyclerView.ViewHolder(itemView){
        val receiveMessage = itemView.findViewById<TextView>(R.id.txt_recceived_recres)
        val name = itemView.findViewById<TextView>(R.id.name_response)
        var senderImg = itemView.findViewById<ImageView>(R.id.senderImg_response)
        var respondingToMsg =itemView.findViewById<TextView>(R.id.txt_sent_recres)

        init {
            itemView.setOnClickListener {
                listener.onItemClick(adapterPosition)
            }

        }


    }
}