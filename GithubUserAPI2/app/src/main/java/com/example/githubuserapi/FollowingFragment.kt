package com.example.githubuserapi

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubuserapi.databinding.FragmentFollowBinding

class FollowingFragment: Fragment(R.layout.fragment_follow) {

    private var _binding : FragmentFollowBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: FollowingViewModel
    private lateinit var adapter: UserAdapter
    private lateinit var username: String

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val args = arguments
        username = args?.getString(DetailUserActivity.EXTRA_USERNAME).toString()
        _binding = FragmentFollowBinding.bind(view)

        adapter = UserAdapter()
        adapter.notifyDataSetChanged()

        binding.apply {
            recyclerView.setHasFixedSize(true)
            recyclerView.layoutManager = LinearLayoutManager(activity)
            recyclerView.adapter = adapter
        }
        showLoading(true)
        viewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())[FollowingViewModel::class.java]
        viewModel.setListFollowing(username)
        viewModel.getListFollowing().observe(viewLifecycleOwner, {
            if (it!= null)
                adapter.setList(it)
                showLoading(false)

        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun showLoading(state : Boolean){
        if (state){
            binding.SHOWPROGRESS.visibility = View.VISIBLE
        } else {
            binding.SHOWPROGRESS.visibility = View.GONE
        }
    }
}