package com.example.autokatalog

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.firebase.firestore.FirebaseFirestore
import com.example.autokatalog.autokatalogapp.data.Autopart
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.runtime.remember
//import com.google.api.Context
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.autokatalog.ui.detail_screen.data.DetailNavObject
import com.example.autokatalog.ui.detail_screen.ui.DetailsScreen
import com.example.autokatalog.ui.login.LoginScreen
import com.example.autokatalog.ui.login.data.LoginScreenObject
import com.example.autokatalog.ui.login.data.MainScreenDataObject
import com.example.autokatalog.ui.main_screen.Mainscreen
import com.example.autokatalog.ui.user.UserProfile
import com.example.autokatalog.ui.user.data.ProfileNavData
import com.google.firebase.auth.auth


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //val fs = Firebase.firestore
        //fs.collection("autoparts").document().set()
        //enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            val auth = remember {
                com.google.firebase.Firebase.auth
            }
            var user by remember { mutableStateOf(auth.currentUser) }
            //addScreen()
            NavHost(navController = navController, startDestination = LoginScreenObject) {
                composable<LoginScreenObject> {
                    LoginScreen(auth = auth, { navData ->
                        navController.navigate(navData)
                    }, updateUser = { newUser ->
                        user = newUser
                    }
                    )
                }
                composable<MainScreenDataObject> { navEntry ->
                    val navData = navEntry.toRoute<MainScreenDataObject>()
                    Mainscreen(navData, { aut ->
                        navController.navigate(
                            DetailNavObject(
                                tipe = aut.tipe,
                                subTipe = aut.subTipe,
                                name = aut.name,
                                code = aut.code,
                                carTipe = aut.carTipe,
                                carSeries = aut.carSeries,
                                carModel = aut.carModel,
                                carModification = aut.carModification,
                                creator = aut.creator,
                                numInWarehouse = aut.numInWarehouse,
                                cost = aut.cost,
                                images = aut.images
                            )

                        )
                    }, { profile -> navController.navigate(profile) })
                }
                composable<DetailNavObject> { navEntry ->
                    val navData = navEntry.toRoute<DetailNavObject>()
                    DetailsScreen(navData)
                }
                composable<ProfileNavData> { navEntry ->
                    val navData = navEntry.toRoute<ProfileNavData>()
                    println("open ")
                    UserProfile(navData, auth, user) {
                        navController.popBackStack(route = LoginScreenObject, inclusive = false)
                    }
                }
            }

            //MainScreen()
            //ShowScreen()
            //LoginScreen()
            //com.example.autokatalog.ui.main_screen.MainScreen()
            /*AutokatalogTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }*/
        }
    }
}

@Composable
fun addScreen() {
    val db = FirebaseFirestore.getInstance()
    //val storage = Firebase.storage.reference.child("images")
    var tipe by remember { mutableStateOf("") }
    var subTipe by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    var code by remember { mutableStateOf("") }
    var carTipe by remember { mutableStateOf("") }
    var carSeries by remember { mutableStateOf("") }
    var carModel by remember { mutableStateOf("") }
    var carModification by remember { mutableStateOf("") }
    var creator by remember { mutableStateOf("") }
    var numInWarehouse by remember { mutableStateOf("") }
    var cost by remember { mutableStateOf("") }

    // Для отображения Snackbar
    var snackbarVisible by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.Start
    ) {
        TextField(value = tipe, onValueChange = { tipe = it }, label = { Text("Тип") })
        TextField(value = subTipe, onValueChange = { subTipe = it }, label = { Text("Подтип") })
        TextField(value = name, onValueChange = { name = it }, label = { Text("Название") })
        TextField(value = code, onValueChange = { code = it }, label = { Text("Код") })
        TextField(
            value = carTipe,
            onValueChange = { carTipe = it },
            label = { Text("Тип автомобиля") })
        TextField(
            value = carSeries,
            onValueChange = { carSeries = it },
            label = { Text("Серия автомобиля") })
        TextField(
            value = carModel,
            onValueChange = { carModel = it },
            label = { Text("Модель автомобиля") })
        TextField(
            value = carModification,
            onValueChange = { carModification = it },
            label = { Text("Модификация автомобиля") })
        TextField(value = creator, onValueChange = { creator = it }, label = { Text("Создатель") })
        TextField(
            value = numInWarehouse,
            onValueChange = { numInWarehouse = it },
            label = { Text("Количество на складе") })
        TextField(value = cost, onValueChange = { cost = it }, label = { Text("Стоимость") })

        Button(onClick = {
            val autopart = Autopart(
                tipe,
                subTipe,
                name,
                code,
                carTipe,
                carSeries,
                carModel,
                carModification,
                creator,
                numInWarehouse,
                cost
            )
            db.collection("autoparts").document(code).set(autopart)
                .addOnSuccessListener {
                    // Очистка полей после успешного добавления
                    tipe = ""
                    subTipe = ""
                    name = ""
                    code = ""
                    carTipe = ""
                    carSeries = ""
                    carModel = ""
                    carModification = ""
                    creator = ""
                    numInWarehouse = ""
                    cost = ""

                    // Показать Snackbar
                    snackbarVisible = true
                }
                .addOnFailureListener {
                    // Обработка ошибки
                }
        }) {
            Text("Добавить")
        }
        // Snackbar для отображения сообщения об успешном добавлении
        if (snackbarVisible) {
            Snackbar(
                action = {
                    Button(onClick = { snackbarVisible = false }) {
                        Text("Закрыть")
                    }
                },
                modifier = Modifier.padding(8.dp)
            ) {
                Text("Автозапчасть успешно добавлена!")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ShowScreen() {
    val fs = Firebase.firestore
    val list = remember {
        mutableStateOf(emptyList<Autopart>())
    }
    fs.collection("autoparts").addSnapshotListener { snapShot, exception ->

        list.value = snapShot?.toObjects(Autopart::class.java) ?: emptyList()
    }
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceBetween

    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.8f)
        ) {
            items(list.value) { autopart ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp)
                ) {
                    Text(text = autopart.name)
                }
            }
        }
        Spacer(modifier = Modifier.height(10.dp))
    }
    /*AutokatalogTheme {
        Greeting("Android")
    }*/
}
/*private fun bitmapToByteArray(context: Context): ByteArray {
    val bitmap = BitmapFactory.decodeResource(context.resources, R.drawable.Volk)
    val baos = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
    return baos.toByteArray()
}*/