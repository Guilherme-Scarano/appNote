package br.edu.ifsuldeminas.mch.note;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    EditText emailEditText, passwordEditText;
    Button loginBtn;
    ProgressBar progressBar;
    TextView createAccountBtnTextView;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Inicialização dos componentes da interface
        emailEditText = findViewById(R.id.email_edit_text);
        passwordEditText = findViewById(R.id.senha_edit_text);
        loginBtn = findViewById(R.id.login_btn);
        progressBar = findViewById(R.id.progress_bar);
        createAccountBtnTextView = findViewById(R.id.create_account_text_view_btn);

        // Obtenção do objeto SharedPreferences para armazenar o email do usuário
        sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        String savedEmail = sharedPreferences.getString("email", "");
        emailEditText.setText(savedEmail);

        // Definição dos listeners de clique para os botões
        loginBtn.setOnClickListener((v) -> loginUser());
        createAccountBtnTextView.setOnClickListener((v) -> startActivity(new Intent(LoginActivity.this, CreateAccountActivity.class)));
    }

    void loginUser() {
        // Obtenção do email e senha inseridos pelo usuário
        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        // Validação dos dados inseridos
        boolean isValidated = validateData(email, password);
        if (!isValidated) {
            return;
        }

        // Salvar o email nas SharedPreferences
        saveEmailToSharedPreferences(email);

        // Fazer o login no Firebase
        loginAccountInFirebase(email, password);
    }

    void saveEmailToSharedPreferences(String email) {
        // Salvar o email nas SharedPreferences
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("email", email);
        editor.apply();
    }

    void loginAccountInFirebase(String email, String password) {
        // Fazer o login no Firebase utilizando o FirebaseAuth
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        changeInProgress(true);
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                changeInProgress(false);
                if (task.isSuccessful()) {
                    // Login bem sucedido
                    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
                    if (firebaseAuth.getCurrentUser().isEmailVerified()) {
                        // Ir para a MainActivity
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        finish();
                    } else {
                        // Exibir mensagem de email não verificado
                        Utility.showToast(LoginActivity.this, "Email não foi verificado. Por favor, verifique seu email.");
                    }
                } else {
                    // Login falhou
                    Utility.showToast(LoginActivity.this, task.getException().getLocalizedMessage());
                }
            }
        });
    }

    void changeInProgress(boolean inProgress) {
        // Alterar a visibilidade da ProgressBar e do botão de login com base no progresso
        if (inProgress) {
            progressBar.setVisibility(View.VISIBLE);
            loginBtn.setVisibility(View.GONE);
        } else {
            progressBar.setVisibility(View.GONE);
            loginBtn.setVisibility(View.VISIBLE);
        }
    }

    boolean validateData(String email, String password) {
        // Validar os dados inseridos pelo usuário

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailEditText.setError("Email inválido");
            return false;
        }
        if (password.length() < 6) {
            passwordEditText.setError("Senha tem que possuir 6 caracteres ou mais");
            return false;
        }
        return true;
    }
}

