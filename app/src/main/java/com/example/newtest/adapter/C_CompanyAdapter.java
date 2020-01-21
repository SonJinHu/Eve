package com.example.newtest.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatCheckedTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.newtest.R;
import com.example.newtest.api.Charger;
import com.example.newtest.common.Shared;

import java.util.ArrayList;

public class C_CompanyAdapter extends RecyclerView.Adapter<C_CompanyAdapter.CompanyViewHolder> {

    private Context context;
    private AppCompatCheckedTextView ctv_selectAll;
    private ArrayList<Charger.Company> companyList;
    private ArrayList<Boolean> checkList;

    public C_CompanyAdapter(final Context context, final AppCompatCheckedTextView ctv_selectAll, ArrayList<Charger.Company> companyList, final ArrayList<Boolean> checkList) {
        this.context = context;
        this.ctv_selectAll = ctv_selectAll;
        this.companyList = companyList;
        this.checkList = checkList;
        initSelectAllButton();
    }

    @NonNull
    @Override
    public CompanyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.item_company, parent, false);
        return new C_CompanyAdapter.CompanyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final CompanyViewHolder holder, final int position) {
        Glide.with(context)
                .load("http://115.68.223.21" + companyList.get(position).getImg())
                .into(holder.iv);

        holder.ctv.setText(companyList.get(position).getName());

        if (checkList.get(position)) {
            holder.ctv.setChecked(true);
        } else {
            holder.ctv.setChecked(false);
        }

        holder.ctv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.ctv.isChecked()) {
                    holder.ctv.setChecked(false);
                    checkList.set(position, false);
                    ctv_selectAll.setChecked(false);
                } else {
                    holder.ctv.setChecked(true);
                    checkList.set(position, true);
                    boolean isAllSelected = true;
                    for (int i = 0; i < checkList.size(); i++) {
                        if (!checkList.get(i)) {
                            isAllSelected = false;
                            break;
                        }
                    }
                    if (isAllSelected)
                        ctv_selectAll.setChecked(true);
                }
                Shared.setCompanyCheck(context, checkList);
            }
        });
    }

    @Override
    public int getItemCount() {
        return companyList.size();
    }

    class CompanyViewHolder extends RecyclerView.ViewHolder {
        ImageView iv;
        AppCompatCheckedTextView ctv;

        CompanyViewHolder(@NonNull View itemView) {
            super(itemView);
            iv = itemView.findViewById(R.id.filter_iv);
            ctv = itemView.findViewById(R.id.popup_appCompatCheckedTextView);
        }
    }

    private void initSelectAllButton() {
        int count = 0;
        for (int i = 0; i < checkList.size(); i++) {
            if (checkList.get(i))
                count++;
        }

        if (count == companyList.size()) {
            ctv_selectAll.setChecked(true);
        } else {
            ctv_selectAll.setChecked(false);
        }

        ctv_selectAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ctv_selectAll.isChecked()) {
                    ctv_selectAll.setChecked(false);
                    for (int i = 0; i < checkList.size(); i++) {
                        checkList.set(i, false);
                    }
                } else {
                    ctv_selectAll.setChecked(true);
                    for (int i = 0; i < checkList.size(); i++) {
                        checkList.set(i, true);
                    }
                }
                Shared.setCompanyCheck(context, checkList);
                notifyDataSetChanged();
            }
        });
    }
}
