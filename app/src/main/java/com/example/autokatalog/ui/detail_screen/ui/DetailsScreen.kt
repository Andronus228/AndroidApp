package com.example.autokatalog.ui.detail_screen.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.autokatalog.R
import com.example.autokatalog.ui.detail_screen.data.DetailNavObject
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Preview(showBackground = true)
@Composable
fun DetailsScreen(
    navObject: DetailNavObject = DetailNavObject()
) {
    Column(
        modifier = Modifier
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        val customTextStyle = TextStyle(
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )
        if(navObject.images.isNotEmpty()) ImageSlider(navObject.images)
        else {
            AsyncImage(
                model = R.drawable.logo,
                contentDescription = "",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(170.dp)
                    .clip(RoundedCornerShape(15.dp)),
                contentScale = ContentScale.Crop
            )
        }
        // Вывод полей объекта
        Text(text = "Тип: ${navObject.tipe}", style = customTextStyle)
        Text(text = "Подтип: ${navObject.subTipe}", style = customTextStyle)
        Text(text = "Название: ${navObject.name}", style = customTextStyle)
        Text(text = "Код: ${navObject.code}", style = customTextStyle)
        Text(text = "Тип автомобиля: ${navObject.carTipe}", style = customTextStyle)
        Text(text = "Серия автомобиля: ${navObject.carSeries}", style = customTextStyle)
        Text(text = "Модель автомобиля: ${navObject.carModel}", style = customTextStyle)
        Text(text = "Модификация автомобиля: ${navObject.carModification}", style = customTextStyle)
        Text(text = "Создатель: ${navObject.creator}", style = customTextStyle)
        Text(text = "Количество на складе: ${navObject.numInWarehouse}", style = customTextStyle)
        Text(text = "Стоимость: ${navObject.cost}", style = customTextStyle)

        Spacer(modifier = Modifier.height(16.dp))
        // Слайдер изображений
        /*if (imageUrls.isNotEmpty()) {
            ImageSlider(imageUrls)
        }*/
    }
}

@Composable
fun ImageSlider(images: List<Any>) {
    var currentImageIndex by remember { mutableStateOf(0) }
    var isAnimating by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    Box(
        modifier = Modifier
            .height(200.dp)
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        // Scrollable Row of Cards
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            itemsIndexed(images) { index, image ->
                Card(
                    modifier = Modifier
                        .width(300.dp)
                        .height(200.dp)
                        .clickable {
                            if (index != currentImageIndex && !isAnimating) {
                                isAnimating = true
                                coroutineScope.launch {
                                    val delayMillis = 500L
                                    // Wait for the card to change color before animating
                                    delay(delayMillis / 2)
                                    currentImageIndex = index
                                    delay(delayMillis)
                                    isAnimating = false
                                }
                            }
                        }
                ) {
                    AsyncImage(
                        model = image as String,
                        contentDescription = "Translated description of what the image contains",
                        modifier = Modifier.fillMaxSize() // Изменено здесь
                    )
                }
            }
        }
    }
}
