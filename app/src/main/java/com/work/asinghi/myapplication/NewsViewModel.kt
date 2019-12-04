package com.work.asinghi.myapplication

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers


class NewsViewModel : ViewModel(){

    private val _messageList = MutableLiveData<String>()
    val messageList: LiveData<String> get() = _messageList

    private val _searchList = MutableLiveData<Search>()
    val searchList: LiveData<Search> get() = _searchList

    fun getNews(search: String, page: Int) {
        SearchService.getSearchList(search,  page)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { result ->
                _messageList.postValue(result)
            }
    }

    fun setSearchList(list: ArrayList<Search>, int: Int) {
        _searchList.value = list[int]
    }
}