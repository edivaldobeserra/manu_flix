package devandroid.edivaldo.manuflix.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleCoroutineScope;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import devandroid.edivaldo.manuflix.R;
import devandroid.edivaldo.manuflix.adapter.AdapterBreve;
import devandroid.edivaldo.manuflix.adapter.AdapterCategoria;
import devandroid.edivaldo.manuflix.helper.FirebaseHelper;
import devandroid.edivaldo.manuflix.model.Categoria;
import devandroid.edivaldo.manuflix.model.Post;

public class BreveFragment extends Fragment {
    private List<Post> postList = new ArrayList<>();
    private AdapterBreve adapterBreve;
    private RecyclerView rvPost;
    private ProgressBar progressBar;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_breve, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        iniciaComponentes(view);

        configRv();
        recuperaPost();
    }

    private void configRv(){
        rvPost.setLayoutManager(new LinearLayoutManager(getContext()));
        rvPost.setHasFixedSize(true);
        adapterBreve = new AdapterBreve(postList);
        rvPost.setAdapter(adapterBreve);

    }

    private void recuperaPost(){
        DatabaseReference postRef = FirebaseHelper.getDatabaseReference()
                .child("posts");
        postRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()){
                    postList.add(ds.getValue(Post.class));
                }


                progressBar.setVisibility(View.GONE);
                adapterBreve.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void iniciaComponentes(View view){
        rvPost = view.findViewById(R.id.rvPosts);
        progressBar = view.findViewById(R.id.progressBar);

    }

}