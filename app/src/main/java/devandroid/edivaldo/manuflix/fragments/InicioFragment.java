package devandroid.edivaldo.manuflix.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import devandroid.edivaldo.manuflix.R;
import devandroid.edivaldo.manuflix.autenticacao.LoginActivity;
import devandroid.edivaldo.manuflix.helper.FirebaseHelper;


public class InicioFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_inicio, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        view.findViewById(R.id.btn_login).setOnClickListener(view1 -> {
            if (FirebaseHelper.getAutenticado()) {
                Toast.makeText(requireContext(),"usuario já autenticado",Toast.LENGTH_SHORT).show();
            }else {
                startActivity(new Intent(requireActivity(), LoginActivity.class));
            }
        } );
    }
}