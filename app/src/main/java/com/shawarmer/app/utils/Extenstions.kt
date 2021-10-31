package com.shawarmer.app.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.graphics.drawable.Drawable
import android.net.Uri
import android.util.Patterns
import android.view.View
import android.webkit.MimeTypeMap
import android.widget.ImageView
import android.widget.Toast
import androidx.databinding.BindingAdapter
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.shawarmer.app.R
import java.math.RoundingMode
import java.text.DecimalFormat

fun Double.roundOffDecimal(): Double {
    val df = DecimalFormat("#.##")
    df.roundingMode = RoundingMode.CEILING
    return df.format(this).toDouble()
}

fun Context.toast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun View.snackBar(message: String? ) {
    val snackBar = Snackbar.make(this, message!!, Snackbar.LENGTH_LONG)
    val view = snackBar.view
    val textView = view.findViewById<View>(R.id.snackbar_text)
    textView.textAlignment = View.TEXT_ALIGNMENT_CENTER
    snackBar.show()
}

fun View.snackBarWithAction(
    message: String,
    actionTitle: String
) {
    val snackBar = Snackbar.make(rootView, message, Snackbar.LENGTH_LONG)
    val view = snackBar.view
    val textView = view.findViewById<View>(R.id.snackbar_text)
    textView.textAlignment = View.TEXT_ALIGNMENT_TEXT_START
    snackBar.duration = 10000
    snackBar.show()
}

fun Fragment.getNavigationResult(key: String = "result") =
    findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<String>(key)

fun Fragment.setNavigationResult(result: String, key: String = "result") {
    findNavController().previousBackStackEntry?.savedStateHandle?.set(key, result)
}

fun CharSequence?.isValidEmail() =
    !isNullOrEmpty() && Patterns.EMAIL_ADDRESS.matcher(this).matches()

fun getFileExtension(activity: Activity, uri: Uri): String {
    return MimeTypeMap.getSingleton()
        .getExtensionFromMimeType(activity.contentResolver.getType(uri))!!
}

@SuppressLint("CheckResult")
@BindingAdapter(
    "srcUrl",
    "circleCrop",
    "placeholder",
    requireAll = false // make the attributes optional
)
fun ImageView.bindSrcUrl(
    url: String,
    circleCrop: Boolean = false,
    placeholder: Drawable?,
) = Glide.with(this).load(url).let { request ->

    if (circleCrop) {
        request.circleCrop()
    }

    if (placeholder != null) {
        request.placeholder(placeholder)
    }

    request.into(this)
}