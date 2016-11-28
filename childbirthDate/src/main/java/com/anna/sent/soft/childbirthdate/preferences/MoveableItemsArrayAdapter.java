package com.anna.sent.soft.childbirthdate.preferences;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.anna.sent.soft.childbirthdate.R;
import com.anna.sent.soft.childbirthdate.age.LocalizableObject;

public class MoveableItemsArrayAdapter extends ArrayAdapter<String> implements
		OnClickListener {
	private static final String TAG = "moo";
	@SuppressWarnings("unused")
	private static final boolean DEBUG = false;
	@SuppressWarnings("unused")
	private static final boolean DEBUG_CREATION = false;

	private String wrapMsg(String msg) {
		return getClass().getSimpleName() + ": " + msg;
	}

	@SuppressWarnings("unused")
	private void log(String msg, boolean scenario) {
		if (scenario) {
			Log.d(TAG, wrapMsg(msg));
		}
	}

	private List<LocalizableObject> mValues;

	public List<LocalizableObject> getValues() {
		return mValues;
	}

	private static class ViewHolder {
		private int position;
		private TextView textView;
	}

	public MoveableItemsArrayAdapter(Context context,
			List<LocalizableObject> values) {
		super(context, R.layout.dialog_list_item);
		mValues = values;
	}

	@Override
	public int getCount() {
		return mValues.size();
	}

	@SuppressLint("InflateParams")
	@Override
	public View getView(int position, View contentView, ViewGroup viewGroup) {
		View view;
		ViewHolder viewHolder;
		if (contentView == null) {
			LayoutInflater layoutInflater = (LayoutInflater) getContext()
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = layoutInflater.inflate(R.layout.dialog_list_item, null);

			viewHolder = new ViewHolder();

			view.setTag(viewHolder);

			viewHolder.textView = (TextView) view
					.findViewById(R.id.textViewItem);

			Button buttonUp = (Button) view.findViewById(R.id.buttonUp);
			buttonUp.setTag(viewHolder);
			buttonUp.setOnClickListener(this);

			Button buttonDown = (Button) view.findViewById(R.id.buttonDown);
			buttonDown.setTag(viewHolder);
			buttonDown.setOnClickListener(this);

			Button buttonDelete = (Button) view.findViewById(R.id.buttonDelete);
			buttonDelete.setTag(viewHolder);
			buttonDelete.setOnClickListener(this);
		} else {
			view = contentView;
			viewHolder = (ViewHolder) view.getTag();
		}

		viewHolder.position = position;
		viewHolder.textView.setText(mValues.get(position)
				.toString(getContext()));

		return view;
	}

	public void setItems(List<LocalizableObject> objects) {
		mValues.clear();
		mValues.addAll(objects);
		notifyDataSetChanged();
	}

	public void addItem(LocalizableObject object) {
		mValues.add(object);
		notifyDataSetChanged();
	}

	public void addItems(List<LocalizableObject> objects) {
		mValues.addAll(objects);
		notifyDataSetChanged();
	}

	public void removeItem(int position) {
		if (position < mValues.size()) {
			mValues.remove(position);
			notifyDataSetChanged();
		}
	}

	public void removeItems() {
		mValues.clear();
		notifyDataSetChanged();
	}

	public void upItem(int position) {
		if (position < 0 || position >= mValues.size()) {
			return;
		}

		LocalizableObject object = mValues.get(position);
		if (position > 0) {
			mValues.remove(position);
			mValues.add(position - 1, object);
			notifyDataSetChanged();
		}
	}

	public void downItem(int position) {
		if (position < 0 || position >= mValues.size()) {
			return;
		}

		LocalizableObject object = mValues.get(position);
		if (position < mValues.size() - 1) {
			mValues.remove(position);
			mValues.add(position + 1, object);
			notifyDataSetChanged();
		}
	}

	@Override
	public void onClick(View v) {
		ViewHolder viewHolder = (ViewHolder) v.getTag();
		if (viewHolder != null) {
			int position = viewHolder.position;
			switch (v.getId()) {
			case R.id.buttonUp:
				upItem(position);
				break;
			case R.id.buttonDown:
				downItem(position);
				break;
			case R.id.buttonDelete:
				removeItem(position);
				break;
			}
		}
	}
}