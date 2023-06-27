package br.edu.ifsuldeminas.mch.note;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class InformationFragment extends Fragment {

    private TextView informationTextView;

    public InformationFragment() {
        // Construtor vazio obrigatório para fragmentos
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Infla o layout para este fragmento
        View view = inflater.inflate(R.layout.fragment_information, container, false);
        informationTextView = view.findViewById(R.id.information_text_view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Aqui você pode obter as informações desejadas para exibir no fragmento
        String information = "O aplicativo de anotações é uma ferramenta prática e versátil que permite aos usuários criar, gerenciar e organizar suas anotações de forma eficiente. Com uma interface intuitiva e recursos essenciais, o aplicativo torna mais fácil capturar e armazenar informações importantes em um único lugar.";

        // Define o texto no TextView do fragmento
        informationTextView.setText(information);
    }
}

