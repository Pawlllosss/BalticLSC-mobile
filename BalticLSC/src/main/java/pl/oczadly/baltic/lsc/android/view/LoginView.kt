package pl.oczadly.baltic.lsc.android.view

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import kotlin.coroutines.CoroutineContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import pl.oczadly.baltic.lsc.android.AndroidUserState
import pl.oczadly.baltic.lsc.android.MainActivity
import pl.oczadly.baltic.lsc.android.R
import pl.oczadly.baltic.lsc.lazyPromise
import pl.oczadly.baltic.lsc.login.LoginApi

class LoginView : AppCompatActivity(), CoroutineScope {

    private val job = Job()

    private val loginApi = LoginApi()
    private val login by lazyPromise {
        withContext(Dispatchers.IO) {
            try {
                return@withContext loginApi.login().data
            } catch (e: Exception) {
                e.printStackTrace()
                // TODO: fix toast there or use either?
//                Toast.makeText(activity, "Error when fetching api", Toast.LENGTH_LONG).show()
                return@withContext null
            }
        }
    }

    override val coroutineContext: CoroutineContext
        get() = job

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.login_view)

        val button: Button = findViewById(R.id.button_sign_in)
        button.setOnClickListener {
            loginUser()
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }


    private fun loginUser() {
        val email = findViewById<EditText>(R.id.edit_text_email).text.toString().trim()
        val password = findViewById<EditText>(R.id.edit_text_password).text.toString().trim()
        val userState = AndroidUserState(applicationContext)

        // TODO: experiment with lifecycleScope
        launch(Dispatchers.Main) {
            try {
                val authResponse = lazyPromise {
                    withContext(Dispatchers.IO) {
                        try {
                            return@withContext loginApi.login(email, password).data
                        } catch (e: Exception) {
                            e.printStackTrace()
                            // TODO: fix toast there or use either?
//                          Toast.makeText(activity, "Error when fetching api", Toast.LENGTH_LONG).show()
                            return@withContext null
                        }
                    }
                }.value.await()!!

                userState.saveAccessToken(authResponse.token)

                Intent(baseContext, MainActivity::class.java).also {
                    it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(it)
                }

                // TODO: update view or something?
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}