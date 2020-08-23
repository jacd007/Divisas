package com.jacd.divisas.ui.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.jacd.divisas.R;
import com.jacd.divisas.common.Utils;


public class ToolsFragment extends Fragment {

    private static final String TAG = "ToolsFragment";
    private SharedPreferences settings;
    private SharedPreferences.Editor edit;
    private Context mContext;
    private EditText etValu1;
    private EditText etValu2;
    private EditText etValu3;
    private Button btnAccept;
    private Switch aSwitch;
    private TextView ttv;
    private double BC;
    private double DB;
    private double DC;
    private LinearLayout tt;
    private int d,l;

    public ToolsFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_tools, container, false);
        initComponent(view);
        return view;
    }

    private void initComponent(View view) {
        mContext = this.getContext();
        settings = mContext.getSharedPreferences(getString(R.string.shared_key), 0);
        edit = settings.edit();
        BC = Double.parseDouble(settings.getString("t1","0.02000"));
        DB = Double.parseDouble(settings.getString("t2","300000.00000"));
        DC = Double.parseDouble(settings.getString("t3","3300.00000"));

        etValu1 = view.findViewById(R.id.et_today_value1);
        etValu2 = view.findViewById(R.id.et_today_value2);
        etValu3 = view.findViewById(R.id.et_today_value3);

        aSwitch = view.findViewById(R.id.sw_mode);

        boolean isDark = settings.getBoolean("modeDark",false);
        aSwitch.setChecked(isDark);
        tt = view.findViewById(R.id.llBackTools);
        ttv = view.findViewById(R.id.tvv);
         d = mContext.getResources().getColor(R.color.modeDark);
         l = mContext.getResources().getColor(R.color.modeLight);
        tt.setBackgroundColor(isDark ?d:l);

        btnAccept = view.findViewById(R.id.btn_today_view);

        aSwitch.setOnClickListener(view1 -> {
            edit.putBoolean("modeDark",aSwitch.isChecked());
            edit.commit();
            modeBackground(aSwitch.isChecked()?d:l, aSwitch.isChecked()?l:d);
        });

    }

    private void modeBackground(int colorBack, int colorText){
        tt.setBackgroundColor(colorBack);
        ttv.setTextColor(colorText);
        aSwitch.setTextColor(colorText);
    }

    @Override
    public void onStart() {
        super.onStart();
        btnAccept.setOnClickListener(view1 -> {
            BC = Double.parseDouble(etValu1.getText().toString());
            DB = Double.parseDouble(etValu2.getText().toString());
            DC = Double.parseDouble(etValu3.getText().toString());

            double CB = 1 / Double.parseDouble(etValu1.getText().toString());
            double BD = 1 / Double.parseDouble(etValu2.getText().toString());
            double CD = 1 / Double.parseDouble(etValu3.getText().toString());

            edit.putString("BC",""+ BC);
            edit.putString("DB",""+ DB);
            edit.putString("DC",""+ DC);
            edit.putString("CB",""+ CB);
            edit.putString("BD",""+ BD);
            edit.putString("CD",""+ CD);

            edit.putBoolean("modeDark",aSwitch.isChecked());
            edit.commit();

            Utils.getMessageSnackBar(view1, "Actualizando Tasas del día", mContext, false);
        });
    }
}