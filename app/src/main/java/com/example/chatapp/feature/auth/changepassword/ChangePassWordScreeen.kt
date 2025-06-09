package com.example.chatapp.feature.auth.changepassword

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
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
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.chatapp.R
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


@Composable
fun changePassWordScreen(navController: NavController){
    var pass by remember { mutableStateOf("") }
    var newpass by remember { mutableStateOf("") }
    var passVisibile by remember { mutableStateOf(true) }
    var visible = if(passVisibile) PasswordVisualTransformation() else VisualTransformation.None
    var newPassWordVisible by remember { mutableStateOf(true) }
    var newVisible = if(newPassWordVisible) PasswordVisualTransformation() else VisualTransformation.None
    var cfPassWord by remember { mutableStateOf("") }
    var cfPassWordVisible by remember { mutableStateOf(true) }
    var cfVisible = if (cfPassWordVisible) PasswordVisualTransformation() else VisualTransformation.None
    val context = LocalContext.current
    var email by remember { mutableStateOf("") }
    val viewModel : ChangePassWordViewModel = hiltViewModel()
    val changeState = viewModel.state.collectAsState()
    val scope = rememberCoroutineScope()
    LaunchedEffect(key1 = changeState.value) {
        when(changeState.value){
            is changePassState.Success ->{
                FirebaseAuth.getInstance().signOut()
                navController.navigate("login")
            }
            is changePassState.Error ->{
                val message = (changeState.value as changePassState.Error).message
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
            }
            else ->{}
        }
    }
    Column (
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ){
        Image(
            painter = painterResource(R.drawable.logo),
            contentDescription = null,
            modifier = Modifier.size(150.dp)
        )
        OutlinedTextField(
            value = email,
            onValueChange ={ email = it },
            label = {
                Text(text = "Enter your email")
            },
            modifier = Modifier.width(350.dp),
            singleLine = true,
            trailingIcon = {
                IconButton(onClick = {
                    passVisibile = !passVisibile
                }) {
                    Icon(Icons.Default.Email, contentDescription = null, Modifier.size(30.dp))
                }
            },

        )

        OutlinedTextField(
                value = pass,
                onValueChange ={ pass = it },
                label = {
                    Text(text = "Enter your old pass word")
                },
                modifier = Modifier.width(350.dp),
                singleLine = true,
                trailingIcon = {
                    IconButton(onClick = {
                        passVisibile = !passVisibile
                    }) {
                         Icon(Icons.Default.Lock, contentDescription = null, Modifier.size(30.dp))
                    }
                },
                visualTransformation = visible
                )
        OutlinedTextField(
            value = newpass,
            onValueChange = {newpass = it},
            label = {
                Text(text = "Enter your new pass word")
            },
            modifier = Modifier.width(350.dp),
            singleLine = true,
            trailingIcon = {
                IconButton(onClick = {
                    newPassWordVisible = !newPassWordVisible
                }) {
                    Icon(Icons.Default.Lock, contentDescription = null,Modifier.size(30.dp))
                }
            },
            visualTransformation = newVisible
        )
        OutlinedTextField(
            value = cfPassWord,
            onValueChange = {cfPassWord = it},
            modifier = Modifier.width(350.dp),
            label = {
                Text(text = "Enter your new pass word again")
            },
            singleLine = true,
            trailingIcon = {
                IconButton(onClick = {
                    cfPassWordVisible = !cfPassWordVisible
                }) {
                    Icon(Icons.Default.Lock, contentDescription = null, Modifier.size(30.dp))
                }
            },
            visualTransformation = cfVisible
        )
        Spacer(Modifier.height(10.dp))
        if(changeState.value == changePassState.Loading){
            CircularProgressIndicator()
        }else {
            Button(
                onClick = {
                    scope.launch {
                        viewModel.changePassWord(newpass,pass, email)
                    }
                },
                modifier = Modifier.width(350.dp),
                enabled = email.isNotEmpty() && pass.isNotEmpty() && newpass.isNotEmpty() && cfPassWord.isNotEmpty() && newpass == cfPassWord
            ) {
                Text(
                    "Confirm",
                    fontSize = 20.sp
                )
            }
        }
        TextButton(onClick = {
            navController.navigate("login")
        }) {
            Text("Continue to sign in", fontSize = 20.sp)
        }
    }
}