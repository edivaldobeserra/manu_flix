package devandroid.edivaldo.manuflix.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import devandroid.edivaldo.manuflix.R;
import devandroid.edivaldo.manuflix.adapter.AdapterCategoria;
import devandroid.edivaldo.manuflix.adapter.AdapterDownload;
import devandroid.edivaldo.manuflix.helper.FirebaseHelper;
import devandroid.edivaldo.manuflix.model.Categoria;
import devandroid.edivaldo.manuflix.model.Download;
import devandroid.edivaldo.manuflix.model.Post;


public class DownloadFragment extends Fragment implements AdapterDownload.OnClickListener {
    private AdapterDownload adapterDownload;
    private final List<Post> postList    = new ArrayList<>();
    private final List<String> downloadList = new ArrayList<>();

    private RecyclerView rvPosts;
    private ProgressBar progressBar;
    private TextView textInfo;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_download, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        iniciaComponentes(view);
        recuperaDownload();
        configRv();
    }
    private void configRv(){
        rvPosts.setLayoutManager(new LinearLayoutManager(getContext()));
        rvPosts.setHasFixedSize(true);
        adapterDownload = new AdapterDownload(postList, this);
        rvPosts.setAdapter(adapterDownload);

    }

    private void recuperaDownload(){

        DatabaseReference DownloadRef = FirebaseHelper.getDatabaseReference()
                .child("downloads");
        DownloadRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()){
                   downloadList.add(ds.getValue(String.class));
                }

                recuperaPost(downloadList);

                adapterDownload.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void recuperaPost(List<String> downloadList){
        DatabaseReference postRef = FirebaseHelper.getDatabaseReference()
                .child("posts");
        postRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()){
                    Post post = ds.getValue(Post.class);
                    if (downloadList.contains(post.getId())){
                        postList.add(post);

                    }
                }

              listisEmpty();



                progressBar.setVisibility(View.GONE);
                Collections.reverse(postList);
                adapterDownload.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void listisEmpty(){
        if (!downloadList.isEmpty()){
            textInfo.setText("");
        }else{
            textInfo.setText("Nenhum download efetuado");
        }


    }
    private void iniciaComponentes(View view){
        rvPosts = view.findViewById(R.id.rvPosts);
        progressBar = view.findViewById(R.id.progressBar);
        textInfo = view.findViewById(R.id.textInfo);
    }

    @Override
    public void onItemClickListener(Post post) {
        postList.remove(post);
        adapterDownload.notifyDataSetChanged();

        downloadList.remove(post.getId());
        Download.salvar(downloadList);

        listisEmpty();

    }
}