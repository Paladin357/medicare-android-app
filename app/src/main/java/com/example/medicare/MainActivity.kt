package com.example.medicare

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme(
                colorScheme = lightColorScheme(
                    primary = Color(0xFF00796B),
                    onPrimary = Color.White,
                    secondary = Color(0xFF004D40),
                    background = Color(0xFFF4F7F6),
                    surface = Color.White
                )
            ) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    RefillRequestScreen(
                        onRequestSuccess = { patientName, category, quantity ->
                            // Explicit Intent to navigate to ReceiptActivity
                            val intent = Intent(this@MainActivity, ReceiptActivity::class.java).apply {
                                putExtra("EXTRA_PATIENT_INFO", patientName)
                                putExtra("EXTRA_CATEGORY", category)
                                putExtra("EXTRA_QUANTITY", quantity)
                            }
                            startActivity(intent)
                        }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RefillRequestScreen(
    onRequestSuccess: (patientName: String, category: String, quantity: Int) -> Unit
) {
    val context = LocalContext.current

    // Form state variables
    var patientInput by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("Tablets") }
    var quantityText by remember { mutableStateOf("1") }

    // Validation error states
    var patientError by remember { mutableStateOf<String?>(null) }
    var quantityError by remember { mutableStateOf<String?>(null) }

    val categories = listOf("Tablets", "Syrup", "Capsules")
    val scrollState = rememberScrollState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "MediCare",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "• Refill Request",
                            color = Color(0xFFB2DFDB),
                            fontSize = 16.sp
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 20.dp, vertical = 16.dp)
                .verticalScroll(scrollState),
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            // Header information card
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFE0F2F1)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Prescription Refill System",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.secondary
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Enter patient identification, select medication type and specify quantity to submit your refill request.",
                        fontSize = 13.sp,
                        color = Color(0xFF37474F),
                        lineHeight = 18.sp
                    )
                }
            }

            // 1. Patient Name / ID input field
            OutlinedTextField(
                value = patientInput,
                onValueChange = {
                    patientInput = it
                    if (it.isNotBlank()) patientError = null
                },
                label = { Text("Patient Name or Patient ID") },
                placeholder = { Text("e.g. John Doe or PID-5821") },
                isError = patientError != null,
                supportingText = {
                    patientError?.let {
                        Text(text = it, color = MaterialTheme.colorScheme.error)
                    }
                },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(10.dp)
            )

            // 2. Medicine Category / Dosage Form Selection
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(1.dp, Color(0xFFCFD8DC))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Select Dosage Form / Category",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 15.sp,
                        color = Color(0xFF263238)
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    categories.forEach { category ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp))
                                .clickable { selectedCategory = category }
                                .padding(vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = (category == selectedCategory),
                                onClick = { selectedCategory = category },
                                colors = RadioButtonDefaults.colors(
                                    selectedColor = MaterialTheme.colorScheme.primary
                                )
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = category,
                                fontSize = 15.sp,
                                fontWeight = if (category == selectedCategory) FontWeight.SemiBold else FontWeight.Normal,
                                color = Color(0xFF37474F)
                            )
                        }
                    }
                }
            }

            // 3. Quantity / Duration Selection
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(1.dp, Color(0xFFCFD8DC))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Quantity / Duration (Units)",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 15.sp,
                        color = Color(0xFF263238)
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        // Decrement Button
                        OutlinedButton(
                            onClick = {
                                val current = quantityText.toIntOrNull() ?: 1
                                if (current > 1) {
                                    quantityText = (current - 1).toString()
                                    quantityError = null
                                }
                            },
                            shape = RoundedCornerShape(8.dp),
                            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
                        ) {
                            Text(text = "−", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                        }

                        // Direct Numeric Input
                        OutlinedTextField(
                            value = quantityText,
                            onValueChange = {
                                quantityText = it
                                if (it.isNotBlank() && (it.toIntOrNull() ?: 0) > 0) {
                                    quantityError = null
                                }
                            },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            singleLine = true,
                            isError = quantityError != null,
                            modifier = Modifier.width(120.dp),
                            shape = RoundedCornerShape(8.dp)
                        )

                        // Increment Button
                        OutlinedButton(
                            onClick = {
                                val current = quantityText.toIntOrNull() ?: 0
                                quantityText = (current + 1).toString()
                                quantityError = null
                            },
                            shape = RoundedCornerShape(8.dp),
                            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
                        ) {
                            Text(text = "+", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                        }
                    }

                    quantityError?.let {
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = it,
                            color = MaterialTheme.colorScheme.error,
                            fontSize = 12.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            // 4. Request Button with Input Validation & Navigation
            Button(
                onClick = {
                    var isValid = true

                    // Validation: Patient Name/ID must not be empty
                    if (patientInput.trim().isEmpty()) {
                        patientError = "Patient Name or Patient ID is required"
                        isValid = false
                    } else {
                        patientError = null
                    }

                    // Validation: Quantity must be valid integer > 0
                    val parsedQuantity = quantityText.toIntOrNull()
                    if (parsedQuantity == null || parsedQuantity <= 0) {
                        quantityError = "Please enter a valid quantity greater than 0"
                        isValid = false
                    } else {
                        quantityError = null
                    }

                    if (isValid) {
                        Toast.makeText(
                            context,
                            "Validation successful! Navigating to Receipt...",
                            Toast.LENGTH_SHORT
                        ).show()
                        onRequestSuccess(patientInput.trim(), selectedCategory, parsedQuantity!!)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Text(
                    text = "Request Refill",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
