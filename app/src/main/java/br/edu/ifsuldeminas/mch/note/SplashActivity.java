package br.edu.ifsuldeminas.mch.note;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import br.edu.ifsuldeminas.mch.note.R;

public class SplashActivity extends AppCompatActivity {

    private static final int SPLASH_DURATION = 3000; // Duração da tela de splash em milissegundos

    private boolean isActivityValid = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Button skipButton = findViewById(R.id.skip_button);
        skipButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToNextScreen();
            }
        });

        // Define um atraso para exibir o fragmento de informações
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (isActivityValid) {
                    showInformationFragment();
                }
            }
        }, SPLASH_DURATION);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Define que a atividade não é mais válida ao ser destruída
        isActivityValid = false;
    }

    private void showInformationFragment() {
        if (!isActivityValid) {
            return; // Verifica se a atividade ainda é válida antes de exibir o fragmento
        }

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        // Cria uma instância do fragmento de informações
        Fragment informationFragment = new InformationFragment();

        // Substitui o conteúdo do layout da SplashActivity pelo fragmento
        fragmentTransaction.replace(R.id.splash_container, informationFragment);

        if (!isActivityValid) {
            return; // Verifica novamente se a atividade ainda é válida antes de fazer o commit
        }

        fragmentTransaction.commit();

        // Define um atraso para iniciar a próxima atividade após exibir o fragmento
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (isActivityValid) {
                    navigateToNextScreen();
                }
            }
        }, SPLASH_DURATION);
    }

    private void navigateToNextScreen() {
        // Verifica se o usuário está logado
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) {
            startActivity(new Intent(SplashActivity.this, LoginActivity.class));
        } else {
            startActivity(new Intent(SplashActivity.this, MainActivity.class));
        }
        finish();
    }
}
