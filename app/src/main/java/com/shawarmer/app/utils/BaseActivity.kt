package com.shawarmer.app.utils

import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.view.View
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import com.shawarmer.app.R
import com.shawarmer.app.ui.checkout.CheckoutFragment

open class BaseActivity : AppCompatActivity() {

    private lateinit var progressDialog: Dialog

    fun showProgressDialog(){
        progressDialog = Dialog(this)
        progressDialog.window!!.setBackgroundDrawableResource(android.R.color.transparent)
        progressDialog.setContentView(R.layout.dialog_progress_layout)
        progressDialog.setCancelable(false)
        progressDialog.setCanceledOnTouchOutside(false)
        progressDialog.show()
    }

    fun dismissProgressDialog(){
        progressDialog.dismiss()
    }

    fun showAlertDialog(message: String) {
        AlertDialog.Builder(this)
            .setMessage(message)
            .setPositiveButton(R.string.button_ok, null)
            .setCancelable(false)
            .show()
    }

    fun showAlertDialog(messageId: Int) {
        showAlertDialog(getString(messageId))
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        val navHostFragment: NavHostFragment? =
            supportFragmentManager.findFragmentById(R.id.mainNavHost) as NavHostFragment?
        val currentFragment = navHostFragment!!.childFragmentManager.fragments[0]
        if(currentFragment is CheckoutFragment)
            currentFragment.onNewIntent(intent)

    }


}