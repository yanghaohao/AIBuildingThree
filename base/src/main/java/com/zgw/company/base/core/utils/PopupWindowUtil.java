package com.zgw.company.base.core.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.zgw.company.base.R;
import com.zgw.company.base.core.adapter.BaseRecyclerAdapter;
import com.zgw.company.base.core.adapter.YoungSmartViewHolder;
import com.zgw.company.base.core.interfaces.DialogFunction;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PopupWindowUtil {


    public static void setAddPopupWindow(View rootView, final Context context, final Activity activity, final DialogFunction listener, String...contents) {
        View popView = LayoutInflater.from(context).inflate(R.layout.pupwindow_bottom_list, null);
        final PopupWindow popupWindow = new PopupWindow(popView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        setBackgroundAlpha(activity, 0.5f);

        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setFocusable(true);

        popupWindow.showAtLocation(rootView, Gravity.BOTTOM | Gravity.LEFT, 0, 0);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                setBackgroundAlpha(activity, 1.0f);
            }
        });

        RecyclerView tvScanFast = popView.findViewById(R.id.rv_pop_bot_list);
        tvScanFast.setLayoutManager(new LinearLayoutManager(context));
        Button btnCancle = popView.findViewById(R.id.btn_cancle);
        final List<String> needGetContent = new ArrayList<>(Arrays.asList(contents));
        BaseRecyclerAdapter<String> baseRecyclerAdapter = new BaseRecyclerAdapter<String>(needGetContent,R.layout.item_pupo_text) {
            @Override
            protected void onBindViewHolder(YoungSmartViewHolder holder, String model, int position) {
                holder.text(R.id.tv_scan_fast, model);
                if (position == needGetContent.size()-1) holder.gone(R.id.pup_line);
            }
        };
        tvScanFast.setAdapter(baseRecyclerAdapter);
        baseRecyclerAdapter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (popupWindow.isShowing()){
                    popupWindow.dismiss();
                }
                listener.popupWindow(popupWindow, position);
            }
        });
        btnCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
    }

    /**
     * 设置添加屏幕的背景透明度
     *
     * @param bgAlpha 屏幕透明度0.0-1.0 1表示完全不透明
     */
    public static void setBackgroundAlpha(Activity activity, float bgAlpha) {
        WindowManager.LayoutParams lp = activity.getWindow()
                .getAttributes();
        lp.alpha = bgAlpha;
        activity.getWindow().setAttributes(lp);
    }
}
