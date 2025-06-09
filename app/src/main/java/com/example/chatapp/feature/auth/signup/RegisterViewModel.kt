package com.example.chatapp.feature.auth.signup

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor() : ViewModel(){
    private val _state = MutableStateFlow<registerState>(registerState.Nothing)
    val state = _state.asStateFlow()

    suspend fun register(email: String, password: String){
        _state.value = registerState.Loading
        try {
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email,password).await()
            _state.value = registerState.Success
        }catch (e: Exception){
            _state.value = registerState.Error(e.message ?: "don't know")
        }

    }
}

// seal class is used to manage the state of registation

sealed class registerState{
    object Success : registerState()
    object Loading : registerState()
    object Nothing : registerState()
    data class Error(val message: String) : registerState()
}