package com.example.a520

// For Newsdata.io

data class NewsResponse(var status: String, var totalResults: Int, var results: Array<ResultsArray>, var nextPage: Int)
data class ResultsArray(var title: String, var link: String, var keywords: Array<String>?,
                        var creator: Array<String>?, var video_url: String?, var description: String,
                        var content: String?, var pubDate: String, var image_url: String?,
                        var source_id: String, var country: Array<String>, var category: Array<String>?,
                        var language: String)

/*
For Newscatcher

data class NewsResponse(var status: String, var total_hits: Int, var page: Int,
                        var total_pages: Int, var page_size: Int, var articles: Array<Article>,
                        var user_input: UserInput)

data class Article(var summary: String, var country: String, var author: String?,
                   var link: String, var language: String, var media: String,
                   var title: String, var media_content: Array<String>, var clean_url: String,
                   var rights: String, var rank: Int, var topic: String,
                   var published_date: String, var _id: String, var _score: Double)

data class UserInput(var q: String, var search_in: String, var lang: String,
                     var ranked_only: String, var sort_by: String, var from: String,
                     var page: Int, var size: Int, var media: String)
 */
