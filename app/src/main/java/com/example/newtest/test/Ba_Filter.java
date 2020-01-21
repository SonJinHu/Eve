package com.example.newtest.test;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import com.example.newtest.R;
import com.example.newtest.common.CheckableImageView;

import java.util.Objects;

public class Ba_Filter extends DialogFragment {

    private static final String TAG = "dialog_full_screen";

    public static void display(FragmentManager fragmentManager) {
        Ba_Filter dialog = new Ba_Filter();
        dialog.show(fragmentManager, TAG);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.AppTheme_FullScreenDialog);
    }

    @Override
    public View onCreateView(@SuppressWarnings("NullableProblems") LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.c_filter, container, false);
        initFilterType(view);
        return view;
    }

    @Override
    public void onViewCreated(@SuppressWarnings("NullableProblems") View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Toolbar toolbar = view.findViewById(R.id.c_toolbar);
        toolbar.inflateMenu(R.menu.c_filter_menu);
        toolbar.setTitle("검색 필터");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_init:
                        Toast.makeText(getContext(), "'초기화' 버튼을 클릭했습니다.", Toast.LENGTH_SHORT).show();
                        return true;
                    default:
                        return false;
                }
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        int width = ViewGroup.LayoutParams.MATCH_PARENT;
        int height = ViewGroup.LayoutParams.MATCH_PARENT;
        Objects.requireNonNull(getDialog().getWindow()).setLayout(width, height);
        Objects.requireNonNull(getDialog().getWindow()).setWindowAnimations(R.style.AppTheme_FullScreenDialog);
    }

    private void initFilterType(View view) {
        CheckableImageView iv_ac3 = view.findViewById(R.id.ba_iv_ac3);
        CheckableImageView iv_chaDemo = view.findViewById(R.id.ba_iv_cha);
        CheckableImageView iv_combo = view.findViewById(R.id.ba_iv_combo);
        CheckableImageView iv_single = view.findViewById(R.id.c_iv_single);
        CheckableImageView iv_super = view.findViewById(R.id.c_iv_super);
        CheckableImageView iv_h2 = view.findViewById(R.id.c_iv_h2);

        TextView tv_ac3 = view.findViewById(R.id.ba_tv_ac3);
        TextView tv_chaDemo = view.findViewById(R.id.ba_tv_cha);
        TextView tv_combo = view.findViewById(R.id.c_tv_combo);
        TextView tv_single = view.findViewById(R.id.c_tv_single);
        TextView tv_super = view.findViewById(R.id.c_tv_super);
        TextView tv_h2 = view.findViewById(R.id.ba_tv_h2);

        iv_ac3.setOnCheckedChangeListener(getListener(tv_ac3));
        iv_chaDemo.setOnCheckedChangeListener(getListener(tv_chaDemo));
        iv_combo.setOnCheckedChangeListener(getListener(tv_combo));
        iv_single.setOnCheckedChangeListener(getListener(tv_single));
        iv_super.setOnCheckedChangeListener(getListener(tv_super));
        iv_h2.setOnCheckedChangeListener(getListener(tv_h2));
    }

    private CheckableImageView.setOnCheckedChangeListener getListener(final TextView tv) {
        return new CheckableImageView.setOnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(boolean isChecked) {
                if (isChecked) {
                    tv.setTextColor(getResources().getColor(R.color.colorBlue));
                    return;
                }
                tv.setTextColor(getResources().getColor(R.color.colorDefault));
            }
        };
    }

}