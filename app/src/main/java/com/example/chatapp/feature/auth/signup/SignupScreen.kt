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
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.chatapp.R
import com.example.chatapp.feature.auth.signup.SignUpViewModel
import com.example.chatapp.feature.auth.signup.signUpState
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

@Composable
fun signUpScreen(navController: NavHostController) {
    val coroutineScope = rememberCoroutineScope()
    val viewModel : SignUpViewModel = hiltViewModel()
    var email by remember { mutableStateOf("") }

    val uiState = viewModel.state.collectAsState()

    fun onEmailChange(newValue: String){
        email = newValue
    }

    var passWord by remember { mutableStateOf("") }

    fun onPassWordChange(newValue: String){
        passWord = newValue
    }
    var name by remember { mutableStateOf("") }

    fun onNameChange(newValue: String){
        name = newValue
    }

    var confirmPassWord by remember { mutableStateOf("") }

    fun onConfirmPassWordChange(newValue: String){
        confirmPassWord = newValue
    }

    val context = LocalContext.current
    LaunchedEffect(key1 = uiState.value) {
        when(uiState.value){
            is signUpState.Success -> {
                navController.navigate("home")
            }
            is signUpState.Error -> {
                val errorMessage = (uiState.value as signInState.Error).message
                // Xử lý thông báo lỗi, ví dụ: hiển thị Toast
                Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
            }
            else -> {}
        }
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
                value = name,
                onValueChange = {onNameChange(it)},
                label = {Text(text = "Enter your full name")},
                placeholder = {Text("test@gmail.com")},
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = email,
                onValueChange = {onEmailChange(it)},
                label = {Text(text = "Enter your email")},
                placeholder = {Text("test@gmail.com")},
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = passWord,
                onValueChange = {onPassWordChange(it)},
                label = {Text(text = "Enter your password")},
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = confirmPassWord,
                onValueChange = {onConfirmPassWordChange(it)},
                label = {Text(text = "Confirm Password")},
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth(),
                isError = passWord.isNotEmpty() && confirmPassWord.isNotEmpty() && passWord != confirmPassWord
            )
            Spacer(modifier = Modifier.height(16.dp))
            if(uiState.value == signUpState.Loading){
                CircularProgressIndicator()
            }else{
                Button(
                    onClick = {
                        coroutineScope.launch {
                            viewModel.singUp(name = name,email = email, password =  passWord)
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = name.isNotEmpty()
                            && email.isNotEmpty()
                            && passWord.isNotEmpty()
                            && confirmPassWord.isNotEmpty()
                            && passWord == confirmPassWord
                ) {
                    Text(text = "Sign Up",
                        fontSize = 22.sp
                    )
                }
            }
            TextButton(onClick = {navController.navigate("login")}) {
                Text(text = "Already have an account? Sign In",
                    fontSize = 18.sp
                )
            }
        }
    }
}

