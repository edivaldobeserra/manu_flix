package devandroid.edivaldo.manuflix.autenticacao;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import devandroid.edivaldo.manuflix.R;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        configClicks();
    }

    public void configClicks(){
        findViewById(R.id.btn_cadastro).setOnClickListener(view ->
                startActivity(new Intent(this, CadastroActivity.class)));
    }
}