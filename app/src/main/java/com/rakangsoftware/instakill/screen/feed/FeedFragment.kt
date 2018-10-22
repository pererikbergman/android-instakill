package com.rakangsoftware.instakill.screen.feed


import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot

import com.rakangsoftware.instakill.R
import com.rakangsoftware.instakill.data.Post
import kotlinx.android.synthetic.main.feed_fragment.*

class FeedFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.feed_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val query = FirebaseFirestore.getInstance()
            .collection("feed")
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .limit(50)

        val option = FirestoreRecyclerOptions.Builder<Post>()
            .setQuery(query, Post::class.java)
            .setLifecycleOwner(this)
            .build()

        feed_list.layoutManager = LinearLayoutManager(view.context)
        feed_list.adapter = PostAdapter(option)

    }

    companion object {
        @JvmStatic
        fun newInstance() = FeedFragment()
    }
}
