package pl.oczadly.baltic.lsc.android

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.asLiveData
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import pl.oczadly.baltic.lsc.UserState
import pl.oczadly.baltic.lsc.android.view.ViewPageAdapter

class MainActivity : AppCompatActivity() {

    companion object {

        lateinit var state: UserState

        fun setUserState(userState: UserState) {
            state = userState
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val pager: ViewPager = findViewById(R.id.pager)
        AndroidUserState(applicationContext!!).accessToken.asLiveData().observe(this, {
            if (it != null) {
                setUserState(UserState(it))
            }
        })

        val fragmentAdapter = ViewPageAdapter(supportFragmentManager, applicationContext!!)
        pager.adapter = fragmentAdapter

        val tabLayout: TabLayout = findViewById(R.id.tab_layout)
        tabLayout.setupWithViewPager(pager)
    }

}
