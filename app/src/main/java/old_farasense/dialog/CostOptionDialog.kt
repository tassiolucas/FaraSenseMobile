//package old_farasense.dialog
//
//import android.app.Activity
//import android.app.DatePickerDialog
//import android.app.Dialog
//import android.content.DialogInterface
//import android.os.Bundle
//import android.text.Editable
//import android.text.InputType
//import android.text.TextWatcher
//import android.util.Log
//import android.view.View
//import android.widget.Button
//import android.widget.EditText
//import android.widget.Toast
//import farasense.mobile.R
//import farasense.mobile.databinding.AdapterItemConsumptionCostBinding
//import old_farasense.util.DateUtil
//import old_farasense.util.EnergyUtil
//import old_farasense.util.Preferences
//import org.joda.time.DateTime
//import java.math.BigDecimal
//import java.text.DecimalFormat
//import java.text.NumberFormat
//import java.text.ParseException
//import java.text.SimpleDateFormat
//import java.util.*
//
//class CostOptionDialog(val activity: Activity, itemConsumptionCost: AdapterItemConsumptionCostBinding?) : Dialog(activity) {
//
//    private val zero = 0.0F
//
//    private var maturityDateInput: EditText? = null
//    private var rateKhwInput: EditText? = null
//    private var dateCalendar: Calendar? = null
//    private var rateFlagInput: EditText? = null
//    private var cancelButton: Button? = null
//    private var saveButton: Button? = null
//    private var rateKhw = ""
//    private var rateFlag = ""
//
//    private var rateKhwValue: Double? = null
//    private var rateFlagValue: Double? = null
//    private var itemConsumptionCost = itemConsumptionCost
//
//    internal var dateChose: DatePickerDialog.OnDateSetListener = DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
//        dateCalendar!!.set(Calendar.YEAR, year)
//        dateCalendar!!.set(Calendar.MONTH, month)
//        dateCalendar!!.set(Calendar.DAY_OF_MONTH, dayOfMonth)
//        updateDateLabel()
//    }
//
//    private fun initDialog() {
//        maturityDateInput = findViewById(R.id.maturity_date_input)
//        rateKhwInput = findViewById(R.id.rate_kwh_input)
//        rateFlagInput = findViewById(R.id.rate_flag_input)
//        cancelButton = findViewById(R.id.button_cancel)
//        saveButton = findViewById(R.id.button_save)
//
//        dateCalendar = Calendar.getInstance()
//    }
//
//    override fun onStart() {
//        super.onStart()
//        initDialog()
//
//        maturityDateInput!!.inputType = InputType.TYPE_NULL
//        maturityDateInput!!.setOnClickListener {
//            val datePickerDialog = DatePickerDialog(context,
//                    R.style.dialog_theme,
//                    dateChose,
//                    dateCalendar!!.get(Calendar.YEAR),
//                    dateCalendar!!.get(Calendar.MONTH),
//                    dateCalendar!!.get(Calendar.DAY_OF_MONTH))
//
//            datePickerDialog.show()
//        }
//
//        rateKhwInput!!.addTextChangedListener(object : TextWatcher {
//            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
//
//            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
//                if (count > 0) {
//                    if (s.toString() != rateKhw) {
//                        rateKhwInput!!.removeTextChangedListener(this)
//
//                        val cleanBigDouble = parse(s.toString(), Locale.FRANCE)
//
//                        val cleanString = cleanBigDouble.toString().replace(".", "")
//
//                        val parsed = cleanString.toDouble()
//
//                        val formatted = NumberFormat.getCurrencyInstance().format(parsed / 100)
//
//                        rateKhwValue = parsed / 100
//
//                        if (!formatted.isEmpty()) {
//                            rateKhw = formatted
//                            rateKhwInput!!.setText(formatted)
//                            rateKhwInput!!.setSelection(formatted.length)
//
//                            rateKhwInput!!.addTextChangedListener(this)
//                        }
//                    }
//                }
//            }
//
//            override fun afterTextChanged(s: Editable) {}
//        })
//
//        rateFlagInput!!.addTextChangedListener(object : TextWatcher {
//            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
//
//            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
//                if (count > 0) {
//                    if (s.toString() != rateFlag) {
//                        rateFlagInput!!.removeTextChangedListener(this)
//
//                        val cleanBigDouble = parse(s.toString(), Locale.FRANCE)
//
//                        val cleanString = cleanBigDouble.toString().replace(".", "")
//
//                        val parsed = cleanString.toDouble()
//
//                        val formatted = NumberFormat.getCurrencyInstance().format(parsed / 100)
//
//                        rateFlagValue = parsed / 100
//
//                        if (!formatted.isEmpty()) {
//                            rateFlag = formatted
//                            rateFlagInput!!.setText(formatted)
//                            rateFlagInput!!.setSelection(formatted.length)
//
//                            rateFlagInput!!.addTextChangedListener(this)
//                        }
//                    }
//                }
//            }
//
//            override fun afterTextChanged(s: Editable) {}
//        })
//
//        // Controles do Cost Dialog Option
//        cancelButton!!.setOnClickListener { this.dismiss() }
//
//        saveButton!!.setOnClickListener {
//            if (dateCalendar != null && rateKhwValue != null && rateKhwValue != zero.toDouble()) {
//                Preferences.getInstance(context).setMaturityDate(dateCalendar!!.time)
//                Preferences.getInstance(context).rateKwh = rateKhwValue!!.toFloat()
//                if(rateFlagValue != null && rateFlagValue != zero.toDouble())
//                    Preferences.getInstance(context).rateFlag = rateFlagValue!!.toFloat()
//
//                val maturityDate = DateTime(dateCalendar!!.timeInMillis)
//                val startDate = DateUtil.get30DaysAgo(maturityDate)
//
//                itemConsumptionCost?.labelEndPeriodComsumption?.visibility = View.VISIBLE
//                itemConsumptionCost?.labelStartPeriodComsumption?.visibility = View.VISIBLE
//                itemConsumptionCost?.labelAo?.visibility = View.VISIBLE
//                itemConsumptionCost?.labelCostComsumption?.visibility = View.VISIBLE
//
//                itemConsumptionCost?.labelCostComsumption?.text = "???"
//                itemConsumptionCost?.labelStartPeriodComsumption?.text = startDate.dayOfMonth.toString() + "/" + startDate.monthOfYear.toString()
//                itemConsumptionCost?.labelEndPeriodComsumption?.text = maturityDate!!.dayOfMonth.toString() + "/" + maturityDate!!.monthOfYear.toString()
//
//                val totalCost = if (rateFlagValue != null && rateFlagValue != 0.0)
//                    EnergyUtil.getValueCost(maturityDate, rateKhwValue!!.toFloat(), rateFlagValue!!.toFloat())
//                else
//                    EnergyUtil.getValueCost(maturityDate, rateKhwValue!!.toFloat())
//
//                val format = DecimalFormat("0.00")
//
//                val formatted = format.format(totalCost)
//                if (totalCost != null && totalCost != zero.toDouble()) {
//                    itemConsumptionCost?.labelCostComsumption?.text = "RS $formatted"
//                }
//
//                dismiss()
//
//                Toast.makeText(context, R.string.alert_save_sucess, Toast.LENGTH_SHORT).show()
//            } else {
//                Toast.makeText(context, R.string.alert_empty_or_value_invalid, Toast.LENGTH_SHORT).show()
//            }
//        }
//    }
//
//    override fun setOnDismissListener(listener: DialogInterface.OnDismissListener?) {
//        super.setOnDismissListener(listener)
//        this.setOnDismissListener {  }
//    }
//
//    private fun updateDateLabel() {
//        val myFormat = "dd/MM/yyyy" //In which you need put here
//        val sdf = SimpleDateFormat(myFormat, Locale("pt", "BR"))
//
//        maturityDateInput!!.setText(sdf.format(dateCalendar!!.time))
//    }
//
//    // Para converter a moeda para Double novamente
//    @Throws(ParseException::class)
//    fun parse(amount: String, locale: Locale): BigDecimal {
//        val format = NumberFormat.getNumberInstance(locale)
//        if (format is DecimalFormat) {
//            format.isParseBigDecimal = true
//        }
//        return format.parse(amount.replace("[^\\d.,]".toRegex(), "")) as BigDecimal
//    }
//}