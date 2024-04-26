package com.francisco.myapplication

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Alignment
import com.francisco.myapplication.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApplicationTheme { //Displays the MyApp Composable
                MyApp()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyApp() {
    //This Mutes the state to hold the list of todo items
    var itemList by remember { mutableStateOf(List(100) { "To Do ${it + 1}" }) } // Start numbering from 1
//the scaffold provides basic layout structure for the app
    Scaffold(
        topBar = { //This displays the title of the App
            TopAppBar(
                title = { Text("ToDo List") },
                colors = TopAppBarDefaults.smallTopAppBarColors()
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
        ) {
            itemsIndexed(itemList.toList()) { index, itemText ->
                var isEditing by remember { mutableStateOf(false) }
                EditableListItem(
                    index = index,
                    text = itemText,
                    isEditing = isEditing,
                    onEdit = { newText ->
                        itemList = itemList.toMutableList().apply { set(index, newText) }
                        isEditing = false // Exit editing mode after saving
                    },
                    onDelete = { itemList = itemList.toMutableList().apply { removeAt(index) } },
                    onToggleEditMode = { isEditing = !isEditing }
                )
                Divider()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditableListItem(
    index: Int,
    text: String,
    isEditing: Boolean,
    onEdit: (String) -> Unit,
    onDelete: () -> Unit,
    onToggleEditMode: () -> Unit
) {
    var currentText by remember { mutableStateOf(text) }

    // Using Card instead of Surface for better default padding and elevation
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant // Use color scheme from theme
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp) // Optional elevation for a card-like effect
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp), // Add padding for the Row content
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically // Center the items vertically
        ) {
            if (isEditing) {
                TextField(
                    value = currentText,
                    onValueChange = { newText -> currentText = newText },
                    label = { Text("Edit To Do ${index + 1}") },
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 8.dp), // Add padding at the end of the TextField
                    colors = TextFieldDefaults.textFieldColors() // Use default Material 3 TextField colors
                )
                IconButton(onClick = { onEdit(currentText) }) {
                    Icon(imageVector = Icons.Default.Check, contentDescription = "Save")
                }
            } else {
                Text(
                    text = text,
                    modifier = Modifier
                        .weight(1f)
                        .align(Alignment.CenterVertically) // Vertically center the Text
                )
                IconButton(onClick = onToggleEditMode) {
                    Icon(imageVector = Icons.Default.Edit, contentDescription = "Edit")
                }
            }

            IconButton(onClick = onDelete) {
                Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete")
            }
        }
    }
}
//Kevin
@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    MyApplicationTheme {
        MyApp()
    }
}