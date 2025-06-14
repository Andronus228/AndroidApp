package com.example.autokatalog.ui.login

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun RoundedCornerTextField(
    text:String,
    label:String,
    colour: Color,
    onValueChange: (String)->Unit
) {
   TextField(value = text, onValueChange = {
       onValueChange(it)
   },
       shape = RoundedCornerShape(25.dp),
       colors = TextFieldDefaults.colors(
           unfocusedContainerColor = Color.White,
           focusedContainerColor = Color.White,
           unfocusedIndicatorColor = Color.Transparent,
           focusedIndicatorColor = Color.Transparent
       ),
       modifier = Modifier.fillMaxWidth().border(1.dp,Color(0x9C008BF5), RoundedCornerShape(25.dp)),
       label = {
           Text(text = label, color = colour)
       },
       singleLine = true
       )
}