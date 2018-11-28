package farasense.mobile.view.ui.dialog;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import farasense.mobile.R;
import farasense.mobile.databinding.DashboardDataBinding;
import farasense.mobile.util.Preferences;
import farasense.mobile.view.ui.activity.DashboardActivity;

public class CostOptionDialog extends Dialog {

    private EditText maturityDateInput;
    private EditText rateKhwInput;
    private Calendar dateCalendar;
    private EditText rateFlagInput;
    private Button cancelButton;
    private Button saveButton;
    private String rateKhw = "";
    private String rateFlag = "";

    private Double rateKhwValue;
    private Double rateFlagValue;
    private Activity activity;

    public CostOptionDialog(Activity activity) {
        super(activity);

        this.activity = activity;
    }

    @Override
    protected void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
    }

    private void initDialog() {
        maturityDateInput = findViewById(R.id.maturity_date_input);
        rateKhwInput = findViewById(R.id.rate_kwh_input);
        rateFlagInput = findViewById(R.id.rate_flag_input);
        cancelButton = findViewById(R.id.button_cancel);
        saveButton = findViewById(R.id.button_save);

        dateCalendar = Calendar.getInstance();
    }

    @Override
    protected void onStart(){
        super.onStart();
        initDialog();

        maturityDateInput.setInputType(InputType.TYPE_NULL);
        maturityDateInput.setOnClickListener(v -> {
            DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), R.style.dialog_theme, dateChose, dateCalendar.get(Calendar.YEAR), dateCalendar.get(Calendar.MONTH), dateCalendar.get(Calendar.DAY_OF_MONTH));
            datePickerDialog.show();
        });

        rateKhwInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!s.toString().equals(rateKhw)){
                    rateKhwInput.removeTextChangedListener(this);

                    String cleanString = s.toString().replaceAll("[R$,.]", "");

                    double parsed = Double.parseDouble(cleanString);
                    String formatted = NumberFormat.getCurrencyInstance().format((parsed/100));

                    rateKhwValue = (parsed/100);

                    if (!formatted.isEmpty()) {
                        rateKhw = formatted;
                        rateKhwInput.setText(formatted);
                        rateKhwInput.setSelection(formatted.length());

                        rateKhwInput.addTextChangedListener(this);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        rateFlagInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!s.toString().equals(rateFlag)){
                    rateFlagInput.removeTextChangedListener(this);

                    String cleanString = s.toString().replaceAll("[R$,.]", "");

                    double parsed = Double.parseDouble(cleanString);
                    String formatted = NumberFormat.getCurrencyInstance().format((parsed/100));

                    rateFlagValue = (parsed/100);

                    if (!formatted.isEmpty()) {
                        rateFlag = formatted;
                        rateFlagInput.setText(formatted);
                        rateFlagInput.setSelection(formatted.length());

                        rateFlagInput.addTextChangedListener(this);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        // Controles do Cost Dialog Option
        cancelButton.setOnClickListener(v -> {
            this.dismiss();
        });

        saveButton.setOnClickListener(v -> {
            if (dateCalendar != null && rateKhwValue != null && rateFlagValue != null && rateKhwValue != 0 && rateFlagValue != 0) {
                Preferences.getInstance().setMaturityDate(dateCalendar.getTime());
                Preferences.getInstance().setRateKwh(rateKhwValue.floatValue());
                Preferences.getInstance().setRateFlag(rateFlagValue.floatValue());

                dismiss();

                Intent intent = activity.getIntent();
                activity.finish();
                activity.startActivity(intent);

                Toast.makeText(getContext(), R.string.alert_save_sucess, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), R.string.alert_empty_or_value_invalid, Toast.LENGTH_SHORT).show();
            }
        });
    }

    DatePickerDialog.OnDateSetListener dateChose = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            dateCalendar.set(Calendar.YEAR, year);
            dateCalendar.set(Calendar.MONTH, month);
            dateCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateDateLabel();
        }
    };

    private void updateDateLabel() {
        String myFormat = "dd/MM/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, new Locale("pt","BR"));

        maturityDateInput.setText(sdf.format(dateCalendar.getTime()));
    }
}