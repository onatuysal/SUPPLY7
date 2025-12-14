package com.supply7.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.supply7.app.ui.theme.SUPPLY7Theme
import android.util.Log
import androidx.lifecycle.lifecycleScope
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.ui.unit.dp


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        val productsState = mutableStateOf<List<Map<String, Any>>>(emptyList())
        val db = FirebaseFirestore.getInstance()

        db.collection("products")
            .get()
            .addOnSuccessListener { result ->
                val list = result.documents.mapNotNull { it.data }
                productsState.value = list
                Log.d("SUPPLY7", "READ OK: ${list.size} items")
            }
            .addOnFailureListener { e ->
                Log.e("SUPPLY7", "READ FAIL", e)
            }
        setContent {
            SUPPLY7Theme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }


        val product = hashMapOf(
            "name" to "Deneme Ürün",
            "price" to 150
        )

        db.collection("products")
            .add(product)
            .addOnSuccessListener {
                Log.d("SUPPLY7", "Firestore write OK")
            }
            .addOnFailureListener { e ->
                Log.e("SUPPLY7", "Firestore write FAIL", e)
            }
        db.collection("products")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    Log.d(
                        "SUPPLY7",
                        "READ -> ${document.id} => ${document.data}"
                    )
                }
            }
            .addOnFailureListener { e ->
                Log.e("SUPPLY7", "Firestore read FAIL", e)
            }

    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    SUPPLY7Theme {
        Greeting("Android")
    }
}