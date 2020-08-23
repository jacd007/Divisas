package com.jacd.divisas.ui.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LayoutAnimationController;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jacd.divisas.R;
import com.jacd.divisas.common.Utils;

public class TodayFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = "TodayFragment";
    private SharedPreferences settings;
    private SharedPreferences.Editor edit;
    private Context mContext;
    private LinearLayout llView4;
    private TextView tvRates1;
    private TextView tvRates2;
    private TextView tvRates3;
    private EditText etValu1;
    private EditText etValu2;
    private EditText etValu3;
    private Button btnClear;
    private Button btnAccept;
    private CheckBox btnSelect1;
    private CheckBox btnSelect2;
    private CheckBox btnSelect3;
    private double BC;
    private double DB;
    private double DC;
    private InputMethodManager imm;

    public TodayFragment() {
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
        View view = inflater.inflate(R.layout.fragment_today, container, false);
        initComponent(view);
        return view;

    }

    @SuppressLint("SetTextI18n")
    private void initComponent(View view) {
        mContext = this.getContext();
        settings = mContext.getSharedPreferences(getString(R.string.shared_key), 0);
        edit = settings.edit();
        BC = Double.parseDouble(settings.getString("BC","0.00000"));
        DC = Double.parseDouble(settings.getString("DC","0000.00000"));
        DB = Double.parseDouble(settings.getString("DB","000000.00000"));

        imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);

        double CB = 1 / Double.parseDouble(settings.getString("CB","0.00000"));
        double BD = 1 / Double.parseDouble(settings.getString("BD","0.00000"));
        double CD = 1 / Double.parseDouble(settings.getString("CD","0.00000"));

        llView4 = view.findViewById(R.id.v_item_4);
        btnSelect1 = view.findViewById(R.id.btn_item_1);
        btnSelect2 = view.findViewById(R.id.btn_item_2);
        btnSelect3 = view.findViewById(R.id.btn_item_3);
        tvRates1 = view.findViewById(R.id.tv_today_rates1);
        tvRates2 = view.findViewById(R.id.tv_today_rates2);
        tvRates3 = view.findViewById(R.id.tv_today_rates3);
        etValu1 = view.findViewById(R.id.et_today_value1);
        etValu2 = view.findViewById(R.id.et_today_value2);
        etValu3 = view.findViewById(R.id.et_today_value3);

        btnAccept = view.findViewById(R.id.btn_today_view);
        btnClear = view.findViewById(R.id.btn_today_clear);

        boolean isDark = settings.getBoolean("modeDark",false);
        LinearLayout tt = view.findViewById(R.id.llBackToday);
        int d = R.color.modeDark;
        int l = R.color.modeLight;
        tt.setBackgroundColor(mContext.getResources().getColor(isDark ?d:l));
        ((TextView)view.findViewById(R.id.tvh1)).setTextColor(mContext.getResources().getColor(isDark ?l:d));
        ((TextView)view.findViewById(R.id.tvh2)).setTextColor(mContext.getResources().getColor(isDark ?l:d));
        ((TextView)view.findViewById(R.id.tvh3)).setTextColor(mContext.getResources().getColor(isDark ?l:d));
        tvRates1.setTextColor(mContext.getResources().getColor(isDark ?l:d));
        tvRates2.setTextColor(mContext.getResources().getColor(isDark ?l:d));
        tvRates3.setTextColor(mContext.getResources().getColor(isDark ?l:d));

        try {
            InputMethodManager inputManager = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
        }catch (NullPointerException e){
            e.printStackTrace();
        }

        inits(view);

        if (BC==0.00000 || DB==0.00000 || DC==0.00000){
            Utils.getMessageSnackBar(view,"No ha guardado tasa del día",mContext, true);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    refreshFragment();
                }
            }, 1000);
        }

        String v1 = "-> "+String.format("%.5f", BC);
        String v2 = "-> "+String.format("%.5f", DB);
        String v3 = "-> "+String.format("%.5f", DC);

        tvRates1.setText(v1.contains(".00000")?v1.replace(".00000",".00"):v1);
        tvRates2.setText(v2.contains(".00000")?v2.replace(".00000",".00"):v2);
        tvRates3.setText(v3.contains(".00000")?v3.replace(".00000",".00"):v3);

        btnAccept.setText(R.string.title_show_rate);

        btnClear.setOnClickListener(view1 -> {
            inits(view1);
        });

        btnAccept.setOnClickListener(view1 -> {
            btnAccept.setText(llView4.getVisibility()==View.GONE?R.string.title_hide_rate:R.string.title_show_rate);
            animate(llView4.getVisibility()==View.GONE, llView4);
        });


        btnSelect1.setOnClickListener(this);
        btnSelect2.setOnClickListener(this);
        btnSelect3.setOnClickListener(this);



    }

    private void isSelectedEdit(int select) {

        etValu1.setEnabled(select==1);
        etValu2.setEnabled(select==2);
        etValu3.setEnabled(select==3);

        btnSelect1.setChecked(select==1);
        btnSelect2.setChecked(select==2);
        btnSelect3.setChecked(select==3);

        EditText et = null,eto1 = null,eto2 = null;
        double ti =0.0,to1=0.0,to2=0.0;
        switch (select) {
            case 1:
                et = etValu1;
                eto1 = etValu2;
                eto2 = etValu3;
                to1 = DC;
                to2 = DB;
                break;
            case 2:
                et = etValu2;
                eto1 = etValu1;
                eto2 = etValu3;
                to1 = 1 / DC;
                to2 = 1 / BC;
                break;
            case 3:
                et = etValu3;
                eto1 = etValu1;
                eto2 = etValu2;
                to1 = 1/DB;
                to2 = BC;
                break;
        }

        assert et != null;
        et.requestFocus(); //Asegurar que editText tiene focus
        //et.setText("");
        imm.showSoftInput(et, InputMethodManager.SHOW_IMPLICIT);
        EditText finalEto = eto1;
        EditText finalEto1 = eto2;
        double finalTo = to1;
        double finalTo1 = to2;
        /*
        et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @SuppressLint("SetTextI18n")
            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length() != 0){
                    if (btnSelect1.isChecked()){
                        etValu2.setText(""+calculate(s.toString(), DC));
                        etValu3.setText(""+calculate(s.toString(), DB));
                    }else if (btnSelect2.isChecked()){
                        etValu1.setText(""+calculate(s.toString(), 1 / DC));
                        etValu3.setText(""+calculate(s.toString(), 1 / BC));
                    }else{
                        etValu1.setText(""+calculate(s.toString(), 1/DB));
                        etValu2.setText(""+calculate(s.toString(), BC));
                    }

                }

            }
        });
        */
     /*   */
        //mFollowText(et, to1, to2, eto1, eto2);
    }

    @Override
    public void onStart() {
        super.onStart();
        etValu1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @SuppressLint("SetTextI18n")
            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length() != 0 && btnSelect1.isChecked()){
                    etValu2.setText(""+calculate(s.toString(), DC));
                    etValu3.setText(""+calculate(s.toString(), DB));
                }

            }
        });
        etValu2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @SuppressLint("SetTextI18n")
            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length() != 0 && btnSelect2.isChecked()){
                    etValu1.setText(""+calculate(s.toString(), 1 / DC));
                    etValu3.setText(""+calculate(s.toString(), 1 / BC));
                }

            }
        });
        etValu3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @SuppressLint("SetTextI18n")
            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length() != 0 && btnSelect3.isChecked()){
                    etValu1.setText(""+calculate(s.toString(), 1/DB));
                    etValu2.setText(""+calculate(s.toString(), BC));
                }

            }
        });

    }

    private void inits(View view) {


        etValu1.setEnabled(false);
        etValu2.setEnabled(false);
        etValu3.setEnabled(false);
        etValu1.setText(null);
        etValu2.setText(null);
        etValu3.setText(null);
        btnSelect1.setChecked(false);
        btnSelect2.setChecked(false);
        btnSelect3.setChecked(false);
        btnSelect1.setVisibility(View.VISIBLE);
        btnSelect2.setVisibility(View.VISIBLE);
        btnSelect3.setVisibility(View.VISIBLE);
    }

    /*
    private void isSelectedEdit1(int option) {

        switch (option){
            case 1:
                etValu1.setEnabled(true);
                etValu1.requestFocus(); //Asegurar que editText tiene focus
                etValu2.setEnabled(false);
                etValu3.setEnabled(false);
                imm.showSoftInput(etValu1, InputMethodManager.SHOW_IMPLICIT);
                btnSelect1.setVisibility(View.GONE);
                btnSelect2.setVisibility(View.VISIBLE);
                btnSelect3.setVisibility(View.VISIBLE);
                llView1.setBackgroundColor(mContext.getResources().getColor(R.color.color_select_true));
                llView2.setBackgroundColor(mContext.getResources().getColor(R.color.color_select_false));
                llView3.setBackgroundColor(mContext.getResources().getColor(R.color.color_select_false));
                break;
            case 2:
                etValu1.setEnabled(false);
                etValu2.setEnabled(true);
                etValu2.requestFocus(); //Asegurar que editText tiene focus
                etValu3.setEnabled(false);
                imm.showSoftInput(etValu2, InputMethodManager.SHOW_IMPLICIT);
                btnSelect1.setVisibility(View.VISIBLE);
                btnSelect2.setVisibility(View.GONE);
                btnSelect3.setVisibility(View.VISIBLE);
                llView1.setBackgroundColor(mContext.getResources().getColor(R.color.color_select_false));
                llView2.setBackgroundColor(mContext.getResources().getColor(R.color.color_select_true));
                llView3.setBackgroundColor(mContext.getResources().getColor(R.color.color_select_false));
                break;
            case 3:
                etValu1.setEnabled(false);
                etValu2.setEnabled(false);
                etValu3.setEnabled(true);
                imm.showSoftInput(etValu3, InputMethodManager.SHOW_IMPLICIT);
                etValu3.requestFocus(); //Asegurar que editText tiene focus
                btnSelect1.setVisibility(View.VISIBLE);
                btnSelect2.setVisibility(View.VISIBLE);
                btnSelect3.setVisibility(View.GONE);
                llView1.setBackgroundColor(mContext.getResources().getColor(R.color.color_select_false));
                llView2.setBackgroundColor(mContext.getResources().getColor(R.color.color_select_false));
                llView3.setBackgroundColor(mContext.getResources().getColor(R.color.color_select_true));
                break;
        }



    }
     */
    /*
    private void mFollowText(final EditText inEdit, final double t1, final double t2, final EditText outEdit1, final EditText outEdit2){
        inEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @SuppressLint("DefaultLocale")
            @Override
            public void afterTextChanged(Editable s) {
//                Log.w(TAG,"resp: "+s);
                outEdit1.setText(inEdit.getText().length() != 0 ? ""+calculate(Double.parseDouble(s.toString()), t1): "0.00000");
                outEdit2.setText(inEdit.getText().length() != 0 ? ""+calculate(Double.parseDouble(s.toString()), t2): "0.00000");
            }
        });
    }
    */

    private String calculate(String value, double tasa) {
        try {

            String sub = value.isEmpty()||value.equals("null")?"0.000000":value;
            double values = Double.parseDouble(sub);
            values = Utils.formatFiveDecimal(values * tasa);
            return String.valueOf(values);

        }catch (Exception e){
            e.printStackTrace();
            return "0.00000";
        }

    }

    private void animate(boolean show_or_hide, LinearLayout layoutAnimate){
        AnimationSet set = new AnimationSet(true);
        Animation animation = null;
        if (show_or_hide){
            //desde la esquina inferior derecha a la superior izquierda
            animation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 1.0f, Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f);
            //animation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 1.0f, Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 1.0f, Animation.RELATIVE_TO_SELF, 0.0f);
        }
        else{    //desde la esquina superior izquierda a la esquina inferior derecha
            animation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 1.0f, Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f);
            //animation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 1.0f, Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 1.0f);
        }
        //duración en milisegundos
        animation.setDuration(500);
        set.addAnimation(animation);
        LayoutAnimationController controller = new LayoutAnimationController(set, 0.25f);

        layoutAnimate.setLayoutAnimation(controller);
        layoutAnimate.startAnimation(animation);
        layoutAnimate.setVisibility(show_or_hide?View.VISIBLE:View.GONE);
    }

    private void refreshFragment() {
        try {
            FragmentManager fragmentManager = this.getActivity().getSupportFragmentManager();
            ToolsFragment fragment = new ToolsFragment();
            fragmentManager.beginTransaction().replace(R.id.content, fragment).commit();
        }catch (Exception e){e.printStackTrace();}

    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (BC==0.00000 || DB==0.00000 || DC==0.00000){
            Toast.makeText(mContext, "No ha guardado tasa del día", Toast.LENGTH_SHORT).show();
          new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    refreshFragment();
                }
            }, 1000);

        }else {
            Log.w(TAG,"nada");
            switch (id) {
                case R.id.btn_item_1:
                    isSelectedEdit(1);
                    //mFollowText(etValu1, DC, DB, etValu2, etValu3);
                    break;
                case R.id.btn_item_2:
                    isSelectedEdit(2);
                    //mFollowText(etValu2, DC, BC, etValu1, etValu3);
                    break;
                case R.id.btn_item_3:
                    isSelectedEdit(3);
                    //mFollowText(etValu3, BC, DB, etValu1, etValu2);
                    break;
                case R.id.btn_today_accept:
                    etValu1.setText(""+calculate(etValu1.getText().toString(), 0.00) );
                    etValu2.setText(""+calculate(etValu2.getText().toString(), 0.00) );
                    etValu3.setText(""+calculate(etValu3.getText().toString(), 0.00) );
                    break;
            }

        }

    }
}