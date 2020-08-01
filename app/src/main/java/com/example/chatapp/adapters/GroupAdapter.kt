package com.example.chatapp.adapters

import com.example.chatapp.R
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.row_other_messages_rv.view.*
import kotlinx.android.synthetic.main.row_self_messages_rv.view.*

//for self messaging
class ChatSelfItem(private val message: String) : Item<ViewHolder>() {
    override fun getLayout(): Int {
        return R.layout.row_self_messages_rv
    }

    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.tvSelfMsg.text = message
    }
}

//for others messaging
class ChatOtherItem(private val message: String) : Item<ViewHolder>() {
    override fun getLayout(): Int {
        return R.layout.row_other_messages_rv
    }

    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.tvOthersMsg.text = message
    }
}