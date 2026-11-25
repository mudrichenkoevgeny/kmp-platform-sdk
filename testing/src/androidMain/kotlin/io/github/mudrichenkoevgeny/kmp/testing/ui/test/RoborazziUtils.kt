package io.github.mudrichenkoevgeny.kmp.testing.ui.test

import androidx.compose.ui.test.SemanticsNodeInteraction
import com.github.takahirom.roborazzi.RoborazziOptions
import com.github.takahirom.roborazzi.captureRoboImage

/**
 * Captures a Roborazzi screenshot for the current semantics node into the module's snapshot directory.
 *
 * Configures [RoborazziOptions] with a small change threshold to tolerate minor cross-OS font
 * antialiasing differences between Windows and Linux CI runners.
 *
 * @param testInstance Test class instance used to derive the snapshot subdirectory name.
 * @param stateName Human-readable UI state name sanitized into the snapshot filename.
 */
public fun SemanticsNodeInteraction.captureAppScreen(
    testInstance: Any,
    stateName: String
) {
    val className = testInstance::class.java.simpleName
    val safeStateName = stateName.replace(Regex("[^A-Za-z0-9]"), "_")

    val filePath = "src/androidHostTest/snapshots/$className/$safeStateName.png"

    captureRoboImage(
        filePath = filePath,
        roborazziOptions = RoborazziOptions(
            compareOptions = RoborazziOptions.CompareOptions(
                changeThreshold = 0.001F
            )
        )
    )
}
