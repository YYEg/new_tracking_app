package com.example.project

import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.util.Log
import java.io.IOException
import java.util.UUID

class ConnectThread(private val device : BluetoothDevice) : Thread() {
    private val uuid = UUID.randomUUID().toString()
    private var mSocket : BluetoothSocket? = null
    init {
       try {

           mSocket = device.createRfcommSocketToServiceRecord(UUID.fromString(uuid))
       } catch (e: IOException){
           Log.d("MyLog", "Ошибка! ${e}")
       } catch (e: SecurityException){

       }
    }

    override fun run() {
        try {
            Log.d("MyLog", "Connecting...")
            mSocket?.connect()
            Log.d("MyLog", "Done connect!")
        } catch (e: IOException){
            Log.d("MyLog", "Ошибка соединения! ${e}")
        } catch (e: SecurityException){

        }
    }

    fun closeConnection(){
        try {
            mSocket?.close()
        } catch (e: IOException){

        }
    }
}