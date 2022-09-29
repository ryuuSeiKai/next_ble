package com.example.next_ble

import android.content.Context
import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.plugin.common.BinaryMessenger
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel

class NextBlePlugin : FlutterPlugin, MethodChannel.MethodCallHandler {
    override fun onAttachedToEngine(binding: FlutterPlugin.FlutterPluginBinding) {
        initalizePlugin(binding.binaryMessenger, binding.applicationContext)
    }

    override fun onDetachedFromEngine(binding: FlutterPlugin.FlutterPluginBinding) {
        // deinitalize logic
    }

    companion object {
        lateinit var pluginController: PluginController

        @JvmStatic
        private fun initalizePlugin(messenger: BinaryMessenger, context: Context) {
            val channel = MethodChannel(messenger, "next_ble_method")
            channel.setMethodCallHandler(NextBlePlugin())
            pluginController = PluginController()
            pluginController.initialize(messenger, context)
        }
    }

    override fun onMethodCall(call: MethodCall, result: MethodChannel.Result) {
        pluginController.execute(call, result)
    }
}
