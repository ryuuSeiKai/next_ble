import 'dart:async';

import 'package:flutter/material.dart';
import 'package:next_ble/next_ble.dart';

void main() {
  runApp(const MyApp());
}

class MyApp extends StatefulWidget {
  const MyApp({super.key});

  @override
  State<MyApp> createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  final String _platformVersion = 'Unknown';
  final _nextBlePlugin = NextBle();

  @override
  void initState() {
    super.initState();
    initPlatformState();
  }

  // Platform messages are asynchronous, so we initialize in an async method.
  Future<void> initPlatformState() async {
    // Platform messages may fail, so we use a try/catch PlatformException.
    // We also handle the message potentially returning null.
    // try {
    //   platformVersion = await _nextBlePlugin.getPlatformVersion() ??
    //       'Unknown platform version';
    // } on PlatformException {
    //   platformVersion = 'Failed to get platform version.';
    // }
    // _nextBlePlugin.initialize()

    // If the widget was removed from the tree while the asynchronous platform
    // message was in flight, we want to discard the reply rather than calling
    // setState to update our non-existent appearance.
    if (!mounted) return;

    setState(() {
      // _platformVersion = platformVersion;
    });
  }

  Future<void> bleAdvertising() async {
    try {
      await _nextBlePlugin.initialize();
      // final data = await _nextBlePlugin.startAdvertings();
      // print(data);
    } catch (e) {
      debugPrint(e.toString());
    }
  }

  @override
  Widget build(BuildContext context) {
    // List<>

    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(
          title: const Text('Plugin example app'),
        ),
        body: Center(
          child: Column(
            children: [
              Text('Running on: $_platformVersion\n'),
              ElevatedButton(
                onPressed: bleAdvertising,
                child: const Text('Try to adverting bluetooth'),
              ),
              // StreamBuilder(
              //   stream: _nextBlePlugin.getSnapshotDiscovered(),
              //   builder: (context, snapshot) {
              //     if (snapshot.hasData) {
              //       print(snapshot.data);
              //       return Text("${snapshot.data}");
              //     }
              //     return Container();
              //   },
              // )
            ],
          ),
        ),
      ),
    );
  }
}
