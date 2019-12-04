package com.work.asinghi.myapplication

import androidx.fragment.app.Fragment
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.GsonBuilder
import com.squareup.okhttp.OkHttpClient
import com.squareup.okhttp.Request
import com.squareup.okhttp.Response
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_news.*
import java.io.IOException


class NewsActivityFragment : Fragment() {

    lateinit var recyclerView: RecyclerView
    lateinit var button: Button

    var searchText: EditText? = null
    private var previousTotal = 0
    private var loading = true
    private val visibleThreshold = 5
    var firstVisibleItem: Int = 0
    var visibleItemCount: Int = 0
    var totalItemCount: Int = 0
    var page: Int = 0

    var search: ArrayList<Search> = ArrayList()
    private lateinit var viewModel: NewsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_news, container, false)
        recyclerView = view.findViewById(R.id.recyclerView)
        searchText = view.findViewById(R.id.searchText)
        button = view.findViewById(R.id.button)

        activity?.let {
            viewModel = ViewModelProviders.of(it).get(NewsViewModel::class.java)
        }

        val mLayoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)

        recyclerView.layoutManager = mLayoutManager



        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (dy > 0) {
                    visibleItemCount = mLayoutManager.getChildCount();
                    totalItemCount = mLayoutManager.getItemCount();
                    firstVisibleItem = mLayoutManager.findFirstVisibleItemPosition();

                    if (loading) {
                        if (totalItemCount > previousTotal) {
                            loading = false;
                            previousTotal = totalItemCount;
                        }
                    }
                    if (!loading && (totalItemCount - visibleItemCount)
                        <= (firstVisibleItem + visibleThreshold)
                    ) {

                        initSearchList()
                        loading = true;
                    }
                }
            }
        })

        button.setOnClickListener {
            page = 0
            initSearchList()
        }

        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewLifecycleOwner.let { lifecycleOwner ->
            viewModel.messageList.observe(lifecycleOwner, androidx.lifecycle.Observer {
                processFinish(it)
            })
        }
    }

    override fun onResume() {

        super.onResume()
        activity?.title = "News"
    }

    fun initSearchList() {
        page++
        val searchText = searchText?.text ?: ""
        when {
            searchText.isNotEmpty() -> viewModel.getNews(searchText.toString(), page)
        }

    }

    fun processFinish(output: String) {

        val builder = GsonBuilder()
        val gson = builder.create()

        val searchList: SearchList = gson.fromJson(output, SearchList::class.java)
        search = ArrayList()
        search.addAll(searchList.hits)



        recyclerView.adapter = CustomAdapter(search) {

            viewModel.setSearchList(search, it)
            fragmentManager?.beginTransaction()
                ?.replace(R.id.fragment, NewsDetailFragment.newInstance())?.addToBackStack(null)
                ?.commit()
        }


    }

    companion object {
        private val TAG = NewsActivityFragment::class.java.simpleName

        fun newInstance(): NewsActivityFragment {
            return NewsActivityFragment()
        }
    }
}
