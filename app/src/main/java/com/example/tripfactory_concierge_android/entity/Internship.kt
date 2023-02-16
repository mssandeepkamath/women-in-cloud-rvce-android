package com.example.tripfactory_concierge_android.entity

import java.io.Serializable


data class Internship(
    var id:Int,
    var company_name: String,
    var opening: String,
    var role_description: String,
    var requirements: String,
    var manager: String,
    var start_date: String,
    var end_date: String,
    var mode: String,
    var type: String,
    var location: String,
) : Serializable
