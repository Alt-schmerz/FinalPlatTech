package com.example.terravive

data class NotificationItem(
    var username: String = "",
    var message: String = "",
    var profileImageResource: Int = 0,
    var contentImageResource: Int = 0,
    var category: String = ""
)
