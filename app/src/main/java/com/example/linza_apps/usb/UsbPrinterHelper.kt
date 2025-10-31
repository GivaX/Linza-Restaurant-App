package com.example.linza_apps.usb

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.hardware.usb.UsbConstants
import android.hardware.usb.UsbDevice
import android.hardware.usb.UsbManager
import android.util.Log

class UsbPrinterHelper(private val context: Context) {
    private val usbManager = context.getSystemService(Context.USB_SERVICE) as UsbManager
    private val ACTION_USB_PERMISSION = "com.example.linza_apps.USB_PERMISSION"

    fun initPrinter(): UsbDevice? {
        val deviceList = usbManager.deviceList
        val printer = deviceList.values.firstOrNull { device->
            (0 until device.interfaceCount).any { i->
                device.getInterface(i).interfaceClass == UsbConstants.USB_CLASS_PRINTER
            }
        }

        Log.d("Printer", "Printer: ${printer?.deviceName}, ${printer?.interfaceCount}, ${printer?.vendorId}")
        for (device in deviceList.values) {
            Log.d("Printer", "Devices: ${device.deviceName}, ${device.interfaceCount}, ${device.vendorId}")
        }

        if (printer != null) {
            if (!usbManager.hasPermission(printer)) {
                val permissionIntent = PendingIntent.getBroadcast(
                    context, 0, Intent(ACTION_USB_PERMISSION), PendingIntent.FLAG_IMMUTABLE
                )
                usbManager.requestPermission(printer, permissionIntent)
            } else {
                Log.d("Printer", "Printer already has permissions")
            }
        } else {
            Log.d("Printer", "No printer found")
        }
        return printer
    }

    fun print(data: ByteArray) {
        val printer = usbManager.deviceList.values.firstOrNull { device->
            (0 until device.interfaceCount).any { i->
                device.getInterface(i).interfaceClass == UsbConstants.USB_CLASS_PRINTER
            }
        }
        if (printer == null) {
            Log.e("Printer", "Printer not found")
            return
        }

        if (!usbManager.hasPermission(printer)) {
            Log.e("Printer", "No permission for printer")
            return
        }

        val connection = usbManager.openDevice(printer)
        val iface = printer.getInterface(0)
        val endpointOut = (0 until iface.endpointCount)
            .map { iface.getEndpoint(it) }
            .firstOrNull{ it.direction == UsbConstants.USB_DIR_OUT }

        if (endpointOut == null) {
            Log.e("Printer", "No OUT endpoint found")
            connection.close()
            return
        }

        connection.claimInterface(iface, true)
        connection.bulkTransfer(endpointOut, data, data.size, 2000)
        connection.releaseInterface(iface)
        connection.close()
    }
}