package com.example.autokatalog.ui.main_screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.autokatalog.R
//import com.example.autokatalog.ui.login.LoginScreen

@Composable
fun DrawerBody(
    onHomeClick: () -> Unit ={},
    onFavClick: () -> Unit ={},
    onProfileClick: () -> Unit = {}
) {
    val pagesList = listOf(
        "Home",
        "Favorites",
        "Navigation",
        "Opinion",
        "User"
    )

    Box (modifier = Modifier.fillMaxSize()){

        Image(
            modifier = Modifier.fillMaxSize(),
            painter = painterResource(id = R.drawable.back4),
            contentDescription = "",
            alpha = 0.2f,
            contentScale = ContentScale.Crop
        )
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer (modifier = Modifier.height(16.dp))
            Text(
                text = "Categories",
                fontSize = 20.sp,
                color = Color.Black,
                fontWeight = FontWeight.Bold
            )
            Spacer (modifier = Modifier.height(10.dp))
            Box(
                modifier = Modifier.fillMaxWidth()
                    .height(1.dp).background(Color(0x32FFFFFF))
            )
            LazyColumn(Modifier.fillMaxSize()) {
                items(pagesList){ item ->
                    Column(
                        Modifier
                            .fillMaxWidth()
                            .clickable {
                                if (pagesList[0]==item)
                                    onHomeClick()
                                if(pagesList[1]==item)
                                    onFavClick()
                                if(pagesList[4]== item)
                                    onProfileClick()
                            }
                    ){
                        Spacer (modifier = Modifier.height(12.dp))
                        Text(
                            text = item,
                            color = Color.White,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier
                                .fillMaxWidth()
                                .wrapContentWidth()
                        )
                        Spacer (modifier = Modifier.height(12.dp))
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(1.dp)
                                .background(Color(0x32FFFFFF))
                        )
                    }
                }
            }
        }
    }
}