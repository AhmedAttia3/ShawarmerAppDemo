package com.shawarmer.app

import android.os.Bundle
import android.util.Log
import androidx.core.content.ContextCompat
import com.shawarmer.app.databinding.ActivityMainBinding
import com.shawarmer.app.utils.BaseActivity
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : BaseActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setStatusBarPadding()
    }

    fun setStatusBarPadding(){
        var result = 0
        val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
        if (resourceId > 0) {
            result = resources.getDimensionPixelSize(resourceId)
        }
        binding.root.setPadding(0,result,0,0)
    }

    fun whiteBackground(){
        binding.root.setBackgroundColor(ContextCompat.getColor(this,R.color.white))
    }

}