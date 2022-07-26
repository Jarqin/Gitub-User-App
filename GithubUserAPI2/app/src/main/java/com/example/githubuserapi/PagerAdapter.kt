package com.example.githubuserapi

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class PagerAdapter(fm: AppCompatActivity, data: Bundle) : FragmentStateAdapter(fm) {

    private var fragmentBundle: Bundle = data

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        var fragment: Fragment? = null
        when (position){
            0-> fragment = FollowersFragment()
            1-> fragment = FollowingFragment()
        }
        fragment?.arguments = this.fragmentBundle
        return fragment as Fragment
    }
}