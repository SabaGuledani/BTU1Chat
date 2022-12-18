package com.saba.messagingapp

import android.widget.ImageView

class Message {
    var message:String? = null
    var senderId:String? = null
    var sendername:String? = null
    var senderImgUrl:String? = null

    constructor(){}

    constructor(message:String?, senderId:String?,sendername:String?, senderImgUrl:String?){
        this.message = message
        this.senderId = senderId
        this.sendername = sendername
        this.senderImgUrl = senderImgUrl
    }
}