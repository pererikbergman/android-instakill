package com.rakangsoftware.instakill.data

import java.sql.Timestamp

data class Post(var name: String = "", var url: String = "", var data: Timestamp? = null)