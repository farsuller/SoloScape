package com.soloscape.database.data.local

import android.content.Context
import com.soloscape.util.SharedPreferenceDelegate
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class YourNameManager @Inject constructor(@ApplicationContext context: Context) {
    var yourName: String by SharedPreferenceDelegate(context, "your_name")
}
