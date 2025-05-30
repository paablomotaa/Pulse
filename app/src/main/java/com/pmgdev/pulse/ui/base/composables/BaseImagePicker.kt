import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage


@Composable
fun BasePickerImage(image: String?, onImageChange: (String) -> Unit) {

// Launcher para seleccionar la imagen
    val imagePickerLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            onImageChange(uri.toString())
        }
    // Imagen seleccionada o placeholder
    Box(
        modifier = Modifier
            .size(300.dp)
            .border(BorderStroke(1.dp, Color.Black), shape = RectangleShape)
            .background(Color.Gray, shape = RectangleShape)
            .clip(RectangleShape)
            .clickable { imagePickerLauncher.launch("image/*") },
        contentAlignment = Alignment.Center
    ) {
        if (!image.isNullOrEmpty() && image.isNotBlank()) {
            AsyncImage(
                model = image,
                contentDescription = "Imagen seleccionada",
                modifier = Modifier
                    .size(300.dp),
                contentScale = ContentScale.Crop
            )

        } else {
            Icon(
                Icons.Default.Add,
                contentDescription = "hola",
            )
        }
    }
}
@Composable
fun BasePickerImageProfile(image: String?, onImageChange: (String) -> Unit) {

    val imagePickerLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            onImageChange(uri.toString())
        }
    Box(
        modifier = Modifier
            .size(100.dp)
            .border(BorderStroke(1.dp, Color.Black), shape = CircleShape)
            .background(Color.Gray, shape = CircleShape)
            .clip(CircleShape)
            .clickable { imagePickerLauncher.launch("image/*") },
        contentAlignment = Alignment.Center
    ) {
        if (!image.isNullOrEmpty() && image.isNotBlank()) {
            AsyncImage(
                model = image,
                contentDescription = "Imagen seleccionada",
                modifier = Modifier
                    .size(100.dp),
                contentScale = ContentScale.Crop
            )
        } else {
            Icon(
                Icons.Default.Add,
                contentDescription = "hola",
            )
        }
    }
}