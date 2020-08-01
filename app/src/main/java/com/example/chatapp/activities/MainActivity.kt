package com.example.chatapp.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.chatapp.R
import com.example.chatapp.fragments.ExistingUsersFragment
import com.example.chatapp.fragments.RegisterFragment
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val uID = FirebaseAuth.getInstance().uid
        if (uID == null) {
            supportFragmentManager.beginTransaction()
                .add(
                    R.id.fragment_container,
                    RegisterFragment()
                ).commit()
        } else {
            supportFragmentManager.beginTransaction()
                .add(
                    R.id.fragment_container,
                    ExistingUsersFragment()
                ).commit()
        }
    }
}