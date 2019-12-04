package com.work.asinghi.myapplication

data class SearchList(val hits: Array<Search>)

data class Search(val title: String, val created_at: String, val url: String)