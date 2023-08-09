package devandroid.edivaldo.manuflix.fragments;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.gun0912.tedpermission.PermissionListener;

import com.gun0912.tedpermission.normal.TedPermission;

import java.util.List;

import devandroid.edivaldo.manuflix.R;
import devandroid.edivaldo.manuflix.activity.MainActivity;


public class AddFragment extends Fragment {
    private final int SELECAO_GALERIA = 100;
    private String caminhoImagem;

    private ImageView imageView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        iniciaComponentes(view);
        congigClick();
    }
    private void congigClick(){
        imageView.setOnClickListener(v -> verificarPermissaoGaleria());}

    private void verificarPermissaoGaleria(){
        PermissionListener permissionlistener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                abrirGaleria();

            }

            @Override
            public void onPermissionDenied(List<String> deniedPermissions) {
                Toast.makeText(getContext(),"Permissão Negada", Toast.LENGTH_SHORT).show();

            }

        };

        TedPermission.create()
                .setPermissionListener(permissionlistener)
                .setDeniedTitle("Permissões")
                .setDeniedMessage("Se você não aceitar a permissão não poderá acessar a Galeria do dispositivo, deseja ativar a permissão agora ?")
                .setDeniedCloseButtonText("Não")
                .setGotoSettingButtonText("Sim")
                .setPermissions(Manifest.permission.READ_EXTERNAL_STORAGE)
                .check();


    }

    private void abrirGaleria(){
        Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, SELECAO_GALERIA);


    }


    private void iniciaComponentes(View view){
        imageView = view.findViewById(R.id.imageV);


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK){
            if (requestCode == SELECAO_GALERIA){

                Uri imagemSelecionada = data.getData();
                caminhoImagem = imagemSelecionada.toString();

                try {

                    Bitmap bitmap;

                    if (Build.VERSION.SDK_INT<28){
                        bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(),imagemSelecionada);
                    }else {
                        ImageDecoder.Source source = ImageDecoder.createSource(getActivity().getContentResolver(),imagemSelecionada);
                        bitmap = ImageDecoder.decodeBitmap(source);
                    }

                    imageView.setImageBitmap(bitmap);

                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        }
    }
}