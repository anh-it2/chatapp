package com.example.chatapp.feature.auth.signin

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel

class SignInViewModel @Inject constructor() : ViewModel(){

    private val _state = MutableStateFlow<signInState>(signInState.Nothing)
    val state = _state.asStateFlow()

    suspend fun singIn(email: String, password: String){
        _state.value = signInState.Loading
        try{
            FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password).await()
            _state.value = signInState.Success
        } catch (e: Exception){
            _state.value = signInState.Error(e.message ?: "Unknown Error")
        }
    }
}

sealed class signInState{
    object Nothing : signInState()
    object Loading : signInState()
    object Success : signInState()
    data class Error(val message: String) : signInState()
}