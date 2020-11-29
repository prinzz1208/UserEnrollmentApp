package com.example.userenrollmentapp

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.google.gson.Gson

fun <T> convertToDataClass(
    data: MutableMap<String, Any>,
    pojo: Class<T>
): T {
    val gson = Gson()
    data.toMap()
    val jsonElement = gson.toJsonTree(data)
    return gson.fromJson(jsonElement, pojo)
}

fun closeKeyboard(context: Context, view: View) {
    val imm = context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(view.windowToken, 0)
    view.clearFocus()
}
