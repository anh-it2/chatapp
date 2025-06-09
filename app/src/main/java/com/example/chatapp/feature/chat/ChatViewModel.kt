package com.example.chatapp.feature.chat

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.layout.PaddingValues
import androidx.lifecycle.ViewModel
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.chatapp.FirebaseMessageService
import com.example.chatapp.model.Message
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.storage.storage
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.json.JSONObject
import java.util.UUID
import javax.inject.Inject


//import androidx.lifecycle.ViewModel
//import com.example.chatapp.model.Message
//import com.google.firebase.Firebase
//import com.google.firebase.auth.auth
//import com.google.firebase.database.DataSnapshot
//import com.google.firebase.database.DatabaseError
//import com.google.firebase.database.ValueEventListener
//import com.google.firebase.database.database
//import dagger.hilt.android.lifecycle.HiltViewModel
//import kotlinx.coroutines.flow.MutableStateFlow
//import kotlinx.coroutines.flow.asStateFlow
//import java.util.UUID
//import javax.inject.Inject
//
//@HiltViewModel
//class ChatViewModel @Inject constructor(): ViewModel() {
//    private val _messages = MutableStateFlow<List<Message>>(emptyList())
//    val message = _messages.asStateFlow()
//    val db = Firebase.database
//
//    fun sendMessage(channelID: String, messageText: String) {
//        val message = Message(
//            db.reference.push().key ?: UUID.randomUUID().toString(),
//            Firebase.auth.currentUser?.uid ?: "",
//            messageText,
//            System.currentTimeMillis().toString(),
//            Firebase.auth.currentUser?.displayName ?: "",
//            null.toString(),
//            null
//        )
//
//        db.reference.child("messages").child(channelID).push().setValue(message)
//    }
//    fun listenForMessages(channelID: String) {
//        db.getReference("messages").child(channelID).orderByChild("createdAt")
//            .addValueEventListener(object : ValueEventListener {
//                override fun onDataChange(snapshot: DataSnapshot) {
//                    val list = mutableListOf<Message>()
//                    snapshot.children.forEach { data ->
//                        val message = data.getValue(Message::class.java)
//                        message?.let {
//                            list.add(it)
//                        }
//                    }
//                    _messages.value = list
//                }
//
//                override fun onCancelled(error: DatabaseError) {
//                    // Handle error
//                }
//            })
//    }
//}
@HiltViewModel
class ChatViewModel @Inject constructor(@ApplicationContext val context: Context) : ViewModel(){
    val db = Firebase.database
    private var _messages = MutableStateFlow<List<Message>>(emptyList())
    val messages = _messages.asStateFlow()

    fun sendMessage(channelId: String, textMess: String, image: String? = null){
        val message = Message(
            id = db.reference.push().key ?: UUID.randomUUID().toString(),
            senderId = Firebase.auth.currentUser?.uid ?: "",
            senderName = Firebase.auth.currentUser?.displayName ?: "",
            message = textMess,
            createdAt = System.currentTimeMillis().toString(),
            senderImage = null.toString(),
            imageUrl = image,

        )
        db.getReference("message").child(channelId).push().setValue(message)
    }

    fun sendImageMessage(uri: Uri, channelId: String){
        val imageRef = Firebase.storage.reference.child("image/${UUID.randomUUID()}")
        imageRef.putFile(uri).continueWithTask { task ->
            if (!task.isSuccessful) {
                task.exception?.let {
                    throw it
                }
            }
            imageRef.downloadUrl
        }.addOnCompleteListener { task ->
            val currentUser = Firebase.auth.currentUser
            if (task.isSuccessful) {
                val downloadUri = task.result
                sendMessage(channelId, null.toString(), downloadUri.toString())
            }
        }
    }
    fun listenForMessage(channelId: String){
        db.getReference("message").child(channelId).orderByChild("createdAt")
            .addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    var list = mutableListOf<Message>()
                    snapshot.children.forEach{
                        val message = it.getValue(Message::class.java)
                        message?.let {
                            list.add(message)
                        }
                    }
                    _messages.value = list
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })
        subscribeForNotification(channelId)
    }

    private fun subscribeForNotification(channelId: String){
        FirebaseMessaging.getInstance().subscribeToTopic("group_$channelId").addOnCompleteListener{
            if(it.isSuccessful){
                Log.d("ChatViewModel", "Subscribed to topic: group_$channelId")
            }
            else{
                Log.d("ChatViewModel", "Failed to subscribed to topic: group_$channelId")
            }
        }
    }

    private fun postNotificationToUsers(
        channelID: String,
        senderName: String,
        messageContent: String
    ) {
        val fcmUrl = "https://fcm.googleapis.com/v1/projects/chatapp-3fde9/messages:send"
        val jsonBody = JSONObject().apply {
            put("message", JSONObject().apply {
                put("topic", "group_$channelID")
                put("notification", JSONObject().apply {
                    put("title", "New message in $channelID")
                    put("body", "$senderName: $messageContent")
                })
            })
        }

        val requestBody = jsonBody.toString()

        val request = object : StringRequest(Method.POST, fcmUrl, Response.Listener {
            Log.d("ChatViewModel", "Notification sent successfully")
        }, Response.ErrorListener {
            Log.e("ChatViewModel", "Failed to send notification")
        }) {
            override fun getBody(): ByteArray {
                return requestBody.toByteArray()
            }

            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                headers["Authorization"] = "Bearer }"
                headers["Content-Type"] = "application/json"
                return headers
            }
        }
        val queue = Volley.newRequestQueue(context)
        queue.add(request)
    }

//    private fun getAccessToken(): String {
//        val inputStream = context.resources.openRawResource(R.raw.chatter_key)
//        val googleCreds = GoogleCredentials.fromStream(inputStream)
//            .createScoped(listOf("https://www.googleapis.com/auth/firebase.messaging"))
//        return googleCreds.refreshAccessToken().tokenValue
//    }
}
