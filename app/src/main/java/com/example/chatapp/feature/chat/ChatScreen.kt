    package com.example.chatapp.feature.chat

    import android.content.pm.PackageManager
    import android.net.Uri
    import android.os.Environment
    import androidx.activity.compose.rememberLauncherForActivityResult
    import androidx.activity.result.contract.ActivityResultContracts
    import androidx.compose.foundation.Image
    import androidx.compose.foundation.background
    import androidx.compose.foundation.layout.Arrangement
    import androidx.compose.foundation.layout.Box
    import androidx.compose.foundation.layout.Column
    import androidx.compose.foundation.layout.Row
    import androidx.compose.foundation.layout.Spacer
    import androidx.compose.foundation.layout.fillMaxSize
    import androidx.compose.foundation.layout.fillMaxWidth
    import androidx.compose.foundation.layout.height
    import androidx.compose.foundation.layout.padding
    import androidx.compose.foundation.layout.size
    import androidx.compose.foundation.lazy.LazyColumn
    import androidx.compose.foundation.lazy.items
    import androidx.compose.foundation.lazy.rememberLazyListState
    import androidx.compose.foundation.shape.CircleShape
    import androidx.compose.foundation.shape.RoundedCornerShape
    import androidx.compose.foundation.text.KeyboardActions
    import androidx.compose.foundation.text.KeyboardOptions
    import androidx.compose.material.icons.Icons
    import androidx.compose.material.icons.filled.Add
    import androidx.compose.material.icons.filled.ArrowBack
    import androidx.compose.material.icons.filled.Person
    import androidx.compose.material.icons.filled.Send
    import androidx.compose.material3.AlertDialog
    import androidx.compose.material3.ExperimentalMaterial3Api
    import androidx.compose.material3.Icon
    import androidx.compose.material3.IconButton
    import androidx.compose.material3.Scaffold
    import androidx.compose.material3.Text
    import androidx.compose.material3.TextButton
    import androidx.compose.material3.TextField
    import androidx.compose.material3.TextFieldDefaults
    import androidx.compose.material3.TopAppBar
    import androidx.compose.material3.TopAppBarDefaults
    import androidx.compose.runtime.Composable
    import androidx.compose.runtime.LaunchedEffect
    import androidx.compose.runtime.collectAsState
    import androidx.compose.runtime.getValue
    import androidx.compose.runtime.mutableStateOf
    import androidx.compose.runtime.remember
    import androidx.compose.runtime.setValue
    import androidx.compose.ui.Alignment
    import androidx.compose.ui.Modifier
    import androidx.compose.ui.draw.clip
    import androidx.compose.ui.graphics.Color
    import androidx.compose.ui.layout.ContentScale
    import androidx.compose.ui.platform.LocalSoftwareKeyboardController
    import androidx.compose.ui.res.painterResource
    import androidx.compose.ui.text.input.ImeAction
    import androidx.compose.ui.unit.dp
    import androidx.core.content.FileProvider
    import androidx.navigation.NavController
    import androidx.hilt.navigation.compose.hiltViewModel
    import coil.compose.AsyncImage
    //    x
    import com.example.chatapp.R
    import com.example.chatapp.model.Message
    import com.example.chatapp.ui.theme.DarkGrey
    import com.example.chatapp.ui.theme.Purple
    import com.google.firebase.Firebase
    import com.google.firebase.auth.auth
    import java.io.File
    import java.util.Date
    import java.util.Locale


    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun ChatScreen(navController: NavController, channelId: String, channelName: String){
        Scaffold(
            containerColor = Color.Black,
            topBar = {
                TopAppBar(
                    title = { Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center){
                        Text(text = channelName, color = Color.White)
                    } },
                    navigationIcon = {
                        IconButton(onClick = {
                            navController.popBackStack()
                        }) {
                            Icon(Icons.Default.ArrowBack, contentDescription = null, tint = Color.White)
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.DarkGray
                    )
                )
            }
        ) {
            val viewModel : ChatViewModel = hiltViewModel()
            var chooserDialog by remember { mutableStateOf(false) }
//            val cameraImageUri = remember { mutableStateOf<Uri?>(null) }
            var cameraImageUri by remember { mutableStateOf<Uri?>(null) }
            fun createImageUri(): Uri{
                val timestamp = android.icu.text.SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
                val storageDir = navController.context.getExternalFilesDirs(Environment.DIRECTORY_PICTURES).first()
                return FileProvider.getUriForFile(
                    navController.context,
                    "${navController.context.packageName}.provider",
                    File.createTempFile("JPEG_${timestamp}_",".jpg",storageDir).apply {
                        cameraImageUri = Uri.fromFile(this)
                    }
                )
            }
            val cameraImageLaucher = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.TakePicture()
            ) { success ->
                if(success){
                    cameraImageUri?.let {
                        viewModel.sendImageMessage(it, channelId)
                    }
                }
            }
            val imageLauncher = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.GetContent()
            ) { uri: Uri? ->
                uri?.let {
                    viewModel.sendImageMessage(it, channelId)
                }

            }
            val permissionLauncher = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.RequestPermission()
            ) { isGranted ->
                if(isGranted){
                    cameraImageLaucher.launch(createImageUri())
                }
            }

            Column(modifier = Modifier
                .fillMaxSize()
                .padding(it)) {
                LaunchedEffect(key1 = true) {
                    viewModel.listenForMessage(channelId)
                }
                val message = viewModel.messages.collectAsState()
                ChatMessage(
                    message = message.value,
                    onSendMessaage = {
                            message ->
                        viewModel.sendMessage(channelId, message)
                    },
                    onImageClicked = {
                        chooserDialog = true
                    }
                )
//                fun createImageUri():Uri {
//                    val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
//                    val storageDir = navController.context.getExternalFilesDirs(Environment.DIRECTORY_PICTURES).first()
//                    return FileProvider.getUriForFile(
//                        navController.context,
//                        "${navController.context.packageName}.provider",
//                        File.createTempFile("JPEG_${timestamp}_",".jpg",storageDir).apply {
//                            cameraImageUri.value = Uri.fromFile(this)
//                        }
//                    )
//                }
                if(chooserDialog){
                    ContentSelectionDialog(onCameraSelected = {
                        chooserDialog = false
                        if(navController.context.checkSelfPermission(android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED){
                            cameraImageLaucher.launch(createImageUri())
                        } else {
                            //request permission
                            permissionLauncher.launch(android.Manifest.permission.CAMERA)
                        }
                    }, onGallerySelected = {
                        chooserDialog = false
                        imageLauncher.launch("image/*")
                    })
                }
            }
            }
        }

    @Composable
    fun ContentSelectionDialog(onCameraSelected: () -> Unit, onGallerySelected: () -> Unit){
        AlertDialog(onDismissRequest = {},
            confirmButton = {
                TextButton(onClick = onCameraSelected) {
                    Text(text = "Camera", color = Color.Black)
                }
            },
            dismissButton = {
                TextButton(onClick = onGallerySelected) {
                    Text(text = "Gallery", color = Color.Black)
                }
            },
            title = { Text(text = "Select your sources") },
            text = { Text(text = "Would you like to pick an image from the gallery or use the camera") })
    }
    @Composable
    fun ChatMessage(
        message: List<Message>,
        onSendMessaage: (String) -> Unit,
        onImageClicked: () -> Unit
    ){
        var msg by remember { mutableStateOf("") }
        val hideKeyboardController = LocalSoftwareKeyboardController.current
        val listState = rememberLazyListState()
        LaunchedEffect(message.size) {
            listState.animateScrollToItem(message.size)
        }
        Box(
            modifier = Modifier.fillMaxSize()
        ){
            Column(
                modifier = Modifier.fillMaxSize()
            ) {

                LazyColumn (
                    modifier = Modifier.padding(bottom = 60.dp).weight(1f),
                    state = listState
                ){
                    items(message){ message ->
                        ChatBubble(message = message)
                    }
                }
            }
            Row(modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(0.dp)
                .background(color = androidx.compose.ui.graphics.Color.DarkGray)
                .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
                ){
                IconButton(
                    onClick = {
                        onImageClicked()
                    },
                ) {
                    Icon(
                        Icons.Default.Add, contentDescription = null,
                        modifier = Modifier.size(35.dp),
                        tint = Color.White
                        )
                }
                TextField(
                    value = msg,
                    onValueChange = {
                        msg = it
                    },
                    modifier = Modifier.weight(1f),
                    placeholder = {
                        Text(text = "Type your message")
                    },
                    keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            hideKeyboardController?.hide()
                        }
                    ),
                    colors = TextFieldDefaults.colors().copy(
                        focusedContainerColor = DarkGrey,
                        unfocusedContainerColor = DarkGrey,
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedPlaceholderColor = Color.White,
                        unfocusedPlaceholderColor = Color.White
                    )
                )

                IconButton(
                    onClick = {
                        if(msg != ""){

                            onSendMessaage(msg)
                            msg = ""
                        }
                    }
                ) {
                    Icon(Icons.Filled.Send, contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(35.dp)
                        )
                }
            }
        }
    }

    @Composable

    fun ChatBubble(message: Message){
        val isCurrentUser = message.senderId == Firebase.auth.currentUser?.uid
        val bubbleColor = if(isCurrentUser){
            Purple
        }else{
            Color.DarkGray
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp, horizontal = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),

        ){
            val aligment = if(isCurrentUser) Arrangement.End else Arrangement.Start
                Row (
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = aligment
                    )
                {
                    if(!isCurrentUser){
                        Icon(Icons.Default.Person, contentDescription = null, tint = Color.White)
                        Column (
                            modifier = Modifier
                                .padding(10.dp)
                                .background(color = bubbleColor, shape = RoundedCornerShape(8.dp))
                                .padding(10.dp)
                        ){
                            if (message.imageUrl != null){

                                AsyncImage(
                                    model = message.imageUrl,
                                    contentDescription = null,
                                    modifier = Modifier.size(200.dp),
                                    contentScale = ContentScale.Crop
                                )

                            } else{
                                Text(
                                    message.message.trim(),
                                    color = Color.White,
                                )
                            }
                        }
                    }else{
                        Column(
                            modifier = Modifier
                                .padding(10.dp)
                                .background(color = bubbleColor, shape = RoundedCornerShape(8.dp))
                                .padding(10.dp),
                        ){
                            if (message.imageUrl != null){
                                AsyncImage(
                                    model = message.imageUrl,
                                    contentDescription = null,
                                    modifier = Modifier.size(200.dp),
                                    contentScale = ContentScale.Crop
                                )
                                Spacer(modifier = Modifier.height(10.dp))
                            } else{
                                Text(
                                    message.message.trim(),
                                    color = Color.White,

                                    )
                            }
                        }
                        Image(painter = painterResource(id = R.drawable.friend),
                            contentDescription = null,
                            modifier = Modifier
                                .size(width = 40.dp, height = 50.dp)
                                .clip(CircleShape))
                    }


                }
            }

    }


