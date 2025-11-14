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
    val ACTION_USB_PERMISSION = "com.example.linza_apps.USB_PERMISSION"

    // SNBC BTP-M300 specific IDs
//    private val SNBC_VENDOR_ID = 0x154F  // 5455 in decimal
//    private val SNBC_PRODUCT_ID = 0x154F // 5455 in decimal

    private var cachedPrinter: UsbDevice? = null

    fun getAllUsbDevices(): List<UsbDevice> {
        val devices = usbManager.deviceList.values.toList()
        Log.d("Printer", "Found ${devices.size} USB device(s)")
        devices.forEach { device ->
            Log.d("Printer", """
                Device: ${device.deviceName}
                VID: ${device.vendorId} (0x${device.vendorId.toString(16).uppercase()})
                PID: ${device.productId} (0x${device.productId.toString(16).uppercase()})
                Class: ${device.deviceClass}
            """.trimIndent())
        }
        return devices
    }

    fun selectPrinter(device: UsbDevice) {
        cachedPrinter = device
        Log.d("Printer", """
            Printer selected:
            Device: ${device.deviceName}
            VID: 0x${device.vendorId.toString(16).uppercase()}
            PID: 0x${device.productId.toString(16).uppercase()}
        """.trimIndent())

        if (!usbManager.hasPermission(device)) {
            Log.d("Printer", "Requesting permission...")
            val permissionIntent = PendingIntent.getBroadcast(context, 0, Intent(ACTION_USB_PERMISSION),
                PendingIntent.FLAG_IMMUTABLE)
            usbManager.requestPermission(device,permissionIntent)
        } else {
            Log.d("Printer", "Permission already granted")
        }
    }

    fun hasPermission(): Boolean {
        val printer = cachedPrinter ?: return false
        return usbManager.hasPermission(printer)
    }

    fun isPrinterSelected(): Boolean = cachedPrinter != null

    fun clearSelectedPrinter() {
        cachedPrinter = null
        Log.d("Printer", "Printer selection cleared")
    }

//    fun initPrinter(): UsbDevice? {
//        val deviceList = usbManager.deviceList
//        Log.d("Printer", "=== USB Device Scan ===")
//        Log.d("Printer", "Total devices found: ${deviceList.size}")
//
//        deviceList.values.forEach { device ->
//            Log.d(
//                "Printer", """
//                Device: ${device.deviceName}
//                Vendor ID: ${device.vendorId}
//                Product ID: ${device.productId}
//                Interface Count: ${device.interfaceCount}
//            """.trimIndent()
//            )
//            // Check each interface
//            for (i in 0 until device.interfaceCount) {
//                val iface = device.getInterface(i)
//                Log.d("Printer", "  Interface $i - Class: ${iface.interfaceClass}, SubClass: ${iface.interfaceSubclass}, Protocol: ${iface.interfaceProtocol}")
//            }
//        }
//
//        // Find SNBC BTP-M300 printer
//        val printer = deviceList.values.firstOrNull { device ->
//            // Match by exact vendor and product ID
//            val isMatch = device.vendorId == SNBC_VENDOR_ID &&
//                    device.productId == SNBC_PRODUCT_ID
//
//            if (isMatch) {
//                Log.d("Printer", "✓ Match found! VID=${device.vendorId}, PID=${device.productId}")
//            }
//
//            isMatch
//        }
//
//        if (printer != null) {
//            cachedPrinter = printer
//            Log.d("Printer", "Printer found: ${printer.deviceName}")
//
//            if (!usbManager.hasPermission(printer)) {
//                Log.d("Printer", "Requesting permission for printer")
//                val permissionIntent = PendingIntent.getBroadcast(
//                    context, 0, Intent(ACTION_USB_PERMISSION), PendingIntent.FLAG_IMMUTABLE
//                )
//                usbManager.requestPermission(printer, permissionIntent)
//            } else {
//                Log.d("Printer", "Printer already has permissions")
//                return printer
//            }
//        } else {
//            Log.d("Printer", "No printer found")
//        }
//        return printer
//    }

    fun getCachedPrinter(): UsbDevice? = cachedPrinter

    fun print(data: ByteArray): Boolean {
//        val printer = usbManager.deviceList.values.firstOrNull { device->
//            (0 until device.interfaceCount).any { i->
//                device.getInterface(i).interfaceClass == UsbConstants.USB_CLASS_PRINTER
//            }
//        }
//        val printer = cachedPrinter ?: initPrinter()

        val printer = cachedPrinter

        if (printer == null) {
            Log.e("Printer", "Printer not found")
            return false
        }

        if (!usbManager.hasPermission(printer)) {
            Log.e("Printer", "No permission for printer")
            return false
        }

        return try {
            val connection = usbManager.openDevice(printer)
            if (connection == null) {
                Log.e("Printer", "Failed to open USB connection")
                return false
            }

            val iface = printer.getInterface(0)
            val endpointOut = (0 until iface.endpointCount)
                .map { iface.getEndpoint(it) }
                .firstOrNull{ it.direction == UsbConstants.USB_DIR_OUT }

            if (endpointOut == null) {
                Log.e("Printer", "No OUT endpoint found")
                connection.close()
                return false
            }

            connection.claimInterface(iface, true)
            val bytesTransferred = connection.bulkTransfer(endpointOut, data, data.size, 2000)
            connection.releaseInterface(iface)
            connection.close()

            Log.d("Printer", "Printed $bytesTransferred bytes")
            true
        } catch (e: Exception) {
            Log.e("Printer", "Print error", e)
            false
        }
    }

    fun testConnection(): Boolean {
        Log.d("Printer", "=== TESTING PRINTER ===")
        val testData = byteArrayOf(
            0x1B, 0x40,
            *"TEST PRINT\n".toByteArray(),
            *"Printer Ready!\n".toByteArray(),
            *"==================\n".toByteArray(),
            *"\n\n\n".toByteArray(),
            0x1D, 0x56, 0x00 // Cut
        )
        return print(testData)
    }
}