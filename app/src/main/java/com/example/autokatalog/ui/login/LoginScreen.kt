package com.example.autokatalog.ui.login

import android.content.Context
import android.media.Image
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable;
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role.Companion.Image
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContentProviderCompat.requireContext
import com.example.autokatalog.R
import com.example.autokatalog.ui.login.data.MainScreenDataObject
import com.google.firebase.auth.auth
import com.google.firebase.Firebase
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.auth.User

@Composable
fun LoginScreen(
    auth: FirebaseAuth,
    onNavigateToMainScreen:(MainScreenDataObject) -> Unit,
    updateUser: (FirebaseUser?) -> Unit
) {

    val errorState = remember{
        mutableStateOf("")
    }

    val emailState = remember{
        mutableStateOf("")
    }
    val passwordState = remember{
        mutableStateOf("")
    }

    Image(painter = painterResource(
        id = R.drawable.back4),
        contentDescription = "BG",
        modifier = Modifier.fillMaxSize(),
        contentScale = ContentScale.Crop
    )
    //go to another page

    Column(modifier = Modifier.fillMaxSize().padding(
        start = 50.dp, end = 50.dp
    ),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = "VolksWagen Autoparts",
            color = Color.Black,
            //FontWeight = FontWeight.Bold,
            fontSize = 30.sp,
            //FontFamily = FontFamily.Serif
        )
        Spacer(modifier = Modifier.height(10.dp))
        RoundedCornerTextField(
            text = emailState.value,
            label = "Email",
            Color.Gray
        ) {
            emailState.value = it
        }
         /*TextField(value = emailState.value, onValueChange = {
             emailState.value = it
         })*/
        Spacer(modifier = Modifier.height(10.dp))
        RoundedCornerTextField(
            text = passwordState.value,
            label = "Password",
            Color.Gray
        ) {
            passwordState.value = it
        }
        Spacer(modifier = Modifier.height(10.dp))
        //
        if (errorState.value.isNotEmpty())
            Text(
            text = errorState.value,
            color = Color.Red,
            textAlign = TextAlign.Center
                )

        Button(onClick={
            signIn(auth, emailState.value, passwordState.value,
                onSignInSuccess = { navData ->
                    Log.d("Log", "SignIn Success")
                    //showToast( requireContext(),"rgg")
                    onNavigateToMainScreen(navData)
                    updateUser(auth.currentUser)
                },
                onSignInFailure = { error->
                    errorState.value = error
                })
        }){
            Text(text = "Sing In")
        }
        Spacer(modifier = Modifier.height(10.dp))
        Button(onClick={
            signUp(auth, emailState.value, passwordState.value,
                onSignUpSuccess = {
                    Log.d("Log", "SignUp Success")
                },
                onSignUpFailure = { error->
                    errorState.value = error
                })
        }){
            Text(text = "Sing Up")
        }
        //Out + delete
    }
}

fun showToast(context: Context, message: String) {
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}

private fun signUp(auth: FirebaseAuth, email:String, password: String,
                    onSignUpSuccess: () -> Unit, onSignUpFailure: (String) -> Unit){

    if(email.isBlank()||password.isBlank()){
        onSignUpFailure("Enter email and password")
        return
    }

    auth.createUserWithEmailAndPassword(email,password)
        .addOnCompleteListener { task ->
            if (task.isSuccessful){
                Log.d("MyLog", "Sign Up successful")
                onSignUpSuccess()
            }
        }
        .addOnFailureListener{
            onSignUpFailure(it.message?:"Sign Up Error")
        }

}

private fun signIn(auth: FirebaseAuth, email:String, password: String,
                   onSignInSuccess: (MainScreenDataObject) -> Unit, onSignInFailure: (String) -> Unit){

    if(email.isBlank()||password.isBlank()){
        onSignInFailure("Enter email and password")
        return
    }

    auth.signInWithEmailAndPassword(email,password)
        .addOnCompleteListener { task ->
            if (task.isSuccessful){
                Log.d("MyLog", "Sign In successful")
                onSignInSuccess(
                    MainScreenDataObject(
                        task.result.user?.uid!!,
                        task.result.user?.email!!
                    )
                )
            }
        }
        .addOnFailureListener{
            onSignInFailure(it.message?:"Sign In Error")
        }
}

private fun deleteAccount(auth: FirebaseAuth, email:String, password: String){
    val credential = EmailAuthProvider.getCredential(email, password)
    auth.currentUser?.reauthenticate(credential)?.addOnCompleteListener {
        if (it.isSuccessful){
            auth.currentUser?.delete()?.addOnCompleteListener{
                if (it.isSuccessful){
                    Log.d("MyLog", "Account deleted")
                }else{
                    Log.d("MyLog", "Delete failure")
                }
            }
        }else{
            Log.d("MyLog", "Failure reautification")
        }
    }
}

private fun signOut(auth: FirebaseAuth){
    auth.signOut()
}