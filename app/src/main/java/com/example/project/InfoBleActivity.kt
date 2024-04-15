package com.example.project

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button

class InfoBleActivity : AppCompatActivity() {
    private lateinit var bAdapter: BluetoothAdapter
    private lateinit var bluetoothController: BleController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_info_ble)
        initBtAdapter()
        val mac = intent.getStringExtra("macAddress")
        bluetoothController = BleController(bAdapter)
        val btnConnect = findViewById<Button>(R.id.btnConnect)
        btnConnect.setOnClickListener {
            bluetoothController.connect(mac ?: "")
            Log.d("MyLog", "${mac}")
        }
    }
    private fun initBtAdapter(){
        val bManager = getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        bAdapter = bManager.adapter
    }
}