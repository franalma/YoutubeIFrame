package com.youtube.fragment.iframe

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.firebase.rest.youtubeweb.YoutubeIframeFragment
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class YoutubePlayerActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_youtube_player)
        GlobalScope.launch {
            delay(1000)
            runOnUiThread {
                supportFragmentManager.beginTransaction().replace(R.id.id_container,
                YoutubeIframeFragment.newInstance("M7lc1UVf-VE")).commit()
            }
        }
    }
}
