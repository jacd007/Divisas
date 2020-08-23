package com.jacd.divisas.ui.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.material.navigation.NavigationView;
import com.jacd.divisas.R;
import com.jacd.divisas.ui.fragment.ToolsFragment;
import com.jacd.divisas.ui.fragment.HomeFragment;
import com.jacd.divisas.ui.fragment.ProfileFragment;
import com.jacd.divisas.ui.fragment.TodayFragment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;


public class NavigationActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private final String TAG = "NavigationActivity";
    private Toolbar toolbar;
    private DrawerLayout drawer;
    private NavigationView navigationView;
    private ActionBarDrawerToggle toggle;
    private Fragment mSelectedFragment;

    private SharedPreferences settings;
    private SharedPreferences.Editor editor;

    private ImageView navIcon;
    private TextView navName;
    private TextView navEmail;

    private String username;
    private String email;
    private String name;
    private String image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);
        initComponent();
    }

    private void initComponent() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        settings = getSharedPreferences(getString(R.string.shared_key), 0);
        editor = settings.edit();

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        View headerView = navigationView.getHeaderView(0);

        settings = getSharedPreferences(getString(R.string.shared_key), 0);
        editor = settings.edit();

        navName = headerView.findViewById(R.id.nav_header_user);
        navEmail = headerView.findViewById(R.id.nav_header_email);
        navIcon = headerView.findViewById(R.id.nav_header_icon);

        username = settings.getString(getString(R.string.username),"N/A");
        name = settings.getString(getString(R.string.name),"N/A");
        email = settings.getString(getString(R.string.email),"N/A");
        image = settings.getString(getString(R.string.image),"N/A");

        //navName.setText(""+name);
        navName.setText("Bienvenido a DT");
        navEmail.setVisibility(View.GONE);

        navEmail.setText(""+email);

        setImage(this, image);

        mSelectedFragment = null;
        selectFragment( 0 );

    }

    private void setImage(Context context, String image){
        String im = image.length() >= 5 ? image.substring(0, 5) : "";
        if (im.equals("https")) {
            Glide.with(context)
                    .load(image)
                    .centerCrop()
                    .placeholder(R.drawable.user)
                    .into(navIcon);
        }else if (image.contains("data:image/png;base64")) {
            byte[] decodedString = Base64.decode(image.replaceAll("data:image/png;base64", ""), Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            navIcon.setImageBitmap(decodedByte);
        } else {
            navIcon.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_logo));
        }
    }


    private void selectFragment(int id){
        switch (id) {
            case 0:
                mSelectedFragment = new TodayFragment();
                getSupportActionBar().setTitle("Tasa del día");
                break;

            case 1:
                mSelectedFragment = new HomeFragment();
                getSupportActionBar().setTitle("Tasa del día");
                break;
            case 2:
                mSelectedFragment = new ProfileFragment();
                getSupportActionBar().setTitle("Mi perfil");
                break;
            case 3:
                mSelectedFragment = new ToolsFragment();
                getSupportActionBar().setTitle("Configuración");
                break;
        }
        FragmentTransaction tx = getSupportFragmentManager().beginTransaction();
        tx.replace(R.id.content, mSelectedFragment);
        tx.commit();
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_home:
                selectFragment(0);
                break;
            case R.id.nav_operations:
                selectFragment(3);
                break;
            case R.id.nav_logout:
                finish();
                break;
        }

        return true;

    }


    class CambioTasao extends AsyncTask<String, String, String> {
        private Context context;
        private String Value;
        private String Tasa;

        private double tasa;
        private double valor;

        public CambioTasao(Context context, String value, String tasa) {
            this.context = context;
            Value = value;
            Tasa = tasa;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            tasa = Double.parseDouble(Tasa);
            valor = Double.parseDouble(Value);
        }

        @Override
        protected String doInBackground(String... strings) {

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }
    }

}