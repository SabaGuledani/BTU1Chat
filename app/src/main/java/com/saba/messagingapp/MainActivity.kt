package com.saba.messagingapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.saba.messagingapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var mDbRef: DatabaseReference

    private lateinit var messageList:ArrayList<Message>
    private lateinit var messageAdapter: MessageAdapter




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        mDbRef = FirebaseDatabase.getInstance().getReference()

        val senderUid = FirebaseAuth.getInstance().currentUser?.uid











        var messageRecyclerView = binding.recycler
        var messagebox = binding.messageBox
        var sendbutton:ImageView =binding.sendImage
        var textinv = binding.textinv
        messageList = ArrayList()
        messageAdapter = MessageAdapter(this,messageList)

        messageRecyclerView.layoutManager = LinearLayoutManager(this)
        messageRecyclerView.adapter = messageAdapter





        mDbRef.child("chat").child("messages")
            .addValueEventListener(object:ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    messageList.clear()
                    for(postSnapshot in snapshot.children){
                        val message = postSnapshot.getValue((Message::class.java))
                        messageList.add((message!!))
                    }
                    messageAdapter.notifyDataSetChanged()
                    messageRecyclerView.scrollToPosition(messageAdapter.itemCount-1)
                }

                override fun onCancelled(error: DatabaseError) {

                }

            })



        sendbutton.setOnClickListener{
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
}