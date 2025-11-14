package com.example.linza_apps.usb

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.hardware.usb.UsbDevice
import android.hardware.usb.UsbManager
import android.util.Log
import android.widget.Toast
import com.example.linza_apps.MyApp

class UsbReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        Log.d("Printer", "UsbReceiver: Action received - ${intent.action}")

        when (intent.action) {
            "com.example.linza_apps.USB_PERMISSION" -> {
                val device = intent.getParcelableExtra<UsbDevice>(UsbManager.EXTRA_DEVICE)
                val granted = intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false)

                if (granted && device != null) {
                    Log.d("Printer", "Permission GRANTED for ${device.deviceName}")
                    Toast.makeText(context, "Printer connected!", Toast.LENGTH_SHORT).show()

                    // Update app state
                    (context.applicationContext as? MyApp)?.setPrinterReady(true)
                } else {
                    Log.e("Printer", "Permission DENIED for ${device?.deviceName}")
                    Toast.makeText(context, "Printer permission denied", Toast.LENGTH_SHORT).show()
                    (context.applicationContext as? MyApp)?.setPrinterReady(false)
                }
            }

            UsbManager.ACTION_USB_DEVICE_ATTACHED -> {
                Log.d("Printer", "USB device attached")
                val device = intent.getParcelableExtra<UsbDevice>(UsbManager.EXTRA_DEVICE)
                Toast.makeText(context, "USB device attached: ${device?.deviceName}", Toast.LENGTH_SHORT).show()
            }

            UsbManager.ACTION_USB_DEVICE_DETACHED -> {
                Log.d("Printer", "USB device detached")
                Toast.makeText(context, "Printer disconnected", Toast.LENGTH_SHORT).show()
                (context.applicationContext as? MyApp)?.setPrinterReady(false)
            }
        }
    }
}