package com.rakangsoftware.instakill.data

import com.google.firebase.Timestamp

data class Post(var name: String = "", var url: String = "", var date: Timestamp? = null)