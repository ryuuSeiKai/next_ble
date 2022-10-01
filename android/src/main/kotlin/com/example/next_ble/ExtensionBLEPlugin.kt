package com.example.next_ble

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.provider.Settings
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel

const val tag = "BluetoothCTEPlugin"

// Permissions and request constants
private const val REQUEST_COARSE_LOCATION_PERMISSIONS = 1451

class ExtensionBLEPlugin(context: Context, activity: Activity) {

    private var bluetoothAdapter: BluetoothAdapter? = null
    private var context: Context
    private var activity: Activity

    init {
        this.context = context
        this.activity = activity
        val bluetoothManager: BluetoothManager =
            activity.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager

        this.bluetoothAdapter = bluetoothManager.adapter
    }

    fun getName(result: MethodChannel.Result) {
        ensurePermissions(object : EnsurePermissionsCallback {
            @SuppressLint("MissingPermission")
            override fun onResult(granted: Boolean) {
                if (granted) {
                    result.success(bluetoothAdapter!!.name)
                }
            }
        })
    }

    fun setName(call: MethodCall, result: MethodChannel.Result) {
        if (!call.hasArgument("name")) {
            result.error("invalid_argument", "argument 'name' not found", null)
        }

        try {
            val name: String? = call.argument("name")
            if (ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.BLUETOOTH_CONNECT
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return
            }
            result.success(bluetoothAdapter!!.setName(name))
        } catch (ex: java.lang.ClassCastException) {
            result.error("invalid_argument", "'name' argument is required to be string", null)
        }

    }

    fun startDiscovery(result: MethodChannel.Result) {
        Log.d(tag, "Starting discovery")

        val intent = IntentFilter()

        intent.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)
        intent.addAction(BluetoothDevice.ACTION_FOUND)

        ensurePermissions(object : EnsurePermissionsCallback {
            @SuppressLint("MissingPermission")
            override fun onResult(granted: Boolean) {
                if (granted) {
                    bluetoothAdapter!!.startDiscovery()
                    Log.d(tag, "Starting discovery")
                    result.success(null)
                }
            }
        })
    }

    fun requestDiscoverable(call: MethodCall, result: MethodChannel.Result) {
        if (!call.hasArgument("duration")) {
            result.error("invalid_argument", "argument 'duration' not found", null)
        }

        val duration = call.argument<Int>("duration")

        val requestCode = 1
        val discoverableIntent: Intent =
            Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE).apply {
                putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, duration)
            }

        ActivityCompat.startActivityForResult(activity, discoverableIntent, requestCode, null)
    }

    fun openSetting() {
        ContextCompat.startActivity(
            activity,
            Intent(Settings.ACTION_BLUETOOTH_SETTINGS),
            null
        )
    }

    private interface EnsurePermissionsCallback {
        fun onResult(granted: Boolean)
    }

    private var pendingPermissionsEnsureCallbacks: EnsurePermissionsCallback? = null

    private fun ensurePermissions(callbacks: EnsurePermissionsCallback) {
        if (ContextCompat.checkSelfPermission(
                activity,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
            != PackageManager.PERMISSION_GRANTED
            || ContextCompat.checkSelfPermission(
                activity,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                activity,
                arrayOf(
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ),
                REQUEST_COARSE_LOCATION_PERMISSIONS
            )
            pendingPermissionsEnsureCallbacks = callbacks
        } else {
            callbacks.onResult(true)
        }
    }
}