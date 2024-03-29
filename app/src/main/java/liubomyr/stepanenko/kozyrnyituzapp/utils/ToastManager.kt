package liubomyr.stepanenko.kozyrnyituzapp.utils

import android.content.Context
import android.content.SharedPreferences
import android.widget.Toast

object ToastManager {
    private lateinit var context: Context

    fun init(context: Context) {
        this.context = context
    }

    fun showToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}
