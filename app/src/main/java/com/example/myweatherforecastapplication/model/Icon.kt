package com.example.myweatherforecastapplication.model

class Icon {
    companion object {
        fun getIcon(iconId: String): String {
            var icon = " "
            when {
                iconId.startsWith("01") -> {
                    icon = "clearsky"
                }
                iconId.startsWith("02") -> {
                    icon = "fewclouds"
                }
                iconId.startsWith("03") -> {
                    icon = "clouds"
                }
                iconId.startsWith("04") -> {
                    icon = "clouds"
                }
                iconId.startsWith("09") -> {
                    icon = "showerrain"
                }
                iconId.startsWith("10") -> {
                    icon = "rain"
                }
                iconId.startsWith("11") -> {
                    icon = "thunderstorm"
                }
                iconId.startsWith("13") -> {
                    icon = "snow"
                }
                iconId.startsWith("50") -> {
                    icon = "mist"
                }
            }
            return icon
        }
    }
}