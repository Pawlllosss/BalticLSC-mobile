package pl.oczadly.baltic.lsc.android

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import pl.oczadly.baltic.lsc.android.view.ViewPageAdapter

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val pager: ViewPager = findViewById(R.id.pager)
        val fragmentAdapter = ViewPageAdapter(supportFragmentManager)
        pager.adapter = fragmentAdapter

        val tabLayout: TabLayout = findViewById(R.id.tab_layout)
        tabLayout.setupWithViewPager(pager)
    }
}
