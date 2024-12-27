package com.soloscape.util

import android.content.Context
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

class SharedPreferenceDelegate(
    private val context: Context,
    private val name: String,
    private val defaultValue: String = "",
) : ReadWriteProperty<Any?, String> {

    private val sharedPreferences by lazy {
        context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
    }

    override fun getValue(thisRef: Any?, property: KProperty<*>): String {
        return sharedPreferences.getString(name, defaultValue) ?: defaultValue
    }

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: String) {
        return sharedPreferences.edit().putString(name, value).apply()
    }
}
