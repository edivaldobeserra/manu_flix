package devandroid.edivaldo.manuflix.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.media.MediaActionSound;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.normal.TedPermission;

import java.util.HashMap;
import java.util.List;

import devandroid.edivaldo.manuflix.R;
import devandroid.edivaldo.manuflix.helper.FirebaseHelper;
import devandroid.edivaldo.manuflix.model.Post;

public class PlayerActivity extends AppCompatActivity {

    private VideoView videoView;
    private Button btnUpload;
    private Button btngaleria;
    private String title;
    EditText textNome;
    private final int SELECAO_GALERIA = 100;
    private ImageView imageView;
    private Button btnSalvar;
    private ImageView imageFake;
    private String caminhoImagem = null;
    private String caminhoVideo = null;
    private ProgressBar progressBar;
    private EditText editGenero;
    private EditText editElenco;
    private EditText editAno;
    private EditText editDuracao;
    private EditText editSinopse;
    private EditText editTitulo;



    private static final int VIDEO_PICK_GALLERY_CODE = 100;
    private static final int VIDEO_PICK_CAMERA_CODE = 101;
    private static final int CAMERA_REQUEST_CODE = 102;

    private String[] cameraPermissions;
    private Uri videoUri = null;
    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        iniciaComponentes();

        congigClick();

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Aguarde");
        progressDialog.setMessage("Upload Video");
        progressDialog.setCanceledOnTouchOutside(false);


