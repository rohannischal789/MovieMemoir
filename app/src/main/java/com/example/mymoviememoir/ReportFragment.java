package com.example.mymoviememoir;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.mymoviememoir.entity.CinemaMovie;
import com.example.mymoviememoir.entity.MonthMovie;
import com.example.mymoviememoir.networkconnection.NetworkConnection;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class ReportFragment extends Fragment {
    final Calendar myCalendar = Calendar.getInstance();
    NetworkConnection networkConnection = null;
    EditText etStartDate;
    EditText etEndDate;
    Spinner spinnerYear;
    PieChart pieChart;
    BarChart chart;


    PieData pieData;
    PieDataSet pieDataSet;
    List<PieEntry> pieEntries;
    ArrayList PieEntryLabels;

    int[] colors = new int[]{Color.LTGRAY,Color.BLACK,Color.BLUE,Color.GREEN,Color.YELLOW};
    public ReportFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the View for this fragment
        View view = inflater.inflate(R.layout.report_fragment, container, false);
        networkConnection = new NetworkConnection();

        etStartDate = view.findViewById(R.id.etStartDate);
        initializeDatePicker(etStartDate);

        etEndDate = view.findViewById(R.id.etEndDate);
        spinnerYear = view.findViewById(R.id.spinnerYear);

        initializeDatePicker(etEndDate);
        SharedPreferences sharedPref = getActivity().getSharedPreferences("data", Context.MODE_PRIVATE);
        final int personID = sharedPref.getInt("personID", 0);

        spinnerYear.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long
                    id) {
                String selectedYear = parent.getItemAtPosition(position).toString();

                GetWatchedMoviesPerMonth getWatchedMoviesPerMonth = new GetWatchedMoviesPerMonth();
                getWatchedMoviesPerMonth.execute( String.valueOf( personID), selectedYear);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        chart = view.findViewById(R.id.barchart);

        pieChart = view.findViewById(R.id.pieChart);
        getEntries();
        Description desc = new Description();
        desc.setText("ABC");
        pieDataSet = new PieDataSet(pieEntries, "PPPP");
        pieChart.setUsePercentValues(true);
        pieData = new PieData(pieDataSet);
        pieDataSet.setColors(ColorTemplate.JOYFUL_COLORS);
        pieChart.setData(pieData);
        pieChart.invalidate();
        /*pieDataSet.setSliceSpace(2f);
        pieDataSet.setValueTextColor(Color.WHITE);
        pieDataSet.setValueTextSize(10f);
        pieDataSet.setSliceSpace(5f);*/
        /*pieChart = view.findViewById(R.id.pieChart);
        //pieChart.setUsePercentValues(true);
        ArrayList<PieEntry> yvalues = new ArrayList<PieEntry>();
        yvalues.add(new PieEntry(8, "Test1"));
        yvalues.add(new PieEntry(15, "Test2"));
        yvalues.add(new PieEntry(12, "Test3"));
        yvalues.add(new PieEntry(25, "Test4"));
        yvalues.add(new PieEntry(23, "Test5"));
        yvalues.add(new PieEntry(17, "Test6"));

        PieDataSet dataSet = new PieDataSet(yvalues, "");
        dataSet.setColors(colors);

        PieData data = new PieData(dataSet);
        // In Percentage term
        //data.setValueFormatter(new PercentFormatter());
        // Default value
        //data.setValueFormatter(new DefaultValueFormatter(0));
        pieChart.setData(data);
        pieChart.invalidate();*/

        return view;
    }

    private void getEntries() {
        pieEntries = new ArrayList<>();
        pieEntries.add(new PieEntry(2, "A"));
        pieEntries.add(new PieEntry(4, "B"));
        pieEntries.add(new PieEntry(6, "C"));
        pieEntries.add(new PieEntry(8, "D"));
        pieEntries.add(new PieEntry(7, "E"));
        pieEntries.add(new PieEntry(3, "F"));
    }

    private void initializeDatePicker(final EditText edt) {
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel(edt);
            }

        };

        edt.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                DatePickerDialog dialog = new DatePickerDialog(getContext(), date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH));
                dialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                dialog.show();
            }
        });
    }

    private void updateLabel(EditText edt) {
        String myFormat = "dd/MM/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.ENGLISH);
        edt.setText(sdf.format(myCalendar.getTime()));
    }

    private class GetMoviesWatchedPerPostcode extends AsyncTask<String, Void, List<CinemaMovie>> {

        @Override
        protected List<CinemaMovie> doInBackground(String... params) {
            return networkConnection.getMoviesWatchedPerPostcode(params[0], params[1], params[2]);
        }

        @Override
        protected void onPostExecute(List<CinemaMovie> result) {


        }
    }

    private class GetWatchedMoviesPerMonth extends AsyncTask<String, Void, List<MonthMovie>> {

        @Override
        protected List<MonthMovie> doInBackground(String... params) {
            return networkConnection.getWatchedMoviesPerMonth(params[0], params[1]);
        }

        @Override
        protected void onPostExecute(List<MonthMovie> result) {
            ArrayList<BarEntry> data = new ArrayList();
            ArrayList<String> labels = new ArrayList<>();
            int i = 0;
            for (MonthMovie mov:result) {
                data.add(new BarEntry(i,mov.getCount()));
                labels.add(mov.getWatchedMonth());
                i++;
            }
            chart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(labels));

            BarDataSet bardataset = new BarDataSet(data, "Watched Movies Per Month");
            chart.animateY(5000);

            bardataset.setColors(ColorTemplate.COLORFUL_COLORS);
            BarData barData = new BarData(bardataset);
            chart.setData(barData);
        }
    }
}