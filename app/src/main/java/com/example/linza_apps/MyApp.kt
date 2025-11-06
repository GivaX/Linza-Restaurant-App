package com.example.linza_apps

import android.app.Application
import android.util.Log
import com.example.linza_apps.usb.UsbPrinterHelper

class MyApp: Application() {
    lateinit var usbHelper: UsbPrinterHelper
        private set

    private var printerReady = false

    override fun onCreate() {
        super.onCreate()
        try {
            usbHelper = UsbPrinterHelper(this)
            Log.d("Printer", "MyApp: USB Helper Created")
        } catch (e: Exception) {
            Log.e("Printer", "MyApp: Failed to Initialize printer", e)
        }
    }
    fun isPrinterReady(): Boolean = printerReady

    fun setPrinterReady(ready: Boolean) {
        printerReady = ready
    }

    companion object{
        lateinit var instance: MyApp
            private set
    }
}