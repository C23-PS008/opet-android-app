package com.c23ps008.opet.ui.screen.permissions_dialog

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun PermissionsDialogScreen(
    permissionTextProvider: PermissionTextProvider,
    isPermanentlyDeclined: Boolean,
    onDismiss: () -> Unit,
    onOkClick: () -> Unit,
    onGoToAppSettingsClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = "Permission required") },
        confirmButton = {
            Column(modifier = Modifier.fillMaxWidth()) {
                Divider()
                Text(
                    text = if (isPermanentlyDeclined) "Grant Permission" else "Ok",
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            if (isPermanentlyDeclined) {
                                onGoToAppSettingsClick()
                            } else {
                                onOkClick()
                            }
                        }
                        .padding(16.dp)
                )
            }
        },
        text = {
            Text(
                text = permissionTextProvider.getDescription(isPermanentlyDeclined)
            )
        },
        modifier = modifier
    )

}

interface PermissionTextProvider {
    fun getDescription(isPermanentlyDeclined: Boolean): String
}

class CameraPermissionTextProvider : PermissionTextProvider {
    override fun getDescription(isPermanentlyDeclined: Boolean): String {
        return if (isPermanentlyDeclined) {
            "The camera permission has been permanently declined. To use the camera feature, the camera permission needs to be granted. Please open the app settings and enable the camera permission to continue using this feature."
        } else {
            "To access the camera, the app requires permission to use the camera. Please grant the camera permission to enable the camera functionality."
        }
    }
}

class LocationPermissionTextProvider : PermissionTextProvider {
    override fun getDescription(isPermanentlyDeclined: Boolean): String {
        return if (isPermanentlyDeclined) {
            "The location permission has been permanently declined. To use the location feature, the location permission needs to be granted. Please open the app settings and enable the location permission to continue using this feature."
        } else {
            "To access the location, the app requires permission to use the location. Please grant the location permission to enable the location functionality."
        }
    }
}