package com.solomoon.mytriptracker.ui;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.solomoon.mytriptracker.App;
import com.solomoon.mytriptracker.R;
import com.solomoon.mytriptracker.core.DefaultAppSettingsManager;
import com.solomoon.mytriptracker.core.DefaultUserManager;
import com.solomoon.mytriptracker.exceptions.IncorrectPasswordException;
import com.solomoon.mytriptracker.exceptions.NotFoundException;
import com.solomoon.mytriptracker.exceptions.UserAlreadyExistsException;
import com.solomoon.mytriptracker.models.AppSettings;
import com.solomoon.mytriptracker.models.User;

import java.security.NoSuchAlgorithmException;

public class MainActivity extends AppCompatActivity {

    private static final int MIN_PASSWORD_LENGTH = 5;

    private Button btnLogin, btnRegister;
    private ConstraintLayout root;
    private LayoutInflater inflater;

    private DefaultUserManager userManager;
    private DefaultAppSettingsManager appSettingsManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        userManager = new DefaultUserManager(App.getInstance().getDatabase());
        appSettingsManager = new DefaultAppSettingsManager(App.getInstance().getDatabase());

        btnLogin = findViewById(R.id.btnLogin);
        btnRegister = findViewById(R.id.btnRegister);

        root = findViewById(R.id.root_element);
        inflater = LayoutInflater.from(this);

        btnLogin.setOnClickListener(v -> {
            showLoginDialog();
        });

        btnRegister.setOnClickListener(v -> {
            showRegisterDialog();
        });

        autologin();
    }

    private void autologin() {
        appSettingsManager = new DefaultAppSettingsManager(App.getInstance().getDatabase());

        AppSettings appSettings = appSettingsManager.getCurrentAppSettings();
        if (appSettings.getCurrentUserId() != null) {
            TripListActivity.start(this);
            finish();
        }
    }

    private void showRegisterDialog() {
        View registerView = inflater.inflate(R.layout.activity_register, null);

        final EditText edtFirstName = registerView.findViewById(R.id.edtFirst_name);
        final EditText edtLastName = registerView.findViewById(R.id.edtLast_name);
        final EditText edtLogin = registerView.findViewById(R.id.edtLogin);
        final EditText edtPassword = registerView.findViewById(R.id.edtPassword);

        AlertDialog.Builder registerDialog = new AlertDialog.Builder(this);
        registerDialog
                .setCancelable(false)
                .setTitle(R.string.register_dialog_title)
                .setMessage(R.string.register_dialog_message)
                .setView(registerView)
                .setNegativeButton(R.string.dialog_negative_button, null)
                .setPositiveButton(R.string.dialog_positive_register, null);

        final AlertDialog alertDialog = registerDialog.create();
        alertDialog.setOnShowListener(dialog -> {
            Button btnDialogPositive = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
            btnDialogPositive.setOnClickListener(v -> {
                if (TextUtils.isEmpty(edtFirstName.getText().toString())) {
                    Toast.makeText(this, R.string.empty_first_name, Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(edtLastName.getText().toString())) {
                    Toast.makeText(this, R.string.empty_last_name, Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(edtLogin.getText().toString())) {
                    Toast.makeText(this, R.string.empty_login, Toast.LENGTH_SHORT).show();
                    return;
                }

                if (edtPassword.getText().toString().length() < MIN_PASSWORD_LENGTH) {
                    Toast.makeText(this, R.string.min_password_length_message, Toast.LENGTH_SHORT).show();
                    return;
                }

                String userPassword = edtPassword.getText().toString();

                User user = new User();
                user.setFirstName(edtFirstName.getText().toString());
                user.setLastName(edtLastName.getText().toString());
                user.setLogin(edtLogin.getText().toString());
                user.setPassword(userPassword);

                try {
                    userManager.registerNewUser(user);
                    user = userManager.login(user.getLogin(), userPassword);
                    loginProcessing(user);
                    alertDialog.dismiss();
                } catch (UserAlreadyExistsException | NoSuchAlgorithmException e) {
                    Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });

        alertDialog.show();

    }

    private void showLoginDialog() {

        View loginView = inflater.inflate(R.layout.activity_login, null);

        final EditText edtLogin = loginView.findViewById(R.id.edtLogin);
        final EditText edtPassword = loginView.findViewById(R.id.edtPassword);

        AlertDialog.Builder loginDialog = new AlertDialog.Builder(this);
        loginDialog
                .setTitle(R.string.login_dialog_title)
                .setMessage(R.string.login_dialog_message)
                .setView(loginView)
                .setNegativeButton(R.string.dialog_negative_button, null)
                .setPositiveButton(R.string.dialog_positive_login, null);

        AlertDialog alertDialog = loginDialog.create();
        alertDialog.setOnShowListener(dialog -> {
            Button btnDialogPositive = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
            btnDialogPositive.setOnClickListener(v -> {
                if (TextUtils.isEmpty(edtLogin.getText().toString())) {
                    Toast.makeText(this, R.string.empty_login, Toast.LENGTH_SHORT).show();
                    return;
                }

                try {
                    User user = userManager.login(edtLogin.getText().toString(), edtPassword.getText().toString());
                    loginProcessing(user);
                    alertDialog.dismiss();
                } catch (NotFoundException | IncorrectPasswordException | NoSuchAlgorithmException e) {
                    Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }

            });
        });

        alertDialog.show();
    }

    private void loginProcessing(User user) {
        appSettingsManager.updateCurrentUserId(user.getId());
        TripListActivity.start(this);
    }

}
