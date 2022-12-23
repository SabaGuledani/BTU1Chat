package com.saba.messagingapp

import android.content.ClipData
import android.content.Context
import android.text.Layout
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.view.menu.ActionMenuItemView
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase

class MessageAdapter(val context: Context, val messageList: ArrayList<Message>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    val ITEM_RECEIVE = 1
    val ITEM_SENT = 2
    val ITEM_RESPONSE = 3
    val ITEM_RESPONSE_RECEIVE = 4

    //click listeners
    private lateinit var mlistener: onItemClickListener
    private lateinit var longlistener: OnLongCLickListener
    private lateinit var messageKeyList: ArrayList<String>

    interface onItemClickListener {
        fun onItemClick(position: Int): String?
    }

    fun setOnItemClickListener(listener: onItemClickListener) {
        mlistener = listener
    }

    //long click

    interface OnLongCLickListener {
        fun onItemLongClick(position: Int)
    }

    fun setOnItemLongClickListener(listener: OnLongCLickListener) {
        longlistener = listener
    }


    //recyclerview
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == 1) {
            val view: View = LayoutInflater.from(context).inflate(R.layout.receive, parent, false)
            return ReceiveViewHolder(view, mlistener, longlistener)
        } else if (viewType == 2) {
            val view: View = LayoutInflater.from(context).inflate(R.layout.sent, parent, false)
            return SentViewHolder(view, mlistener,longlistener)
        } else if (viewType == 3) {
            val view: View = LayoutInflater.from(context).inflate(R.layout.response, parent, false)
            return SentResponseViewHolder(view, mlistener,longlistener)
        } else {
            val view: View =
                LayoutInflater.from(context).inflate(R.layout.response_received, parent, false)
            return ReceiveResponseViewHolder(view, mlistener,longlistener)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val currentMessage = messageList[position]


        if (holder.javaClass == SentViewHolder::class.java) {
            val holder = holder as SentViewHolder
            holder.sentMessage.text = currentMessage.message


            if (currentMessage.reactguja == "guja" || currentMessage.reactjesus == "jesus"
                || currentMessage.reactmgeli == "mgeli" || currentMessage.reactsaxia == "saxia" ||
                currentMessage.reactbarbi == "barbi" || currentMessage.reactnini == "nini"
            ) {
                (holder.layout).visibility = View.VISIBLE
            } else {
                (holder.layout).visibility = View.GONE
            }

            if (currentMessage.reactguja == "guja") {
                (holder.guja).visibility = View.VISIBLE
            } else {
                (holder.guja).visibility = View.GONE
            }
            if (currentMessage.reactnini == "nini") {
                (holder.nini).visibility = View.VISIBLE
            } else {
                (holder.nini).visibility = View.GONE
            }
            if (currentMessage.reactjesus == "jesus") {
                (holder.jesus).visibility = View.VISIBLE
            } else {
                (holder.jesus).visibility = View.GONE
            }
            if (currentMessage.reactbarbi == "barbi") {
                (holder.barbi).visibility = View.VISIBLE
            } else {
                (holder.barbi).visibility = View.GONE
            }
            if (currentMessage.reactsaxia == "saxia") {
                (holder.saxia).visibility = View.VISIBLE
            }else {
                (holder.saxia).visibility = View.GONE
            }
            if (currentMessage.reactmgeli == "mgeli") {
                (holder.mgeli).visibility = View.VISIBLE
            }else {
                (holder.mgeli).visibility = View.GONE
            }


        } else if (holder.javaClass == ReceiveViewHolder::class.java) {
            val holder = holder as ReceiveViewHolder
            holder.receiveMessage.text = currentMessage.message
            holder.name.text = currentMessage.sendername.toString()
            if (currentMessage.reactguja == "guja" || currentMessage.reactjesus == "jesus"
                || currentMessage.reactmgeli == "mgeli" || currentMessage.reactsaxia == "saxia" ||
                currentMessage.reactbarbi == "barbi" || currentMessage.reactnini == "nini"
            ) {
                (holder.layout).visibility = View.VISIBLE
            } else {
                (holder.layout).visibility = View.GONE
            }
            if (currentMessage.reactguja == "guja") {
                (holder.guja).visibility = View.VISIBLE
            } else {
                (holder.guja).visibility = View.GONE
            }
            if (currentMessage.reactnini == "nini") {
                (holder.nini).visibility = View.VISIBLE
            } else {
                (holder.nini).visibility = View.GONE
            }
            if (currentMessage.reactjesus == "jesus") {
                (holder.jesus).visibility = View.VISIBLE
            } else {
                (holder.jesus).visibility = View.GONE
            }
            if (currentMessage.reactbarbi == "barbi") {
                (holder.barbi).visibility = View.VISIBLE
            } else {
                (holder.barbi).visibility = View.GONE
            }
            if (currentMessage.reactsaxia == "saxia") {
                (holder.saxia).visibility = View.VISIBLE
            }else {
                (holder.saxia).visibility = View.GONE
            }
            if (currentMessage.reactmgeli == "mgeli") {
                (holder.mgeli).visibility = View.VISIBLE
            }else {
                (holder.mgeli).visibility = View.GONE
            }
            Glide.with(context)
                .load(currentMessage.senderImgUrl.toString())
                .fitCenter()
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(holder.senderImg)

        } else if (holder.javaClass == SentResponseViewHolder::class.java) {
            val holder = holder as SentResponseViewHolder
            holder.sentMessage.text = currentMessage.message
            holder.responseToMsg.text = currentMessage.responsetext
            if (currentMessage.reactguja == "guja" || currentMessage.reactjesus == "jesus"
                || currentMessage.reactmgeli == "mgeli" || currentMessage.reactsaxia == "saxia" ||
                currentMessage.reactbarbi == "barbi" || currentMessage.reactnini == "nini"
            ) {
                (holder.layout).visibility = View.VISIBLE
            } else {
                (holder.layout).visibility = View.GONE
            }
            if (currentMessage.reactguja == "guja") {
                (holder.guja).visibility = View.VISIBLE
            } else {
                (holder.guja).visibility = View.GONE
            }
            if (currentMessage.reactnini == "nini") {
                (holder.nini).visibility = View.VISIBLE
            } else {
                (holder.nini).visibility = View.GONE
            }
            if (currentMessage.reactjesus == "jesus") {
                (holder.jesus).visibility = View.VISIBLE
            } else {
                (holder.jesus).visibility = View.GONE
            }
            if (currentMessage.reactbarbi == "barbi") {
                (holder.barbi).visibility = View.VISIBLE
            } else {
                (holder.barbi).visibility = View.GONE
            }
            if (currentMessage.reactsaxia == "saxia") {
                (holder.saxia).visibility = View.VISIBLE
            }else {
                (holder.saxia).visibility = View.GONE
            }
            if (currentMessage.reactmgeli == "mgeli") {
                (holder.mgeli).visibility = View.VISIBLE
            }else {
                (holder.mgeli).visibility = View.GONE
            }
        } else {
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

            if (currentMessage.reactguja == "guja" || currentMessage.reactjesus == "jesus"
                || currentMessage.reactmgeli == "mgeli" || currentMessage.reactsaxia == "saxia" ||
                currentMessage.reactbarbi == "barbi" || currentMessage.reactnini == "nini"
            ) {
                (holder.layout).visibility = View.VISIBLE
            } else {
                (holder.layout).visibility = View.GONE
            }
            if (currentMessage.reactguja == "guja") {
                (holder.guja).visibility = View.VISIBLE
            } else {
                (holder.guja).visibility = View.GONE
            }
            if (currentMessage.reactnini == "nini") {
                (holder.nini).visibility = View.VISIBLE
            } else {
                (holder.nini).visibility = View.GONE
            }
            if (currentMessage.reactjesus == "jesus") {
                (holder.jesus).visibility = View.VISIBLE
            } else {
                (holder.jesus).visibility = View.GONE
            }
            if (currentMessage.reactbarbi == "barbi") {
                (holder.barbi).visibility = View.VISIBLE
            } else {
                (holder.barbi).visibility = View.GONE
            }
            if (currentMessage.reactsaxia == "saxia") {
                (holder.saxia).visibility = View.VISIBLE
            }else {
                (holder.saxia).visibility = View.GONE
            }
            if (currentMessage.reactmgeli == "mgeli") {
                (holder.mgeli).visibility = View.VISIBLE
            }else {
                (holder.mgeli).visibility = View.GONE
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        val currentMessage = messageList[position]
        if (FirebaseAuth.getInstance().currentUser?.uid.equals(currentMessage.senderId)) {
            if (currentMessage.responsetext == "") {
                return ITEM_SENT
            } else {
                return ITEM_RESPONSE
            }
        } else {
            if (currentMessage.responsetext == "") {
                return ITEM_RECEIVE
            } else {
                return ITEM_RESPONSE_RECEIVE
            }

        }
    }

    override fun getItemCount(): Int {
        return messageList.size
    }

    class SentViewHolder(itemView: View, listener: onItemClickListener,listenerLong: OnLongCLickListener) :
        RecyclerView.ViewHolder(itemView) {
        val sentMessage = itemView.findViewById<TextView>(R.id.txt_sent)
        var guja = itemView.findViewById<ImageView>(R.id.circleguja)
        var barbi = itemView.findViewById<ImageView>(R.id.circlebarbare)
        var nini = itemView.findViewById<ImageView>(R.id.circlenini)
        var saxia = itemView.findViewById<ImageView>(R.id.circlesaxia)
        var mgeli = itemView.findViewById<ImageView>(R.id.circlemgelisaxia)
        var jesus = itemView.findViewById<ImageView>(R.id.jesus)
        var layout = itemView.findViewById<ViewGroup>(R.id.reactsLayout)

        init {
            itemView.setOnClickListener {
                listener.onItemClick(adapterPosition)


            itemView.setOnLongClickListener {
                    listenerLong.onItemLongClick(adapterPosition)
                    true
                }
            }
        }
    }

    class SentResponseViewHolder(itemView: View, listener: onItemClickListener,listenerLong: OnLongCLickListener) :
        RecyclerView.ViewHolder(itemView) {
        val sentMessage = itemView.findViewById<TextView>(R.id.txt_sent_response)
        val responseToMsg = itemView.findViewById<TextView>(R.id.txt_recceived_response)
        var guja = itemView.findViewById<ImageView>(R.id.circleguja)
        var barbi = itemView.findViewById<ImageView>(R.id.circlebarbare)
        var nini = itemView.findViewById<ImageView>(R.id.circlenini)
        var saxia = itemView.findViewById<ImageView>(R.id.circlesaxia)
        var mgeli = itemView.findViewById<ImageView>(R.id.circlemgelisaxia)
        var jesus = itemView.findViewById<ImageView>(R.id.jesus)
        var layout = itemView.findViewById<ViewGroup>(R.id.reactsLayout)

        init {
            itemView.setOnClickListener {
                listener.onItemClick(adapterPosition)
            }
            itemView.setOnLongClickListener {
                listenerLong.onItemLongClick(adapterPosition)
                true
            }
        }


    }

    class ReceiveViewHolder(
        itemView: View,
        listener: onItemClickListener,
        listenerLong: OnLongCLickListener
    ) : RecyclerView.ViewHolder(itemView) {
        val receiveMessage = itemView.findViewById<TextView>(R.id.txt_recceived)
        val name = itemView.findViewById<TextView>(R.id.name)
        var senderImg = itemView.findViewById<ImageView>(R.id.senderImg)
        var guja = itemView.findViewById<ImageView>(R.id.circleguja)
        var barbi = itemView.findViewById<ImageView>(R.id.circlebarbare)
        var nini = itemView.findViewById<ImageView>(R.id.circlenini)
        var saxia = itemView.findViewById<ImageView>(R.id.circlesaxia)
        var mgeli = itemView.findViewById<ImageView>(R.id.circlemgelisaxia)
        var jesus = itemView.findViewById<ImageView>(R.id.jesus)
        var layout = itemView.findViewById<ViewGroup>(R.id.reactsLayout)


        init {
            itemView.setOnClickListener {
                listener.onItemClick(adapterPosition)
            }
            itemView.setOnLongClickListener {
                listenerLong.onItemLongClick(adapterPosition)
                true
            }
        }


    }

    class ReceiveResponseViewHolder(itemView: View, listener: onItemClickListener,listenerLong: OnLongCLickListener) :
        RecyclerView.ViewHolder(itemView) {
        val receiveMessage = itemView.findViewById<TextView>(R.id.txt_recceived_recres)
        val name = itemView.findViewById<TextView>(R.id.name_response)
        var senderImg = itemView.findViewById<ImageView>(R.id.senderImg_response)
        var respondingToMsg = itemView.findViewById<TextView>(R.id.txt_sent_recres)
        var guja = itemView.findViewById<ImageView>(R.id.circleguja)
        var barbi = itemView.findViewById<ImageView>(R.id.circlebarbare)
        var nini = itemView.findViewById<ImageView>(R.id.circlenini)
        var saxia = itemView.findViewById<ImageView>(R.id.circlesaxia)
        var mgeli = itemView.findViewById<ImageView>(R.id.circlemgelisaxia)
        var jesus = itemView.findViewById<ImageView>(R.id.jesus)
        var layout = itemView.findViewById<ViewGroup>(R.id.reactsLayout)

        init {
            itemView.setOnClickListener {
                listener.onItemClick(adapterPosition)
            }
            itemView.setOnLongClickListener {
                listenerLong.onItemLongClick(adapterPosition)
                true
            }

        }


    }
}