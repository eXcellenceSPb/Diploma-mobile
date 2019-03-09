package com.example.vserg.myapplication;

import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MedicalActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private String mTime;
    private String mAntib;
    private String mComm;
    private String mAnatoxDoze;
    private String mAnatoxName;
    private String mAntidoteDoze;
    private String mAntidoteName;
    private String mAnaest;
    private String mSer;
    private String wound;
    private String mLocation;
    private String mDiag;
    private String mEvac;
    private String mPlace;
    private String mTransport;
    private String mQueue;
    private String mInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medical);

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        Button send = findViewById(R.id.sendm);
        send.setOnClickListener(view -> {
            send(mTime, mAntib, mComm, mAnatoxName,
                    mAnatoxDoze, mAntidoteName, mAntidoteDoze,
                    mAnaest, mSer, wound,
                    mLocation, mDiag, mEvac,
                    mPlace, mTransport, mQueue, mInfo);
            onClick();
        });
        initialize();

        ///чтение из полей доделать
    }

    public void initialize(){
        Spinner serrum = findViewById(R.id.serrum);
        final String[] ser = getResources().getStringArray(R.array.ser);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item, ser);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        serrum.setAdapter(adapter);

        Spinner evac = findViewById(R.id.evac);
        final String[] eva = getResources().getStringArray(R.array.evac);
        ArrayAdapter<String> eAdapt = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item,eva);
        eAdapt.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        evac.setAdapter(eAdapt);

        Spinner queue = findViewById(R.id.queue);
        final String[] queu = getResources().getStringArray(R.array.queue);
        ArrayAdapter<String> qAdapt = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item,queu);
        qAdapt.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        queue.setAdapter(qAdapt);

        Spinner place = findViewById(R.id.place);
        final String[] plac = getResources().getStringArray(R.array.place);
        ArrayAdapter<String> pAdapt = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item,plac);
        pAdapt.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        place.setAdapter(pAdapt);

        Spinner transport = findViewById(R.id.transport);
        final String[] transpor = getResources().getStringArray(R.array.transport);
        ArrayAdapter<String> tAdapt = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item,transpor);
        tAdapt.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        transport.setAdapter(tAdapt);

        Spinner commit = findViewById(R.id.comm);
        final String[] commi = getResources().getStringArray(R.array.commit);
        ArrayAdapter<String> comAdapt = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item,commi);
        comAdapt.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        commit.setAdapter(comAdapt);

        Spinner wound = findViewById(R.id.wound);
        final String[] woun = getResources().getStringArray(R.array.wound);
        ArrayAdapter<String> wAdapt = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item,woun);
        wAdapt.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        wound.setAdapter(wAdapt);
    }

    public void onClick() {
        Toast toast = Toast.makeText(this, "Отправлено", Toast.LENGTH_LONG);
        toast.setGravity(Gravity.TOP, 0, 160);
        toast.show();
    }

    private void send(String mTime, String mAntib, String mComm, String mAnatoxName,
                      String mAnatoxDoze, String mAntidoteName, String mAntidoteDoze,
                      String mAnaest, String mSer, String wound,
                      String mLocation, String mDiag, String mEvac,
                      String mPlace, String mTransport, String mQueue, String mInfo) {
        String jsonStr = "{\"wound_time\":\"" + mTime + "\"," +
                "\"antibiotic\":\"" + mAntib + "\"," +
                "\"serum\":\"" + mSer + "\"," +
                "\"anatoxin\":\"" + mAnatoxName + "(" + mAnatoxDoze + ")" + "\"," +
                "\"antidot\":\"" + mAntidoteName + "(" + mAntidoteDoze + ")" + "\"," +
                "\"anaesthetic\":\"" + mAnaest + "\"," +
                "\"commit\":\"" + mComm + "\"," +
                "\"wound\":\"" + wound + "\"," +
                "\"location\":\"" + mLocation + "\"," +
                "\"diagnosis\":\"" + mDiag + "\"," +
                "\"evacuation\":\"" + mEvac + "\"," +
                "\"place\":\"" + mPlace + "\"," +
                "\"transport\":\"" + mTransport + "\"," +
                "\"queue\":\"" + mQueue + "\"," +
                "\"info\":\"" + mInfo + "\"}";

        String url = "http://192.168.0.83:8090/api/med";

        OkHttpClient client = new OkHttpClient();

        RequestBody body = RequestBody.create(JSON, jsonStr);

        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                call.cancel();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.d("TAG", response.body().string());
            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.activity_medical) {
            Intent secAct = new Intent(getApplicationContext(), MedicalActivity.class);
            startActivity(secAct);
        } else if (id == R.id.activity_main) {
            Intent act = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(act);
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
