package com.example.linza_apps

import android.app.Application
import android.util.Log
import com.example.linza_apps.usb.UsbPrinterHelper

class MyApp: Application() {
    lateinit var usbHelper: UsbPrinterHelper
        private set

    override fun onCreate() {
        super.onCreate()
        try {
            usbHelper = UsbPrinterHelper(this)
            usbHelper.initPrinter()
        } catch (e: Exception) {
            Log.e("Printer", "MyApp: Failed to Initialize printer", e)
        }
    }
}