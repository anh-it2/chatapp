package com.example.chatapp.feature.auth.signup

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

//@HiltViewModel

//class SignUpViewModel @Inject constructor() : ViewModel(){
//
//    private val _state = MutableStateFlow<signUpState>(signUpState.Nothing)
//    val state = _state.asStateFlow()
//
//    suspend fun singUp(name: String, email: String, password: String){
//        _state.value = signUpState.Loading
//        try{
//            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email,password).await()
//            _state.value = signUpState.Success
//        } catch (e: Exception){
//            _state.value = signUpState.Error(e.message ?: "Unknown Error")
//        }
//    }
//}
//
//sealed class signUpState{
//    object Nothing : signUpState()
//    object Loading : signUpState()
//    object Success : signUpState()
//    data class Error(val message: String) : signUpState()
//}