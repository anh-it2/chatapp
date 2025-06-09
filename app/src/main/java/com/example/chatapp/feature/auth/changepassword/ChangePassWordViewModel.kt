package com.example.chatapp.feature.auth.changepassword

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
class ChangePassWordViewModel @Inject constructor() : ViewModel(){
    private var _state = MutableStateFlow<changePassState>(changePassState.Nothing)
    val state = _state.asStateFlow()

    suspend fun changePassWord(newPass: String, pass: String, email: String){
        _state.value = changePassState.Loading

        try {
            FirebaseAuth.getInstance().signInWithEmailAndPassword(email,pass).await()
            FirebaseAuth.getInstance().currentUser!!.updatePassword(newPass).await()
            _state.value = changePassState.Success
        }catch (e: Exception){
            _state.value = changePassState.Error(e.message ?: "Unknown Error")
        }
    }
}

sealed class changePassState{
    object Success : changePassState()
    object Loading : changePassState()
    object Nothing : changePassState()
    data class Error(val message: String) : changePassState()
}