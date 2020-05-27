package com.example.mymoviememoir;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
    NetworkConnection networkConnection=null;
    EditText etFirstName;
    EditText etSurname;
    EditText etBirthday;
    EditText etAddress;
    EditText etPostcode;
    EditText etUsername1;
    EditText etPassword;
    Spinner sState;
    RadioGroup radioGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        networkConnection=new NetworkConnection();

        EditText edittext= findViewById(R.id.etBirthday);
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
                DatePickerDialog dialog = new DatePickerDialog(SignUpActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH));
                dialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                dialog.show();
            }
        });

        etFirstName = findViewById(R.id.etFirstName);
        etSurname = findViewById(R.id.etSurname);
        etBirthday = findViewById(R.id.etBirthday);
        etAddress = findViewById(R.id.etAddress);
        etPostcode = findViewById(R.id.etPostcode);
        etUsername1 = findViewById(R.id.etUsername1);
        etPassword = findViewById(R.id.etPassword);
        sState= findViewById(R.id.stateSpinner);
        radioGroup = findViewById(R.id.radiogroup);

        Button btnRegister = findViewById(R.id.btnRegister);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String username = etUsername1.getText().toString();
                String firstName = etFirstName.getText().toString();
                String surname = etSurname.getText().toString();
                String birthday = etBirthday.getText().toString();
                String address = etAddress.getText().toString();
                String postcode = etPostcode.getText().toString();
                String password = etPassword.getText().toString();
                String state = sState.getSelectedItem().toString();
                int selectedId = radioGroup.getCheckedRadioButtonId();
                RadioButton radioButton = findViewById(selectedId);
                String gender = radioButton.getText().toString();
                if (!username.isEmpty() && !password.isEmpty()&& !firstName.isEmpty()&& !surname.isEmpty()&& !birthday.isEmpty()&& !address.isEmpty()
                        && !postcode.isEmpty()&& !state.isEmpty() && !gender.isEmpty()) {
                    doesUsernameExistTask usernameExistTask = new doesUsernameExistTask();
                    usernameExistTask.execute(username);
                }
                else
                {
                    Toast.makeText(getApplicationContext(),"Please enter all the values",Toast.LENGTH_SHORT).show();
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
                SharedPreferences sharedPref = getApplicationContext().getSharedPreferences("data", Context.MODE_PRIVATE);
                SharedPreferences.Editor spEditor = sharedPref.edit();
                spEditor.putInt("personID", num);
                spEditor.apply();
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

    private class doesUsernameExistTask extends AsyncTask<String, Void, Boolean> {

        @Override
        protected Boolean doInBackground(String... params) {
            return networkConnection.doesUsernameExist(params[0]);
        }

        @Override
        protected void onPostExecute(Boolean result) {

            if(!result) {
                String username = etUsername1.getText().toString();
                String firstName = etFirstName.getText().toString();
                String surname = etSurname.getText().toString();
                String birthday = etBirthday.getText().toString();
                String address = etAddress.getText().toString();
                String postcode = etPostcode.getText().toString();
                String password = etPassword.getText().toString();
                String state = sState.getSelectedItem().toString();
                int selectedId = radioGroup.getCheckedRadioButtonId();
                RadioButton radioButton = findViewById(selectedId);
                String gender = radioButton.getText().toString();

                //todo: validate for all stringd
                if (!username.isEmpty() && !password.isEmpty()&& !firstName.isEmpty()&& !surname.isEmpty()&& !birthday.isEmpty()&& !address.isEmpty()
                        && !postcode.isEmpty()&& !state.isEmpty() && !gender.isEmpty()) {
                    registerUser reg = new registerUser();
                    reg.execute(firstName, surname, gender, birthday, address, state, postcode, username, Util.getMd5(password));
                } else {
                    Toast.makeText(getApplicationContext(), "Please enter username and password", Toast.LENGTH_SHORT).show();
                }
            }
            else
            {
                Toast.makeText(getApplicationContext(), "Username already exists! Please use a different one", Toast.LENGTH_SHORT).show();
            }
            //TextView resultTextView = findViewById(R.id.tvCheck);
            //resultTextView.setText(details);
            //Todo: save in shared preferences if all good
        }
    }


}
