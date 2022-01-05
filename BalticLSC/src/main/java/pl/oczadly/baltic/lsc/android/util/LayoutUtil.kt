package pl.oczadly.baltic.lsc.android.util

import android.R
import android.content.Context
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView

fun <T> addSpinnerToViewGroup(values: List<T>, layout: ViewGroup, context: Context): Spinner {
    val spinner = Spinner(context)
    spinner.adapter = ArrayAdapter(
        context,
        R.layout.simple_spinner_dropdown_item,
        values
    )
    layout.addView(spinner)

    return spinner
}

fun addTextViewToViewGroup(value: String, layout: ViewGroup, context: Context): TextView {
    val textView = TextView(context)
    textView.text = value
    layout.addView(textView)

    return textView
}

fun convertDpToPixels(dp: Int, density: Float): Int = (dp * density + 0.5f).toInt()