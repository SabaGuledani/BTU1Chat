package com.saba.messagingapp

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
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
    private lateinit var messageAdapter: MessageAdapter
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
        val drawerLayout:DrawerLayout = binding.drawerLayout
        val navView:NavigationView = binding.navView

        toggle = ActionBarDrawerToggle(this,drawerLayout,R.string.open, R.string.close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)


        navView.setNavigationItemSelectedListener {
            when(it.itemId){
                R.id.log_out -> {FirebaseAuth.getInstance().signOut()
                        finish()}

            }
            true
        }






        messageList = ArrayList()
        messageAdapter = MessageAdapter(this, messageList)

        messageRecyclerView.layoutManager = LinearLayoutManager(this)
        messageRecyclerView.adapter = messageAdapter

        var header:View = navView.getHeaderView(0)


        FirebaseDatabase.getInstance().getReference("user").child("$senderUid").get()
            .addOnSuccessListener {
                if (it.exists()) {
                    val username = it.child("name").value
                    val img = it.child("img").value
                    val email = it.child("email").value
                    var userName = header.findViewById<TextView>(R.id.user_name_menu)
                    var userEmail = header.findViewById<TextView>(R.id.email_menu)
                    var image = header.findViewById<ImageView>(R.id.circle_img)
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
            var name: String
            var image: String


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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(toggle.onOptionsItemSelected(item)){
            return true
        }
        return super.onOptionsItemSelected(item)

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