package com.example.tubespppb2;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.tubespppb2.databinding.PageLoginBinding;
import com.example.tubespppb2.presenter.PresenterLoginActivity;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener, ILoginActivity, IError {
    private PresenterLoginActivity presenter;
    private PageLoginBinding binding;
    private String role;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.binding = PageLoginBinding.inflate(getLayoutInflater());
        View view = this.binding.getRoot();
        setContentView(view);
        this.presenter = new PresenterLoginActivity(getApplicationContext(), this, this);
        this.role = "";
        this.binding.btnLogin.setOnClickListener(this::onClick);
        this.binding.btnRoleAdmin.setOnClickListener(this::onClick);
        this.binding.btnRoleDosen.setOnClickListener(this::onClick);
        this.binding.btnRoleMahasiswa.setOnClickListener(this::onClick);
        this.binding.etEmail.setOnClickListener(this::onClick);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == this.binding.btnLogin.getId()) {
            String email = this.binding.etEmail.getEditText().getText().toString();
            String password = this.binding.etPassword.getEditText().getText().toString();
            this.presenter.setPostAuthenticate(email, password, role);
        } else if (v.getId() == this.binding.btnRoleMahasiswa.getId()) {
            this.removeRoleError();
            this.role = "student";
            toggleBtnRole(
                    this.binding.tvMahasiswa,
                    this.binding.tvAdmin,
                    this.binding.tvDosen
            );
        } else if (v.getId() == this.binding.btnRoleDosen.getId()) {
            this.removeRoleError();
            this.role = "lecturer";
            toggleBtnRole(
                    this.binding.tvDosen,
                    this.binding.tvAdmin,
                    this.binding.tvMahasiswa
            );
        } else if (v.getId() == this.binding.btnRoleAdmin.getId()) {
            this.removeRoleError();
            this.role = "admin";
            toggleBtnRole(
                    this.binding.tvAdmin,
                    this.binding.tvDosen,
                    this.binding.tvMahasiswa
            );
        } else if (v.getId() == this.binding.etEmail.getId()) {
            this.removeEmailError();
        }
    }

    @Override
    public void showEmailError() {
        this.binding.tvErrorEmail.setText("email tidak sesuai");
        this.binding.tvErrorEmail.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.merah));
    }

    @Override
    public void removeEmailError() {
        this.binding.tvErrorEmail.setText("");
    }

    @Override
    public void showRoleError() {
        this.binding.tvErrorRole.setText("role harus dipilih");
        this.binding.tvErrorRole.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.merah));
    }

    @Override
    public void removeRoleError() {
        this.binding.tvErrorRole.setText("");
    }

    @Override
    public void showError(String msg) {
        Context context = getApplicationContext();
        CharSequence text = msg;
        int duration = Toast.LENGTH_LONG;
        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }

    @Override
    public void goToHomePage() {
        clearForm();
        startActivity(new Intent(LoginActivity.this, MainActivity.class));
        finish();
    }

    private void toggleBtnRole(TextView tvClicked, TextView tvNotClicked1, TextView tvNotClicked2) {
        tvClicked.setTypeface(tvClicked.getTypeface(), Typeface.BOLD);
        tvClicked.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.black));
        tvNotClicked1.setTypeface(tvNotClicked1.getTypeface(), Typeface.NORMAL);
        tvNotClicked1.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.abu));
        tvNotClicked2.setTypeface(tvNotClicked2.getTypeface(), Typeface.NORMAL);
        tvNotClicked2.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.abu));
    }

    private void clearForm() {
        this.binding.etPassword.getEditText().setText("");
        this.binding.etEmail.getEditText().setText("");
        this.role = "";
        this.removeEmailError();
        this.removeRoleError();
    }
}
