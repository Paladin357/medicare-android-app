# MediCare Android Application

Implementation of **CSE224 (Fundamentals of Android) - Practical 1 / SET-2**

## Features Implemented
1. **Refill Request Screen (`MainActivity`)**:
   - Patient Name or Patient ID input with validation (cannot be empty).
   - Medicine Category / Dosage Form selection (`Tablets`, `Syrup`, `Capsules`).
   - Quantity/duration selector with increment/decrement steppers and numeric validation (> 0).
   - "Request Refill" validation button that navigates via **Explicit Intent**.

2. **Order Receipt Screen (`ReceiptActivity`)**:
   - Receives data passed via Explicit Intent (`EXTRA_PATIENT_INFO`, `EXTRA_CATEGORY`, `EXTRA_QUANTITY`).
   - Formatted prescription refill receipt with itemized price calculation:
     - Tablets: ₹15.00/unit
     - Syrup: ₹85.00/bottle
     - Capsules: ₹25.00/unit
     - Dispensing & Handling Fee: ₹10.00
     - Estimated Total Cost
   - **Activity Lifecycle Logging in Logcat** during initialization (`onCreate`) and other lifecycle phases (`onStart`, `onResume`, `onPause`, `onStop`, `onDestroy`).
   - "Return to Request Screen" button that safely finishes the receipt activity and returns to the input screen.

## How to Test in Android Studio:
1. Open Android Studio and choose **Open**, selecting this directory `C:\Users\HomePC\.gemini\antigravity\scratch\medicare`.
2. Sync Gradle and Run on an Emulator or connected Physical Device.
3. Open the **Logcat** tab at the bottom of Android Studio and filter by:
   ```text
   ReceiptActivityLifecycle
   ```
4. Enter patient details, select dosage form and quantity, and click **Request Refill**.
5. Observe the lifecycle log message in Logcat upon initialization.
6. Click **Return to Request Screen** to navigate back.
