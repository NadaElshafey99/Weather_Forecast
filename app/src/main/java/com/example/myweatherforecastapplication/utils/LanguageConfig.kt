package com.example.myweatherforecastapplication.utils

import android.content.Context
import android.content.ContextWrapper
import java.util.*

class LanguageConfig {
    companion object {
        fun changeLanguage(context: Context, languageCode: String): ContextWrapper {
            var context = context
            val resources = context.resources
            val configuration = resources.configuration
            val systemLocale = configuration.locales.get(0)
            if (!languageCode.equals("") && !systemLocale?.language.equals(languageCode)) {
                val locale = Locale(languageCode)
                Locale.setDefault(locale)
                configuration?.setLocale(locale)
                context = context.createConfigurationContext(configuration)

            }
            return ContextWrapper(context)
        }
    }
}