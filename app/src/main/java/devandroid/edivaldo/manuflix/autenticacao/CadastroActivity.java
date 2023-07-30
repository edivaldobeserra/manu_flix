package devandroid.edivaldo.manuflix.autenticacao;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;

import devandroid.edivaldo.manuflix.R;

public class CadastroActivity extends AppCompatActivity {
    private EditText edtEmail;
    private EditText edtSenha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        iniciaComponentes();
        configClicks();
    }

    private void validaDados() {
        String email = edtEmail.getText().toString().trim();
        String senha = edtSenha.getText().toString().trim();

        if (!email.isEmpty()) {
            if (!senha.isEmpty()) {

                cadastroFirebase(email, senha);

        } else {
            edtSenha.requestFocus();
            edtSenha.setError("Informe uma senha");
        }

    }else {
        edtEmail.requestFocus();
        edtEmail.setError("Informe uma E-mail.");
        }

    }

    private void cadastroFirebase(String email, String senha) {


    }

    public void configClicks() {
        findViewById(R.id.btn_Login).setOnClickListener(view -> finish());
    }

    private void iniciaComponentes() {
        edtEmail = findViewById(R.id.edtEmail);
        edtSenha = findViewById(R.id.edtSenha);

    }
}
