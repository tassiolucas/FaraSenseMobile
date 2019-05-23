package farasense.mobile.view.ui.dialog

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.widget.Button
import android.widget.SeekBar
import android.widget.Toast
import farasense.mobile.R
import farasense.mobile.view.ui.activity.custom_view.RealTimeCurrentIndicatorView

class RealTimeIndicatorOptionsDialog(val activity: Activity, val realTimeCurrentIndicatorView: RealTimeCurrentIndicatorView) : Dialog(activity) {

    private lateinit var sensitySelector: SeekBar
    private lateinit var saveButton: Button
    private lateinit var saveCancel: Button

    private var sensitityValueSet = 0

    override fun onStart() {
        super.onStart()

        sensitySelector = findViewById(R.id.indicator_sensitivy_seek_bar)
        saveCancel = findViewById(R.id.button_cancel)
        saveButton = findViewById(R.id.button_save)

        sensitySelector.max = 4
        sensitySelector.incrementProgressBy(1)

        sensitySelector.progress = 0

        initDialog()
    }

    private fun initDialog() {

        sensitySelector.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                sensitityValueSet = progress
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {

            }
        })

        saveButton.setOnClickListener {
            realTimeCurrentIndicatorView.setSensitivity(sensitityValueSet)
            Toast.makeText(activity, "Escala de " + sensitityValueSet.toString(), Toast.LENGTH_SHORT) // TODO: Continuar daqui.
        }

        saveCancel.setOnClickListener{
            dismiss()
        }
    }
}