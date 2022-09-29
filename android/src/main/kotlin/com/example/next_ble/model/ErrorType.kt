package com.example.next_ble.model

enum class ConnectionErrorType(val code: Int) {
    UNKNOWN(0),
    FAILEDTOCONNECT(1)
}

enum class ClearGattCacheErrorType(val code: Int) {
    UNKNOWN(0)
}

enum class CharacteristicErrorType(val code: Int) {
    UNKNOWN(0)
}

enum class NegotiateMtuErrorType(val code: Int) {
    UNKNOWN(0)
}

enum class ScanErrorType(val code: Int) {
    UNKNOWN(0)
}
