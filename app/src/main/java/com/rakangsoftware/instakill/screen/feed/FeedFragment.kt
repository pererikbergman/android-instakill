package com.rakangsoftware.instakill.screen.feed


import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.rakangsoftware.instakill.R
import com.rakangsoftware.instakill.data.Post
import com.rakangsoftware.instakill.databinding.FeedFragmentBinding
import kotlinx.android.synthetic.main.feed_fragment.view.*


class FeedFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FeedFragmentBinding = DataBindingUtil.inflate(inflater, R.layout.feed_fragment, container, false)
        binding.executePendingBindings()

        val query = FirebaseFirestore.getInstance()
            .collection("feed")
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .limit(50)

        query.addSnapshotListener { querySnapshot, firebaseFirestoreException ->
            println("uncle :" + querySnapshot?.metadata?.isFromCache)
            println("uncle querySnapshot:" + querySnapshot?.documents?.size)
        }

        val options = FirestoreRecyclerOptions.Builder<Post>()
            .setQuery(query, Post::class.java)
            .setLifecycleOwner(this)
            .build()

        val adapter = object : FirestoreRecyclerAdapter<Post, PostViewHolder>(options) {
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
                return PostViewHolder.newInstance(parent)
            }

            protected override fun onBindViewHolder(holder: PostViewHolder, position: Int, model: Post) {
                holder.bind(model)
            }
        }

        val linearLayoutManager = LinearLayoutManager(container!!.context)
        adapter.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                linearLayoutManager.smoothScrollToPosition(binding.feedList, null, 0)
            }
        })

        binding.feedList.layoutManager = linearLayoutManager
        binding.feedList.feed_list.adapter = adapter

        return binding.root
    }

    companion object {
        fun newInstance() = FeedFragment()
    }
}
