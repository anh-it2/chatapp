package com.example.chatapp.feature.home

import androidx.compose.runtime.key
import androidx.lifecycle.ViewModel
import com.example.chatapp.model.Channel
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.database
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(): ViewModel() {
    private val firebaseDatabase = Firebase.database
    private val _channels = MutableStateFlow<List<Channel>>(emptyList())
    val channels = _channels.asStateFlow()
    init {
        println("🔥 ViewModel INIT called")
        getChannels()
    }

    private fun getChannels() {
        firebaseDatabase.getReference("channel").get()
            .addOnSuccessListener { snapshot ->
                val list = mutableListOf<Channel>()
                snapshot.children.forEach { data ->
                    val name = data.getValue(String::class.java)
                    val id = data.key
                    if (id != null && name != null) {
                        val channel = Channel(id = id, name = name)
                        list.add(channel)
                    }
                }
                _channels.value = list
            }
            .addOnFailureListener {
                println("❌ Firebase error: ${it.message}")
            }
    }

    fun addChannel(name: String) {
        val key = firebaseDatabase.getReference("channel").push().key
        firebaseDatabase.getReference("channel").child(key!!).setValue(name).addOnSuccessListener {
            getChannels()
        }
    }
}
//private var _channels = MutableStateFlow<List<Channel>>(emptyList())
//    val channels = _channels.asStateFlow()
//    val FirebaseDatabase = Firebase.database
//    init {
//        getChannel()
//    }
//    fun getChannel(){
//        FirebaseDatabase.getReference("channel").get().addOnSuccessListener {
//            var list = mutableListOf<Channel>()
//            it.children.forEach{ data ->
//                val channel = Channel(id = data.key!!, name = data.value.toString())
//                list.add(channel)
//            }
//            _channels.value = list
//        }
//    }
//    fun addChannel(name: String){
//        val channelId = FirebaseDatabase.getReference("channel").push().key
//        FirebaseDatabase.getReference("channel").child(channelId!!).setValue(name).addOnSuccessListener {
//            getChannel()
//        }
//    }
//
//}