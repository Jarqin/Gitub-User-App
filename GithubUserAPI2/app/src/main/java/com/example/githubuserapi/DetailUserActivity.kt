package com.example.githubuserapi

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.annotation.StringRes
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.githubuserapi.databinding.ActivityDetailUserBinding
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DetailUserActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailUserBinding
    private lateinit var viewModel: DetailUserMainModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val username = intent.getStringExtra(EXTRA_USERNAME)
        val id = intent.getIntExtra(EXTRA_ID, 0)
        val avatar = intent.getStringExtra(EXTRA_URL)

        val bundle = Bundle()
        bundle.putString(EXTRA_USERNAME, username)

        viewModel = ViewModelProvider(this)[DetailUserMainModel::class.java]

        viewModel.setUserDetail(username.toString())
        viewModel.getUserDetail().observe(this, {
            if (it != null) binding.apply {
                tvUsername.text = it.name
                tvName.text = it.login
                val followers = getString(R.string.followers_string)
                val output = followers.format(it.followers)
                tvFollowers.text = output
                val following = getString(R.string.following_string)
                val output2 = following.format(it.following)
                tvFollowing.text = output2
                tvCompany.text = it.company
                tvLocation.text = it.location
                val repository = getString(R.string.repository_string)
                val output3 = repository.format(it.public_repos)
                tvRepository.text = output3
                Glide.with(this@DetailUserActivity)
                    .load(it.avatar_url)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .centerCrop()
                    .into(ivProfile)
            }
        })

        var checked = false
        CoroutineScope(Dispatchers.IO).launch {
            val count = viewModel.checkUser(id)
            withContext(Dispatchers.Main){
                if (count != null){
                    if (count>0){
                        binding.favoriteToggle.isChecked = true
                        checked = true
                    } else {
                        binding.favoriteToggle.isChecked = false
                        checked = false
                    }
                }
            }
        }

        binding.favoriteToggle.setOnClickListener{
            checked = !checked
            if (checked){
                if (username != null) {
                    if (avatar != null) {
                        viewModel.addToFavorite(username, id, avatar)
                    }
                }
            } else {
                viewModel.removeFromFavorite(id)
            }
            binding.favoriteToggle.isChecked = checked
        }

        binding.btnBack.setOnClickListener {
            val moveIntent = Intent(this@DetailUserActivity, MainActivity::class.java)
            startActivity(moveIntent)
        }

        val sectionPagerAdapter = PagerAdapter (this, bundle)
        binding.apply {
            viewPager.adapter = sectionPagerAdapter
            TabLayoutMediator(tabs, viewPager) { tab, position ->
                tab.text = resources.getString(TAB_TITLES[position])
            }.attach()
            supportActionBar?.elevation = 0f
        }
    }

    companion object{
        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.tab_1,
            R.string.tab_2
        )
        const val EXTRA_USERNAME = "Extra"
        const val EXTRA_ID = "extra_id"
        const val EXTRA_URL = "extra_url"
    }
}