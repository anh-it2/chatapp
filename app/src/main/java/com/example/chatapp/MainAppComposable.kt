package com.example.chatapp

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.chatapp.feature.auth.signin.signUpScreen
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.example.chatapp.feature.auth.changepassword.changePassWordScreen
import com.example.chatapp.feature.auth.signin.signinScreen
import com.example.chatapp.feature.chat.ChatScreen
import com.example.chatapp.feature.home.HomeScreen
import com.google.firebase.auth.FirebaseAuth


@Composable

fun MainApp(){
//    Surface(modifier = Modifier.fillMaxSize()) {
//        val navController = rememberNavController()
//
//        val currentUser = FirebaseAuth.getInstance().currentUser
//
//        val start = if(currentUser != null) "login" else "login"
//        NavHost(navController = navController, startDestination = "login") {
//
//            composable(route = "login"){
//                signinScreen(navController)
//            }
//            composable(route = "signup"){
//                signUpScreen(navController)
//            }
//            composable(route = "home"){
//                HomeScreen(navController)
//            }
//
//
//        }
//
//    }
    Surface(modifier = Modifier.fillMaxSize()) {
        val navController = rememberNavController()
        val currentUser = FirebaseAuth.getInstance().currentUser
        val start = if (currentUser != null) "home" else "login"
        NavHost(navController = navController, startDestination = start) {
            composable(route = "login") {
                signinScreen(navController)
            }
            composable(route = "register") {
                signUpScreen(navController)
            }
            composable(route = "home") {
                HomeScreen(navController)
            }
            composable(route = "chat/{channelId}/{channelName}", arguments = listOf(
                navArgument("channelId") {
                    type = NavType.StringType
                },
                navArgument("channelName"){
                    type = NavType.StringType
                }
            )
            ) {
                val channelId = it.arguments?.getString("channelId") ?: ""
                val channelName = it.arguments?.getString("channelName") ?: ""
                ChatScreen(navController, channelId, channelName)
            }
            composable(route = "changePassWord"){
                changePassWordScreen(navController)
            }

        }
    }
}