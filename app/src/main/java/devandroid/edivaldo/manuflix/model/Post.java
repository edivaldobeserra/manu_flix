package devandroid.edivaldo.manuflix.model;

import com.google.firebase.database.DatabaseReference;

import java.io.Serializable;

import devandroid.edivaldo.manuflix.helper.FirebaseHelper;

public class Post implements Serializable {

    private String  id;
    private String titulo;
    private String genero;
    private String elenco;
    private String ano;
    private String duracao;
    private String sinopse;

    private String imagem;
    private String video;

    public Post() {
        DatabaseReference postRef = FirebaseHelper.getDatabaseReference();
        this.setId(postRef.push().getKey());
    }

    public void salvar(){
        DatabaseReference postRef = FirebaseHelper.getDatabaseReference()
                .child("posts")
                .child(getId());
        postRef.setValue(this);
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public String getElenco() {
        return elenco;
    }

    public void setElenco(String elenco) {
        this.elenco = elenco;
    }

    public String getAno() {
        return ano;
    }

    public void setAno(String ano) {
        this.ano = ano;
    }

    public String getDuracao() {
        return duracao;
    }

    public void setDuracao(String duracao) {
        this.duracao = duracao;
    }

    public String getSinopse() {
        return sinopse;
    }

    public void setSinopse(String sinopse) {
        this.sinopse = sinopse;
    }

    public String getImagem() {
        return imagem;
    }

    public void setImagem(String imagem) {
        this.imagem = imagem;
    }
}
