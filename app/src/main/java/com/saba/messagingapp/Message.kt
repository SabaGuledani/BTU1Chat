package com.saba.messagingapp

import android.widget.ImageView

class Message {
    var message:String? = null
    var senderId:String? = null
    var sendername:String? = null
    var senderImgUrl:String? = null
    var reactguja:String? = null
    var reactnini:String? = null
    var reactsaxia:String? = null
    var reactmgeli:String? = null
    var reactjesus:String? = null
    var reactbarbi:String? = null
    var responsetext:String? = null

    constructor(){}

    constructor(message:String?, senderId:String?,sendername:String?, senderImgUrl:String?,responseText:String
                ,reactguja:String?,reactsaxia:String?,reactmgeli:String?,reactnini:String?,reactbarbi:String?,reactjesus:String?){
        this.message = message
        this.senderId = senderId
        this.sendername = sendername
        this.senderImgUrl = senderImgUrl
        this.responsetext = responseText
        this.reactguja = reactguja
        this.reactnini = reactnini
        this.reactjesus = reactjesus
        this.reactsaxia = reactsaxia
        this.reactbarbi = reactbarbi
        this.reactmgeli = reactmgeli

    }
}