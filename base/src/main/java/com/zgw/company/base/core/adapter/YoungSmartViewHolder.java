package com.zgw.company.base.core.adapter;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

import android.text.SpannableString;
import android.util.TypedValue;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.ColorRes;
import androidx.annotation.DrawableRes;
import androidx.annotation.IdRes;
import androidx.annotation.StringRes;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

public class YoungSmartViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private final AdapterView.OnItemClickListener mListener;
    private int mPosition = -1;

    public YoungSmartViewHolder(View itemView, AdapterView.OnItemClickListener mListener) {
        super(itemView);
        this.mListener = mListener;
        itemView.setOnClickListener(this);

        /*
         * 设置水波纹背景
         */
        if (itemView.getBackground() == null) {
            TypedValue typedValue = new TypedValue();
            Resources.Theme theme = itemView.getContext().getTheme();
            int top = itemView.getPaddingTop();
            int bottom = itemView.getPaddingBottom();
            int left = itemView.getPaddingLeft();
            int right = itemView.getPaddingRight();
            if (theme.resolveAttribute(android.R.attr.selectableItemBackground, typedValue, true)) {
                itemView.setBackgroundResource(typedValue.resourceId);
            }
            itemView.setPadding(left, top, right, bottom);
        }
    }

    public void setPosition(int position) {
        mPosition = position;
    }

    @Override
    public void onClick(View v) {
        if (mListener != null) {
            int position = getAdapterPosition();
            if(position >= 0){
                mListener.onItemClick(null, v, position, getItemId());
            } else if (mPosition > -1) {
                mListener.onItemClick(null, v, mPosition, getItemId());
            }
        }
    }

    public View needClickView(@IdRes int resId){
        return findViewById(resId);
    }

    private View findViewById(int id) {
        return id == 0 ? itemView : itemView.findViewById(id);
    }

    public YoungSmartViewHolder text(int id, CharSequence sequence) {
        View view = findViewById(id);
        if (view instanceof TextView) {
            ((TextView) view).setText(sequence);
        }
        return this;
    }

    public YoungSmartViewHolder text(int id, SpannableString sequence) {
        View view = findViewById(id);
        if (view instanceof TextView) {
            ((TextView) view).setText(sequence);
        }
        return this;
    }

    public YoungSmartViewHolder backgroundResource(int id, @DrawableRes @ColorRes int resource) {
        View view = findViewById(id);
        if (view instanceof ImageView) {
            ((ImageView) view).setBackgroundResource(resource);
        }
        return this;
    }

    public YoungSmartViewHolder backgroundResourceId(int id, @DrawableRes @ColorRes int resource) {
        View view = findViewById(id);
        view.setBackgroundResource(resource);
        return this;
    }

    public YoungSmartViewHolder backgroundImage(int id, Drawable sequence) {
        View view = findViewById(id);
        if (view instanceof ImageView) {
            ((ImageView) view).setBackground(sequence);
        }
        return this;
    }

    public YoungSmartViewHolder imageResource(int id, @ColorRes @DrawableRes int sequence) {
        View view = findViewById(id);
        if (view instanceof ImageView) {
            ((ImageView) view).setImageResource(sequence);
        }
        return this;
    }

    public YoungSmartViewHolder imageBitmap(int id, Bitmap sequence) {
        View view = findViewById(id);
        if (view instanceof ImageView) {
            ((ImageView) view).setImageBitmap(sequence);
        }
        return this;
    }

    public YoungSmartViewHolder orientation(int id, int orientation) {
        View view = findViewById(id);
        if (view instanceof LinearLayout) {
            ((LinearLayout) view).setOrientation(orientation);
        }
        return this;
    }

    public YoungSmartViewHolder text(int id, @StringRes int stringRes) {
        View view = findViewById(id);
        if (view instanceof TextView) {
            ((TextView) view).setText(stringRes);
        }
        return this;
    }

    public YoungSmartViewHolder textColorId(int id, int colorId) {
        View view = findViewById(id);
        if (view instanceof TextView) {
            ((TextView) view).setTextColor(ContextCompat.getColor(view.getContext(), colorId));
        }
        return this;
    }

    public YoungSmartViewHolder image(int id, int imageId) {
        View view = findViewById(id);
        if (view instanceof ImageView) {
            ((ImageView) view).setImageResource(imageId);
        }
        return this;
    }

    public ImageView glideImage(int id) {
        return (ImageView) findViewById(id);
    }

    public View backgroudResource(int id) {
        return findViewById(id);
    }

    public YoungSmartViewHolder gone(int id) {
        View view = findViewById(id);
        if (view != null) {
            view.setVisibility(View.GONE);
        }
        return this;
    }

    public YoungSmartViewHolder visible(int id) {
        View view = findViewById(id);
        if (view != null) {
            view.setVisibility(View.VISIBLE);
        }
        return this;
    }
}