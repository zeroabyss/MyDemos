package com.example.aiy.crime;


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

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by Aiy on 2016/12/3.
 */

public class DatePickerFragment extends DialogFragment {
    private DatePicker datePicker;
    public static final String EXTRA_DATE="DatePickerFragment_date";
    private static final String ARG_DATE="date";
    public static DatePickerFragment newIntent(Date date){
        Bundle arg=new Bundle();
        arg.putSerializable(ARG_DATE,date);
        DatePickerFragment fragment=new DatePickerFragment();
        fragment.setArguments(arg);
        return fragment;
    }



    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View v= LayoutInflater.from(getActivity())
                .inflate(R.layout.dialog_date,null);

        Date date=(Date)getArguments().getSerializable(ARG_DATE);
        Calendar calendar=Calendar.getInstance();
        calendar.setTime(date);
        int year=calendar.get(Calendar.YEAR);
        int month=calendar.get(Calendar.MONTH);
        int day=calendar.get(Calendar.DAY_OF_MONTH);

        datePicker=(DatePicker)v.findViewById(R.id.date_picker);
        datePicker.init(year,month,day,null);

        return new AlertDialog.Builder(getActivity())
                .setView(v)
                .setTitle(R.string.date_picker_title)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int year=datePicker.getYear();
                        int month=datePicker.getMonth();
                        int day=datePicker.getDayOfMonth();
                        Date date=new GregorianCalendar(year,month,day).getTime();
                        sentResult(Activity.RESULT_OK,date);
                    }
                })
                .create();
    }
    private void sentResult(int resultCode,Date date){
        if (getTargetFragment()==null){
            return;
        }
        Intent intent=new Intent();
        intent.putExtra(EXTRA_DATE,date);
        getTargetFragment().onActivityResult(getTargetRequestCode(),resultCode,intent);
    }
}
