package com.ghostdev.notesin.presentation.notes

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.ghostdev.notesin.R
import com.ghostdev.notesin.data.model.Note
import com.ghostdev.notesin.navigation.Destinations
import com.ghostdev.notesin.presentation.base.TopAppBarComponent
import com.ghostdev.notesin.presentation.home.HomeLogic
import com.ghostdev.notesin.presentation.home.LocalHomeLogic

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotesComponent(
    controller: NavController,
    edit: Boolean
) {
    val homeLogic = LocalHomeLogic.current
    val context = LocalContext.current.applicationContext

    val title = rememberSaveable {
        if (!edit)
        mutableStateOf("")
        else
            mutableStateOf(homeLogic.noteItem.value!!.title)
    }
    val content = rememberSaveable {
        if (!edit)
        mutableStateOf("")
        else
        mutableStateOf(homeLogic.noteItem.value!!.content)
    }
    var colorDialogShow by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF111317))
    ) {
        TopAppBarComponent(
            title = {},
            navigationIcon = {
                Icon(
                    modifier = Modifier
                        .clickable {
                            controller.popBackStack()
                            homeLogic.selectColor(Color(0xFFFFBE0D))
                        },
                    painter = painterResource(id = R.drawable.back),
                    contentDescription = null,
                    tint = Color.White
                )
            },
            actions = {
                ConcentricCirclesComponent(
                    modifier = Modifier
                        .clickable { colorDialogShow = true},
                    color = Color(homeLogic.selectedColor.collectAsState().value.toArgb())
                )

                Spacer(modifier = Modifier.width(30.dp))

                Icon(
                    modifier = Modifier
                        .clickable {
                            if (validateFields(title.value, content.value)) {
                                if (!edit) {
                                    val note = Note(
                                        id = 0,
                                        title = title.value,
                                        content = content.value,
                                        dateTime = System.currentTimeMillis(),
                                        color = homeLogic.selectedColor.value.toArgb(),
                                        )
                                    homeLogic.addNote(note)
                                    homeLogic.selectColor(Color(0xFFFFBE0D))
                                    controller.popBackStack()
                                } else {
                                    val note = Note(
                                        id = homeLogic.noteItem.value!!.id,
                                        title = title.value,
                                        content = content.value,
                                        dateTime = System.currentTimeMillis(),
                                        color = homeLogic.selectedColor.value.toArgb(),
                                    )
                                    homeLogic.updateNote(note)
                                    homeLogic.selectColor(Color(0xFFFFBE0D))
                                    controller.popBackStack()
                                }
                            } else {
                                Toast.makeText(context, "Title or content cannot be empty", Toast.LENGTH_SHORT).show()
                            }
                        },
                    painter = painterResource(id = R.drawable.save),
                    contentDescription = null,
                    tint = Color.White
                )

                Spacer(modifier = Modifier.width(30.dp))

                Icon(
                    painter = painterResource(id = R.drawable.more),
                    contentDescription = null,
                    tint = Color.White
                )
            }
        )

        Spacer(modifier = Modifier.height(30.dp))

        TextField(
            value = title.value,
            onValueChange = { title.value =  it },
            placeholder = {
                Text(
                    text = "Title",
                    color = Color.Gray,
                    fontWeight = FontWeight.Bold,
                    fontSize = 22.sp
                )
            },
            colors = TextFieldDefaults.textFieldColors(
                containerColor = Color(0xFF111317),
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            textStyle = TextStyle(
                fontSize = 22.sp,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
        )

        HorizontalDivider(
            modifier = Modifier
                .padding(start = 20.dp, end = 20.dp, top = 8.dp, bottom = 8.dp)
        )

        TextField(
            value = content.value,
            onValueChange = { content.value =  it },
            placeholder = {
                Text(
                    text = "",
                    color = Color.Gray,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            },
            colors = TextFieldDefaults.textFieldColors(
                containerColor = Color(0xFF111317),
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            textStyle = TextStyle(
                fontSize = 18.sp,
                color = Color.White
            )
        )
    }
    if (colorDialogShow) {
        BottomSheetDialog(onDismiss = {colorDialogShow = false})
    }
}

@Composable
fun ConcentricCirclesComponent(
    modifier: Modifier,
    color: Color
) {
    Box(
        modifier = modifier
            .size(22.dp)
            .wrapContentSize(),
        contentAlignment = Alignment.Center
    ) {
        Canvas(
            modifier = modifier.size(22.dp)
        ) {
            // Draw outer circle
            drawCircle(
                color = color,
                style = Stroke(width = 6f)
            )
            // Draw inner circle
            drawCircle(
                color = color,
                radius = size.minDimension / 4
            )
        }
    }
}

@Composable
fun BottomSheetDialog(
    onDismiss: () -> Unit
) {
    val homeLogic = LocalHomeLogic.current
    val colors = listOf(
        Color(0xFFFFBE0D),
        Color(0xFF41BC62),
        Color(0xFF2071F9),
        Color(0xFFEA4436),
        Color(0xFFFF8838),
        Color(0xFF6A45EF)
    )

    // State to track the selected color
    val selectedColor by homeLogic.selectedColor.collectAsState()

    Dialog(onDismissRequest = { onDismiss() }) {
        // Box with rounded corners
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp)) // Add rounded corners
                .background(Color(0xFF242930)) // Set background color
                .padding(16.dp) // Padding inside the dialog
        ) {
            Column {
                // Header Text
                Text(
                    text = "Change color",
                    color = Color.White,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Color options list
                colors.forEach { color ->
                    Spacer(modifier = Modifier.height(8.dp))

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(4.dp)
                            .height(56.dp),
                        shape = RoundedCornerShape(8.dp), // Rounded corners for each color card
                        border = if (color == selectedColor) {
                            BorderStroke(2.dp, Color(0xFFFFBE0D)) // Add a border if selected
                        } else null,
                        colors = CardDefaults.cardColors(
                            containerColor = Color(0xFF2E343B)
                        ),
                        onClick = {
                            homeLogic.selectColor(color)
                            onDismiss()
                        } // Set selected color on click
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(18.dp)
                        ) {
                            // Draw the circle
                            Canvas(
                                modifier = Modifier
                                    .size(24.dp)
                                    .clip(RoundedCornerShape(12.dp))
                            ) {
                                drawCircle(
                                    color = color,
                                    radius = size.minDimension / 2f
                                )
                            }

                            Spacer(modifier = Modifier.width(16.dp))

                            // Item text (Placeholder text for now)
                            Text(
                                text = when (color) {
                                    Color(0xFFFFBE0D) -> "Personal"
                                    Color(0xFF41BC62) -> "Work"
                                    Color(0xFF2071F9) -> "Private"
                                    Color(0xFFEA4436) -> "Urgent"
                                    Color(0xFFFF8838) -> "Important"
                                    Color(0xFF6A45EF) -> "Miscellaneous"
                                    else -> "Category"
                                },
                                color = Color.White,
                                fontSize = 16.sp,
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }
            }
        }
    }
}

fun validateFields(title: String, content: String): Boolean {
    return title.isNotBlank() && content.isNotBlank()

}