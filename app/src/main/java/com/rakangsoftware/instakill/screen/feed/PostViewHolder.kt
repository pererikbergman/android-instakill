package com.rakangsoftware.instakill.screen.feed

import android.databinding.DataBindingUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.rakangsoftware.instakill.R
import com.rakangsoftware.instakill.data.Post
import com.rakangsoftware.instakill.databinding.PostItemBinding

class PostViewHolder(val binding: PostItemBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(post: Post) {
       binding.post = post
    }

    companion object {
        fun newInstance(viewGroup: ViewGroup): PostViewHolder {
            return PostViewHolder(
                DataBindingUtil.inflate(
                    LayoutInflater.from(viewGroup.context),
                    R.layout.post_item, viewGroup, false
                )
            )
        }

    }
}