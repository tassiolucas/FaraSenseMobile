package farasense.mobile.view.ui.dialog

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.widget.Button
import android.widget.SeekBar
import farasense.mobile.R

class RealTimeIndicatorOptionsDialog(val activity: Activity) : Dialog(activity) {

    private lateinit var sensitySelector: SeekBar
    private lateinit var saveButton: Button
    private lateinit var saveCancel: Button



    override fun onStart() {
        super.onStart()

        sensitySelector = findViewById(R.id.indicator_sensitivy_seek_bar)
        saveCancel = findViewById(R.id.button_cancel)
        saveButton = findViewById(R.id.button_save)

        sensitySelector.progress = 0
        sensitySelector.max = 100
        sensitySelector.incrementProgressBy(10)

        initDialog()
    }

    private fun initDialog() {

        sensitySelector.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {

            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {

            }
        })

        saveButton.setOnClickListener {

        }

        saveCancel.setOnClickListener{

        }
    }
}