package com.example.linza_apps.usb

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.hardware.usb.UsbDevice
import android.hardware.usb.UsbManager
import android.util.Log

class UsbReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == "com.example.linza_apps.USB_PERMISSION") {
            val device = intent.getParcelableExtra<UsbDevice>(UsbManager.EXTRA_DEVICE)
            if (intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false)) {
                if (device != null) {
                    Log.d("Printer", "Permission granted for ${device.deviceName}")
                }
            } else {
                Log.d("Printer", "Permission denied for ${device?.deviceName}")
            }
        }
    }
}