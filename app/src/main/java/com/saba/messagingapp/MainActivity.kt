package com.saba.messagingapp

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.saba.messagingapp.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var mDbRef: DatabaseReference

    private lateinit var toggle: ActionBarDrawerToggle

    private lateinit var messageList: ArrayList<Message>
    private lateinit var messageKeyList: ArrayList<String>
    private lateinit var messageAdapter: MessageAdapter
    private lateinit var reacts: String
    var active = intent?.extras?.getBoolean("true")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        createNotificationChannel()


        mDbRef = FirebaseDatabase.getInstance().getReference()

        val senderUid = FirebaseAuth.getInstance().currentUser?.uid


        val messageRecyclerView = binding.recycler
        val messagebox = binding.messageBox
        val sendbutton: ImageView = binding.sendImage
        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        var replytext = binding.replyText
        var replyreset = binding.resetReply
        var responselayout = binding.responseLayout
        toggle = ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        //navview handling
        navView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.log_out -> {
                    FirebaseAuth.getInstance().signOut()
                    startActivity(Intent(this, LogIn::class.java))
                    finish()
                }
                R.id.name_change -> {
                    startActivity(Intent(this, changeName::class.java))
                }

            }
            true
        }






        messageList = ArrayList()
        messageAdapter = MessageAdapter(this, messageList)

        messageRecyclerView.layoutManager = LinearLayoutManager(this)
        messageRecyclerView.adapter = messageAdapter

        var header: View = navView.getHeaderView(0)

        //header info display
        FirebaseDatabase.getInstance().getReference("user").child("$senderUid").get()
            .addOnSuccessListener {
                if (it.exists()) {
                    val username = it.child("name").value
                    val img = it.child("img").value
                    val email = it.child("email").value
                    val userName = header.findViewById<TextView>(R.id.user_name_menu)
                    val userEmail = header.findViewById<TextView>(R.id.email_menu)
                    val image = header.findViewById<ImageView>(R.id.circle_img)
                    userEmail.setText(email.toString())
                    userName.setText(username.toString())
                    Glide.with(this)
                        .load(img.toString())
                        .fitCenter()
                        .skipMemoryCache(true)
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .into(image)

                }
            }

        //displaying messages and notifying

        messageKeyList = ArrayList()
        mDbRef.child("chat").child("messages")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    messageList.clear()
                    messageKeyList.clear()
                    for (postSnapshot in snapshot.children) {
                        val message = postSnapshot.getValue((Message::class.java))
                        var messagekey = postSnapshot.key
                        messageList.add((message!!))
                        messageKeyList.add(messagekey!!)

                    }
                    var message = messageList.last()
                    var builder = NotificationCompat.Builder(applicationContext, "1")
                        .setSmallIcon(R.drawable.ic_baseline_sms_24)
                        .setContentTitle(message.sendername)
                        .setContentText(message.message)
                        .setPriority(NotificationCompat.PRIORITY_HIGH)
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

        var responseMessage = ""
        //response click
        messageAdapter.setOnItemClickListener(object : MessageAdapter.onItemClickListener {
            override fun onItemClick(position: Int): String {
                responseMessage = messageList[position].message.toString()
                responselayout.visibility = View.VISIBLE
                replytext.text = responseMessage
                return responseMessage
            }
        })

        //response cancel
        replyreset.setOnClickListener {
            responseMessage = ""
            responselayout.visibility = View.GONE
            replytext.text = responseMessage
        }

        reacts = ""

        messageAdapter.setOnItemLongClickListener(object : MessageAdapter.OnLongCLickListener {
            override fun onItemLongClick(position: Int) {
                (binding.layoutEditText).visibility = View.GONE
                (binding.layoutEmoji).visibility = View.VISIBLE
                var clickedMessage = messageKeyList[position]

                (binding.guja).setOnClickListener {
                    mDbRef.child("chat").child("messages").child(clickedMessage).child("reactguja")
                        .setValue("guja")
                    visibility()
                }
                (binding.niniShoki).setOnClickListener {
                    mDbRef.child("chat").child("messages").child(clickedMessage).child("reactnini")
                        .setValue("nini")
                    visibility()
                }
                (binding.barbareDafoe).setOnClickListener {
                    mDbRef.child("chat").child("messages").child(clickedMessage).child("reactbarbi")
                        .setValue("barbi")
                    visibility()
                }
                (binding.saxia).setOnClickListener {
                    mDbRef.child("chat").child("messages").child(clickedMessage).child("reactsaxia")
                        .setValue("saxia")
                    visibility()
                }
                (binding.saxiasMgluriPorma).setOnClickListener {
                    mDbRef.child("chat").child("messages").child(clickedMessage).child("reactmgeli")
                        .setValue("mgeli")
                    visibility()
                }
                (binding.jesus).setOnClickListener {
                    mDbRef.child("chat").child("messages").child(clickedMessage).child("reactjesus")
                        .setValue("jesus")
                    visibility()
                }


            }
        })


        //sending message
        sendbutton.setOnClickListener {
            val message = messagebox.text.toString()
            var name: String
            var image: String

            if (message != "") {
                FirebaseDatabase.getInstance().getReference("user").child("$senderUid").get()
                    .addOnSuccessListener {

                        if (it.exists()) {
                            val username = it.child("name").value
                            val img = it.child("img").value
                            name = username.toString()
                            image = img.toString()
                            var reactguja = ""
                            var reactnini = ""
                            var reactsaxia = ""
                            var reactmgeli = ""
                            var reactjesus = ""
                            var reactbarbi = ""
                            val messageObject = Message(
                                message, senderUid, name, image, responseMessage,
                                reactguja, reactsaxia, reactmgeli, reactnini, reactbarbi, reactjesus
                            )

                            mDbRef.child("chat").child("messages").push()
                                .setValue(messageObject)

                            messagebox.setText("")
                            responseMessage = ""
                            responselayout.visibility = View.GONE


                        }
                    }
            }


        }
    }


    //toggle
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)) {
            return true
        }
        return super.onOptionsItemSelected(item)

    }

    //active status?
    override fun onResume() {
        super.onResume()
        active = true
    }

    override fun onPause() {
        super.onPause()
        active = false

    }

    fun visibility() {
        (binding.layoutEditText).visibility = View.VISIBLE
        (binding.layoutEmoji).visibility = View.GONE
    }

    //notification channel function
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