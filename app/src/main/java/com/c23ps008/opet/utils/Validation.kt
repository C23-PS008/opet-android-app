package com.c23ps008.opet.utils

import android.util.Patterns
import java.util.regex.Pattern

object Validation {
    fun email(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
}