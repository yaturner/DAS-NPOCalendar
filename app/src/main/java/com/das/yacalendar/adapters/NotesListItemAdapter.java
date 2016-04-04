package com.das.yacalendar.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.das.yacalendar.R;
import com.das.yacalendar.notes.Note;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by yaturner on 4/3/2016.
 */
public class NotesListItemAdapter extends ArrayAdapter {
    private final static String TAG = NotesListItemAdapter.class.getSimpleName();

    private ArrayList<Note> notes;
    private Context context;
    private int resId;

    public NotesListItemAdapter(Context context, int resId, List<Note> objects) {
        super(context, resId, objects);
        this.context = context;
        this.notes.addAll(objects);
        this.resId = resId;
    }

    public Note getItem(int position) {
        return notes.get(position);
    }

    @Override
    public int getCount() {
        return notes.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        if (row == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(resId, parent, false);
        }

        return row;
    }
}
