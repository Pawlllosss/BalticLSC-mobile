package pl.oczadly.baltic.lsc.android.view.computation

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import pl.oczadly.baltic.lsc.android.R
import pl.oczadly.baltic.lsc.android.view.app.entity.AppListItemEntity
import pl.oczadly.baltic.lsc.android.view.app.entity.AppReleaseEntity

class ComputationTaskAdd : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val appIntent = intent.getSerializableExtra("appListItemEntity") as? AppListItemEntity
        if (appIntent == null) {
            finish()
        }
        setContentView(R.layout.activity_computation_task_add)
        setSupportActionBar(findViewById(R.id.toolbar))

        val app = appIntent!!
        val versionSpinner = findViewById<Spinner>(R.id.computation_task_version_spinner)
        // TODO: need to set items for spinner. Probably need to create a custom array adapter and associate it with the spinner https://android--code.blogspot.com/2020/03/android-kotlin-create-spinner.html
        val adapter: ArrayAdapter<AppReleaseEntity> = ArrayAdapter(this, R.id.computation_task_version_spinner, app.releases)
        versionSpinner.adapter = adapter

        // TODO: https://stackoverflow.com/questions/17713610/how-to-restart-previous-activity-in-android
        findViewById<Button>(R.id.computation_task_create_button)
            .setOnClickListener {
                finish()
            }

        findViewById<Button>(R.id.computation_task_cancel_button)
            .setOnClickListener {
                finish()
            }
    }
}