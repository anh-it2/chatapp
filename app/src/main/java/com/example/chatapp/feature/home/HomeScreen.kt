package com.example.chatapp.feature.home




import android.content.ClipData.Item
import android.graphics.Paint.Style
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.chatapp.ui.theme.DarkGrey
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable

fun HomeScreen(navController: NavController){
    val viewModel = hiltViewModel<HomeViewModel>()
    val channels by viewModel.channels.collectAsState()
    val currentUser = FirebaseAuth.getInstance().currentUser
//    LaunchedEffect(key1 = currentUser) {
//        when(currentUser){
//            null ->{
//                navController.navigate("login")
//            }
//            else ->{}
//        }
//    }
    val addChannel = remember {
        mutableStateOf(false)
    }
    val sheetState = rememberModalBottomSheetState()
    var drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent =  {
            Column (
                modifier = Modifier.width(300.dp).fillMaxHeight()
                    .clip(RoundedCornerShape(topEnd = 20.dp, bottomEnd = 20.dp))
                    .background(Color.White).padding(20.dp, 40.dp)
            ){
                Row {
                    Icon(Icons.Default.Person, contentDescription = null)
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        currentUser?.email?: "",
                        style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold)
                        )
                }
                Divider(
                    color = Color.Black,
                    thickness = 1.dp
                )
                Spacer(Modifier.height(20.dp))
                Row (
                    modifier = Modifier.fillMaxWidth().height(50.dp).clickable {

                        FirebaseAuth.getInstance().signOut();
                        navController.navigate("login")
                    },
                    verticalAlignment = Alignment.CenterVertically

                ){
                    Icon(Icons.Filled.Settings, contentDescription = null)
                    Spacer(Modifier.width(20.dp))
                    Text(
                        "Log out",
                        style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold)
                    )
                }
            }
        }
    ){
        Scaffold (
            floatingActionButton = {
                Box(modifier = Modifier
                    .padding(16.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color.Blue)
                    .clickable {
                        addChannel.value = true
                    }) {
                    Text(
                        text = "Add Channel", modifier = Modifier.padding(16.dp), color = Color.White
                    )
                }
            },
            containerColor = Color.Black
        ){

            Column (modifier = Modifier
                .padding(it)
                .fillMaxSize()
            ){
                Box(
                    modifier = Modifier
                        .padding(it)
                        .fillMaxSize()
                ){
                    LazyColumn {
                        item {
                            TextButton(onClick = {
                                scope.launch {
                                    drawerState.open()
                                }
                            }) {
                                Text(
                                    text = "Message",
                                    style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold),
                                    color = Color.White,
                                    modifier = Modifier.padding(16.dp)
                                )
                            }
                        }

                        item {
                            TextField(value = "",
                                onValueChange = {},
                                placeholder = { Text(text = "Search...") },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp, vertical = 8.dp)
                                    .clip(
                                        RoundedCornerShape(40.dp)
                                    ),
                                textStyle = TextStyle(color = Color.LightGray),
                                colors = TextFieldDefaults.colors().copy(
                                    focusedContainerColor = DarkGrey,
                                    unfocusedContainerColor = DarkGrey,
                                    focusedTextColor = Color.Gray,
                                    unfocusedTextColor = Color.Gray,
                                    focusedPlaceholderColor = Color.Gray,
                                    unfocusedPlaceholderColor = Color.Gray,
                                    focusedIndicatorColor = Color.Gray
                                ),
                                trailingIcon = {
                                    Icon(
                                        imageVector = Icons.Filled.Search, contentDescription = null
                                    )
                                })
                            Spacer(modifier = Modifier.height(15.dp))
                        }

                        items(channels){channel ->
                            Column {
                                ChannelItem(channelName = channel.name) {
                                    navController.navigate("chat/${channel.id}/${channel.name}")
                                }
                            }
                        }
                    }
                }

            }

        }
    }
    if (addChannel.value) {
        ModalBottomSheet(onDismissRequest = { addChannel.value = false }, sheetState = sheetState) {
            AddChannelDialog {
                viewModel.addChannel(it)
                addChannel.value = false
            }
        }
    }
}
@Composable
fun ChannelItem(
    channelName: String,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 2.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(DarkGrey)
    ) {
        Row(
            modifier = Modifier
                .align(Alignment.CenterStart)
                .clickable {
                    onClick()
                },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .padding(8.dp)
                    .size(70.dp)
                    .clip(CircleShape)
                    .background(Color.Yellow.copy(alpha = 0.3f))

            ) {
                Text(
                    text = channelName[0].uppercase(),
                    color = Color.White,
                    style = TextStyle(fontSize = 35.sp),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.align(Alignment.Center)
                )
            }


            Text(text = channelName, modifier = Modifier.padding(8.dp), color = Color.White, style = TextStyle(fontSize = 20.sp))
        }
    }
}
            @Composable
            fun AddChannelDialog(onAddChannel: (String) -> Unit) {
                val channelName = remember {
                    mutableStateOf("")
                }
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = androidx.compose.foundation.layout.Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = "Add Channel")
                    Spacer(modifier = Modifier.padding(8.dp))
                    TextField(value = channelName.value, onValueChange = {
                        channelName.value = it
                    }, label = { Text(text = "Channel Name") }, singleLine = true)
                    Spacer(modifier = Modifier.padding(8.dp))
                    Button(
                        onClick = { onAddChannel(channelName.value) },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(text = "Add")
                    }
                }
            }



