package com.anna.sent.soft.childbirthdate.preferences;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Parcel;
import android.os.Parcelable;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ListView;

import com.anna.sent.soft.childbirthdate.R;
import com.anna.sent.soft.childbirthdate.age.ISetting;
import com.anna.sent.soft.childbirthdate.age.LocalizableObject;
import com.anna.sent.soft.childbirthdate.age.SettingsParser;

import java.util.List;

public abstract class MoveableItemsPreference extends DialogPreference
        implements OnClickListener {
    private String mValue;
    private MoveableItemsArrayAdapter mAdapter;

    protected MoveableItemsPreference(Context context) {
        this(context, null);
    }

    protected MoveableItemsPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        mValue = getDefaultValue();
        setDialogLayoutResource(R.layout.dialog_list);
        setPositiveButtonText(android.R.string.ok);
        setNegativeButtonText(android.R.string.cancel);
        setDialogIcon(null);
    }

    @Override
    protected void onSetInitialValue(boolean restore, Object defaultValue) {
        setValue(restore ? getPersistedString(getDefaultValue()) : (String) defaultValue);
    }

    @Override
    protected Object onGetDefaultValue(TypedArray a, int index) {
        return a.getString(index);
    }

    @Override
    protected void onBindDialogView(View view) {
        super.onBindDialogView(view);

        // get inflater
        LayoutInflater inflater = LayoutInflater.from(getContext());

        // get button 'add'
        Button buttonAdd = view.findViewById(R.id.buttonAdd);
        buttonAdd.setOnClickListener(this);

        // inflate view 'add'
        View viewAdd = inflater.inflate(getAddLayoutResourceId(), null);

        // find parent of view 'add'
        ViewGroup viewAddParent = view.findViewById(R.id.lastItem);

        // add view 'add' to parent
        viewAddParent.addView(viewAdd, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

        // get list view
        ListView listView = view.findViewById(R.id.listView);

        // and then setup adapter
        mAdapter = new MoveableItemsArrayAdapter(getContext(), toList(mValue));
        listView.setAdapter(mAdapter);

        setupViewAdd(viewAdd);
    }

    protected abstract String getDefaultValue();

    protected abstract int getAddLayoutResourceId();

    protected abstract void setupViewAdd(View viewAdd);

    @Override
    protected void onDialogClosed(boolean positiveResult) {
        super.onDialogClosed(positiveResult);

        if (positiveResult) {
            List<LocalizableObject> list = mAdapter.getValues();
            setValue(toString(list));
        }
    }

    private void setValue(String value) {
        if (!value.equals(mValue)) {
            mValue = value;
            persistString(value);
            notifyChanged();
        }
    }

    public String getValueToShow() {
        return getValueToShow(mValue);
    }

    public String getValueToShow(String value) {
        StringBuilder result = new StringBuilder();

        List<LocalizableObject> list = toList(value);
        for (int i = 0; i < list.size(); ++i) {
            result.append(list.get(i).toString(getContext()));
            result.append(i == list.size() - 1 ? "" : "; ");
        }

        return result.toString();
    }

    private List<LocalizableObject> toList(String str) {
        return SettingsParser.toList(str, getElement());
    }

    protected abstract ISetting getElement();

    private String toString(List<LocalizableObject> list) {
        return SettingsParser.toString(list);
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();

        SavedState myState = new SavedState(superState);
        myState.value = saveAddValue();
        if (mAdapter != null) {
            myState.values = SettingsParser.toString(mAdapter.getValues());
        } else {
            myState.values = null;
        }

        return myState;
    }

    protected abstract String saveAddValue();

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if (state == null || !state.getClass().equals(SavedState.class)) {
            super.onRestoreInstanceState(state);
            return;
        }

        SavedState myState = (SavedState) state;
        super.onRestoreInstanceState(myState.getSuperState());
        restoreAddValue(myState.value);
        if (mAdapter != null) {
            mAdapter.setItems(SettingsParser.toList(myState.values, getElement()));
        }
    }

    protected abstract void restoreAddValue(String value);

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonAdd:
                addItem(mAdapter);
                break;
        }
    }

    protected abstract void addItem(MoveableItemsArrayAdapter adapter);

    private static class SavedState extends BaseSavedState {
        @SuppressWarnings("unused")
        public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable.Creator<SavedState>() {
            @Override
            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in);
            }

            @Override
            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };

        private String value;
        private String values;

        SavedState(Parcelable superState) {
            super(superState);
        }

        SavedState(Parcel source) {
            super(source);
            value = source.readString();
            values = source.readString();
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            super.writeToParcel(dest, flags);
            dest.writeString(value);
            dest.writeString(values);
        }
    }
}
