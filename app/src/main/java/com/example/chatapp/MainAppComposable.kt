package com.example.chatapp

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.chatapp.feature.auth.signin.signUpScreen
import androidx.compose.ui.Modifier
import com.example.chatapp.feature.auth.signin.signinScreen
import com.example.chatapp.feature.home.HomeScreen
import com.google.firebase.auth.FirebaseAuth


@Composable

fun MainApp(){
    Surface(modifier = Modifier.fillMaxSize()) {
        val navController = rememberNavController()

        val currentUser = FirebaseAuth.getInstance().currentUser

        val start = if(currentUser != null) "login" else "login"
        NavHost(navController = navController, startDestination = "home") {

            composable(route = "login"){
                signinScreen(navController)
            }
            composable(route = "signup"){
                signUpScreen(navController)
            }
            composable(route = "home"){
                HomeScreen(navController)
            }


        }

    }
    
}