//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun HomeScreen(navController: NavController){
//    var addChannel by remember { mutableStateOf(false) }
//    val viewModel : ChannelViewModel = hiltViewModel()
//    val channels = viewModel.channels.collectAsState()
//    var sheetState = rememberModalBottomSheetState()
//    var scope = rememberCoroutineScope()
//    Scaffold (
//        floatingActionButton = {
//            Box(
//                modifier = Modifier.padding(16.dp)
//                    .clip(RoundedCornerShape(16.dp))
//                    .clickable {
//                        addChannel = true
//                    }
//                    .background(Color.Blue.copy(0.8f))
//                    .padding(16.dp)
//            ){
//                Text("Add Channel",
//                    color = Color.White,
//                    fontSize = 18.sp
//                    )
//            }
//        }
//    ){
//        Column (
//            modifier = Modifier.fillMaxSize()
//                .padding(it)
//        ){
//            Box(
//                modifier = Modifier.fillMaxSize()
//                    .padding(it)
//            ){
//                LazyColumn {
//                    items(channels.value){ channel ->
//                        Text(
//                            channel.name,
//                            modifier = Modifier.fillMaxWidth()
//                                .padding(16.dp)
//                                .clip(RoundedCornerShape(16.dp))
//                                .clickable {
//                                    navController.navigate("chat/${channel.id}")
//                                }
//                                .background(Color.Red.copy(0.3f))
//                                .padding(16.dp),
//                            fontSize = 20.sp
//                        )
//
//                    }
//                }
//            }
//        }
//    }
//    if (addChannel){
//        ModalBottomSheet(onDismissRequest = {addChannel = false}, sheetState = sheetState) {
//            addChannelDialog(
//                onAddChannel = { channelName ->
//                    viewModel.addChannel(channelName)
//                    scope.launch {
//                        sheetState.hide()
//                    }
//                    addChannel = false
//                }
//            )
//        }
//    }
//}
//
//@Composable
//fun addChannelDialog(onAddChannel: (String) -> Unit){
//    var channelName by remember { mutableStateOf("") }
//    Column (
//        modifier = Modifier.fillMaxWidth(),
//        horizontalAlignment = Alignment.CenterHorizontally
//    ){
//        Text("Add Channel",
//            fontSize = 20.sp
//            )
//        Spacer(modifier = Modifier.height(10.dp))
//        TextField(
//            value = channelName,
//            onValueChange = {channelName = it}
//        )
//        Spacer(modifier = Modifier.height(10.dp))
//        Button(onClick = {
//            onAddChannel(channelName)
//        }) {
//            Text("Done",
//                fontSize = 18.sp
//                )
//        }
//    }
//}
