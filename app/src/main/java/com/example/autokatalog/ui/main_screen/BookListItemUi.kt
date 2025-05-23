package com.example.autokatalog.ui.main_screen

import android.graphics.drawable.Icon
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.autokatalog.R
import com.example.autokatalog.autokatalogapp.data.Autopart
import com.example.autokatalog.ui.detail_screen.ui.ImageSlider

@Composable
fun BookListItemUi(autopart: Autopart,
                    onFavClick:() -> Unit ={},
                    onAutopartClick: (Autopart) ->Unit={}) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clickable {
                onAutopartClick(autopart)
            }
    ){
        if(autopart.images.isEmpty()){
            AsyncImage(
                model = R.drawable.logo,
                contentDescription = "",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(170.dp)
                    .clip(RoundedCornerShape(15.dp)),
                contentScale = ContentScale.Crop
            )
        } else ImageSlider(autopart.images)
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text="Name: " + autopart.name,
            color = Color.Gray,
            fontSize = 16.sp
        )
        Spacer(modifier = Modifier.height(5.dp))
        Text(
            text="Code: " + autopart.code,
            color = Color.Gray,
            fontSize = 16.sp
        )
        Spacer(modifier = Modifier.height(5.dp))
        Text(
            text="Creator: " + autopart.creator,
            color = Color.Gray,
            fontSize = 16.sp
        )
        Spacer(modifier = Modifier.height(5.dp))
        Text(
            text="In Warehouse: "  + autopart.numInWarehouse,
            color = Color.Gray,
            fontSize = 16.sp
        )
        Spacer(modifier = Modifier.height(5.dp))
        Text(
            text="Cost: " + autopart.cost,
            color = Color.Gray,
            fontSize = 16.sp
        )

        IconButton(onClick={
            onFavClick()
        }){
            Icon(
                if (autopart.isFavorite){
                    Icons.Default.Favorite
                } else{
                    Icons.Default.FavoriteBorder
                },

                contentDescription = ""
            )
        }


    }
}