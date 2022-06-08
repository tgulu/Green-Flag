package com.example.greenflag;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.regex.Pattern;

public class Register extends Activity {

    private TextView enterEmail;
    private TextView createPassword;
    private EditText emailField;
    private EditText originalPassword;
    private EditText repeatPassword;
    private Button nextBtn;
    private ArrayList<String> emailChecker;


    private SharedPreferences sharedPreferences;

    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[a-zA-Z]).{8,20}$");



    private static final String KEY_NAME = "name";
    private static final String KEY_EMAIL = "email";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);

        enterEmail = findViewById(R.id.email);
        originalPassword = findViewById(R.id.passwordBox);
        repeatPassword = findViewById(R.id.repeatPasswordBox);
        emailField = findViewById(R.id.emailBox);
        nextBtn = findViewById(R.id.registerButton);
        createPassword = findViewById(R.id.createPassword);

        emailChecker = new ArrayList<>();

        sharedPreferences = getSharedPreferences("MY_SHARED_PREF",MODE_PRIVATE);

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(KEY_NAME,originalPassword.getText().toString());
                editor.putString(KEY_NAME,repeatPassword.getText().toString());
                editor.putString(KEY_EMAIL,emailField.getText().toString());



                Intent intent = new Intent(Register.this, Welcome.class);
                startActivity(intent);

                Toast.makeText(Register.this,
                        "Login Success",
                        Toast.LENGTH_SHORT).show();
            }
    });
}
    @Override
    protected void onResume() {
        super.onResume();

        emailField.setFocusable(true);
        emailField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() ==8){
                    sharedPreferences.edit().putString("EMAIL", charSequence.toString());
                    nextBtn.setEnabled(true);
                } else{
                    nextBtn.setEnabled(false);
                    emailField.setError("INCORRECT FORMAT");
                }
                validateEmail();
                confirmInput();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        repeatPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                validatePassword();
                confirmInput();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        originalPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                validatePassword();
                confirmInput();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveData();
                Intent intent2 = new Intent(Register.this,Welcome.class);
                startActivity(intent2);
            }
        });
    }
    public void saveData(){

        String enterEmail = emailField.getText().toString().trim();
        sharedPreferences.edit().putString("Email",enterEmail).apply();
        if (emailChecker.contains(enterEmail)){
            Toast error = Toast.makeText(getApplicationContext(),
                    "This email is already in use",
                    Toast.LENGTH_SHORT);
            error.show();
        } else {
            emailChecker.add(enterEmail);
        }

    }

    private boolean validateEmail() {
        String emailInput = emailField.getText().toString().trim();
        if (emailInput.isEmpty()) {
            enterEmail.setError("Field can't be empty");
            return false;

        } else if (!Patterns.EMAIL_ADDRESS.matcher(emailInput).matches()) {
            enterEmail.setError("Please enter a valid email address");
            return false;
        } else {
            enterEmail.setError(null);
            return true;
        }
    }

    private boolean validatePassword(){
        String enterPass = originalPassword.getText().toString().trim();
        String enterPassAgain = repeatPassword.getText().toString().trim();

        if(enterPass.isEmpty()){
            createPassword.setError("This field can't be empty");
            return false;
        }
        else if(!enterPass.equals(enterPassAgain)){
            createPassword.setError(null);
            repeatPassword.setError("Password must match");
            return false;
        }
        else if(!PASSWORD_PATTERN.matcher(enterPass).matches()){
            createPassword.setError("password must be at least 8 characters, 1 uppercase and 1 lowercase");
            return false;
        }
        else{

            repeatPassword.setError(null);
            return true;
        }


    }

    public void confirmInput(){

        //checks to see if the user as entered valid password and allows them to onto next page
        if(validatePassword()==true&&validateEmail()==true){
            nextBtn.setVisibility(View.VISIBLE);
            nextBtn.setEnabled(true);

        }
        else{
            nextBtn.setVisibility(View.INVISIBLE);
            nextBtn.setEnabled(false);
        }
    }

}
