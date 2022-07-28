package com.example.nota.utils

import android.text.TextUtils
import android.util.Patterns
import android.view.View

fun View.validateCredentials(
    userName: String? = null,
    email: String,
    password: String,
    isLogin:Boolean
): Pair<Boolean, String> {
    var res = Pair(true, "")
    if ((!isLogin && TextUtils.isEmpty(userName)) || TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
        res = Pair(false, "Please provide the credentials")
    } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
        res = Pair(false, "Please provide valid email")
    } else if (password.length <= 7) {
        res = Pair(false, "Password length must be greater than 7")
    }
    return res
}