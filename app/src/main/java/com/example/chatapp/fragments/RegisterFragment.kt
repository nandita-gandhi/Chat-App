package com.example.chatapp.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.chatapp.models.PojoUser
import com.example.chatapp.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.fragment_register.*
//fragment for registration of new user - signUp
class RegisterFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_register, container, false)

        //getting different views from layout
        //variables
        val register = view.findViewById<Button>(R.id.btnRegister)
        val haveAccount = view.findViewById<TextView>(R.id.tvHaveAccount)
        //click listeners
        register.setOnClickListener {
            performRegister()
        }
        haveAccount.setOnClickListener {
            fragmentManager!!.beginTransaction().replace(
                R.id.fragment_container,
                LoginFragment()
            )
                .commit()
        }
        return view
    }

    //authenticate user via firebase and them saving it to database @saveUserToFirebase
    private fun performRegister() {
        val email = etEmail.text.toString().trim()
        val password = etPass.text.toString().trim()

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(requireContext(), "Invalid Email or Password", Toast.LENGTH_SHORT).show()
            return
        }
        //Firebase authentication to create user using email and password
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (!it.isSuccessful) {
                    return@addOnCompleteListener
                }
                Log.d("Main", "Successful ${it.result?.user?.uid} : ${it.result?.user?.email}")
                saveUserToFirebase()
                fragmentManager!!.beginTransaction().replace(
                    R.id.fragment_container,
                    ExistingUsersFragment()
                ).commit()
            }
            .addOnFailureListener {
                Toast.makeText(requireContext(), "Failed to create user : ${it.message}", Toast.LENGTH_SHORT).show()
            }
    }
    //save user to firebase
    private fun saveUserToFirebase() {
        val uid = FirebaseAuth.getInstance().uid!!
        val ref = FirebaseDatabase.getInstance().getReference("/users/$uid")
        val user =
            PojoUser(
                uid,
                etUsername.text.toString().trim()
            )
        ref.setValue(user)
            .addOnSuccessListener {
                Log.d("success","successful")
            }
            .addOnFailureListener {
                Log.d("print","${it.message}")
            }
    }
}