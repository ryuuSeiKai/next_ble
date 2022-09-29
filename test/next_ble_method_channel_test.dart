import 'package:flutter/services.dart';
import 'package:flutter_test/flutter_test.dart';
import 'package:next_ble/next_ble_method_channel.dart';

void main() {
  MethodChannelNextBle platform = MethodChannelNextBle();
  const MethodChannel channel = MethodChannel('next_ble');

  TestWidgetsFlutterBinding.ensureInitialized();

  setUp(() {
    channel.setMockMethodCallHandler((MethodCall methodCall) async {
      return '42';
    });
  });

  tearDown(() {
    channel.setMockMethodCallHandler(null);
  });

  test('getPlatformVersion', () async {
    expect(await platform.getPlatformVersion(), '42');
  });
}
