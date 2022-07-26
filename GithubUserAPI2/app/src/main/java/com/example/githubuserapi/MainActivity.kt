package com.example.githubuserapi

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.KeyEvent
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.CompoundButton
import androidx.appcompat.app.AppCompatDelegate
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubuserapi.databinding.ActivityMainBinding
import com.google.android.material.switchmaterial.SwitchMaterial

class MainActivity : AppCompatActivity() {
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainModel
    private lateinit var adapter: UserAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = UserAdapter()
        adapter.notifyDataSetChanged()

        adapter.setOnItemItemCallback(object :UserAdapter.OnItemClickCallback{
            override fun onItemClikced(data: User) {
                Intent (this@MainActivity, DetailUserActivity::class.java).also {
                    it.putExtra(DetailUserActivity.EXTRA_USERNAME, data.login)
                    it.putExtra(DetailUserActivity.EXTRA_ID, data.id)
                    it.putExtra(DetailUserActivity.EXTRA_URL, data.avatar_url)
                    startActivity(it)
                }
            }
        })

        viewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())[MainModel::class.java]

        binding.apply {
            recyclerView.layoutManager = LinearLayoutManager(this@MainActivity)
            recyclerView.setHasFixedSize(true)
            recyclerView.adapter = adapter

            btnSearch.setOnClickListener{
                searchUser()
            }

            etQuery.setOnKeyListener{ _, keyCode, event ->
                if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER){
                    searchUser()
                    return@setOnKeyListener true
                }
                return@setOnKeyListener false
            }

            viewModel.getSearchUser().observe(this@MainActivity, {
                if (it!=null){
                    adapter.setList(it)
                    showLoading(false)
                }
            })
        }
    }

    private fun searchUser(){
        binding.apply {
            val query = etQuery.text.toString()
            if (query.isEmpty()) return
            showLoading(true)
            viewModel.setSeacrhUsers(query)
        }
    }

    private fun showLoading(state : Boolean){
        if (state){
            binding.SHOWPROGRESS.visibility = View.VISIBLE
        } else {
            binding.SHOWPROGRESS.visibility = View.GONE
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.option_menu, menu)
        val sw = menu.findItem(R.id.myswitch).actionView.findViewById<SwitchMaterial>(R.id.switch_theme)
        val pref = SettingPreferences.getInstance(dataStore)
        val mainViewModel = ViewModelProvider(this, ViewModelFactory(pref))[MainViewModel::class.java]
        mainViewModel.getThemeSettings().observe(this,
            { isDarkModeActive: Boolean ->
                if (isDarkModeActive) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                    sw.isChecked = true
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                    sw.isChecked = false
                }
            })

        sw.setOnCheckedChangeListener { _: CompoundButton?, isChecked: Boolean ->
            mainViewModel.saveThemeSetting(isChecked)
        }
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId){
            R.id.favorite_menu ->{
                Intent(this, FavoriteActivity::class.java).also {
                    startActivity(it)
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }
}