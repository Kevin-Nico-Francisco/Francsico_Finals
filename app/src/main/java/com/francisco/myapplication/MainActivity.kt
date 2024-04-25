package com.francisco.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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
import com.francisco.myapplication.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApplicationTheme {
                MyApp()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyApp() {
    var itemList by remember { mutableStateOf(List(100) { "To Do ${it + 1}" }) } // Start numbering from 1

    Scaffold(
        topBar = {
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

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        if (isEditing) {
            TextField(
                value = currentText,
                onValueChange = { newText -> currentText = newText },
                label = { Text("Edit To Do ${index + 1}") }, // Label reflects "To Do" instead of "Item #"
                modifier = Modifier.weight(1f)
            )
            IconButton(onClick = { onEdit(currentText) }) {
                Icon(imageVector = Icons.Default.Check, contentDescription = "Save")
            }
        } else {
            Text(text = text, modifier = Modifier.weight(1f))
            IconButton(onClick = onToggleEditMode) {
                Icon(imageVector = Icons.Default.Edit, contentDescription = "Edit")
            }
        }

        IconButton(onClick = onDelete) {
            Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    MyApplicationTheme {
        MyApp()
    }
}