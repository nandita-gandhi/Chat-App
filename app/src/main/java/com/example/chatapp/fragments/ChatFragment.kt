package com.example.chatapp.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.chatapp.models.PojoChat
import com.example.chatapp.models.PojoUser
import com.example.chatapp.R
import com.example.chatapp.adapters.ChatOtherItem
import com.example.chatapp.adapters.ChatSelfItem
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.fragment_chat.*

//fragment for chat-log
class ChatFragment(private val user: PojoUser) : Fragment() {
    companion object {
        const val TAG = "ChatFragment"
    }

    //variables
    private val mAdapter = GroupAdapter<ViewHolder>()
    private val fromUId = FirebaseAuth.getInstance().uid!!
    private val toUId = user.uid

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        (activity as AppCompatActivity).supportActionBar?.title = user.userName
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_chat, container, false)
        //variables
        val recyclerView: RecyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.adapter = mAdapter

        //fetching previous messages if any
        listenForMessages()
        recyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        recyclerView.scrollToPosition(mAdapter.itemCount)
        val send: Button = view.findViewById(R.id.btnSend)
        send.setOnClickListener {
            //save message to firebase
            userMessageToFirebase()
            etMessage.text.clear()
        }
        return view
    }

    //saving user message to firebase
    private fun userMessageToFirebase() {
        val ref = FirebaseDatabase.getInstance().getReference("/user-messages/$fromUId,$toUId").push()
        val toRef = FirebaseDatabase.getInstance().getReference("/user-messages/$toUId,$fromUId").push()
        val chatMessage = PojoChat(
            etMessage.text.toString().trim(),
            FirebaseAuth.getInstance().uid!!,
            user.uid
        )
        ref.setValue(chatMessage)
            .addOnSuccessListener {
                Log.d("message", "$chatMessage")
            }
        toRef.setValue(chatMessage)
    }

    //fetching messages via database
    private fun listenForMessages() {
        val ref = FirebaseDatabase.getInstance().getReference("/user-messages/$fromUId,$toUId")
        ref.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val chat = snapshot.getValue(PojoChat::class.java)
                if (chat != null) {
                    if (chat.fromUId == FirebaseAuth.getInstance().uid) {
                        mAdapter.add(
                            ChatSelfItem(
                                chat.message
                            )
                        )
                    } else {
                        mAdapter.add(
                            ChatOtherItem(
                                chat.message
                            )
                        )
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
            }
        })

    }
}