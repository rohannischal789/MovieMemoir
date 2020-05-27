package com.example.mymoviememoir;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.mymoviememoir.entity.Cinema;
import com.example.mymoviememoir.model.Movie;
import com.example.mymoviememoir.networkconnection.NetworkConnection;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class AddMemoirActivity extends AppCompatActivity {
    final Calendar myCalendar = Calendar.getInstance();
    private NetworkConnection networkConnection;
    private EditText etMovieName;
    private EditText etReleaseDate;
    private EditText etWatchDate;
    private EditText etWatchTime;
    private Spinner cinemaSpinner;
    private EditText etComment;
    private ImageView imageView;
    private RatingBar ratingMemoir;
    private List<Cinema> cinemas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_add_memoir);
            Bundle bundle = getIntent().getExtras();
            final Movie movie = bundle.getParcelable("selectedMovie");
            etMovieName = findViewById(R.id.etMovieName7);
            etReleaseDate = findViewById(R.id.etReleaseDate);
            etWatchDate = findViewById(R.id.etWatchDate);
            etWatchTime = findViewById(R.id.etWatchTime);
            cinemaSpinner = findViewById(R.id.cinemaSpinner);
            etComment = findViewById(R.id.etComment);
            imageView = findViewById(R.id.ivPoster);
            ratingMemoir = findViewById(R.id.ratingMemoir);
            networkConnection = new NetworkConnection();
            etMovieName.setText(movie.getMovieName());
            etMovieName.setEnabled(false);
            etReleaseDate.setText(new SimpleDateFormat("dd-MM-yyyy").format(movie.getReleaseDate()));
            etReleaseDate.setEnabled(false);
            String fullPath = "https://image.tmdb.org/t/p/w500/" + movie.getMoviePoster();
            Picasso.get()
                    .load(fullPath)
                    .placeholder(R.mipmap.ic_launcher)
                    .resize(200, 200)
                    .centerInside()
                    .into(imageView);

            final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear,
                                      int dayOfMonth) {
                    myCalendar.set(Calendar.YEAR, year);
                    myCalendar.set(Calendar.MONTH, monthOfYear);
                    myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                    updateLabel();
                }

            };

            etWatchDate.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    DatePickerDialog dialog = new DatePickerDialog(AddMemoirActivity.this, date, myCalendar
                            .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                            myCalendar.get(Calendar.DAY_OF_MONTH));
                    dialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                    dialog.show();
                }
            });

            getCinemas fetchCinemas = new getCinemas();
            fetchCinemas.execute();

            etWatchTime.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    Calendar mcurrentTime = Calendar.getInstance();
                    int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                    int minute = mcurrentTime.get(Calendar.MINUTE);
                    TimePickerDialog mTimePicker;
                    mTimePicker = new TimePickerDialog(AddMemoirActivity.this, new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                            etWatchTime.setText( selectedHour + ":" + selectedMinute);
                        }
                    }, hour, minute, true);//Yes 24 hour time
                    mTimePicker.setTitle("Select Time");
                    mTimePicker.show();

                }
            });

            Button btnAddCinema = findViewById(R.id.btnAddCinema1);
            networkConnection = new NetworkConnection();
            btnAddCinema.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(AddMemoirActivity.this, AddCinemaActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putParcelable("selectedMovie", movie);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            });

        }
        catch (Exception e)
        {
            Toast.makeText(getApplicationContext(),e.toString(),Toast.LENGTH_SHORT);
        }
    }

    private void updateLabel() {
        String myFormat = "dd/MM/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.ENGLISH);
        etWatchDate.setText(sdf.format(myCalendar.getTime()));
    }

    private class getCinemas extends AsyncTask<Void, Void, List<Cinema>> {

        @Override
        protected List<Cinema> doInBackground(Void... voids) {
            return networkConnection.getAllCinemas();
        }

        @Override
        protected void onPostExecute(List<Cinema> details) {
            cinemas = details;
            final List<String> list = new ArrayList<String>();
            for (Cinema c : details) {
                list.add(c.getName() + ", " + c.getPostcode());
            }

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),
                    android.R.layout.simple_spinner_item, list );
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            cinemaSpinner.setAdapter(adapter);

        }
    }
}
