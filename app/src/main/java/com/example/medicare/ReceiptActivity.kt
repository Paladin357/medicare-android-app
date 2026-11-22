package com.example.medicare

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ReceiptActivity : ComponentActivity() {

    companion object {
        private const val TAG = "ReceiptActivityLifecycle"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 1. Receive data passed from MainActivity via Explicit Intent
        val patientInfo = intent.getStringExtra("EXTRA_PATIENT_INFO") ?: "Unknown Patient"
        val category = intent.getStringExtra("EXTRA_CATEGORY") ?: "Tablets"
        val quantity = intent.getIntExtra("EXTRA_QUANTITY", 1)

        // 2. Requirement: Log an Activity lifecycle message in Logcat during initialization
        Log.i(
            TAG,
            "onCreate() initialized: Refill Request Received -> Patient: $patientInfo | Category: $category | Quantity: $quantity"
        )

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
                    ReceiptScreen(
                        patientInfo = patientInfo,
                        category = category,
                        quantity = quantity,
                        onBackClicked = {
                            Log.i(TAG, "Return button clicked: finishing ReceiptActivity to return to MainActivity.")
                            finish()
                        }
                    )
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        Log.i(TAG, "onStart() lifecycle called: ReceiptActivity is becoming visible.")
    }

    override fun onResume() {
        super.onResume()
        Log.i(TAG, "onResume() lifecycle called: ReceiptActivity is now interactive.")
    }

    override fun onPause() {
        super.onPause()
        Log.i(TAG, "onPause() lifecycle called: ReceiptActivity is pausing.")
    }

    override fun onStop() {
        super.onStop()
        Log.i(TAG, "onStop() lifecycle called: ReceiptActivity is no longer visible.")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.i(TAG, "onDestroy() lifecycle called: ReceiptActivity is being destroyed.")
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReceiptScreen(
    patientInfo: String,
    category: String,
    quantity: Int,
    onBackClicked: () -> Unit
) {
    // Pricing calculation based on medicine category
    val unitPrice = when (category) {
        "Tablets" -> 15.00
        "Syrup" -> 85.00
        "Capsules" -> 25.00
        else -> 20.00
    }
    val dispensingFee = 10.00
    val subtotal = unitPrice * quantity
    val estimatedTotalCost = subtotal + dispensingFee

    val formattedDate = SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault()).format(Date())
    val orderId = "MC-${(100000..999999).random()}"
    val scrollState = rememberScrollState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "MediCare - Order Receipt",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClicked) {
                        Text(
                            text = "←",
                            color = Color.White,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold
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
            // Confirmation Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFE8F5E9)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "✓",
                        color = Color(0xFF2E7D32),
                        fontSize = 26.sp,
                        fontWeight = FontWeight.ExtraBold
                    )
                    Spacer(modifier = Modifier.width(14.dp))
                    Column {
                        Text(
                            text = "Prescription Refill Approved",
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1B5E20),
                            fontSize = 16.sp
                        )
                        Text(
                            text = "Your request has been verified and recorded.",
                            fontSize = 13.sp,
                            color = Color(0xFF388E3C)
                        )
                    }
                }
            }

            // Receipt Paper Layout Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(1.dp, Color(0xFFCFD8DC)),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "OFFICIAL RECEIPT",
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = orderId,
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 13.sp,
                            color = Color(0xFF78909C)
                        )
                    }

                    HorizontalDivider(color = Color(0xFFECEFF1), thickness = 1.dp)

                    // Order and Patient Details
                    ReceiptRow(label = "Patient Name / ID", value = patientInfo)
                    ReceiptRow(label = "Date & Time", value = formattedDate)
                    ReceiptRow(label = "Dosage Form / Category", value = category)
                    ReceiptRow(label = "Prescribed Quantity", value = "$quantity unit(s)")

                    HorizontalDivider(color = Color(0xFFECEFF1), thickness = 1.dp)

                    // Pricing breakdown
                    ReceiptRow(label = "Unit Price ($category)", value = "₹${"%.2f".format(unitPrice)}")
                    ReceiptRow(label = "Subtotal ($quantity × ₹${"%.2f".format(unitPrice)})", value = "₹${"%.2f".format(subtotal)}")
                    ReceiptRow(label = "Dispensing / Service Fee", value = "₹${"%.2f".format(dispensingFee)}")

                    HorizontalDivider(color = MaterialTheme.colorScheme.primary, thickness = 2.dp)

                    // Estimated Total Cost
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Estimated Total Cost",
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            color = MaterialTheme.colorScheme.secondary
                        )
                        Text(
                            text = "₹${"%.2f".format(estimatedTotalCost)}",
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 20.sp,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            // Return to Request Screen Button
            Button(
                onClick = onBackClicked,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Text(
                    text = "Return to Request Screen",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun ReceiptRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            fontSize = 14.sp,
            color = Color(0xFF546E7A)
        )
        Text(
            text = value,
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color(0xFF263238)
        )
    }
}
