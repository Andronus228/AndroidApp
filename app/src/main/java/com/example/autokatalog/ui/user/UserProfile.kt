package com.example.autokatalog.ui.user

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.example.autokatalog.ui.user.data.ProfileNavData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun UserProfile(
    navData: ProfileNavData,
    auth: FirebaseAuth,
    user: FirebaseUser?,
    popToLogin: () -> Unit
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val state = rememberScrollState()
    var name by remember { mutableStateOf("") }
    var surname by remember { mutableStateOf("") }
    var age by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var city by remember { mutableStateOf("") }
    var country by remember { mutableStateOf("") }

    val db = remember { Firebase.firestore }

    LaunchedEffect(Unit) {
        coroutineScope.launch(Dispatchers.IO) {
            getUserInfo(db, navData.uid) { map ->
                name = map["name"] ?: ""
                surname = map["surname"] ?: ""
                age = map["age"] ?: ""
                address = map["address"] ?: ""
                city = map["city"] ?: ""
                country = map["country"] ?: ""
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(state)
    ) {
        Text("profile", modifier = Modifier.align(Alignment.CenterHorizontally))

        TextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Name") },
            modifier = Modifier.fillMaxWidth()
        )
        TextField(
            value = surname,
            onValueChange = { surname = it },
            label = { Text("Surname") },
            modifier = Modifier.fillMaxWidth()
        )
        TextField(
            value = age,
            onValueChange = { age = it },
            label = { Text("Age") },
            modifier = Modifier.fillMaxWidth()
        )
        TextField(
            value = address,
            onValueChange = { address = it },
            label = { Text("Address") },
            modifier = Modifier.fillMaxWidth()
        )
        TextField(
            value = city,
            onValueChange = { city = it },
            label = { Text("City") },
            modifier = Modifier.fillMaxWidth()
        )
        TextField(
            value = country,
            onValueChange = { country = it },
            label = { Text("Country") },
            modifier = Modifier.fillMaxWidth()
        )
        Button(
            onClick = {
                coroutineScope.launch(Dispatchers.IO) {
                    saveInfo(
                        db,
                        navData.uid,
                        ProfileNavData(name, surname, age, address, city, country)
                    )
                }
            },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text("Save changes")
        }
        Button(
            onClick = {
                auth.signOut()
                popToLogin()
            },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text("log out")
        }
        Button(
            onClick = {
                auth.signOut()
                delete(db, user,navData.uid) { Toast.makeText(
                    context, "Unable to delete account now",
                    Toast.LENGTH_SHORT).show() }
                popToLogin()
            },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text("Delete account")
        }
    }
}

private fun getUserInfo(
    db: FirebaseFirestore,
    uid: String,
    setData: (Map<String, String>) -> Unit
) {
    db.collection("users")
        .document(uid)
        .collection("info")
        .document("info")
        .get()
        .addOnSuccessListener { document ->
            if (document != null) {
                try{ setData(document.data as Map<String, String>) }
                catch (e: Exception) {
                    setData(emptyMap())
                }
                Log.d("!!!", "DocumentSnapshot data: ${document.data}")
            } else {
                Log.d("!!!", "No such document")
            }
        }
        .addOnFailureListener { exception ->
            Log.d("!!!", "get failed with ", exception)
        }
}

private fun saveInfo(
    db: FirebaseFirestore,
    uid: String,
    favorite: ProfileNavData
) {
    val favRef = db.collection("users")
        .document(uid)
        .collection("info")
        .document("info")

    favRef.set(favorite)
}

private fun delete(
    db: FirebaseFirestore,
    user: FirebaseUser?,
    uid: String,
    showToast: () -> Unit
) {
    val favRef = db.collection("users")
        .document(uid)

    favRef.delete()

    user?.delete()
        ?.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Log.d("!!!", "User account deleted.")
            }
        }
    if(user == null) {
        showToast()
    }
}