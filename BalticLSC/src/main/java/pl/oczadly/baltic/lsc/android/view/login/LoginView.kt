package pl.oczadly.baltic.lsc.android.view.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.asLiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import pl.oczadly.baltic.lsc.android.MainActivity
import pl.oczadly.baltic.lsc.android.R
import pl.oczadly.baltic.lsc.android.user.AndroidUserState
import pl.oczadly.baltic.lsc.lazyPromise
import pl.oczadly.baltic.lsc.login.LoginApi
import pl.oczadly.baltic.lsc.util.JWTVerifier
import kotlin.coroutines.CoroutineContext

class LoginView : AppCompatActivity(), CoroutineScope {

    private val job = Job()

    private val loginApi = LoginApi()
    private val tokenVerifier = JWTVerifier()

    override val coroutineContext: CoroutineContext
        get() = job

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val userState = AndroidUserState(applicationContext)

        setContentView(R.layout.login_view)

        val button: Button = findViewById(R.id.button_sign_in)
        button.setOnClickListener {
            Log.i(this.localClassName, "Clicked login button: ${System.nanoTime()}")
            loginUser(userState)
        }

        userState.accessToken.asLiveData().observe(this, { token ->
            if (token != null && tokenVerifier.isTokenValid(token)) {
                Intent(baseContext, MainActivity::class.java).also {
                    it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(it)
                }
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }

    private fun loginUser(userState: AndroidUserState) {
        val email = findViewById<EditText>(R.id.edit_text_email).text.toString().trim()
        val password = findViewById<EditText>(R.id.edit_text_password).text.toString().trim()

        launch(Dispatchers.Main) {
            try {
                lazyPromise {
                    withContext(Dispatchers.IO) {
                        try {
                            return@withContext loginApi.login(email, password).data
                        } catch (e: Exception) {
                            e.printStackTrace()
                            return@withContext null
                        }
                    }
                }.value.await()?.let { userState.saveAccessToken(it.token) }
                    ?: displayUnableToSignIn()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun displayUnableToSignIn() {
        Toast.makeText(
            applicationContext,
            "Unable to sign in",
            Toast.LENGTH_SHORT
        ).show()
    }
}