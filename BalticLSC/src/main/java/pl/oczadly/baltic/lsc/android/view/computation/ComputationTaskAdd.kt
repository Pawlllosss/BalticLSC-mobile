package pl.oczadly.baltic.lsc.android.view.computation

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import pl.oczadly.baltic.lsc.android.R

class ComputationTaskAdd : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_computation_task_add)

        setSupportActionBar(findViewById(R.id.toolbar))

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