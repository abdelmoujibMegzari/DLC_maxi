package com.example.dlc_maxi;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    private TextView begin;
    private Date endDate;
    private Date beginDate;
    private boolean beginSet = false;
    private TextView end;
    private boolean endSet = false;
    private DatePickerDialog.OnDateSetListener beginDateSetListener;
    private DatePickerDialog.OnDateSetListener endDateSetListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.begin = findViewById(R.id.dateDebut);
        this.end = findViewById(R.id.dateFin);
        Button verifier = findViewById(R.id.verify);
        Button clear = findViewById(R.id.clear);
        TextView response =findViewById(R.id.response);
        this.beginDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                beginDate = new Date(year-1900,month,day);
                begin.setText(DateFormat.getDateInstance(DateFormat.SHORT, Locale.FRANCE).format(beginDate));
                beginSet=true;
            }
        };

        this.begin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal =Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);
                Context c = getApplicationContext();
                DatePickerDialog dialog = new DatePickerDialog(MainActivity.this,
                        AlertDialog.THEME_HOLO_LIGHT,
                        beginDateSetListener,
                        year,
                        month,
                        1);
                //dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });
        this.endDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int mounth, int day) {
                endDate = new Date(year-1900,mounth,day);
                end.setText(DateFormat.getDateInstance(DateFormat.SHORT, Locale.FRANCE).format(endDate));
                endSet=true;
            }
        };
        this.end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal =Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);
                Context c = getApplicationContext();
                DatePickerDialog dialog = new DatePickerDialog(MainActivity.this,
                        AlertDialog.THEME_HOLO_LIGHT,
                        endDateSetListener,
                        year,
                        month,
                        1);
                dialog.show();
            }
        });
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                beginSet=false;
                endSet=false;
                begin.setText("Date de production");
                end.setText("Date d' expiration");
                response.setText("");
                getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
            }
        });
        verifier.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (beginSet && endSet) {
                    if (accepted(beginDate, endDate)) {
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                            Calendar day = Calendar.getInstance();
                            Date today = new Date(day.get(Calendar.YEAR)-1900,day.get(Calendar.MONTH),day.get(Calendar.DAY_OF_MONTH));
                            LocalDate Limit = (new Date((endDate.getTime()-beginDate.getTime())/3+beginDate.getTime())).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                            Period diff = Period.between(today.toInstant().atZone(ZoneId.systemDefault()).toLocalDate(), Limit);
                            Log.d(TAG,Limit.toString());
                            response.setText("reste: "+diff.getYears()+ " ans" +diff.getMonths() + " mois"+ diff.getDays() + " jours");
                        }
                        getWindow().setBackgroundDrawable(new ColorDrawable(Color.GREEN));
                    } else {
                        getWindow().setBackgroundDrawable(new ColorDrawable(Color.RED));
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                            Calendar day = Calendar.getInstance();
                            Date today = new Date(day.get(Calendar.YEAR)-1900,day.get(Calendar.MONTH),day.get(Calendar.DAY_OF_MONTH));
                            LocalDate Limit = (new Date((endDate.getTime()-beginDate.getTime())/3+beginDate.getTime())).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                            Period diff = Period.between(Limit, today.toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
                            Log.d(TAG,Limit.toString());
                            response.setText("dépassé de : "+diff.getYears()+ " ans" +diff.getMonths() + " mois"+ diff.getDays() + " jours");
                        }
                    }
                } else {
                    Context c = getApplicationContext();
                    Toast toast = Toast.makeText(c, "veillez saisir les date", Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });


    }


    private boolean accepted(Date dateProduction,Date dateExp){
        Calendar day = Calendar.getInstance();
        Date today = new Date(day.get(Calendar.YEAR)-1900,day.get(Calendar.MONTH),day.get(Calendar.DAY_OF_MONTH));
        Log.d(TAG, String.valueOf(dateProduction.getTime()));
        Log.d(TAG, String.valueOf(today.getTime()));
        Log.d(TAG, String.valueOf(dateExp.getTime()));
        return ((dateExp.getTime()-dateProduction.getTime())/3>today.getTime()-dateProduction.getTime());
    }
}