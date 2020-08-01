package com.example.chatapp.fragments

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.chatapp.adapters.ExistingUsersAdapter
import com.example.chatapp.models.PojoUser
import com.example.chatapp.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

//fragment for listing existing users on app
class ExistingUsersFragment : Fragment(),
    ExistingUsersAdapter.UserOnClick {

    //variables
    private val mAdapter by lazy {
        ExistingUsersAdapter(
            this
        )
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        (activity as AppCompatActivity).supportActionBar?.title = "Users"

        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_messages, container, false)
        //for menu icons to display
        setHasOptionsMenu(true)

        //getting views from layout
        val recyclerView: RecyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, true)
        //fetching existing users to load in recycler view
        fetchUsers()
        recyclerView.adapter = mAdapter
        return view
    }
    //fetching users from database
    private fun fetchUsers() {
        val ref = FirebaseDatabase.getInstance().getReference("/users")
        val list = mutableListOf<PojoUser>()
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                snapshot.children.forEach {
                    val user = it.getValue(PojoUser::class.java)!!
                    //checking self - and to not list
                    if (user.uid != FirebaseAuth.getInstance().uid) {
                        list.add(user)
                    }
                    Log.d("user", "$user")
                }
                mAdapter.getUsers(list)
            }
        })
    }

    //@overriding this method to display signOut icon on screen
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.fragment_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    //method for onClick listener on menu views
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.btnSignOut) {
            FirebaseAuth.getInstance().signOut()
            fragmentManager!!.beginTransaction().replace(R.id.fragment_container, RegisterFragment()).commit()
        }
        return super.onOptionsItemSelected(item)
    }

    //implementing interface for specific user OnClick
    override fun onClick(user: PojoUser) {
        fragmentManager!!.beginTransaction().replace(
            R.id.fragment_container,
            ChatFragment(user)
        )
            .addToBackStack(null).commit()
    }
}