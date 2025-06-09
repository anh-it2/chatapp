package com.example.chatapp.feature.auth.signin

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor() : ViewModel(){
    private var _state = MutableStateFlow<loginState>(loginState.Nothing)
    var state = _state.asStateFlow()

    suspend fun login(email: String, password: String){
        _state.value = loginState.Loading
        try {
            FirebaseAuth.getInstance().signInWithEmailAndPassword(email,password).await()
            _state.value = loginState.Success
        }catch (e: Exception){
            _state.value = loginState.Error(e.message ?: "Unknown Error")

        }
    }
    suspend fun changePassWord(newPass: String){
        _state.value = loginState.Loading
        try {
            FirebaseAuth.getInstance().currentUser!!.updatePassword(newPass).await()
            _state.value = loginState.Success
        }catch (e: Exception){
            _state.value = loginState.Error(e.message ?: "Unknown Error")
        }
    }
}

sealed class loginState{
    object Success : loginState()
    object Nothing : loginState()
    object Loading : loginState()
    data class Error(val message: String) : loginState()
}