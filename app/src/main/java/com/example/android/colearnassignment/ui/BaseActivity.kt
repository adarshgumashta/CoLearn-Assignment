package com.example.android.colearnassignment.ui

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.example.android.colearnassignment.Constants
import com.example.android.colearnassignment.model.remote.UnSplashImage

open class BaseActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    fun navigateToDetailActivity(pic: UnSplashImage) {
        val intent = Intent(this, DetailActivity::class.java)
        intent.apply {
            putExtra(Constants.PIC_DATA, pic)
        }
        startActivity(intent)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> onBackPressed()
        }
        return true
    }


    override fun onBackPressed() {
        finish()
    }
}