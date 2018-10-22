package com.rakangsoftware.instakill.screen.feed

import android.view.ViewGroup
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.rakangsoftware.instakill.data.Post

class PostAdapter(options: FirestoreRecyclerOptions<Post>) : FirestoreRecyclerAdapter<Post, PostViewHolder>(options) {
    override fun onCreateViewHolder(viewGroup: ViewGroup, type: Int): PostViewHolder {
        return PostViewHolder.newInstance(viewGroup)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int, post: Post) {
        holder.bind(post)
    }
}