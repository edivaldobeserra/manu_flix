package devandroid.edivaldo.manuflix.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import devandroid.edivaldo.manuflix.R;
import devandroid.edivaldo.manuflix.model.Post;

public class DetalhePostActivity extends AppCompatActivity {

    private TextView textTitulo;
    private ImageView imagemPost;
    private ImageView imageFake;
    private TextView textAno;
    private TextView textDuracao;
    private TextView textElenco;
    private ConstraintLayout btnAssistir;
    private ConstraintLayout btnBaixar;
    private TextView textSinopse;
    private RecyclerView rvPosts;

    private Post post;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhe_post);

        iniciaComponentes();

        configDados();

        configCliques();

    }

    private void configCliques() {
        findViewById(R.id.ibVoltar).setOnClickListener(view -> finish());
    }

    private void configDados() {
        post = (Post) getIntent().getSerializableExtra("postSelecionado");

        textTitulo.setText(post.getTitulo());

        imageFake.setVisibility(View.GONE);
        Picasso.get()
                .load(post.getImagem())
                .into(imagemPost);

        textAno.setText(post.getAno());
        textDuracao.setText(getString(R.string.text_duracao, post.getDuracao()));
        textSinopse.setText(post.getSinopse());
        textElenco.setText(post.getElenco());
    }

    private void iniciaComponentes() {
        textTitulo = findViewById(R.id.textTitulo);
        imagemPost = findViewById(R.id.imagemPost);
        imageFake = findViewById(R.id.imageFake);
        textAno = findViewById(R.id.textAno);
        textDuracao = findViewById(R.id.textDuracao);
        textElenco = findViewById(R.id.textElenco);
        btnAssistir = findViewById(R.id.btnAssistir);
        btnBaixar = findViewById(R.id.btnBaixar);
        textSinopse = findViewById(R.id.textSinopse);
        rvPosts = findViewById(R.id.rvPosts);
    }

}