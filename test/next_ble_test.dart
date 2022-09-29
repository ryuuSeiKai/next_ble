import 'package:flutter_test/flutter_test.dart';
import 'package:next_ble/next_ble.dart';
import 'package:next_ble/next_ble_platform_interface.dart';
import 'package:next_ble/next_ble_method_channel.dart';
import 'package:plugin_platform_interface/plugin_platform_interface.dart';

class MockNextBlePlatform
    with MockPlatformInterfaceMixin
    implements NextBlePlatform {

  @override
  Future<String?> getPlatformVersion() => Future.value('42');
}

void main() {
  final NextBlePlatform initialPlatform = NextBlePlatform.instance;

  test('$MethodChannelNextBle is the default instance', () {
    expect(initialPlatform, isInstanceOf<MethodChannelNextBle>());
  });

  test('getPlatformVersion', () async {
    NextBle nextBlePlugin = NextBle();
    MockNextBlePlatform fakePlatform = MockNextBlePlatform();
    NextBlePlatform.instance = fakePlatform;

    expect(await nextBlePlugin.getPlatformVersion(), '42');
  });
}
