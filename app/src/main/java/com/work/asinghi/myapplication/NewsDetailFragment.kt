package com.work.asinghi.myapplication

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import android.webkit.WebViewClient

class NewsDetailFragment: Fragment() {


    lateinit var webView: WebView
    lateinit var viewModel: NewsViewModel

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.news_detail, container, false)
        webView = view.findViewById(R.id.webView)
        webView.webViewClient = WebViewClient()
        webView.settings.javaScriptEnabled = true

        activity?.let {
            viewModel = ViewModelProviders.of(it).get(NewsViewModel::class.java)
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        webView.loadUrl("https://www.lowes.com")
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewLifecycleOwner.let { lifecycleOwner ->
            viewModel.searchList.observe(lifecycleOwner, Observer {
                webView.loadUrl(it.url.toString())
            })
        }
    }

    override fun onResume() {

        super.onResume()
        webView.onResume()
        activity?.title = "Detail"
    }

    override fun onPause() {
        super.onPause()
        webView.onPause()
    }

    companion object {
        private val TAG = NewsDetailFragment::class.java.simpleName

        fun newInstance(): NewsDetailFragment {
            return NewsDetailFragment()
        }
    }
}