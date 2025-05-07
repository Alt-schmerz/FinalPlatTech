package com.example.terravive.model

data class ActivityEvent(
    var id: String = "",
    var name: String = "",
    var date: String = "",
    var time: String = "",
    var location: String = "",
    val image: Int // Corrected: image is now a constructor parameter
)

