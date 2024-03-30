package com.example.project.ui.ListItems

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.project.R

@Preview(showBackground = true)
@Composable
fun DeviceListItem() {
    Card (
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 10.dp, bottom = 10.dp, start = 10.dp, end = 10.dp),
        colors = CardDefaults.cardColors(Color.Gray),
        elevation = CardDefaults.cardElevation(0.dp),
        shape = RoundedCornerShape(10.dp)
    ) {
        Row(modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically){
            Column (modifier = Modifier.padding(start = 8.dp, top=5.dp, bottom=5.dp), horizontalAlignment = Alignment.CenterHorizontally){
                Button(colors = ButtonDefaults.buttonColors(Color.Cyan), onClick = {}){
                    Text("0", color = Color.Black)
                }
                Text(text = "15 m")
            }
            Column (modifier = Modifier.padding(start = 8.dp, top=5.dp, bottom=5.dp), horizontalAlignment = Alignment.CenterHorizontally){
                Text(text = "Meshtastic 78d8", style = TextStyle(fontSize = 15.sp))
                Text(text = "52.26186 104.26546!", color = Color.Cyan, style = TextStyle(textDecoration = TextDecoration.Underline))
            }
            Column (modifier = Modifier.padding(top=5.dp, bottom=5.dp),
                verticalArrangement = Arrangement.Center){
                Row {
                    Image(painter = painterResource(id = R.drawable.ic_battery), contentDescription = "imBattery", Modifier.size(16.dp).padding(end = 5.dp))
                    Text(text = "89%")
                }
                Row {
                    Image(painter = painterResource(id = R.drawable.ic_connection), contentDescription = "imConnection", Modifier.size(16.dp).padding(end = 5.dp))
                    Text(text = "3d")
                }
            }
        }

    }
}