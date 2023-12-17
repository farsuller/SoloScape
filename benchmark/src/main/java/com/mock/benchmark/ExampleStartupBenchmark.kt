package com.mock.benchmark

import androidx.benchmark.macro.CompilationMode
import androidx.benchmark.macro.FrameTimingMetric
import androidx.benchmark.macro.MacrobenchmarkScope
import androidx.benchmark.macro.StartupMode
import androidx.benchmark.macro.StartupTimingMetric
import androidx.benchmark.macro.junit4.MacrobenchmarkRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.uiautomator.By
import androidx.test.uiautomator.Direction
import androidx.test.uiautomator.Until
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * This is an example startup benchmark.
 *
 * It navigates to the device's home screen, and launches the default activity.
 *
 * Before running this benchmark:
 * 1) switch your app's active build variant in the Studio (affects Studio runs only)
 * 2) add `<profileable android:shell="true" />` to your app's manifest, within the `<application>` tag
 *
 * Run this benchmark from Studio to see startup measurements, and captured system traces
 * for investigating your app's performance.
 */
@RunWith(AndroidJUnit4::class)
class ExampleStartupBenchmark {
    @get:Rule
    val benchmarkRule = MacrobenchmarkRule()

    @Test
    fun startup() = benchmarkRule.measureRepeated(
        packageName = "com.compose.multimodular",
        metrics = listOf(StartupTimingMetric()),
        iterations = 5,
        startupMode = StartupMode.COLD
    ) {
        pressHome()
        startActivityAndWait()
    }

    @Test
    fun scrollAndNavigate() = benchmarkRule.measureRepeated(
        packageName = "com.compose.multimodular",
        metrics = listOf(FrameTimingMetric()),
        iterations = 5,
        startupMode = StartupMode.COLD
    ) {
        pressHome()
        startActivityAndWait()

        addElementsAndScrollDown()
    }
}

fun MacrobenchmarkScope.addElementsAndScrollDown(){
    val googleButton = device.findObject(By.res("google_click"))

    if (!device.hasObject(By.res("google_click"))) {
        // Skip the function on the next iteration
        return
    }
    val reports = device.findObject(By.res("reports_list"))

    device.wait(Until.hasObject(By.res("google_click")), 5000)

    repeat(1){
        googleButton.click()
    }

    device.waitForIdle()

    device.wait(Until.hasObject(By.text("click me")), 20000)

   // reports.fling(Direction.DOWN)


    device.findObject(By.text("click me")).click()

    device.wait(Until.hasObject(By.text("Surprised")), 5000)

}