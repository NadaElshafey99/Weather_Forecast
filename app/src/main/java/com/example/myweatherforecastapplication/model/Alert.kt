package com.example.myweatherforecastapplication.model

data class Alert (
    val senderName: String,
    val event: String,
    val start: Long,
    val end: Long,
    val description: String,
    val tags: List<String>
)