package com.example.mymoviememoir;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mymoviememoir.networkconnection.NetworkConnection;
import com.example.mymoviememoir.utilities.Util;


public class SignInActivity extends AppCompatActivity {
    NetworkConnection networkConnection=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        networkConnection=new NetworkConnection();

        final EditText etUsername = findViewById(R.id.etUsername);
        final EditText etPass = findViewById(R.id.etPass);
        Button btnLogin = findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getUserByUserName verifyUserTask = new getUserByUserName();

                String username = etUsername.getText().toString();
                String password = etPass.getText().toString();
                if (!username.isEmpty() && !password.isEmpty()) {
                    verifyUserTask.execute(username, Util.getMd5(password));
                }
                else
                {
                    Toast.makeText(getApplicationContext(),"Please enter username and password",Toast.LENGTH_SHORT).show();
                }
            }
        });

        Button btnRegister = findViewById(R.id.btnRegister);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignInActivity.this,
                        SignUpActivity.class);
                startActivity(intent);
            }
        });

    }

    private class getUserByUserName extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            return networkConnection.findByUsername(params[0],params[1]);
        }

        @Override
        protected void onPostExecute(String details) {

            try{
                int num = Integer.parseInt(details);
                Toast.makeText(getApplicationContext(), "Login successful" ,Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(SignInActivity.this, MainActivity.class);
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
