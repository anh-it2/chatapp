package com.example.chatapp.feature.auth.signin

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.motionEventSpy
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.chatapp.R
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import androidx.compose.animation.AnimatedVisibility


@Composable
fun signinScreen(navController: NavHostController) {
//    val coroutineScope = rememberCoroutineScope()
//
//    val viewModel : SignInViewModel = hiltViewModel()
//
//    val uiState = viewModel.state.collectAsState()
//
//    var email by remember { mutableStateOf("") }
//
//    fun onEmailChange(newValue: String){
//        email = newValue
//    }
//
//    var passWord by remember { mutableStateOf("") }
//    val context = LocalContext.current
//    LaunchedEffect(key1 = uiState.value) {
//        when(uiState.value){
//            is signInState.Success -> {
//                navController.navigate("home")
//            }
//            is signInState.Error -> {
//                val errorMessage = (uiState.value as signInState.Error).message
//                // Xử lý thông báo lỗi, ví dụ: hiển thị Toast
//                Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
//            }
//            else -> {}
//        }
//    }
//
//    fun onPassWordChange(newValue: String){
//        passWord = newValue
//    }
//    Scaffold (modifier = Modifier.fillMaxSize()){
//        Column(
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(it)
//                .padding(16.dp)
//                .background(Color.White),
//            verticalArrangement = Arrangement.Center,
//            horizontalAlignment = Alignment.CenterHorizontally,
//        ) {
//            Image(painter = painterResource(id = R.drawable.logo), contentDescription = null,
//                modifier = Modifier
//                    .size(200.dp)
//                    .background(Color.White)
//                )
//            OutlinedTextField(
//                value = email,
//                onValueChange = {onEmailChange(it)},
//                label = {Text(text = "Enter your email")},
//                placeholder = {Text("test@gmail.com")},
//                modifier = Modifier.fillMaxWidth()
//            )
//            Spacer(modifier = Modifier.height(10.dp) )
//            OutlinedTextField(
//                value = passWord,
//                onValueChange = {onPassWordChange(it)},
//                label = {Text(text = "Enter your password")},
//                visualTransformation = PasswordVisualTransformation(),
//                modifier = Modifier.fillMaxWidth()
//            )
//            Spacer(modifier = Modifier.height(16.dp))
//            if(uiState.value == signInState.Loading){
//                CircularProgressIndicator()
//            }
//            else{
//
//                Button(
//                    onClick = {
//                        coroutineScope.launch {
//                            viewModel.singIn(email, passWord)
//                        }
//                    },
//                    modifier = Modifier.fillMaxWidth(),
//                    enabled = email.isNotEmpty() && passWord.isNotEmpty()
//                            && (uiState.value == signInState.Nothing || uiState.value is signInState.Error)
//
//                ) {
//                    Text(text = "Sign In",
//                        fontSize = 22.sp
//                    )
//                }
//            }
//            TextButton(onClick = {navController.navigate("signup")}) {
//                Text(text = "Don't have an account? Sign up",
//                    fontSize = 18.sp
//                    )
//            }
//        }
//    }
    var email by remember { mutableStateOf("") }

    fun onEmailChange(newValue: String){
        email = newValue;
    }

    var password by remember { mutableStateOf("") }
    fun onPassWordChange(newValue: String){
        password = newValue
    }
    val viewModel : LoginViewModel = hiltViewModel()
    val coRoutineScope = rememberCoroutineScope()
    var uiState = viewModel.state.collectAsState()
    val context = LocalContext.current
    var passVisible by remember { mutableStateOf(true) }
    var visible = if(passVisible) PasswordVisualTransformation() else VisualTransformation.None
    LaunchedEffect(key1 = uiState.value) {
        when(uiState.value){
            is loginState.Success -> {
                navController.navigate("home")
            }
            is loginState.Error ->{
                val errMessage = (uiState.value as loginState.Error).message
                Toast.makeText(context,errMessage,Toast.LENGTH_SHORT).show()
            }
            else ->{}
        }
    }
    Scaffold (modifier = Modifier.fillMaxSize()){
        Column (
            modifier = Modifier
                .fillMaxSize()
                .padding(it),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            Image(
                painter = painterResource(R.drawable.logo),
                contentDescription = null,
                modifier = Modifier.size(150.dp)
                )
            OutlinedTextField(
                value = email,
                onValueChange = {onEmailChange(it)},
                label = {
                    Text(text = "Enter your email")
                },
                placeholder = {
                    Text(text = "test@gmail.com")
                },
                trailingIcon = {
                    Icon(Icons.Default.Email, contentDescription = null, Modifier.size(30.dp))
                },
                modifier = Modifier.width(350.dp),
                singleLine = true
            )
            Spacer(modifier = Modifier.height(20.dp))
            OutlinedTextField(
                value = password,
                onValueChange = {onPassWordChange(it)},
                label = {
                    Text(text = "Enter your password")
                },
                modifier = Modifier.width(350.dp),
                trailingIcon = {
                    IconButton(onClick = {
                        passVisible = !passVisible
                    }) {
                        Icon(Icons.Default.Lock, contentDescription = null, Modifier.size(30.dp))
                    }
                },

                visualTransformation = visible,
                singleLine = true
            )

            Spacer(modifier = Modifier.height(10.dp) )
            if(uiState.value == loginState.Loading){
                AnimatedVisibility(visible = uiState.value == loginState.Loading) {
                    CircularProgressIndicator(
                        color = Color.Blue,
                        modifier = Modifier.size(36.dp)
                    )
                }

            }else{
                Button(
                    onClick = {
                        coRoutineScope.launch {
                            viewModel.login(email = email, password = password)
                        }
                    },
                    modifier = Modifier.width(350.dp),
                    enabled = email.isNotEmpty() && password.isNotEmpty(),
                ) {
                    Text(
                        text = "Send",
                        fontSize = 18.sp,
                    )
                }
            }
            TextButton(onClick = {
                navController.navigate("register")
            }) {
                Text(
                    text = "Don't have an account? Signin",
                    fontSize = 20.sp
                    )
            }
            TextButton(onClick = {
                navController.navigate("changePassWord")
            }) {
                Text(text = "Change your password", fontSize = 20.sp)
            }
        }
    }
}

