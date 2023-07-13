package com.abdulrahman.ecommerce.util

import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import com.abdulrahman.ecommerce.R
import com.google.android.material.bottomsheet.BottomSheetDialog

fun Fragment.showBottomSheetDialog(callback: BottomSheetCallback) {
    val dialog = BottomSheetDialog(requireContext())
    val dialogView = layoutInflater.inflate(R.layout.fragment_reset_password_dialog, null)
    dialog.setContentView(dialogView)

    val buttonCancel = dialog.findViewById<Button>(R.id.buttonCancel)
    val buttonReset = dialog.findViewById<Button>(R.id.buttonReset)
    val editTextEmail = dialog.findViewById<EditText>(R.id.editTextEmail)

    dialog.show()

    buttonCancel?.setOnClickListener {
        dialog.dismiss()
    }
    buttonReset?.setOnClickListener {
        callback.onDataReceived(editTextEmail?.text.toString())
        dialog.dismiss()
    }
}
interface BottomSheetCallback {
    fun onDataReceived(data: String)
}