package com.example.project

sealed class Screens (val Screen : String){
    data object ChatFragment: Screens("ChatFragment")
    data object SettingsFragment: Screens("SettingsFragment")
    data object MapFragment: Screens("MapFragment")
    data object DeviceFragment: Screens("DeviceFragment")
    data object ConnectionFragment: Screens("ConnectionFragment")
}