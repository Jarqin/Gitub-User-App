package com.example.githubuserapi

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.githubuserapi.databinding.UserItemBinding

class UserAdapter : RecyclerView.Adapter<UserAdapter.UserViewHolder>() {
    private val list = ArrayList<User>()

    private var onItemClickCallback: OnItemClickCallback? = null

    fun setOnItemItemCallback (onItemClickCallback: OnItemClickCallback){
        this.onItemClickCallback = onItemClickCallback
    }

    fun setList(users :ArrayList<User>){
        list.clear()
        list.addAll(users)
        notifyDataSetChanged()
    }
    inner class UserViewHolder(private val binding: UserItemBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(user: User){
            binding.root.setOnClickListener{
                onItemClickCallback?.onItemClikced(user)
            }

            binding.apply {
                Glide.with(itemView)
                    .load(user.avatar_url)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .centerCrop()
                    .into(imageUser)
                ivUser.text = user.login
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view = UserItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return UserViewHolder((view))
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int = list.size

    interface OnItemClickCallback{
        fun onItemClikced(data: User)
    }
}