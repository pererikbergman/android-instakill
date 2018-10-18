package com.rakangsoftware.instakill.utils

import com.bumptech.glide.Glide
import android.databinding.BindingAdapter
import android.widget.ImageView


@BindingAdapter("imageUrl")
fun setImageUrl(view: ImageView, url: String) {
    Glide.with(view.context).load(url).into(view)
}