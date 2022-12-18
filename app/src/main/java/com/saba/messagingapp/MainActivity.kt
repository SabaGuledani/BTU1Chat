package com.saba.messagingapp

import android.app.Activity
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.saba.messagingapp.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var mDbRef: DatabaseReference

    private lateinit var messageList: ArrayList<Message>
    private lateinit var messageAdapter: MessageAdapter
    var active = intent?.extras?.getBoolean("true")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        createNotificationChannel()


        mDbRef = FirebaseDatabase.getInstance().getReference()

        val senderUid = FirebaseAuth.getInstance().currentUser?.uid


        var messageRecyclerView = binding.recycler
        var messagebox = binding.messageBox
        var sendbutton: ImageView = binding.sendImage

        messageList = ArrayList()
        messageAdapter = MessageAdapter(this, messageList)

        messageRecyclerView.layoutManager = LinearLayoutManager(this)
        messageRecyclerView.adapter = messageAdapter





        mDbRef.child("chat").child("messages")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    messageList.clear()
                    for (postSnapshot in snapshot.children) {
                        val message = postSnapshot.getValue((Message::class.java))
                        messageList.add((message!!))
                    }
                    var message = messageList.last()
                    var builder = NotificationCompat.Builder(applicationContext, "1")
                        .setSmallIcon(R.drawable.ic_baseline_sms_24)
                        .setContentTitle(message.sendername)
                        .setContentText(message.message)
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    if (senderUid != message.senderId) {
                        if (active != true) {
                            with(NotificationManagerCompat.from(applicationContext)) {
                                notify(1, builder.build())
                            }
                        }
                    }
                    messageAdapter.notifyDataSetChanged()
                    messageRecyclerView.scrollToPosition(messageAdapter.itemCount - 1)
                }

                override fun onCancelled(error: DatabaseError) {

                }

            })



        sendbutton.setOnClickListener {
            val message = messagebox.text.toString()
            var name = ""
            var image = ""


            FirebaseDatabase.getInstance().getReference("user").child("$senderUid").get()
                .addOnSuccessListener {

                    if (it.exists()) {
                        val username = it.child("name").value
                        val img = it.child("img").value
                        name = username.toString()
                        image = img.toString()
                        val messageObject = Message(message, senderUid, name, image)

                        mDbRef.child("chat").child("messages").push()
                            .setValue(messageObject)

                        messagebox.setText("")


                    }
                }
        }
    }

    override fun onResume() {
        super.onResume()
        active = true
    }

    override fun onPause() {
        super.onPause()
        active = false

    }

    private fun createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "message"
            val descriptionText = "messagenotification"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel("1", name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }



}