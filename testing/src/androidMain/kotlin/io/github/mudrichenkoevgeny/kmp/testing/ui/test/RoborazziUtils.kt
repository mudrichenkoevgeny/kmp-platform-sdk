package io.github.mudrichenkoevgeny.kmp.testing.ui.test

import androidx.compose.ui.test.SemanticsNodeInteraction
import com.github.takahirom.roborazzi.captureRoboImage

public fun SemanticsNodeInteraction.captureAppScreen(
    testInstance: Any,
    stateName: String
) {
    val className = testInstance::class.java.simpleName
    val safeStateName = stateName.replace(Regex("[^A-Za-z0-9]"), "_")

    val filePath = "src/androidHostTest/snapshots/$className/$safeStateName.png"

    captureRoboImage(filePath)
}
