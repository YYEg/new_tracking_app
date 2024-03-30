package com.example.project

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.example.project.ui.theme.ProjectTheme
import android.content.Intent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.lazy.LazyColumn
import com.example.project.ui.ListItems.DeviceListItem

class BluetoothActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ProjectTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color.Green
                ) {
                    Box(modifier = Modifier.fillMaxSize()){
                        LazyColumn(modifier = Modifier.fillMaxSize()){
                            items (3){
                                DeviceListItem()
                            }
                        }
                    }
                }
            }
        }
    }
}