// Reservation.kt
package com.example.terravive.model

data class Reservation(
    var activityName: String = "",
    var activityDate: String = "",
    var activityTime: String = "",
    var activityLocation: String = "",
    var timestamp: Long = System.currentTimeMillis()
)
