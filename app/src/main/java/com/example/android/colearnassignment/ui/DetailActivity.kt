package com.example.android.colearnassignment.ui

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.example.android.colearnassignment.Constants.PIC_DATA
import com.example.android.colearnassignment.R
import com.example.android.colearnassignment.databinding.ActivityDetailBinding
import com.example.android.colearnassignment.loadImage
import com.example.android.colearnassignment.model.remote.UnSplashImage

class DetailActivity : BaseActivity() {
    private lateinit var activityDetailBinding: ActivityDetailBinding
    private var unSplashImage: UnSplashImage? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityDetailBinding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(activityDetailBinding.root)
        loadImage()
    }

    private fun loadImage() {
        unSplashImage = intent.getParcelableExtra(PIC_DATA) as UnSplashImage?
        unSplashImage?.urls.let {
            it?.full?.let { fullImageUrl ->
                loadImage(
                    fullImageUrl,
                    activityDetailBinding.detailImage,
                    activityDetailBinding.progressBarDetail,
                    R.color.black
                )
            } ?: run {
                it?.raw?.let { rawUrl ->
                    loadImage(
                        rawUrl,
                        activityDetailBinding.detailImage,
                        activityDetailBinding.progressBarDetail,
                        R.color.black
                    )
                }
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> onBackPressed()
        }
        return true
    }
}