package com.example.a520.news

data class Dataset(
    var title: String,
    var link: String,
    var source: String,
    var date: String,
    var image: String,
    var selected: Boolean
)

data class LanguageData(var dataset: MutableList<Dataset>, var itemSelectedList: MutableList<Int>)
