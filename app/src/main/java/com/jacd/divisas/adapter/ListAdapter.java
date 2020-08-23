package com.jacd.divisas.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.jacd.divisas.R;
import com.jacd.divisas.models.SelectorModel;

import java.util.ArrayList;
import java.util.List;

class ListViewHolder extends RecyclerView.ViewHolder {

    EditText mValue;
    TextView mSimbol;
    ImageView mImage;
    CheckBox mCheckSelect;


    ListViewHolder(View itemView, AppCompatActivity activity) {
        super(itemView);
        mValue = (EditText) itemView.findViewById(R.id.et_spn_value);
        mSimbol = itemView.findViewById(R.id.tv_spn_simbol);
        mImage = itemView.findViewById(R.id.iv_spn_country);
        mCheckSelect = itemView.findViewById(R.id.btn_item_1);

    }
}

public class ListAdapter extends RecyclerView.Adapter<ListViewHolder> {
    private static final String TAG = "ListAdapter";

    private SharedPreferences settings;
    private static SharedPreferences.Editor editor;

    List<SelectorModel> mData = new ArrayList<>();
    List<SelectorModel> mUpdate = new ArrayList<>();
    private AppCompatActivity Activity;
    private Context context;


    public ListAdapter(Context context, List<SelectorModel> mData) {
        this.mData = mData;
        this.context = context;

        settings = context.getSharedPreferences(context.getResources().getString(R.string.shared_key), 0);
        editor = settings.edit();
    }

 @NonNull
    @Override
    public ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.item, parent, false);
        return new ListViewHolder(itemView, Activity);
    }


    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ListViewHolder holder, int position) {

        holder.mImage.setImageDrawable(context.getResources().getDrawable(mData.get(position).getIdIcon()));
        holder.mSimbol.setText(mData.get(position).getSimbol());
        holder.mCheckSelect.setChecked(false);
        if (!holder.mValue.getText().toString().isEmpty())
            holder.mValue.setText(""+mData.get(position).getMount());

        holder.mValue.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                int x=0;
                if (editable.toString().length() != 0){
                    for (SelectorModel sm: mData){
                        if (position!=x){
                            sm.setMount(Double.parseDouble(editable.toString()));
                            //notifyItemChanged(position, sm);
                        }
                        x++;
                    }
                    //todo: mire termine esto

                }

                switch (position) {
                    case 0:

                        Log.w(TAG, "Value: "+editable.toString());
                        Log.w(TAG, "Cambiar valor pos 1");
                        Log.w(TAG, "Cambiar valor pos 2");
                        break;
                    case 1:

                        Log.w(TAG, "Value: "+editable.toString());
                        Log.w(TAG, "Cambiar valor pos 0");
                        Log.w(TAG, "Cambiar valor pos 2");
                        break;
                    case 2:

                        Log.w(TAG, "Value: "+editable.toString());
                        Log.w(TAG, "Cambiar valor pos 0");
                        Log.w(TAG, "Cambiar valor pos 1");
                        break;
                }
                //refresh();

            }
        });
       /*
        Glide.with(context)
                .load(mData.get(position).getIcon())
                .placeholder(context.getResources().getDrawable(R.drawable.ic_logo))
                .error(context.getResources().getDrawable(R.drawable.ic_logo))
                .into(holder.mImage);
                */


        //todo: ojo aqui
        holder.mCheckSelect.setOnClickListener(v -> {
            for (int i=0; i<getItemCount();i++){
                mData.get(i).getMount();
            }
        });

    }


    public List<SelectorModel> getList() {
       return this.mData;
    }

    public void refresh(List<SelectorModel> list) {
        this.mData = list;
        this.notifyDataSetChanged();
    }

    public void refresh() {
        this.notifyDataSetChanged();

    }


    @Override
    public int getItemCount() {
        if (mData != null)
            return mData.size();
        else
            return 0;
    }

}



















