package pl.oczadly.baltic.lsc.android.view.app.activity.release

import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import kotlin.coroutines.CoroutineContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import pl.oczadly.baltic.lsc.android.MainActivity
import pl.oczadly.baltic.lsc.android.R
import pl.oczadly.baltic.lsc.android.view.app.activity.AppStoreView
import pl.oczadly.baltic.lsc.android.view.app.converter.AppListItemEntityConverter
import pl.oczadly.baltic.lsc.android.view.app.converter.AppResourceRangeEntityConverter
import pl.oczadly.baltic.lsc.android.view.app.converter.AppShelfEntityConverter
import pl.oczadly.baltic.lsc.android.view.app.entity.AppReleaseEntity
import pl.oczadly.baltic.lsc.android.view.app.service.AppService
import pl.oczadly.baltic.lsc.app.AppApi
import pl.oczadly.baltic.lsc.app.dto.AppReleaseEdit

class AppReleaseEditView : AppCompatActivity(), CoroutineScope {

    private val job = Job()

    private val appService = AppService(
        AppApi(MainActivity.apiBasePath, MainActivity.apiPort, MainActivity.state),
        AppListItemEntityConverter(),
        AppShelfEntityConverter()
    )

    private val resourceRangeConverter = AppResourceRangeEntityConverter()

    override val coroutineContext: CoroutineContext
        get() = job

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_app_release_edit)
        setSupportActionBar(findViewById(R.id.toolbar))
        val appRelease =
            intent.getSerializableExtra(AppStoreView.appReleaseIntent) as? AppReleaseEntity
        if (appRelease == null) {
            finish()
        } else {
            findViewById<EditText>(R.id.app_release_edit_version_name_edit_text).setText(appRelease.versionName)
            findViewById<EditText>(R.id.app_release_edit_description_edit_text).setText(appRelease.description)
            findViewById<CheckBox>(R.id.app_release_edit_is_open_source_checkbox).isChecked =
                appRelease.isOpenSource

            findViewById<Button>(R.id.app_release_edit_edit_button)
                .setOnClickListener {
                    val appReleaseEditDTO = getAppReleaseEditDTO(appRelease)
                    launch(job) {
                        appService.editAppRelease(appReleaseEditDTO)
                    }
                    finish()
                }

            findViewById<Button>(R.id.app_release_edit_cancel_button)
                .setOnClickListener {
                    finish()
                }
        }
    }

    private fun getAppReleaseEditDTO(appReleaseEntity: AppReleaseEntity): AppReleaseEdit {
        val versionName =
            findViewById<EditText>(R.id.app_release_edit_version_name_edit_text).text.toString()
                .trim()
        val description =
            findViewById<EditText>(R.id.app_release_edit_description_edit_text).text.toString()
                .trim()
        val isOpenSource =
            findViewById<CheckBox>(R.id.app_release_edit_is_open_source_checkbox).isChecked

        return AppReleaseEdit(
            appReleaseEntity.releaseUid,
            isOpenSource,
            versionName,
            description,
            resourceRangeConverter.convertToResourceRangeDTO(appReleaseEntity.resourceRange)
        )
    }
}
