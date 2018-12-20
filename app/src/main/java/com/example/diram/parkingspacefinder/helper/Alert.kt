package com.example.diram.parkingspacefinder.helper

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.example.diram.parkingspacefinder.R


/**
 * Created by chris on 3/3/2018. for annisa
 */
class Alert(private val context: AppCompatActivity) {

    private var alert: AlertDialog.Builder = AlertDialog.Builder(context, R.style.MyDialogAnimationTheme)
    private lateinit var alertDialog: AlertDialog

    init {

    }

    fun setPositiveBtn(name: String, onclick: AlertClick) {
        alert.setPositiveButton(name) { _, _ ->
            onclick.onClicked()
        }
    }

    fun setPositiveBtn(name: String) {
        alert.setPositiveButton(name) { _, _ ->

        }
    }

    fun setView(view: View) {
        alert.setView(view)
    }

    fun setView(resource: Int): View {
        val view = context.layoutInflater.inflate(resource, null, false)
        setView(view)
        return view
    }

    fun setNegativeBtn(name: String, onclick: AlertClick) {
        alert.setNegativeButton(name) { _, _ ->
            onclick.onClicked()
        }
    }


    fun setNegativeBtn(name: String) {
        alert.setNegativeButton(name) { _, _ ->
        }
    }

    fun setTitle(title: String) {
        alert.setTitle(title)
    }


    fun showAlertDialogue(defaultDialogue: Boolean) {
        if (defaultDialogue) {
            alert.show()
            return
        }
        alertDialog = alert.create()
        alertDialog.window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        alertDialog.show()
    }

    fun dismissAlertDialogue() {
        alertDialog.dismiss()
    }

    fun setCancelable(boolean: Boolean) {
        alert.setCancelable(boolean)
    }

    fun onCanceled(alertCancel: AlertCancel) {
        alertDialog.setOnCancelListener {
            alertCancel.onCanceled()
        }
    }


    fun setMessage(message: String) {
        alert.setMessage(message)
    }

}