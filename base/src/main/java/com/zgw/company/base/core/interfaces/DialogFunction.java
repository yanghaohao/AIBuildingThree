package com.zgw.company.base.core.interfaces;

import android.view.MenuItem;
import android.view.View;
import android.widget.PopupWindow;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.PopupMenu;

public interface DialogFunction {
    void dialog(View view, AlertDialog dialog);
    void dialog(View view, View view1, AlertDialog dialog);
    void dialogThisProject(AlertDialog dialog, String s);

    void dialogListThisProjectList(AlertDialog dialog, String s, int position);

    void popupWindow(PopupWindow dialog, int position);

    void popupMenu(PopupMenu dialog, MenuItem position, String s);

    void thisProjectCutProject();

    void thisProjectEdiText(int spinnerPosition, String mark);
}
