package br.edu.ifsuldeminas.mch.note;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class CreateAccountActivity extends AppCompatActivity {

    // Declaração das variáveis para os componentes da interface
    EditText emailEditText, passwordEditText, confirmPasswordEditText;
    Button createAccountBtn;
    ProgressBar progressBar;
    TextView loginBtnTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        // Inicialização dos componentes da interface
        emailEditText = findViewById(R.id.email_edit_text);
        passwordEditText = findViewById(R.id.senha_edit_text);
        confirmPasswordEditText = findViewById(R.id.confirma_senha_edit_text);
        createAccountBtn = findViewById(R.id.criarconta_btn);
        progressBar = findViewById(R.id.progress_bar);
        loginBtnTextView = findViewById(R.id.login_text_view_btn);

        // Configuração dos listeners de clique para os botões
        createAccountBtn.setOnClickListener(v -> createAccount());
        loginBtnTextView.setOnClickListener(v -> finish());
    }

    // Método chamado quando o botão de criar conta é clicado
    void createAccount() {
        // Obtém os dados dos campos de texto
        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        String confirmPassword = confirmPasswordEditText.getText().toString();

        // Valida os dados inseridos
        boolean isValidated = validateData(email, password, confirmPassword);
        if (!isValidated) {
            return;
        }

        // Cria a conta no Firebase
        createAccountInFirebase(email, password);
    }

    // Método responsável por criar a conta no Firebase
    void createAccountInFirebase(String email, String password) {
        changeInProgress(true); // Altera a visibilidade dos componentes para indicar o progresso

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        // Cria a conta no Firebase utilizando o email e senha fornecidos
        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(CreateAccountActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                changeInProgress(false); // Altera a visibilidade dos componentes para indicar que o progresso terminou
                if (task.isSuccessful()) {
                    // Conta criada com sucesso
                    Toast.makeText(CreateAccountActivity.this, "Conta criada com sucesso, verifique seu email", Toast.LENGTH_SHORT).show();
                    firebaseAuth.getCurrentUser().sendEmailVerification();
                    firebaseAuth.signOut();
                    finish();
                } else {
                    // Falha ao criar a conta
                    Toast.makeText(CreateAccountActivity.this, task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    // Método responsável por alterar a visibilidade dos componentes para indicar o progresso
    void changeInProgress(boolean inProgress) {
        if (inProgress) {
            progressBar.setVisibility(View.VISIBLE);
            createAccountBtn.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.GONE);
            createAccountBtn.setVisibility(View.VISIBLE);
        }
    }

    // Método responsável por validar os dados inseridos
    boolean validateData(String email, String password, String confirmPassword) {
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailEditText.setError("Email inválido");
            return false;
        }
        if (password.length() < 6) {
            passwordEditText.setError("Senha inválida");
            return false;
        }
        if (!password.equals(confirmPassword)) {
            confirmPasswordEditText.setError("Senha não é a mesma");
            return false;
        }
        return true;
    }
}
