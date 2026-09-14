package io.github.mudrichenkoevgeny.kmp.core.common.ui.test

import androidx.compose.ui.test.SemanticsNodeInteraction
import com.github.takahirom.roborazzi.captureRoboImage

fun SemanticsNodeInteraction.captureAppScreen(
    testInstance: Any,
    stateName: String
) {
    val packagePath = testInstance::class.java.name.substringBeforeLast('.').replace('.', '/')
    val className = testInstance::class.java.simpleName
    val safeStateName = stateName.replace(Regex("[^A-Za-z0-9]"), "_")

    val filePath = "src/androidUnitTest/snapshots/images/$packagePath/$className/$safeStateName.png"

    captureRoboImage(filePath)
}
