package com.example.project.ui.fragments

import android.content.Intent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.project.ui.BluetoothActivity
import com.example.project.ui.theme.GreenMenu


@Composable
fun SettingsFragment(){
    val context = LocalContext.current

    Box(modifier = Modifier.fillMaxSize()){
        Column(modifier = Modifier
            .fillMaxSize()
            .align(Alignment.Center),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally) {
            Button(onClick = {
                val intent = Intent(context, BluetoothActivity::class.java)
                context.startActivity(intent)
            }, colors = ButtonDefaults.buttonColors(GreenMenu), modifier = Modifier
                .fillMaxWidth()
                .padding(start = 10.dp, end = 10.dp)) {
                Text(text = "BLE connect")
            }
        }
    }
}