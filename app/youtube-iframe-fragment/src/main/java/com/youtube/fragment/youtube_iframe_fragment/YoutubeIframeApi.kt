package com.firebase.rest.youtubeweb

import android.app.Activity
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import java.lang.ref.WeakReference

class YoutubeIframeApi (private val webview:WebView, private val fragment: YoutubeIframeFragment){

    private fun createWebContent2(videoId: String):String{
        return """
                <!DOCTYPE html>
            <html>
              <body > 
                <style>
                    .container {
                        position: relative;
                        width: 100%;
                        height: 0;
                        padding-bottom: 56.25%;
                       
                    }
                    .video {
                        position: absolute;
                        top: 0;
                        left: 0;
                        width: 100%;
                        height: 100%;
                    }
                </style>
                <script>
                    function play(){
                        var myvideo = document.getElementsByTagName('iframe')[0]; 
                        myvideo.play();
                    }
                </script>
                <div class="container">
                    <iframe class="video" src="https://www.youtube.com/embed/$videoId?rel=0&amp;controls=1&amp;showinfo=1&amp;autoplay=1" 
                    frameborder="0"  encrypted-media allowfullscreen allow="accelerometer; autoplay; encrypted-media; gyroscope; picture-in-picture"
                    onload="play();"/>
            </div>
            </body>
        </html> 
        """
    }
    private fun createWebContent(videoId:String, width:Int, height:Int) :String{

        return """
            <!DOCTYPE html>
            <html>
              <body > 

                <div id="player"></div> 
                <script>
                var iframe;
                 var $ = document.querySelector.bind(document);
                  var tag = document.createElement('script'); 
                  tag.src = "https://www.youtube.com/iframe_api";
                  var firstScriptTag = document.getElementsByTagName('script')[0];
                  firstScriptTag.parentNode.insertBefore(tag, firstScriptTag);
                  var player;
                  function onYouTubeIframeAPIReady() {
                    player = new YT.Player('player', {
                      height: '$height',
                      width: '$width',
                      videoId: '$videoId',
                      events: {
                        'onReady': onPlayerReady,
                        'onStateChange': onPlayerStateChange
                      }
                    });
                  }
                  
                  function setupFullScreen(){
                   var requestFullScreen = iframe.requestFullScreen || iframe.mozRequestFullScreen || iframe.webkitRequestFullScreen;
                    if (requestFullScreen) {
                        requestFullScreen.bind(iframe)();
                        }
                  }
                  
                  function onPlayerReady(event) {
                    iframe =  document.getElementById('player');
                    event.target.playVideo();
                    
                  }
                  function onPlayerStateChange(event){
                   
                    
                  }
                </script>
              </body>
            </html>
                        """

//        return """<!DOCTYPE html>
//        <html>
//        <body>
//        <iframe>
//        <video autoplay muted="muted">
//        <source src="https://www.youtube.com/embed/$videoId" type="video/mp4>
//        </video>
//        <iframe>
//        </body>
//        </html>
//        """
    }


    fun loadVideo(videoId:String, width: Int, height: Int){
        webview.settings.javaScriptEnabled = true
        webview.settings.mediaPlaybackRequiresUserGesture = false
        webview.webChromeClient = ChromeClient(fragment.activity?.window!!, fragment.activity!!)
        webview.webViewClient = WebClient()

        webview.loadData(createWebContent(videoId, width,height),"",
            charset("UTF-8").toString())
    }

    fun loadVideo(videoId:String){
        webview.settings.javaScriptEnabled = true
        webview.settings.mediaPlaybackRequiresUserGesture = false
        webview.webChromeClient = ChromeClient(fragment.activity?.window!!, fragment.activity!!)
        webview.webViewClient = WebClient()

        webview.loadData(createWebContent2(videoId),"",
            charset("UTF-8").toString())
    }

    internal class ChromeClient (private val window:Window, private val activity: Activity): WebChromeClient() {
        private val activityRef = WeakReference(activity)
        private var customView: View? = null
        private var customViewCallback: CustomViewCallback? = null
        private var originalOrientation = 0
        private var originalSystemUiVisibility = 0

        override fun onProgressChanged(view: WebView, progress: Int) {

        }

        override fun getDefaultVideoPoster(): Bitmap? {
            return activityRef.get()?.run {
                BitmapFactory.decodeResource(applicationContext.resources, 2130837573)
            }
        }

        override fun onHideCustomView() {
            activityRef.get()?.run {
                (window.decorView as ViewGroup).removeView(customView)
                customView = null
                window.decorView.systemUiVisibility = originalSystemUiVisibility
                requestedOrientation = originalOrientation
            }
            customViewCallback?.onCustomViewHidden()
            customViewCallback = null
        }

        override fun onShowCustomView(view: View?, viewCallback: CustomViewCallback?) {
            if (customView != null) {
                onHideCustomView()
                return
            }
            customView = view
            activityRef.get()?.run {
                originalSystemUiVisibility = window.decorView.systemUiVisibility
                originalOrientation = requestedOrientation
                customViewCallback = viewCallback
                (window.decorView as ViewGroup).addView(
                    customView,
                    ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                    )
                )
                window.decorView.systemUiVisibility = 3846
            }
        }
    }

    internal class WebClient: WebViewClient(){
    }


}