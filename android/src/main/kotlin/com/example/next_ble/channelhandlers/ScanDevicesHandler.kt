package com.example.next_ble.channelhandlers

import android.os.ParcelUuid
import com.example.next_ble.ProtobufModel as pb
import com.example.next_ble.converters.ProtobufMessageConverter
import com.example.next_ble.converters.UuidConverter
import com.example.next_ble.model.ScanMode
import com.example.next_ble.model.createScanMode
import io.flutter.plugin.common.EventChannel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable

class ScanDevicesHandler(private val bleClient: com.example.next_ble.ble.BleClient) :
    EventChannel.StreamHandler {

    private var scanDevicesSink: EventChannel.EventSink? = null
    private lateinit var scanForDevicesDisposable: Disposable
    private val converter = ProtobufMessageConverter()

    companion object {
        private var scanParameters: ScanParameters? = null
    }

    override fun onListen(objectSink: Any?, eventSink: EventChannel.EventSink?) {
        eventSink?.let {
            scanDevicesSink = eventSink
            startDeviceScan()
        }
    }

    override fun onCancel(objectSink: Any?) {
        stopDeviceScan()
        scanDevicesSink = null
    }

    private fun startDeviceScan() {
        scanParameters?.let { params ->
            scanForDevicesDisposable = bleClient.scanForDevices(
                params.filter,
                params.mode,
                params.locationServiceIsMandatory
            )
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { scanResult ->
                        handleDeviceScanResult(converter.convertScanInfo(scanResult))
                    },
                    { throwable ->
                        handleDeviceScanResult(converter.convertScanErrorInfo(throwable.message))
                    }
                )
        }
            ?: handleDeviceScanResult(converter.convertScanErrorInfo("Scanning parameters are not set"))
    }

    fun stopDeviceScan() {
        if (this::scanForDevicesDisposable.isInitialized) scanForDevicesDisposable.let {
            if (!it.isDisposed) {
                it.dispose()
                scanParameters = null
            }
        }
    }

    fun prepareScan(scanMessage: pb.ScanForDevicesRequest) {
        stopDeviceScan()
        val filter = scanMessage.serviceUuidsList
            .map { ParcelUuid(UuidConverter().uuidFromByteArray(it.data.toByteArray())) }
        val scanMode = createScanMode(scanMessage.scanMode)
        scanParameters =
            ScanParameters(filter, scanMode, scanMessage.requireLocationServicesEnabled)
    }

    private fun handleDeviceScanResult(discoveryMessage: pb.DeviceScanInfo) {
        scanDevicesSink?.success(discoveryMessage.toByteArray())
    }
}

private data class ScanParameters(
    val filter: List<ParcelUuid>,
    val mode: ScanMode,
    val locationServiceIsMandatory: Boolean
)
