package com.sandeep.womenincloudrvce.entity

import java.io.Serializable


data class Project(
    var id:Int,
    var company_name: String,
    var opening: String,
    var description: String,
    var requirements: String,
    var manager: String,
    var start_date: String,
    var end_date: String,
    var resources: String,
) :
    Serializable