package com.example.mymoviememoir;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.mymoviememoir.networkconnection.NetworkConnection;
import com.example.mymoviememoir.utilities.Util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class SignUpActivity extends AppCompatActivity {
    final Calendar myCalendar = Calendar.getInstance();
    EditText edittext;
    NetworkConnection networkConnection=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        networkConnection=new NetworkConnection();
        Button btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUpActivity.this,
                        SignInActivity.class);
                startActivity(intent);
            }
        });


        EditText edittext= (EditText) findViewById(R.id.etBirthday);
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

        edittext.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                new DatePickerDialog(SignUpActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        final EditText etFirstName = findViewById(R.id.etFirstName);
        final EditText etSurname = findViewById(R.id.etSurname);
        final EditText etBirthday = findViewById(R.id.etBirthday);
        final EditText etAddress = findViewById(R.id.etAddress);
        final EditText etPostcode = findViewById(R.id.etPostcode);
        final EditText etUsername1 = findViewById(R.id.etUsername1);
        final EditText etPassword = findViewById(R.id.etPassword);
        final Spinner sState= findViewById(R.id.stateSpinner);
        final RadioGroup radioGroup = findViewById(R.id.radiogroup);

        Button btnRegister = findViewById(R.id.btnRegister);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String firstName = etFirstName.getText().toString();
                String surname = etSurname.getText().toString();
                String birthday = etBirthday.getText().toString();
                String address = etAddress.getText().toString();
                String postcode = etPostcode.getText().toString();
                String username = etUsername1.getText().toString();
                String password = etPassword.getText().toString();
                String state = sState.getSelectedItem().toString();
                int selectedId = radioGroup.getCheckedRadioButtonId();
                RadioButton radioButton = findViewById(selectedId);
                String gender = radioButton.getText().toString();

                //todo: validate for all stringd
                if (!username.isEmpty() && !password.isEmpty()) {
                    registerUser reg = new registerUser();
                    reg.execute(firstName,surname,gender,birthday,address,state,postcode,username, Util.getMd5(password));
                }
                else
                {
                    Toast.makeText(getApplicationContext(),"Please enter username and password",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void updateLabel() {
        String myFormat = "dd/MM/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.ENGLISH);
        EditText edittext= (EditText) findViewById(R.id.etBirthday);
        edittext.setText(sdf.format(myCalendar.getTime()));
    }


    private class registerUser extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            return networkConnection.addPerson(params[0],params[1],params[2],params[3],params[4],params[5],params[6],params[7],params[8]);
        }

        @Override
        protected void onPostExecute(String details) {

            try{
                int num = Integer.parseInt(details);
                Toast.makeText(getApplicationContext(), "Registration successful" ,Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                startActivity(intent);
            } catch (NumberFormatException e) {
                Toast.makeText(getApplicationContext(), details ,Toast.LENGTH_SHORT).show();
            }
            //TextView resultTextView = findViewById(R.id.tvCheck);
            //resultTextView.setText(details);
            //Todo: save in shared preferences if all good
        }
    }



}
