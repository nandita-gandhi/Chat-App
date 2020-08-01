package com.example.chatapp.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.chatapp.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_login.*

//fragment for user login with registered email
class LoginFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_login, container, false)

        //getting views from layout
        //variables
        val login = view.findViewById<Button>(R.id.btnLogin)
        //click listeners
        login.setOnClickListener {
            performLogin()
        }
        return view
    }
    //performing signIn via firebase authentication
    private fun performLogin() {
        val email = etEmail.text.toString().trim()
        val password = etPass.text.toString().trim()

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(requireContext(), "Invalid Email or Password", Toast.LENGTH_SHORT).show()
            return
        }
        //Firebase authentication to signIn user using email and password
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (!it.isSuccessful) {
                    return@addOnCompleteListener
                }
                Log.d("Main", "Successful ${it.result?.user?.uid} : ${it.result?.user?.email}")
                fragmentManager!!.beginTransaction().replace(
                    R.id.fragment_container,
                    ExistingUsersFragment()
                ).commit()
            }
            .addOnFailureListener {
                Toast.makeText(requireContext(), "Failed to create user : ${it.message}", Toast.LENGTH_SHORT).show()
            }
    }
}