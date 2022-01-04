package pl.oczadly.baltic.lsc.android.view.app.activity

import android.app.Activity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import kotlin.coroutines.CoroutineContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import pl.oczadly.baltic.lsc.android.MainActivity
import pl.oczadly.baltic.lsc.android.R
import pl.oczadly.baltic.lsc.android.view.app.converter.AppListItemEntityConverter
import pl.oczadly.baltic.lsc.android.view.app.converter.AppShelfEntityConverter
import pl.oczadly.baltic.lsc.android.view.app.entity.AppListItemEntity
import pl.oczadly.baltic.lsc.android.view.app.service.AppService
import pl.oczadly.baltic.lsc.app.AppApi
import pl.oczadly.baltic.lsc.app.dto.AppEdit

class AppEditView : AppCompatActivity(), CoroutineScope {

    private val job = Job()

    private val appService = AppService(
        AppApi(MainActivity.state),
        AppListItemEntityConverter(),
        AppShelfEntityConverter()
    )

    override val coroutineContext: CoroutineContext
        get() = job

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_app_edit)
        setSupportActionBar(findViewById(R.id.toolbar))
        val appListItem =
            intent.getSerializableExtra(AppStoreView.appListItemIntent) as? AppListItemEntity
        if (appListItem == null) {
            finish()
        } else {
            findViewById<EditText>(R.id.app_edit_name_edit_text).setText(appListItem.name)
            findViewById<EditText>(R.id.app_edit_short_description_edit_text).setText(appListItem.shortDescription)
            findViewById<EditText>(R.id.app_edit_long_description_edit_text).setText(appListItem.longDescription)
            findViewById<EditText>(R.id.app_edit_icon_path_edit_text).setText(appListItem.iconUrl)
            findViewById<EditText>(R.id.app_edit_p_class_edit_text).setText(appListItem.pClass)

            findViewById<Button>(R.id.app_edit_edit_button)
                .setOnClickListener {
                    val appUpdateDTO = getAppUpdateDTO(appListItem)
                    launch(job) {
                        appService.editApp(appUpdateDTO)
                        setResult(Activity.RESULT_OK)
                        finish()
                    }
                }

            findViewById<Button>(R.id.app_edit_cancel_button)
                .setOnClickListener {
                    finish()
                }
        }
    }

    private fun getAppUpdateDTO(appListItem: AppListItemEntity): AppEdit {
        val name =
            findViewById<EditText>(R.id.app_edit_name_edit_text).text.toString().trim()
        val pClass =
            findViewById<EditText>(R.id.app_edit_p_class_edit_text).text.toString().trim()
        val shortDescription =
            findViewById<EditText>(R.id.app_edit_short_description_edit_text).text.toString().trim()
        val longDescription =
            findViewById<EditText>(R.id.app_edit_long_description_edit_text).text.toString().trim()
        val icon =
            findViewById<EditText>(R.id.app_edit_icon_path_edit_text).text.toString().trim()

        return AppEdit(
            name,
            appListItem.uid,
            pClass,
            shortDescription,
            longDescription,
            appListItem.keywords ?: emptyList(),
            icon,
            true
        )
    }
}