        //Permissao da camera
        cameraPermissions = new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE};



        btngaleria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                galeriaDialog();


            }
        });
    }
    private void congigClick(){
        imageView.setOnClickListener(v -> verificarPermissaoGaleria());
        btnSalvar.setOnClickListener(v -> validaDados());

    }
    private void validaDados(){
        String titulo = editTitulo.getText().toString().trim();
        String genero = editGenero.getText().toString().trim();
        String elenco = editElenco.getText().toString().trim();
        String ano = editAno.getText().toString().trim();
        String duracao = editDuracao.getText().toString().trim();
        String sinopse = editSinopse.getText().toString().trim();

        if (!titulo.isEmpty()){
            if (!genero.isEmpty()){
                if (!elenco.isEmpty()){
                    if (!ano.isEmpty()){
                        if (!duracao.isEmpty()){
                            if (!sinopse.isEmpty()){
                                if (caminhoImagem != null){

                                    progressBar.setVisibility(View.VISIBLE);

                                    Post post = new Post();
                                    post.setTitulo(titulo);
                                    post.setGenero(genero);
                                    post.setElenco(elenco);
                                    post.setAno(ano);
                                    post.setDuracao(duracao);
                                    post.setSinopse(sinopse);

                                    salvarImagemFirebase(post);
                                    salvarVideoFirebase(post);



                                }else {
                                    Toast.makeText(this,"Selecione uma imagem",Toast.LENGTH_SHORT).show();
                                }

                            }else {
                                editSinopse.setError("Sinopse obrigatório.");
                            }
                        }else {
                            editDuracao.setError("Duração obrigatório.");
                        }
                    }else {
                        editAno.setError("Ano obrigatório.");
                    }
                }else {
                    editElenco.setError("Elenco obrigatório.");
                }
            }else {
                editGenero.setError("Genero obrigatório.");
            }
        }else {
            editTitulo.setError("Titulo obrigatório.");
        }

    }

    private void salvarImagemFirebase(Post post) {
        StorageReference StorageReference = FirebaseHelper.getStorageReference()
                .child("imagens")
                .child("posts")
                .child(post.getId() + ".jpg");

        UploadTask uploadTask = StorageReference.putFile(Uri.parse(caminhoImagem));
        uploadTask.addOnSuccessListener(taskSnapshot -> StorageReference.getDownloadUrl().addOnCompleteListener(task -> {

            post.setImagem(task.getResult().toString());
            post.salvar();

            limparCampos();

        })).addOnFailureListener(e -> {
            progressBar.setVisibility(View.GONE);
            Toast.makeText(this, "Erro ao salvar cadastro", Toast.LENGTH_SHORT).show();
        });

    }


    private void salvarVideoFirebase(Post post){
        StorageReference StorageReference = FirebaseHelper.getStorageReference()
                .child("videos")
                .child("posts")
                .child(post.getId() + ".mp4");
        UploadTask uploadTask = StorageReference.putFile(Uri.parse(caminhoVideo));
        uploadTask.addOnSuccessListener(taskSnapshot -> StorageReference.getDownloadUrl().addOnCompleteListener(task -> {

            post.setVideo(task.getResult().toString());
            post.salvar();



        })).addOnFailureListener(e -> {

        });



    }



    private void limparCampos(){
        imageView.setImageBitmap(null);
        imageFake.setVisibility(View.VISIBLE);

        editTitulo.getText().clear();
        editGenero.getText().clear();
        editElenco.getText().clear();
        editAno.getText().clear();
        editDuracao.getText().clear();
        editSinopse.getText().clear();
        progressBar.setVisibility(View.GONE);

    }


    private void verificarPermissaoGaleria(){
        PermissionListener permissionlistener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                abrirGaleria();

            }

            @Override
            public void onPermissionDenied(List<String> deniedPermissions) {
                Toast.makeText( PlayerActivity.this,"Permissão Negada", Toast.LENGTH_SHORT).show();

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



   /* private void uploadVideo() {

        progressDialog.show();

        final String timestamp = "" + System.currentTimeMillis();

        String pastaNome = "Videos/"+ "video_" + timestamp;

        StorageReference storageReference = FirebaseStorage.getInstance().getReference(pastaNome);

        storageReference.putFile(videoUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                        while (!uriTask.isSuccessful());
                        Uri downloadUri = uriTask.getResult();
                        if (uriTask.isSuccessful()){

                            HashMap<String,Object> hashMap = new HashMap<>();
                            hashMap.put("VideoUrl", ""+downloadUri);

                            DatabaseReference reference = FirebaseDatabase.getInstance().getReference()
                                    .child("posts");


                            reference.child(timestamp)
                                    .setValue(hashMap)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            progressDialog.dismiss();
                                            Toast.makeText(PlayerActivity.this,"Upload videos", Toast.LENGTH_SHORT).show();

                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            progressDialog.dismiss();
                                            Toast.makeText(PlayerActivity.this,""+e.getMessage(), Toast.LENGTH_SHORT).show();

                                        }
                                    });
                        }

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(PlayerActivity.this,""+e.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });


    }*/

    private void galeriaDialog() {
        String[] options = {"Camera", "Gallery"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Pick Video From")
                .setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0) {
                            if (!checkCameraPermission()) {
                                requestCameraPermission();
                            } else {
                                videoPickCamera();
                            }

                        } else if (which == 1) {
                            videoPickGallery();

                        }

                    }
                })
                .show();
    }

    private void requestCameraPermission() {
        ActivityCompat.requestPermissions(this, cameraPermissions, CAMERA_REQUEST_CODE);

    }

    private boolean checkCameraPermission() {
        boolean result1 = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
        boolean result2 = ContextCompat.checkSelfPermission(this, Manifest.permission.WAKE_LOCK) == PackageManager.PERMISSION_GRANTED;

        return result1 && result2;
    }

    private void videoPickGallery() {
        Intent intent = new Intent();
        intent.setType("video/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Videos"), VIDEO_PICK_GALLERY_CODE);

    }

    private void videoPickCamera() {

        Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        startActivityForResult(intent, VIDEO_PICK_CAMERA_CODE);

    }

  /*  private void setVideo() {
        MediaController mediaController = new MediaController(this);
        mediaController.setAnchorView(videoView);

        videoView.setMediaController(mediaController);
      //  videoView.setVideoURI(videoUri);
        videoView.requestFocus();


    }*/

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case CAMERA_REQUEST_CODE:
                if (grantResults.length > 0) {
                    boolean camereraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean storageAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    if (camereraAccepted && storageAccepted) {
                        videoPickCamera();

                    } else {
                        Toast.makeText(this, "Camera & Storage permissao requerida", Toast.LENGTH_SHORT).show();

                    }
                }

        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if (resultCode == RESULT_OK) {
            if (requestCode == VIDEO_PICK_GALLERY_CODE) {
             Uri videoSelecionado = data.getData();
             caminhoVideo = videoSelecionado.toString();


            } else if (resultCode == VIDEO_PICK_CAMERA_CODE) {
                Uri videoSelecionado = data.getData();
                caminhoVideo = videoSelecionado.toString();

            }
        }

        if (resultCode == Activity.RESULT_OK){
            if (requestCode == SELECAO_GALERIA){

                Uri imagemSelecionada = data.getData();
                caminhoImagem = imagemSelecionada.toString();

                try {

                    Bitmap bitmap;

                    if (Build.VERSION.SDK_INT<28){
                        bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),imagemSelecionada);
                    }else {
                        ImageDecoder.Source source = ImageDecoder.createSource(getContentResolver(),imagemSelecionada);
                        bitmap = ImageDecoder.decodeBitmap(source);
                    }
                    imageFake.setVisibility(View.GONE);
                    imageView.setImageBitmap(bitmap);

                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    public void iniciaComponentes(){

        videoView = findViewById(R.id.videoView);
        btnUpload = findViewById(R.id.btnUpload);
        btngaleria = findViewById(R.id.btnGaleria);

        imageView = findViewById(R.id.imageView);
        btnSalvar = findViewById(R.id.btnSalvar);
        imageFake = findViewById(R.id.imageFake);
        progressBar = findViewById(R.id.progressBar);
        editTitulo = findViewById(R.id.editTitulo);
        editGenero = findViewById(R.id.editGenero);
        editElenco = findViewById(R.id. editElenco);
        editAno = findViewById(R.id. editAno);
        editDuracao = findViewById(R.id.editDuracao);
        editSinopse = findViewById(R.id.editSinopse);

    }
}
