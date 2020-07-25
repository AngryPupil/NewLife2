package net.pupil.newlife.crimialintent;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;

import net.pupil.newlife.R;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by Angry on 2018/2/27.
 */

public class DatePickerFragment extends DialogFragment {

    public static final String EXTRA_DATE =
            "com.bignerdranch.android.criminalintent.date";
    private static final String ARG_CAlENDAR = "calendar";

    private DatePicker mDatePicker;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_date, null);
        mDatePicker = (DatePicker) view.findViewById(R.id.dialog_date_date_picker);
        Calendar calendar = (Calendar) getArguments().getSerializable(ARG_CAlENDAR);
        if (calendar == null) {
            calendar = Calendar.getInstance();
        }
        mDatePicker.init(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), null);
        return new AlertDialog.Builder(getActivity())
                .setTitle(R.string.date_picker_title)
                .setView(view)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int year = mDatePicker.getYear();
                        int month = mDatePicker.getMonth();
                        int day = mDatePicker.getDayOfMonth();
                        Calendar calendar = new GregorianCalendar(year, month, day);
                        sendResult(calendar.getTime());
                    }
                })
                .create();
    }

    private void sendResult(Date date) {
        if (getTargetFragment() == null) {
            return;
        }
        Intent data = new Intent();
        data.putExtra(EXTRA_DATE, date);
        getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, data);
    }

    public static DatePickerFragment newInstance(Calendar calendar) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(ARG_CAlENDAR, calendar);
        DatePickerFragment datePickerFragment = new DatePickerFragment();
        datePickerFragment.setArguments(bundle);
        return datePickerFragment;
    }

}
