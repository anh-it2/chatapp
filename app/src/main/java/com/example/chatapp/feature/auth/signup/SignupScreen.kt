package com.example.chatapp.feature.auth.signin

import android.text.style.IconMarginSpan
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
import androidx.compose.material.icons.filled.Person
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
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.chatapp.R
import com.example.chatapp.feature.auth.signup.RegisterViewModel
import com.example.chatapp.feature.auth.signup.registerState
import com.google.firebase.crashlytics.buildtools.reloc.javax.annotation.meta.When
//import com.example.chatapp.feature.auth.signup.SignUpViewModel
//import com.example.chatapp.feature.auth.signup.signUpState
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

@Composable
fun signUpScreen(navController: NavHostController) {
//    val coroutineScope = rememberCoroutineScope()
//    val viewModel : SignUpViewModel = hiltViewModel()
//    var email by remember { mutableStateOf("") }
//
//    val uiState = viewModel.state.collectAsState()
//
//    fun onEmailChange(newValue: String){
//        email = newValue
//    }
//
//    var passWord by remember { mutableStateOf("") }
//
//    fun onPassWordChange(newValue: String){
//        passWord = newValue
//    }
//    var name by remember { mutableStateOf("") }
//
//    fun onNameChange(newValue: String){
//        name = newValue
//    }
//
//    var confirmPassWord by remember { mutableStateOf("") }
//
//    fun onConfirmPassWordChange(newValue: String){
//        confirmPassWord = newValue
//    }
//
//    val context = LocalContext.current
//    LaunchedEffect(key1 = uiState.value) {
//        when(uiState.value){
//            is signUpState.Success -> {
//                navController.navigate("home")
//            }
//            is signUpState.Error -> {
//                val errorMessage = (uiState.value as signInState.Error).message
//                // Xử lý thông báo lỗi, ví dụ: hiển thị Toast
//                Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
//            }
//            else -> {}
//        }
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
//            )
//            OutlinedTextField(
//                value = name,
//                onValueChange = {onNameChange(it)},
//                label = {Text(text = "Enter your full name")},
//                placeholder = {Text("test@gmail.com")},
//                modifier = Modifier.fillMaxWidth()
//            )
//            OutlinedTextField(
//                value = email,
//                onValueChange = {onEmailChange(it)},
//                label = {Text(text = "Enter your email")},
//                placeholder = {Text("test@gmail.com")},
//                modifier = Modifier.fillMaxWidth()
//            )
//
//            OutlinedTextField(
//                value = passWord,
//                onValueChange = {onPassWordChange(it)},
//                label = {Text(text = "Enter your password")},
//                visualTransformation = PasswordVisualTransformation(),
//                modifier = Modifier.fillMaxWidth()
//            )
//            OutlinedTextField(
//                value = confirmPassWord,
//                onValueChange = {onConfirmPassWordChange(it)},
//                label = {Text(text = "Confirm Password")},
//                visualTransformation = PasswordVisualTransformation(),
//                modifier = Modifier.fillMaxWidth(),
//                isError = passWord.isNotEmpty() && confirmPassWord.isNotEmpty() && passWord != confirmPassWord
//            )
//            Spacer(modifier = Modifier.height(16.dp))
//            if(uiState.value == signUpState.Loading){
//                CircularProgressIndicator()
//            }else{
//                Button(
//                    onClick = {
//                        coroutineScope.launch {
//                            viewModel.singUp(name = name,email = email, password =  passWord)
//                        }
//                    },
//                    modifier = Modifier.fillMaxWidth(),
//                    enabled = name.isNotEmpty()
//                            && email.isNotEmpty()
//                            && passWord.isNotEmpty()
//                            && confirmPassWord.isNotEmpty()
//                            && passWord == confirmPassWord
//                ) {
//                    Text(text = "Sign Up",
//                        fontSize = 22.sp
//                    )
//                }
//            }
//            TextButton(onClick = {navController.navigate("login")}) {
//                Text(text = "Already have an account? Sign In",
//                    fontSize = 18.sp
//                )
//            }
//        }
//    }
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember{
        mutableStateOf("")
    }
    var cfPassWord by remember { mutableStateOf("") }

    val viewModel : RegisterViewModel = hiltViewModel()
    val uiState = viewModel.state.collectAsState()
    val coRoutinScope = rememberCoroutineScope()
    val context = LocalContext.current
    var passVisible by remember { mutableStateOf(true) }
    var cfPassVisible by remember { mutableStateOf(true) }
    var visible = if (passVisible) PasswordVisualTransformation() else VisualTransformation.None
    var cfVisible = if(cfPassVisible) PasswordVisualTransformation() else VisualTransformation.None
    LaunchedEffect(key1 = uiState.value) {
        when(uiState.value){
            is registerState.Success -> {
                navController.navigate("home")
            }
            is registerState.Error -> {
                val errorMessage = (uiState.value as registerState.Error).message
                Toast.makeText(context,errorMessage,Toast.LENGTH_SHORT).show()
            }
            else ->{}
        }
    }
    Scaffold (modifier = Modifier.fillMaxSize()){
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(R.drawable.logo),
                contentDescription = null,
                modifier = Modifier.size(150.dp)
            )
            OutlinedTextField(
                value = name,
                onValueChange = {it -> name = it},
                modifier = Modifier.width(350.dp),
                label = {
                    Text(text = "Enter your name")
                },
                trailingIcon = {
                    Icon(Icons.Default.Person, contentDescription = null, Modifier.size(30.dp))
                }
            )
            Spacer(modifier = Modifier.height(10.dp))
            OutlinedTextField(
                value = email,
                onValueChange = {it -> email = it},
                label = {
                    Text(text = "Enter your email")
                },
                placeholder = {
                    Text(text = "test@gmail.com")
                },
                modifier = Modifier.width(350.dp),
                trailingIcon = {
                    Icon(Icons.Default.Email, contentDescription = null, Modifier.size(30.dp))
                }
            )
            Spacer(modifier = Modifier.height(10.dp))
            OutlinedTextField(
                value = password,
                onValueChange = {it -> password = it},
                label = {
                    Text(text = "Enter your password")
                },
                trailingIcon = {
                    IconButton(onClick = {
                        passVisible = !passVisible
                    }) {
                        Icon(Icons.Default.Lock, contentDescription = null, Modifier.size(30.dp))
                    }
                },
                visualTransformation = visible,
                modifier = Modifier.width(350.dp)
            )
            Spacer(modifier = Modifier.height(10.dp))
            OutlinedTextField(
                value = cfPassWord,
                onValueChange = {it -> cfPassWord = it},
                label = {
                    Text(text = "Enter your re-password")
                },
                trailingIcon = {
                    IconButton(onClick = {
                        cfPassVisible = !cfPassVisible
                    }) {
                        Icon(Icons.Default.Lock, contentDescription = null, Modifier.size(30.dp))
                    }
                },
                modifier = Modifier.width(350.dp)
            )
            Spacer(modifier = Modifier.height(10.dp))
            if(uiState.value == registerState.Loading){
                CircularProgressIndicator()
            }else{

                Button(onClick = {
                    coRoutinScope.launch {
                        viewModel.register(email = email, password = password)
                    }
                },
                    modifier = Modifier.width(350.dp),
                    enabled = name.isNotEmpty() &&
                            email.isNotEmpty() &&
                            password.isNotEmpty() &&
                            cfPassWord.isNotEmpty() &&
                            password == cfPassWord

                ) {
                    Text("Sign in",
                        fontSize = 20.sp
                    )
                }
            }
            TextButton(onClick = {
                navController.navigate("login")
            }) {
                Text(text = "Alread have an account?",
                    fontSize = 18.sp
                    )
            }
        }
    }
}

