package com.example.project.ui

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.Color.GREEN
import android.graphics.Color.RED
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.ui.graphics.Color
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.project.BleConstants
import com.example.project.ItemAdapter
import com.example.project.ListItem
import com.example.project.R
import com.example.project.changeButtonCollor
import com.example.project.ui.theme.GreenMenu
import com.google.android.material.snackbar.Snackbar

class BluetoothActivity : AppCompatActivity(), ItemAdapter.Listener {
    //shared pref переменная
    private var preferences: SharedPreferences? = null
    private lateinit var itemAdapter: ItemAdapter
    //переменная для адаптера на блютуз
    private var bAdapter : BluetoothAdapter? = null
    //переменная блютуз лаунчера
    private lateinit var btLauncher : ActivityResultLauncher<Intent>
    //Спрашиватель у пользователя разрешгений
    private lateinit var permLauncher: ActivityResultLauncher<Array<String>>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        preferences = getSharedPreferences(BleConstants.PREFERENCES, Context.MODE_PRIVATE)

        setContentView(R.layout.activity_bluetooth)
        initRcViews()
        checkPermissions()
        registerBtLauncher()
        initBtAdapter()

        //кнопочка с логотипом ble над списокм устройств
        val imBle = findViewById<ImageButton>(R.id.imBlueTooth)
        imBle.setOnClickListener {
            btLauncher.launch(Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE))
            bluetoothState()
        }

    }
    //функция инициализации ресайслеров
    private fun initRcViews() {
        val rcViewSearch = findViewById<RecyclerView>(R.id.rcViewSearch)
        val rcViewPaired = findViewById<RecyclerView>(R.id.rcViewPaired)
        rcViewPaired.layoutManager = LinearLayoutManager(this)
        itemAdapter = ItemAdapter(this)
        rcViewPaired.adapter = itemAdapter

    }

    private fun getPairedDevices(){
        try {
            val list = ArrayList<ListItem>()
            val deviceLIst = bAdapter?.bondedDevices as Set<BluetoothDevice>
            deviceLIst.forEach{
                list.add(
                    ListItem(
                        it.name,
                        it.address,
                        preferences?.getString(BleConstants.MAC, "") == it.address
                    )
                )
            }
            val tvEmpty = findViewById<TextView>(R.id.tvEmpty)
            tvEmpty.visibility = if(list.isEmpty()) View.VISIBLE else View.GONE
            itemAdapter.submitList(list)
        } catch (e: SecurityException){

        }
    }

    // Получение адаптера ble
    private fun initBtAdapter(){
        val bManager = getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        bAdapter = bManager.adapter
    }

    private fun bluetoothState(){
        val imBle = findViewById<ImageButton>(R.id.imBlueTooth)
        if (bAdapter?.isEnabled == true){
            changeButtonCollor(imBle, GREEN)
            getPairedDevices()
        } else{
            changeButtonCollor(imBle, RED)
        }
    }

    private fun registerBtLauncher(){
        val MY_PERMISSIONS_REQUEST_BLUETOOTH_CONNECT = 123

        // Проверяем, есть ли разрешение BLUETOOTH_CONNECT
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT)
            != PackageManager.PERMISSION_GRANTED) {
            // Если разрешение не предоставлено, запрашиваем его
            ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.BLUETOOTH_CONNECT),
                MY_PERMISSIONS_REQUEST_BLUETOOTH_CONNECT)
        }

        val imBle = findViewById<ImageButton>(R.id.imBlueTooth)
        btLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ){
            if (it.resultCode == RESULT_OK){
                changeButtonCollor(imBle, GREEN)
                getPairedDevices()
                Snackbar.make(imBle, "Bluetooth включен", Snackbar.LENGTH_SHORT).show()
            } else {
                Snackbar.make(imBle, "Bluetooth выключен", Snackbar.LENGTH_SHORT).show()
            }
        }
    }

    //непосрдественно функция проверки разрешений
    private fun checkPermissions(){
        if (!checkBlePermissions()){
            registerPermissionLauncher()
            launchBlePermissions()
        }
    }
    //Проверка блютуз разрешений
    fun checkBlePermissions() : Boolean{
        return if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.S){
            ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                    && ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) == PackageManager.PERMISSION_GRANTED
        } else {
            ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
        }
    }

    // Запуск окон спрашивающих разрешение
    private fun launchBlePermissions(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.S){
            permLauncher.launch(arrayOf(
                Manifest.permission.BLUETOOTH_CONNECT,
                Manifest.permission.ACCESS_FINE_LOCATION
            ))
        } else {
            permLauncher.launch(arrayOf(
                Manifest.permission.BLUETOOTH_CONNECT))
        }
    }

    private fun registerPermissionLauncher(){
        permLauncher = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ){

        }
    }

    private fun saveMac(mac: String){
        val editor = preferences?.edit()
        editor?.putString(BleConstants.MAC, mac)
        editor?.apply()
    }

    override fun onClick(device: ListItem) {
        saveMac(device.mac)
    }


}