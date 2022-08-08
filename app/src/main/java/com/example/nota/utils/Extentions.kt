package com.example.nota.utils

import android.content.ContentUris
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.net.ConnectivityManager
import android.net.ConnectivityManager.*
import android.net.NetworkCapabilities.*
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.text.TextUtils
import android.util.Patterns
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.NavDirections
import androidx.navigation.findNavController
import com.google.android.material.snackbar.Snackbar
import java.util.regex.Pattern

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

fun Context.isConnected(): Boolean {
    val connectivityManager = this.getSystemService(
        Context.CONNECTIVITY_SERVICE
    ) as ConnectivityManager
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        val activeNetwork = connectivityManager.activeNetwork ?: return false
        val capabilities =
            connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
        return when {
            capabilities.hasTransport(TRANSPORT_WIFI) -> true
            capabilities.hasTransport(TRANSPORT_CELLULAR) -> true
            capabilities.hasTransport(TRANSPORT_ETHERNET) -> true
            else -> false
        }
    } else {
        connectivityManager.activeNetworkInfo?.run {
            return when (type) {
                TYPE_WIFI -> true
                TYPE_MOBILE -> true
                TYPE_ETHERNET -> true
                else -> false
            }
        }
    }
    return false
}

fun <T> List<T>.toArrayList(): ArrayList<T> {
    return ArrayList(this)
}

fun Context.toast(msg: String) {
    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
}

fun Context.checkEmail(email: String): Boolean {
    val EMAIL_ADDRESS_REGEX: Pattern = Pattern.compile(
        "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                "\\@" +
                "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                "(" +
                "\\." +
                "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                ")+"
    )
    return EMAIL_ADDRESS_REGEX.matcher(email).matches()
}
fun Context.checkPassword(password: String): Boolean {
    val PASSWORD_REGEX = Pattern.compile("^" +
            "(?=.*[0-9])" +         //at least 1 digit
            "(?=.*[a-z])" +         //at least 1 lower case letter
            "(?=.*[A-Z])" +         //at least 1 upper case letter
            "(?=.*[a-zA-Z])" +      //any letter
            "(?=.*[@#$%^&+=])" +    //at least 1 special character
            "(?=\\S+$)" +           //no white spaces
            ".{8,}" +               //at least 8 characters
            "$")
    return PASSWORD_REGEX.matcher(password).matches()
}

fun EditText.isEmpty(): Boolean {
    return TextUtils.isEmpty(this.text.toString())
}

fun View.snack(message: String) {
    val snack = Snackbar.make(this, message, Snackbar.LENGTH_SHORT)
    snack.show()
}

fun Snackbar.action(@StringRes actionRes: Int, color: Int? = null, listener: (View) -> Unit) {
    setAction(actionRes, listener)
    color?.let { setActionTextColor(ContextCompat.getColor(context, color)) }
}

fun AppCompatActivity.checkPermission(permission: String) = ActivityCompat.checkSelfPermission(
    this,
    permission
)

fun AppCompatActivity.shouldRequestPermissionRationale(permission: String) =
    ActivityCompat.shouldShowRequestPermissionRationale(this, permission)

fun AppCompatActivity.requestAllPermissions(permissionsArray: Array<String>, requestCode: Int) {
    ActivityCompat.requestPermissions(this, permissionsArray, requestCode)
}

fun <T> Context.openActivity(it: Class<T>, extras: Bundle.() -> Unit = {}) {
    val intent = Intent(this, it)
    intent.putExtras(Bundle().apply(extras))
    startActivity(intent)
}

fun View.makeGone() {
    this.visibility = GONE
}

fun View.makeVisible() {
    this.visibility = VISIBLE
}

fun View.moveToNextFragment(directions: Int,dataToPass:Bundle?) {
    findNavController().navigate(directions,dataToPass)
}
