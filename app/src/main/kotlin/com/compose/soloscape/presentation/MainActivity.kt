package com.compose.soloscape.presentation

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.mutableStateOf
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.rememberNavController
import com.compose.soloscape.navigation.SetupNavGraph
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.appupdate.AppUpdateOptions
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.UpdateAvailability
import com.google.firebase.FirebaseApp
import com.soloscape.ui.theme.SoloScapeTheme
import com.soloscape.util.routes.ScreensRoutes
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val isUpdateAvailable = mutableStateOf(false)

    private val activityResultLauncher =
        registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) { result: ActivityResult ->
            if (result.resultCode != RESULT_OK) {
                Toast.makeText(
                    this,
                    "Update Failed! Result Code:${result.resultCode}",
                    Toast.LENGTH_SHORT,
                ).show()
                isUpdateAvailable.value = true
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        enableEdgeToEdge()

        FirebaseApp.initializeApp(this)

        setContent {
            SoloScapeTheme {
                val navController = rememberNavController()
                SetupNavGraph(
                    startDestination = ScreensRoutes.SplashRoute.route,
                    navHostController = navController,
                    isUpdateAvailable = isUpdateAvailable,
                )
            }
        }
        checkAppUpdate()

        onBackPressedDispatcher.addCallback(
            this,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    isUpdateAvailable.value = true
                    onBackPressedDispatcher.onBackPressed()
                }
            },
        )
    }

    private fun checkAppUpdate() {
        val appUpdateManager = AppUpdateManagerFactory.create(applicationContext)
        val appUpdateInfoTask = appUpdateManager.appUpdateInfo

        appUpdateInfoTask.addOnSuccessListener { appUpdateInfo ->
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE &&
                appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)
            ) {
                appUpdateManager.startUpdateFlowForResult(
                    appUpdateInfo,
                    activityResultLauncher,
                    AppUpdateOptions.newBuilder(AppUpdateType.IMMEDIATE).build(),
                )
                isUpdateAvailable.value = true
            } else isUpdateAvailable.value= false
        }
    }
}
