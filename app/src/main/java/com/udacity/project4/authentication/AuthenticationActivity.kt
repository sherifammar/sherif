package com.udacity.project4.authentication

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayoutStates.TAG
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.fragment.app.viewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.map
import androidx.navigation.NavController
import com.example.android.firebaseui_login_sample.LoginViewModel
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.udacity.project4.R
import com.udacity.project4.locationreminders.RemindersActivity

/**
 * This class should be the starting point of the app, It asks the users to sign in / register, and redirects the
 * signed in users to the RemindersActivity.
 */
class AuthenticationActivity : AppCompatActivity() {
    private val viewModel by viewModels<LoginViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Finish tothe user was authenticated, send him to RemindersActivity
        setContentView(R.layout.activity_authentication)



         fun observeAuthenticationState() {

            val authenticationState = AuthenticationActivity.FirebaseUserLiveData().map { user ->
                if (user != null) {
                    LoginViewModel.AuthenticationState.AUTHENTICATED
                    val authButton =findViewById<TextView>(R.id.authButton)
                    authButton.text = getString(R.string.logout_button_text)

                } else {
                    LoginViewModel.AuthenticationState.UNAUTHENTICATED
                    val authButton =findViewById<TextView>(R.id.authButton)
                    authButton.text = getString(R.string.login_button_text)
                    // click on butto tranfer to reminderactivity
                    authButton.setOnClickListener{

                        var i =Intent(this,RemindersActivity::class.java)
                       startActivity(i)
                    }}

                }


        }


    }



//    finish to  Implement the create account and sign in using FirebaseUI, use sign in using email and sign in using Google



    class FirebaseUserLiveData: LiveData<FirebaseUser?>() {

        private val firebaseAuth = FirebaseAuth.getInstance()



        private val authStateListener = FirebaseAuth.AuthStateListener { firebaseAuth ->
            value = firebaseAuth.currentUser
        }


        override fun onActive() {
            firebaseAuth.addAuthStateListener(authStateListener)
        }


        override fun onInactive() {
            firebaseAuth.removeAuthStateListener(authStateListener)
        }}}







