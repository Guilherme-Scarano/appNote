package br.edu.ifsuldeminas.mch.note;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.PopupMenu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.Query;

public class MainActivity extends AppCompatActivity {

    FloatingActionButton addNoteBtn;
    RecyclerView recyclerView;
    ImageButton menuBtn;
    NoteAdapter noteAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Inicialização dos componentes da interface
        addNoteBtn = findViewById(R.id.add_note_btn);
        recyclerView = findViewById(R.id.recyler_view);
        menuBtn = findViewById(R.id.menu_btn);

        // Definição do listener de clique para o botão de adicionar nota
        addNoteBtn.setOnClickListener((v)-> startActivity(new Intent(MainActivity.this,NoteDetailsActivity.class)) );

        // Definição do listener de clique para o botão de menu
        menuBtn.setOnClickListener((v)->showMenu() );

        // Configuração do RecyclerView
        setupRecyclerView();
    }

    void showMenu(){
        // Exibição do menu de opções em um popup menu
        PopupMenu popupMenu  = new PopupMenu(MainActivity.this,menuBtn);
        popupMenu.getMenu().add("Logout");
        popupMenu.show();
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                if(menuItem.getTitle()=="Logout"){
                    // Realizar logout do usuário
                    FirebaseAuth.getInstance().signOut();
                    startActivity(new Intent(MainActivity.this,LoginActivity.class));
                    finish();
                    return true;
                }
                return false;
            }
        });
    }

    void setupRecyclerView(){
        // Configurar o RecyclerView para exibir as notas em ordem decrescente de timestamp

        // Obter a referência à coleção de notas ordenada por timestamp
        Query query  = Utility.getCollectionReferenceForNotes().orderBy("timestamp",Query.Direction.DESCENDING);

        // Configurar as opções do adaptador FirestoreRecyclerOptions
        FirestoreRecyclerOptions<Note> options = new FirestoreRecyclerOptions.Builder<Note>()
                .setQuery(query,Note.class).build();

        // Configurar o layout manager e o adaptador para o RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        noteAdapter = new NoteAdapter(options,this);
        recyclerView.setAdapter(noteAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Iniciar a escuta do adaptador FirestoreRecyclerAdapter
        noteAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        // Parar a escuta do adaptador FirestoreRecyclerAdapter
        noteAdapter.stopListening();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Notificar o adaptador de que os dados podem ter sido alterados
        noteAdapter.notifyDataSetChanged();
    }
}


