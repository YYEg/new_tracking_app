package com.example.project.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.MailOutline
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.project.Screens
import com.example.project.ui.fragments.ChatFragment
import com.example.project.ui.fragments.ConnectionFragment
import com.example.project.ui.fragments.DeviceFragment
import com.example.project.ui.fragments.MapFragment
import com.example.project.ui.fragments.SettingsFragment
import com.example.project.ui.theme.GreenMenu

@Composable
fun BottomAppBar(){
    val navigationController = rememberNavController()
    val context = LocalContext.current.applicationContext
    val selected = remember {
        mutableStateOf(Icons.Default.MailOutline)
    }

    Scaffold(
        bottomBar = {
            androidx.compose.material3.BottomAppBar(
                containerColor = GreenMenu
            ) {
                IconButton(
                    onClick = { selected.value = Icons.Default.MailOutline
                        navigationController.navigate(Screens.ChatFragment.Screen){
                            popUpTo(0)
                        }},
                    modifier = Modifier.weight(1f)) {
                    Icon(
                        Icons.Default.MailOutline, contentDescription = null, modifier =  Modifier.size(26.dp),
                        tint = if(selected.value == Icons.Default.MailOutline) Color.White else Color.DarkGray)
                }
                IconButton(
                    onClick = { selected.value = Icons.Default.Send
                        navigationController.navigate(Screens.ConnectionFragment.Screen){
                            popUpTo(0)
                        }},
                    modifier = Modifier.weight(1f)) {
                    Icon(
                        Icons.Default.Send, contentDescription = null, modifier =  Modifier.size(26.dp),
                        tint = if(selected.value == Icons.Default.Send) Color.White else Color.DarkGray)
                }
                IconButton(
                    onClick = { selected.value = Icons.Default.Person
                        navigationController.navigate(Screens.DeviceFragment.Screen){
                            popUpTo(0)
                        }},
                    modifier = Modifier.weight(1f)) {
                    Icon(
                        Icons.Default.Person, contentDescription = null, modifier =  Modifier.size(26.dp),
                        tint = if(selected.value == Icons.Default.Person) Color.White else Color.DarkGray)
                }
                IconButton(
                    onClick = { selected.value = Icons.Default.LocationOn
                        navigationController.navigate(Screens.MapFragment.Screen){
                            popUpTo(0)
                        }},
                    modifier = Modifier.weight(1f)) {
                    Icon(
                        Icons.Default.LocationOn, contentDescription = null, modifier =  Modifier.size(26.dp),
                        tint = if(selected.value == Icons.Default.LocationOn) Color.White else Color.DarkGray)
                }
                IconButton(
                    onClick = { selected.value = Icons.Default.Settings
                        navigationController.navigate(Screens.SettingsFragment.Screen){
                            popUpTo(0)
                        }},
                    modifier = Modifier.weight(1f)) {
                    Icon(
                        Icons.Default.Settings, contentDescription = null, modifier =  Modifier.size(26.dp),
                        tint = if(selected.value == Icons.Default.Settings) Color.White else Color.DarkGray)
                }
            }
        }
    ) {paddingValues ->
        NavHost(navController = navigationController,
            startDestination = Screens.ChatFragment.Screen,
            modifier = Modifier.padding(paddingValues)){
            composable(Screens.ChatFragment.Screen){ ChatFragment() }
            composable(Screens.ConnectionFragment.Screen){ ConnectionFragment() }
            composable(Screens.DeviceFragment.Screen){ DeviceFragment() }
            composable(Screens.MapFragment.Screen){ MapFragment() }
            composable(Screens.SettingsFragment.Screen){ SettingsFragment() }
        }

    }
}