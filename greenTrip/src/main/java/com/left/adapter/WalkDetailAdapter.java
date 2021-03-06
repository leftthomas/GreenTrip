package com.left.adapter;

import java.util.ArrayList;
import com.left.green.R;
import com.left.instruction.WalkDetailInstruction;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class WalkDetailAdapter extends BaseAdapter {
	private ArrayList<WalkDetailInstruction> list;
	private LayoutInflater inflater;  

	public WalkDetailAdapter(Context context,ArrayList<WalkDetailInstruction> list) {
		this.list = list; 
		inflater =  LayoutInflater.from(context);  
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		WalkDetailInstruction entity = list.get(position);
		ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.walkdetail_item, null);
			holder.name = (TextView) convertView.findViewById(R.id.walkdetailinstruction);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		holder.name.setText(entity.getName());
		return convertView;
	}

	class ViewHolder {
		TextView name;
	}
}
