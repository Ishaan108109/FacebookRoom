package com.example.facebookroom


import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.facebook.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import com.facebook.CallbackManager

class MainActivity : AppCompatActivity() {

    private lateinit var userDatabase: UserDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        userDatabase = UserDatabase.getDatabase(this)

        val loginButton = findViewById<com.facebook.login.widget.LoginButton>(R.id.login_button)
        loginButton.setPermissions("email", "public_profile")
        val callbackManager = CallbackManager.Factory.create()


        loginButton.registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
            override fun onSuccess(result: LoginResult) {
                Log.d(TAG, "onSuccess: ${result.accessToken}")

                getUserProfile(result.accessToken)
            }

            override fun onCancel() {
                Log.d(TAG, "onCancel")
            }



            override fun onError(error: FacebookException) {
                Log.e(TAG, "onError: ${error.message}", error)
            }
        })

        val logoutButton = findViewById<Button>(R.id.logout_button)
        logoutButton.setOnClickListener {
            LoginManager.getInstance().logOut()
            Log.d(TAG, "Logged out")
        }
    }

    private fun getUserProfile(token: AccessToken) {
        val request = GraphRequest.newMeRequest(token) { jsonObject, _ ->
            Log.d(TAG, "getUserProfile: $jsonObject")

            val id = jsonObject?.optString("id")
            val name = jsonObject?.optString("name")
            val email = jsonObject?.optString("email")
            val picture = jsonObject?.optJSONObject("picture")?.optJSONObject("data")?.optString("url")

            val user = id?.let { User(it, name, email, picture) }
            lifecycleScope.launch(Dispatchers.IO) {
                if (user != null) {
                    userDatabase.userDao().insert(user)
                }
                Log.d(TAG, "User saved to database")
            }

        }

        val parameters = Bundle()
        parameters.putString("fields", "id,name,email,picture.type(large)")
        request.parameters = parameters
        request.executeAsync()
    }

    companion object {
        private const val TAG = "MainActivity"
    }
}