package com.anna.sent.soft.childbirthdate.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.anna.sent.soft.childbirthdate.R;
import com.anna.sent.soft.logging.MyLog;

public class ListItemArrayAdapter extends ArrayAdapter<String> implements OnClickListener {
    private final String[] mStrings1;
    private final int mCount;
    private OnCheckedListener mListener;
    private String[] mStrings2;
    private boolean[] mChecked;

    public ListItemArrayAdapter(Context context, String[] strings1) {
        super(context, R.layout.list_item, R.id.text1, strings1);
        mStrings1 = strings1;
        mCount = mStrings1.length;
        mChecked = new boolean[mCount];
        mStrings2 = new String[mCount];
        for (int i = 0; i < mCount; ++i) {
            mChecked[i] = false;
            mStrings2[i] = "";
        }
    }

    private String wrapMsg(String msg) {
        return getClass().getSimpleName() + ": " + msg;
    }

    private void log(String msg) {
        MyLog.getInstance().logcat(Log.DEBUG, wrapMsg(msg));
    }

    public void setOnCheckedListener(OnCheckedListener listener) {
        mListener = listener;
    }

    @Override
    public int getCount() {
        return mCount;
    }

    @SuppressWarnings("InflateParams")
    @NonNull
    @Override
    public View getView(int position, View contentView, @NonNull ViewGroup viewGroup) {
        View view;
        ViewHolder viewHolder;
        if (contentView == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            view = inflater.inflate(R.layout.list_item, null);
            viewHolder = new ViewHolder();
            viewHolder.checkBox = view.findViewById(R.id.checkBox);
            viewHolder.text1 = view.findViewById(R.id.text1);
            viewHolder.text2 = view.findViewById(R.id.text2);
            view.setTag(viewHolder);
        } else {
            view = contentView;
            viewHolder = (ViewHolder) view.getTag();
        }

        viewHolder.position = position;
        viewHolder.checkBox.setChecked(mChecked[position]);
        viewHolder.checkBox.setOnClickListener(this);
        viewHolder.checkBox.setTag(viewHolder);
        viewHolder.text1.setText(mStrings1[position]);
        viewHolder.text2.setText(mStrings2[position]);
        viewHolder.text2.setVisibility(mChecked[position] ? View.VISIBLE : View.GONE);

        return view;
    }

    @Override
    public void onClick(View v) {
        log("onClick");
        ViewHolder viewHolder = (ViewHolder) v.getTag();
        int position = viewHolder.position;
        mChecked[position] = viewHolder.checkBox.isChecked();
        viewHolder.text2.setVisibility(mChecked[position] ? View.VISIBLE : View.GONE);
        if (mListener != null) {
            mListener.checked(position, mChecked[position]);
        }
    }

    public void updateValues(boolean[] checked, String[] strings2) {
        if (checked.length == mCount && strings2.length == mCount) {
            log("update values");
            mChecked = checked;
            mStrings2 = strings2;

            notifyDataSetChanged();
        }
    }

    public void updateValues(String[] strings2) {
        if (strings2.length == mCount) {
            log("update values");
            mStrings2 = strings2;

            notifyDataSetChanged();
        }
    }

    public interface OnCheckedListener {
        void checked(int position, boolean isChecked);
    }

    private static class ViewHolder {
        private int position;
        private CheckBox checkBox;
        private TextView text1;
        private TextView text2;
    }
}
