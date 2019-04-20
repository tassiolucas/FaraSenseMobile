package farasense.mobile.view.ui.dialog

import android.app.Activity
import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import farasense.mobile.R
import farasense.mobile.util.Preferences

class CostOptionDialog(private val activity: Activity) : Dialog(activity) {

    private var maturityDateInput: EditText? = null
    private var rateKhwInput: EditText? = null
    private var dateCalendar: Calendar? = null
    private var rateFlagInput: EditText? = null
    private var cancelButton: Button? = null
    private var saveButton: Button? = null
    private var rateKhw = ""
    private var rateFlag = ""

    private var rateKhwValue: Double? = null
    private var rateFlagValue: Double? = null

    internal var dateChose: DatePickerDialog.OnDateSetListener = DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
        dateCalendar!!.set(Calendar.YEAR, year)
        dateCalendar!!.set(Calendar.MONTH, month)
        dateCalendar!!.set(Calendar.DAY_OF_MONTH, dayOfMonth)
        updateDateLabel()
    }

    private fun initDialog() {
        maturityDateInput = findViewById(R.id.maturity_date_input)
        rateKhwInput = findViewById(R.id.rate_kwh_input)
        rateFlagInput = findViewById(R.id.rate_flag_input)
        cancelButton = findViewById(R.id.button_cancel)
        saveButton = findViewById(R.id.button_save)

        dateCalendar = Calendar.getInstance()
    }

    override fun onStart() {
        super.onStart()
        initDialog()

        maturityDateInput!!.inputType = InputType.TYPE_NULL
        maturityDateInput!!.setOnClickListener { v ->
            val datePickerDialog = DatePickerDialog(context, R.style.dialog_theme, dateChose, dateCalendar!!.get(Calendar.YEAR), dateCalendar!!.get(Calendar.MONTH), dateCalendar!!.get(Calendar.DAY_OF_MONTH))
            datePickerDialog.show()
        }

        rateKhwInput!!.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (s.toString() != rateKhw) {
                    rateKhwInput!!.removeTextChangedListener(this)

                    val cleanString = s.toString().replace("[R$,.]".toRegex(), "")

                    val parsed = java.lang.Double.parseDouble(cleanString)
                    val formatted = NumberFormat.getCurrencyInstance().format(parsed / 100)

                    rateKhwValue = parsed / 100

                    if (!formatted.isEmpty()) {
                        rateKhw = formatted
                        rateKhwInput!!.setText(formatted)
                        rateKhwInput!!.setSelection(formatted.length)

                        rateKhwInput!!.addTextChangedListener(this)
                    }
                }
            }

            override fun afterTextChanged(s: Editable) {}
        })

        rateFlagInput!!.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (s.toString() != rateFlag) {
                    rateFlagInput!!.removeTextChangedListener(this)

                    val cleanString = s.toString().replace("[R$,.]".toRegex(), "")

                    val parsed = java.lang.Double.parseDouble(cleanString)
                    val formatted = NumberFormat.getCurrencyInstance().format(parsed / 100)

                    rateFlagValue = parsed / 100

                    if (!formatted.isEmpty()) {
                        rateFlag = formatted
                        rateFlagInput!!.setText(formatted)
                        rateFlagInput!!.setSelection(formatted.length)

                        rateFlagInput!!.addTextChangedListener(this)
                    }
                }
            }

            override fun afterTextChanged(s: Editable) {}
        })

        // Controles do Cost Dialog Option
        cancelButton!!.setOnClickListener { this.dismiss() }

        saveButton!!.setOnClickListener {
            if (dateCalendar != null && rateKhwValue != null && rateFlagValue != null && rateKhwValue != 0.0 && rateFlagValue != 0.0) {
                Preferences.getInstance().setMaturityDate(dateCalendar!!.time)
                Preferences.getInstance().rateKwh = rateKhwValue!!.toFloat()
                Preferences.getInstance().rateFlag = rateFlagValue!!.toFloat()

                dismiss()

                val intent = activity.intent
                activity.finish()
                activity.startActivity(intent)

                Toast.makeText(context, R.string.alert_save_sucess, Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, R.string.alert_empty_or_value_invalid, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun updateDateLabel() {
        val myFormat = "dd/MM/yyyy" //In which you need put here
        val sdf = SimpleDateFormat(myFormat, Locale("pt", "BR"))

        maturityDateInput!!.setText(sdf.format(dateCalendar!!.time))
    }
}