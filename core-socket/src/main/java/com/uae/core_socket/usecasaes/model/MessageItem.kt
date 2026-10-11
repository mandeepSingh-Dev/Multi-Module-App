package com.uae.core_socket.usecasaes.model


import com.google.gson.annotations.SerializedName

data class MessageItem(
    val message: Message?,
    val timestamp: String?
) {
    data class Message(
        val chatId: String?,
        val createdAt: String?,
        val fileName: String?,
        @SerializedName("_id")
        val id: String?,
        val isRead: Boolean?,
        val message: String?,
        val messageType: String?,
        val mimeType: String?,
        val role: Int?,
        val size: Int?,
        val updatedAt: String?,
        val url: String?,
        val user: User?,
        val userId: String?,
        @SerializedName("__v")
        val v: Int?
    ){
        data class User(
            val _id: String?,
            val fullname: String?,
            val profilePic: String?,
        )

    }
}

/*
{
    "message": {
    "_id": "6a6882f52f77111ee52d21a1",
    "chatId": "6a5f732432115846287e9136",
    "user": null,
    "userId": "69b7bfc6d10d99d0ae00c78d",
    "messageType": "image",
    "fileName": "Image (28).jpg",
    "mimeType": "image/jpeg",
    "size": "486777",
    "url": "https://uaeassist.s3.ap-south-1.amazonaws.com/uaeassist/chat/a50b1b86-e33b-4edb-98e4-f8ceec79dec9.jpg",
    "message": "",
    "isRead": false,
    "role": 2,
    "createdAt": "2026-07-28T10:22:45.829Z",
    "updatedAt": "2026-07-28T10:22:45.829Z",
    "__v": 0
},
    "timestamp": "2026-07-28T10:22:45.844Z"
}*/
