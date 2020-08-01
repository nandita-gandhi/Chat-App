package com.example.chatapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.chatapp.models.PojoUser
import com.example.chatapp.R
import kotlinx.android.synthetic.main.row_users_list_rv.view.*

class ExistingUsersAdapter(val userOnClick: UserOnClick) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var userList = mutableListOf<PojoUser>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return UsersViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.row_users_list_rv, parent, false)
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is UsersViewHolder -> {
                holder.bind(userList[position])
            }
        }
    }

    fun getUsers(list: List<PojoUser>) {
        userList.clear()
        userList.addAll(list)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    inner class UsersViewHolder constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
        init {
            itemView.setOnClickListener {
                userOnClick.onClick(userList[adapterPosition])
            }
        }

        fun bind(user: PojoUser) {
            itemView.apply {
                tvUser.text = user.userName
            }
        }
    }

    interface UserOnClick {
        fun onClick(user: PojoUser)
    }
}