//@Composable
//
//fun ChatScreen(navController: NavController, channelId: String){
//    Scaffold {
//        Column (
//            modifier = Modifier.fillMaxSize()
//                .padding(it)
//        ){
//
//            val viewModel : ChatViewModel = hiltViewModel()
//            val messages = viewModel.messages.collectAsState()
//            LaunchedEffect(key1 = true) {
//                viewModel.listenForMessage(channelId)
//            }
//            ChatMessage(
//                messages = messages.value,
//                {message -> viewModel.sendMessage(channelId, message)}
//            )
//        }
//    }
//}
//
//
//
//    @Composable
//    fun ChatMessage(
//        messages: List<Message>,
//        onMessageSend: (String) -> Unit
//    ){
//        Box(modifier = Modifier.fillMaxSize()){
//            LazyColumn {
//                items(messages){ message ->
//                    BubbleMessage(message)
//                }
//            }
//            var msg by remember { mutableStateOf("") }
//            val keyboardHideController = LocalSoftwareKeyboardController.current
//            Row(
//                modifier = Modifier.fillMaxWidth()
//                    .align(Alignment.BottomCenter),
//                verticalAlignment = Alignment.CenterVertically,
//                horizontalArrangement = Arrangement.Center
//            ){
//                TextField(
//                    value = msg,
//                    onValueChange = {msg = it},
//                    placeholder = { Text(text = "Type your message") },
//                    keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
//                    keyboardActions = KeyboardActions(
//                        onDone = {
//                            keyboardHideController?.hide()
//                        }
//                    )
//                )
//                IconButton(
//                    onClick = {
//                        if(msg != ""){
//                            onMessageSend(msg)
//                            msg =""
//                        }
//
//                    }
//                ) {
//                    Icon(imageVector = Icons.AutoMirrored.Filled.Send, contentDescription = "send")
//                }
//            }
//        }
//    }
//
//@Composable
//fun BubbleMessage(
//    message: Message
//){
//    val isCurrentUser = message.senderId == Firebase.auth.currentUser?.uid
//    val align = if(isCurrentUser) Alignment.CenterEnd else Alignment.CenterStart
//    val bubbleColor = if(isCurrentUser) Color.Blue else Color.Green
//    Box(
//        contentAlignment = align,
//        modifier = Modifier.width(250.dp)
//    ){
//        Text(
//            message.message,
//            color = Color.Black,
//            modifier = Modifier.padding(8.dp).background(color = bubbleColor, shape = RoundedCornerShape(8.dp))
//                .padding(8.dp)
//        )
//    }
//}