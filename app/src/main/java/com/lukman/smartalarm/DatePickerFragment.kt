package com.lukman.smartalarm

import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.DatePicker
import androidx.fragment.app.DialogFragment
import java.util.*

class DatePickerFragment : DialogFragment(), DatePickerDialog.OnDateSetListener { //DialogFragment untuk menggabungkan si fragment sama si activity

    private var dialogListener: DateDialogListener? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val date = calendar.get(Calendar.DAY_OF_MONTH)
        return DatePickerDialog(activity as Context, this, year, month, date)
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        dialogListener = context as DateDialogListener
    }

    override fun onDetach() { // Jaga jaga biar gak error Date picker biar g muncul dua kali
        super.onDetach()
        if (dialogListener != null) dialogListener = null
    }

    override fun onDateSet(p0: DatePicker?, year: Int, month: Int, dayOfMoth: Int) {
        dialogListener?.onDialogDateSet(tag, year, month, dayOfMoth)
        Log.i(tag, "onDateSet: $year $month $dayOfMoth") //untuk memunculkan tag pada verbose logcat
    }

    interface DateDialogListener { //buat tampilan di Activity yang dapat Nilai inputan yang sudah dipilih
        fun onDialogDateSet(tag: String?, year: Int, month: Int, dayOfMoth: Int)
    }
}