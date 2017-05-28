package com.mindspree.days.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.mindspree.days.R;
import com.mindspree.days.model.SearchModel;
import com.mindspree.days.ui.MainActivity;

import java.util.ArrayList;

public class AutoCompleteAdapter extends BaseAdapter {

    private ArrayList<SearchModel> m_List= new ArrayList<SearchModel>();
    private Context context;
    public AutoCompleteAdapter(Context context) {
        m_List =  new ArrayList<SearchModel>();
        this.context = context;
    }

    // 현재 아이템의 수를 리턴
    @Override
    public int getCount() {
        return m_List.size();
    }

    // 현재 아이템의 오브젝트를 리턴, Object를 상황에 맞게 변경하거나 리턴받은 오브젝트를 캐스팅해서 사용
    @Override
    public SearchModel getItem(int position) {
        return m_List.get(position);
    }

    // 아이템 position의 ID 값 리턴
    @Override
    public long getItemId(int position) {
        return position;
    }

    // 출력 될 아이템 관리
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        final Context context = parent.getContext();

        TextView text1 = null;
        CustomHolder holder  = null;

        if ( convertView == null ) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.card_autocomplete, parent, false);
            holder = new CustomHolder();
            holder.mTextTitle = (TextView) convertView.findViewById(R.id.text_title);
            holder.mTextSubTitle = (TextView) convertView.findViewById(R.id.text_subtitle);
            holder.mImagePlace =(ImageView) convertView.findViewById(R.id.image_place);
            convertView.setTag(holder);
        }else{
            holder = (CustomHolder) convertView.getTag();
        }

        holder.mTextTitle.setText(m_List.get(position).title);
        holder.mTextTitle.setTypeface(MainActivity.mTypeface);

        holder.mTextSubTitle.setText(m_List.get(position).address);
        holder.mTextSubTitle.setTypeface(MainActivity.mTypeface);

        return convertView;
    }

    public void add(SearchModel searchText) {
        m_List.add(searchText);
    }

    public void clear() {
        m_List.clear();
    }

    public void remove(int _position) {
        m_List.remove(_position);
    }
    private class CustomHolder {
        TextView mTextTitle;
        TextView mTextSubTitle;
        ImageView mImagePlace;
    }

}
