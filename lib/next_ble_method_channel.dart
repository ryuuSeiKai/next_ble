import 'package:flutter/foundation.dart';
import 'package:flutter/services.dart';

import 'next_ble_platform_interface.dart';

/// An implementation of [NextBlePlatform] that uses method channels.
class MethodChannelNextBle extends NextBlePlatform {
  /// The method channel used to interact with the native platform.
  @visibleForTesting
  final methodChannel = const MethodChannel('next_ble_method');
  final discoveredDevicesChannel = EventChannel('next_ble_scan');
  @override
  Future<String?> getPlatformVersion() async {
    final version =
        await methodChannel.invokeMethod<String>('getPlatformVersion');
    return version;
  }

  @override
  Future<String?> initialize() async {
    return await methodChannel.invokeMethod<String>('initialize');
  }

  @override
  Future<String?> startAdverting() async {
    return await methodChannel.invokeMethod<String>(
      'scanForDevices',
    );
  }

  @override
  Stream<dynamic> getSnapshotDiscovered() {
    return discoveredDevicesChannel.receiveBroadcastStream().cast();
  }

  @override
  startbrosing() {
    // TODO: implement startbrosing
    throw UnimplementedError();
  }
}
