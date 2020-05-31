package com.example.mymoviememoir;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.mymoviememoir.model.Movie;
import com.example.mymoviememoir.networkconnection.NetworkConnection;

public class AddCinemaActivity extends AppCompatActivity {

    EditText etCinemaName;
    EditText etPostcode;
    NetworkConnection networkConnection;
    Movie movie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_cinema);
        getSupportActionBar().setTitle("Add New Cinema");
        etCinemaName = findViewById(R.id.etCinemaName2);
        etPostcode = findViewById(R.id.etPostcode2);
        Bundle bundle = getIntent().getExtras();
        movie = bundle.getParcelable("selectedMovie");
        Button btnAddCinema = findViewById(R.id.btnAddCinema);
        networkConnection = new NetworkConnection();
        btnAddCinema.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String cinemaName = etCinemaName.getText().toString();
                String postcode = etPostcode.getText().toString();
                if (!cinemaName.isEmpty() && !postcode.isEmpty()) {
                    doesCinemaExist cinemaExist = new doesCinemaExist();
                    cinemaExist.execute(cinemaName, postcode);
                } else {
                    Toast.makeText(getApplicationContext(), "Please enter all the values", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private class doesCinemaExist extends AsyncTask<String, Void, Boolean> {

        @Override
        protected Boolean doInBackground(String... params) {
            return networkConnection.doesCinemaExist(params[0], params[1]);
        }

        @Override
        protected void onPostExecute(Boolean result) {

            if (!result) {
                String cinemaName = etCinemaName.getText().toString();
                String postcode = etPostcode.getText().toString();

                addCinema newCinema = new addCinema();
                newCinema.execute(cinemaName, postcode);
            } else {
                Toast.makeText(getApplicationContext(), "Cinema already exists!", Toast.LENGTH_SHORT).show();
            }
            //TextView resultTextView = findViewById(R.id.tvCheck);
            //resultTextView.setText(details);
            //Todo: save in shared preferences if all good
        }
    }

    private class addCinema extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            return networkConnection.addCinema(params[0],params[1]);
        }

        @Override
        protected void onPostExecute(String details) {

            try{
                Toast.makeText(getApplicationContext(), "Cinema Added" ,Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(AddCinemaActivity.this, AddMemoirActivity.class);
                Bundle bundle = new Bundle();
                bundle.putParcelable("selectedMovie", movie);
                intent.putExtras(bundle);startActivity(intent);
            } catch (NumberFormatException e) {
                Toast.makeText(getApplicationContext(), details ,Toast.LENGTH_SHORT).show();
            }
            //TextView resultTextView = findViewById(R.id.tvCheck);
            //resultTextView.setText(details);
            //Todo: save in shared preferences if all good
        }
    }
}
