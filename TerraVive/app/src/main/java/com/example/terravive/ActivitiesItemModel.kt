package com.example.terravive

data class ActivitiesItemModel(
    val id: String = "",
    val organizerEmail: String = "",
    val name: String = "",
    val time: String = "",
    val date: String = "",
    val description: String = "",
    val isApproved: Boolean = false
)
