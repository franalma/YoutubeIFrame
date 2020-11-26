package com.firebase.rest.youtubeweb

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.youtube.fragment.youtube_iframe_fragment.R
import kotlinx.android.synthetic.main.fragment_youtube_iframe.*

class YoutubeIframeFragment : Fragment() {

    private var player:YoutubeIframeApi? = null
    companion object{
        var width:Int = 0
        var height:Int = 0
        private var videoId:String? = null
        var autoSize = false;

        fun newInstance(videoId:String, width:Int = 450, height:Int = 240,
                        autoSize:Boolean  = true):YoutubeIframeFragment{
            YoutubeIframeFragment.videoId = videoId
            YoutubeIframeFragment.width = width
            YoutubeIframeFragment.height = height
            YoutubeIframeFragment.autoSize = autoSize;
            return YoutubeIframeFragment()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_youtube_iframe, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        player = YoutubeIframeApi(id_video_container, this)

        if (autoSize){
            player?.loadVideo(videoId!!)
        }else{
            player?.loadVideo(videoId!!, width, height)
        }

    }
}
