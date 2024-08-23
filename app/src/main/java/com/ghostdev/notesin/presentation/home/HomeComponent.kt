package com.ghostdev.notesin.presentation.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.ghostdev.notesin.R
import com.ghostdev.notesin.data.model.Note
import com.ghostdev.notesin.navigation.Destinations
import com.ghostdev.notesin.presentation.base.SearchBar
import com.ghostdev.notesin.presentation.base.TopAppBarComponent
import com.ghostdev.notesin.utilities.PreferencesHelper
import com.ghostdev.notesin.utilities.toFormattedDateTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeComponent(
    navController: NavController
) {
    PreferencesHelper.setFirstTime(LocalContext.current.applicationContext, false)
    val homeLogic = LocalHomeLogic.current
    val notesList = homeLogic.notesList.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF111317))
    ) {
        TopAppBarComponent(
            title = {
                Text(
                    text = "Notein",
                    color = Color.White
                )
            },
            actions = {
                Icon(
                    painter = painterResource(id = R.drawable.more),
                    contentDescription = null,
                    tint = Color.White
                )
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        SearchBar(
            modifier = Modifier
                .padding(start = 8.dp, end = 8.dp)
        )

        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            if (notesList.value.isEmpty()) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Image(
                        modifier = Modifier
                            .size(140.dp),
                        painter = painterResource(id = R.drawable.no_notes),
                        contentDescription = null
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Keep your life organized..",
                        color = Color(0xFF616878),
                        fontSize = 15.sp
                    )
                }
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(12.dp)
                ) {
                    LazyColumn {
                        items(notesList.value.size) { index ->
                            NotesCardItem(
                                note = notesList.value[index],
                                onNoteClick = {
                                    homeLogic.getNote(notesList.value[index].id)
                                    navController.navigate("${Destinations.ViewNote}/edit=true") {}
                                }
                            )
                        }
                    }
                }
            }

            FloatingActionButton(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(bottom = 60.dp, end = 30.dp),
                containerColor = Color(0xFF2071F9),
                onClick = { navController.navigate("${Destinations.ViewNote}/edit=false") }) {

                Icon(
                painter = painterResource(id = R.drawable.edit),
                contentDescription = null,
                tint = Color.White
                )
            }
        }
    }
}

@Composable
fun NotesCardItem(
    note: Note,
    onNoteClick: () -> Unit = {}
) {
    var showDeleteDialog by remember {
        mutableStateOf(false)
    }
    Card(
        modifier = Modifier
            .pointerInput(Unit) {
                detectTapGestures(
                    onLongPress = {
                        showDeleteDialog = true
                    },
                    onPress = {
                        onNoteClick()
                    }
                )
            }
            .fillMaxWidth()
            .height(150.dp),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF1C2026)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(10.dp)
                    .background(Color(note.color))
            ) {}

            Spacer(modifier = Modifier.width(4.dp))

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(12.dp)
            ) {
                Text(
                    text = note.title,
                    color = Color.White,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = note.dateTime.toFormattedDateTime(),
                        color = Color.Gray,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold
                    )

                    Icon(
                        modifier = Modifier
                            .size(18.dp),
                        painter = painterResource(id = R.drawable.calendar),
                        contentDescription = null,
                        tint = Color.Gray
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                HorizontalDivider()

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = note.content,
                    color = Color.White,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Light,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }

    Spacer(modifier = Modifier.height(22.dp))

    if (showDeleteDialog) {
        DeleteDialog(
            onDismiss = { showDeleteDialog = false },
            note = note
        )
    }
}

@Composable
fun DeleteDialog(onDismiss: () -> Unit, note: Note) {
    val homeLogic = LocalHomeLogic.current
    AlertDialog(
        icon = {
            Icon(
                painter = painterResource(id = R.drawable.delete),
                contentDescription = null
            )
        },
        title = {
            Text(text = "Delete Note?")
        },
        text = {
            Text(text = "Are you sure you want to delete this note?")
        },
        onDismissRequest = {
            onDismiss()
        },
        confirmButton = {
            TextButton(
                onClick = {
                    homeLogic.deleteNote(note)
                    onDismiss()
                }
            ) {
                Text("Delete")
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    onDismiss()
                }
            ) {
                Text("Dismiss")
            }
        }
    )
}
