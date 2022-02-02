package pl.oczadly.baltic.lsc.android

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.asLiveData
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import kotlin.coroutines.CoroutineContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import pl.oczadly.baltic.lsc.ApiConfig
import pl.oczadly.baltic.lsc.UserState
import pl.oczadly.baltic.lsc.android.user.AndroidUserState
import pl.oczadly.baltic.lsc.android.view.ViewPageAdapter
import pl.oczadly.baltic.lsc.android.view.login.LoginView

class MainActivity : AppCompatActivity(), CoroutineScope {

    private val job = Job()

    override val coroutineContext: CoroutineContext
        get() = job

    companion object {
        var apiConfig = ApiConfig("balticlsc.iem.pw.edu.pl", 443, true)
        lateinit var state: UserState

        fun setUserState(userState: UserState) {
            state = userState
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))

        val pager: ViewPager = findViewById(R.id.pager)
        AndroidUserState(applicationContext!!).accessToken.asLiveData().observe(this, {
            if (it != null) {
                setUserState(UserState(it))
            }
        })

        val fragmentAdapter = ViewPageAdapter(supportFragmentManager)
        pager.adapter = fragmentAdapter

        val tabLayout: TabLayout = findViewById(R.id.tab_layout)
        tabLayout.setupWithViewPager(pager)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.action_bar_settings, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.logut_action -> {
            launch(Dispatchers.Main) {
                AndroidUserState(applicationContext!!).clear()

                Intent(baseContext, LoginView::class.java).also {
                    it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(it)
                }
            }
            true
        }
        else -> {
            super.onOptionsItemSelected(item)
        }
    }
}
