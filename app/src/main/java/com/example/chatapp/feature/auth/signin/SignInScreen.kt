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
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
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
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

@Composable
fun signinScreen(navController: NavHostController) {
    val coroutineScope = rememberCoroutineScope()

    val viewModel : SignInViewModel = hiltViewModel()

    val uiState = viewModel.state.collectAsState()

    var email by remember { mutableStateOf("") }

    fun onEmailChange(newValue: String){
        email = newValue
    }

    var passWord by remember { mutableStateOf("") }
    val context = LocalContext.current
    LaunchedEffect(key1 = uiState.value) {
        when(uiState.value){
            is signInState.Success -> {
                navController.navigate("home")
            }
            is signInState.Error -> {
                val errorMessage = (uiState.value as signInState.Error).message
                // Xử lý thông báo lỗi, ví dụ: hiển thị Toast
                Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
            }
            else -> {}
        }
    }

    fun onPassWordChange(newValue: String){
        passWord = newValue
    }
    Scaffold (modifier = Modifier.fillMaxSize()){
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .padding(16.dp)
                .background(Color.White),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Image(painter = painterResource(id = R.drawable.logo), contentDescription = null,
                modifier = Modifier
                    .size(200.dp)
                    .background(Color.White)
                )
            OutlinedTextField(
                value = email,
                onValueChange = {onEmailChange(it)},
                label = {Text(text = "Enter your email")},
                placeholder = {Text("test@gmail.com")},
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(10.dp) )
            OutlinedTextField(
                value = passWord,
                onValueChange = {onPassWordChange(it)},
                label = {Text(text = "Enter your password")},
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
            if(uiState.value == signInState.Loading){
                CircularProgressIndicator()
            }
            else{

                Button(
                    onClick = {
                        coroutineScope.launch {
                            viewModel.singIn(email, passWord)
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = email.isNotEmpty() && passWord.isNotEmpty()
                            && (uiState.value == signInState.Nothing || uiState.value is signInState.Error)

                ) {
                    Text(text = "Sign In",
                        fontSize = 22.sp
                    )
                }
            }
            TextButton(onClick = {navController.navigate("signup")}) {
                Text(text = "Don't have an account? Sign up",
                    fontSize = 18.sp
                    )
            }
        }
    }
}

