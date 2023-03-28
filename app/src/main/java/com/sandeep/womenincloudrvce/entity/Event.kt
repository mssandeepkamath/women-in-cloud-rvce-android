package com.sandeep.womenincloudrvce.entity

import java.io.Serializable

data class Event(
    var id:Int,
    var name: String,
    var description: String,
    var start_date: String,
    var end_date: String,
    var type: String,
    var location: String,
    var poster: String,
) : Serializable
