package com.example.autokatalog.ui.main_screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items // Import the correct items function
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.autokatalog.autokatalogapp.data.Autopart
import com.example.autokatalog.autokatalogapp.data.Favorite
import com.example.autokatalog.ui.login.data.MainScreenDataObject
import com.example.autokatalog.ui.user.data.ProfileNavData
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch

@Composable
fun Mainscreen(
    navData: MainScreenDataObject,
    onBookClick: (Autopart) -> Unit,
    onProfileClick: (ProfileNavData) -> Unit
) {

    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val coroutineScope = rememberCoroutineScope()
    val booksListState = remember { mutableStateOf(emptyList<Autopart>()) }
    val booksSearchListState = remember { mutableStateOf(emptyList<Autopart>()) }
    val isFavListEmptyState = remember { mutableStateOf(false) }
    var query by remember { mutableStateOf("") }

    val db = remember { Firebase.firestore }

    LaunchedEffect(Unit) {
        getAllFavsIds(db, navData.uid) { favs ->
            getAllBooks(db, favs) { books ->
                booksListState.value = books
                booksSearchListState.value = books
            }
        }
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        modifier = Modifier.fillMaxWidth(),
        drawerContent = {
            Column(Modifier.fillMaxWidth(0.7f)) {
                DrawHeader(navData.email)

                DrawerBody(
                    onFavClick = {
                        getAllFavsIds(db, navData.uid) { favs ->
                            getAllFavsBooks(db, favs) { books ->
                                isFavListEmptyState.value = books.isEmpty()
                                booksListState.value = books
                                booksSearchListState.value = books
                            }
                        }
                        coroutineScope.launch {
                            drawerState.close()
                        }
                    },
                    onProfileClick = {
                        onProfileClick(
                            ProfileNavData(
                                "",
                                "",
                                "",
                                "",
                                "",
                                "",
                                navData.uid
                            )
                        )
                    },
                    onHomeClick = {
                        getAllFavsIds(db, navData.uid) { favs ->
                            getAllBooks(db, favs) { books ->
                                booksListState.value = books
                                booksSearchListState.value = books
                            }
                        }
                        coroutineScope.launch {
                            drawerState.close()
                        }
                    }
                )
            }
        }
    ) {
        Scaffold(
            modifier = Modifier.fillMaxSize()
        ) { paddingValues ->
            if (isFavListEmptyState.value) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Empty list",
                        color = Color.LightGray
                    )
                }
            } else {
                Column {
                    TextField(
                        value = query,
                        onValueChange = {
                            query = it
                            booksSearchListState.value =
                                booksListState.value.filter { it.doesMatchSearchQuery(query) }
                        },
                        label = { Text("Search") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues)
                    ) {
                        items(booksSearchListState.value) { book ->
                            BookListItemUi(
                                book,
                                onFavClick = {
                                    booksSearchListState.value = booksSearchListState.value.map { bk ->
                                        if (bk.code == book.code) { // Fixed comparison operator
                                            onFavs(
                                                db,
                                                navData.uid,
                                                Favorite(bk.code),
                                                !bk.isFavorite
                                            )
                                            bk.copy(isFavorite = !bk.isFavorite)
                                        } else {
                                            bk
                                        }
                                    }
                                },
                                onAutopartClick = { auto -> onBookClick(auto) }
                            )
                        }
                    }
                }
            }
        }
    }
}

private fun getAllFavsBooks(
    db: FirebaseFirestore,
    idsList: List<String>,
    onBooks: (List<Autopart>) -> Unit
) {
    if (idsList.isNotEmpty()) {
        db.collection("autoparts")
            .whereIn(FieldPath.documentId(), idsList)
            .get()
            .addOnSuccessListener { task ->
                val booksList = task.toObjects(Autopart::class.java).map { autopart ->
                    autopart.copy(isFavorite = idsList.contains(autopart.code))
                }
                onBooks(booksList)
            }
            .addOnFailureListener {
                onBooks(emptyList()) // Handle failure gracefully
            }
    } else {
        onBooks(emptyList())
    }
}

private fun getAllFavsIds(
    db: FirebaseFirestore,
    uid: String,
    onFavs: (List<String>) -> Unit
) {
    db.collection("users")
        .document(uid)
        .collection("favs")
        .get()
        .addOnSuccessListener { task ->
            val idsList = task.toObjects(Favorite::class.java).map { it.code }
            onFavs(idsList)
        }
        .addOnFailureListener {
            onFavs(emptyList()) // Handle failure gracefully
        }
}

private fun onFavs(
    db: FirebaseFirestore,
    uid: String,
    favorite: Favorite,
    isFav: Boolean
) {
    val favRef = db.collection("users")
        .document(uid)
        .collection("favs")
        .document(favorite.code)

    if (isFav) {
        favRef.set(favorite)
    } else {
        favRef.delete()
    }
}

private fun getAllBooks(
    db: FirebaseFirestore,
    idsList: List<String>,
    onBooks: (List<Autopart>) -> Unit
) {
    db.collection("autoparts")
        .get()
        .addOnSuccessListener { task ->
            val booksList = task.toObjects(Autopart::class.java).map { autopart ->
                autopart.copy(isFavorite = idsList.contains(autopart.code))
            }
            onBooks(booksList)
        }
        .addOnFailureListener {
            onBooks(emptyList()) // Handle failure gracefully
        }
}