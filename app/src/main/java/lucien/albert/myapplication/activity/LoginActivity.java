package lucien.albert.myapplication.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.HashMap;

import lucien.albert.myapplication.AppConfig;
import lucien.albert.myapplication.network.Downloader;
import lucien.albert.myapplication.network.OnDownloadCompleteListener;
import lucien.albert.myapplication.R;
import lucien.albert.myapplication.model.Result;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener, OnDownloadCompleteListener {

    private Button btnValider;
    private EditText txtId;
    private EditText txtPwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btnValider = (Button) findViewById(R.id.btnValider);
        btnValider.setOnClickListener((View.OnClickListener) this) ;
        txtId = (EditText) findViewById(R.id.txtId);
        txtPwd = (EditText) findViewById(R.id.txtPwd);

    }

    @Override
    public void onClick(View v) {

        if(v.getId() == R.id.btnValider) {

            HashMap<String, String> Params = new HashMap<String, String>();
            Params.put("username", txtId.getText().toString());
            Params.put("password", txtPwd.getText().toString());
            save("username", txtId.getText().toString());
            Downloader d = new Downloader(this, "http://www.raphaelbischof.fr/messaging/?function=connect" ,Params);
            d.setDownloaderList(this);
            d.execute();

        }

    }

    @Override
    public void onDownloadCompleted(String result, int type) {

        Gson gson = new Gson();

        Result r = gson.fromJson(result, Result.class);


        if(r == null){

            Toast.makeText(this, "Vérifiez votre connexion !" ,Toast.LENGTH_SHORT).show();

        }
        else if(r.getCode() == 200) {

            Toast.makeText(this, "Vous etes connecté !", Toast.LENGTH_SHORT).show();

            save("accesstoken", r.getAccesstoken());

            Intent newActivity = new Intent(getApplicationContext(), ChannelListActivity.class);
            startActivity(newActivity);

        }
        else{

            Toast.makeText(this, "Identifiant ou mot de passe érroné !" ,Toast.LENGTH_SHORT).show();

        }

    }

    public void save(String key, String value){
        SharedPreferences settings = getSharedPreferences(AppConfig.PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(key, value);
        editor.commit();
    }
}
