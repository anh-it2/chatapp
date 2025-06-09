package com.example.chatapp.feature.home

import androidx.lifecycle.ViewModel
import com.example.chatapp.model.Channel
import com.google.firebase.Firebase
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.database
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.Collections.list
import javax.inject.Inject

@HiltViewModel

class ChannelViewModel @Inject constructor() : ViewModel(){
    private var _channels = MutableStateFlow<List<Channel>>(emptyList())
    val channels = _channels.asStateFlow()
    val FirebaseDatabase = Firebase.database
    init {
        getChannel()
    }
    fun getChannel(){
        FirebaseDatabase.getReference("channel").get().addOnSuccessListener {
            var list = mutableListOf<Channel>()
            it.children.forEach{ data ->
                val channel = Channel(id = data.key!!, name = data.value.toString())
                list.add(channel)
            }
            _channels.value = list
        }
    }
    fun addChannel(name: String){
        val channelId = FirebaseDatabase.getReference("channel").push().key
        FirebaseDatabase.getReference("channel").child(channelId!!).setValue(name).addOnSuccessListener {
            getChannel()
        }
    }
}