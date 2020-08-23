package com.jacd.divisas.ui.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.L;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.jacd.divisas.R;
import com.jacd.divisas.adapter.ListAdapter;
import com.jacd.divisas.common.EndlessRecyclerOnScrollListener;
import com.jacd.divisas.models.SelectorModel;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private final String TAG = "HomeFragment";
    private FloatingActionButton btnFab;
    private Context mContext;
    private RecyclerView rv;
    private ListAdapter adapter;
    private LinearLayoutManager llm;

    private SharedPreferences settings;
    private SharedPreferences.Editor editor;


    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        initComponent(view);
        return view;
    }

    private void initComponent(View view) {
        mContext = HomeFragment.this.getContext();

        settings = mContext.getSharedPreferences(getString(R.string.shared_key), 0);
        editor = settings.edit();
        rv = view.findViewById(R.id.rv_home);

        boolean isDark = settings.getBoolean("modeDark",false);
        LinearLayout tt = view.findViewById(R.id.llBackHome);
        int d = R.color.modeDark;
        int l = R.color.modeLight;
        tt.setBackgroundColor(mContext.getResources().getColor(isDark ?d:l));

    }

    @Override
    public void onStart() {
        super.onStart();
        getList();
    }

    private void refreshFragment() {
        try {
            FragmentManager fragmentManager = this.getActivity().getSupportFragmentManager();
            ToolsFragment fragment = new ToolsFragment();
            fragmentManager.beginTransaction().replace(R.id.content, fragment).commit();
        }catch (Exception e){e.printStackTrace();}

    }

    private void getList() {
        List<SelectorModel> list = new ArrayList<>();
        double BC = Double.parseDouble(settings.getString("BC", "0.00000"));
        double DC = Double.parseDouble(settings.getString("DC", "0000.00000"));
        double DB = Double.parseDouble(settings.getString("DB", "000000.00000"));
        int[] id_icon = new int[]{R.drawable.country_usa, R.drawable.country_colombia, R.drawable.country_venezuela};
        double[] tasa = new double[]{BC,DC,DB};
        String[] country = new String[]{"Estados Unidos", "Colombia", "Venezuela"};
        String[] simbol = new String[]{"$", "COP", "BS"};

        for (int i=0;i<3;i++){
            SelectorModel sm = new SelectorModel();
            sm.setCountry(country[i]);
            sm.setId(i+1);
            sm.setIdIcon(id_icon[i]);
            sm.setIdCountry(i);
            sm.setSimbol(simbol[i]);
            sm.setTaza(tasa[i]);
            list.add(sm);
        }
        print(list);
    }

    private void print(List<SelectorModel> LIST) {
        llm = new LinearLayoutManager(mContext);
        rv.setLayoutManager(llm);
        adapter = new ListAdapter(mContext, LIST);
        rv.setAdapter(adapter);

        //int current = (LIST.size()>=50) ? 30 : LIST.size() ;
        /*rv.setOnScrollListener(new EndlessRecyclerOnScrollListener(llm) {
            @Override
            public void onLoadMore(int current_page) {
                // do something...
            }
        });*/
    }

}