package devandroid.edivaldo.manuflix.model;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;

import java.util.List;

import devandroid.edivaldo.manuflix.helper.FirebaseHelper;

public class Categoria {
    private String id;
    private String nome;
    private List<Post>list;

    public Categoria() {

    }


    public Categoria(String nome) {
        this.nome = nome;

        DatabaseReference categoriaRef = FirebaseHelper.getDatabaseReference();
        this.setId(categoriaRef.push().getKey());

        salvar();
    }

    private void salvar(){
        DatabaseReference categoriaRef = FirebaseHelper.getDatabaseReference()
                .child("categorias")
                        .child(getId());
        categoriaRef.setValue(this);

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
    @Exclude
    public List<Post> getList() {
        return list;
    }

    public void setList(List<Post> list) {
        this.list = list;
    }
}
