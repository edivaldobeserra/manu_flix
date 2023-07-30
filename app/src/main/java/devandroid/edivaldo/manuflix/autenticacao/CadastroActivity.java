package devandroid.edivaldo.manuflix.autenticacao;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import devandroid.edivaldo.manuflix.R;

public class CadastroActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);
        configClicks();
    }

    public void configClicks(){
        findViewById(R.id.btn_Login).setOnClickListener(view -> finish());
    }
}
