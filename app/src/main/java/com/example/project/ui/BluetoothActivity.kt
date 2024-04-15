package com.example.project.ui

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.Color.GREEN
import android.graphics.Color.RED
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.project.BleConstants
import com.example.project.InfoBleActivity
import com.example.project.ItemAdapter
import com.example.project.ListItem
import com.example.project.R
import com.example.project.changeButtonCollor
import com.google.android.material.snackbar.Snackbar

class BluetoothActivity : AppCompatActivity(), ItemAdapter.Listener {
    //shared pref переменная
    private var preferences: SharedPreferences? = null
    private lateinit var itemAdapter: ItemAdapter
    private lateinit var discoveryItemAdapter: ItemAdapter
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
        intentFilters()
        checkPermissions()
        registerBtLauncher()
        initBtAdapter()

        //кнопочка с логотипом ble над списокм устройств
        val imBle = findViewById<ImageButton>(R.id.imBlueTooth)
        imBle.setOnClickListener {
            btLauncher.launch(Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE))
            bluetoothState()
        }
        val imBleSearch = findViewById<ImageButton>(R.id.imBluetoothSearch)
        val progressB = findViewById<ProgressBar>(R.id.pbSearch)
        imBleSearch.setOnClickListener {
            try {
                if(bAdapter?.isEnabled == true){
                    bAdapter?.startDiscovery()
                    it.visibility = View.GONE
                    progressB.visibility = View.VISIBLE
                }
            } catch(e: SecurityException) {
                Log.d("MyLog", "${e}")
            }
        }
        val btnToInfo = findViewById<Button>(R.id.btnToInfo)
        btnToInfo.setOnClickListener {
            // Retrieve the MAC address from SharedPreferences
            val macAddress = preferences?.getString(BleConstants.MAC, "")
            // Create the Intent
            Log.d("MyLog", "${macAddress}")
            val intent = Intent(this, InfoBleActivity::class.java)
            // Pass the MAC address as an extra to the Intent
            intent.putExtra("macAddress", macAddress)
            // Start the InfoBleActivity
            startActivity(intent)
        }

    }

    //функция инициализации ресайслеров
    private fun initRcViews() {
        val rcViewSearch = findViewById<RecyclerView>(R.id.rcViewSearch)
        val rcViewPaired = findViewById<RecyclerView>(R.id.rcViewPaired)
        rcViewPaired.layoutManager = LinearLayoutManager(this)
        rcViewSearch.layoutManager = LinearLayoutManager(this)
        itemAdapter = ItemAdapter(this, false)
        rcViewPaired.adapter = itemAdapter
        discoveryItemAdapter = ItemAdapter(this, true)
        rcViewSearch.adapter = discoveryItemAdapter

    }

    private fun getPairedDevices(){
        try {
            val list = ArrayList<ListItem>()
            val deviceLIst = bAdapter?.bondedDevices as Set<BluetoothDevice>
            deviceLIst.forEach{
                list.add(
                    ListItem(
                        it,
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
                    && ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_SCAN) == PackageManager.PERMISSION_GRANTED
        } else {
            ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
        }
    }

    // Запуск окон спрашивающих разрешение
    private fun launchBlePermissions(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.S){
            permLauncher.launch(arrayOf(
                Manifest.permission.BLUETOOTH_CONNECT,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.BLUETOOTH_SCAN
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

    override fun onClick(item: ListItem) {
        saveMac(item.device.address)
    }

    private val broadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent?.action == BluetoothDevice.ACTION_FOUND) {
                val deviceFounded = intent.getParcelableExtra<BluetoothDevice>(BluetoothDevice.EXTRA_DEVICE)
                deviceFounded?.let {
                    val list = mutableSetOf<ListItem>()
                    list.addAll(discoveryItemAdapter.currentList)
                    list.add(ListItem(it, false))
                    discoveryItemAdapter.submitList(list.toList())
                    discoveryItemAdapter.notifyDataSetChanged() // Notify adapter of data change
                    Log.d("MyLog", "${list}")
                    val tvEmtpySearch = findViewById<TextView>(R.id.tvEmpty2)
                    tvEmtpySearch.visibility = if(list.isEmpty()) View.VISIBLE else View.GONE
                }
            } else if (intent?.action == BluetoothDevice.ACTION_BOND_STATE_CHANGED) {
                //при изменении статуса соединения, список сопряженных устрйоств обновляется
                getPairedDevices()
            } else if (intent?.action == BluetoothAdapter.ACTION_DISCOVERY_FINISHED) {
                // Для крутилки при поиске
                val imBleSearch = findViewById<ImageButton>(R.id.imBluetoothSearch)
                val progressB = findViewById<ProgressBar>(R.id.pbSearch)
                imBleSearch.visibility = View.VISIBLE
                progressB.visibility = View.GONE

            }
        }
    }

    private fun intentFilters(){
        val f1 = IntentFilter(BluetoothDevice.ACTION_FOUND)
        val f2 = IntentFilter(BluetoothDevice.ACTION_BOND_STATE_CHANGED)
        val f3 = IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)
        registerReceiver(broadcastReceiver, f1)
        registerReceiver(broadcastReceiver, f2)
        registerReceiver(broadcastReceiver, f3)
    }

}