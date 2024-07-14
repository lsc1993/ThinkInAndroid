package com.ls.media

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class MediaActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_media)
        setSupportActionBar(findViewById(R.id.toolbar))
    }
}