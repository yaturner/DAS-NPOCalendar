package com.das.yacalendar.adapters;

import android.content.ClipData;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.das.yacalendar.NoteListItemView;
import com.das.yacalendar.R;
import com.das.yacalendar.notes.Note;
import com.das.yacalendar.yacalendar;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Iterator;
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
        if(notes == null)
        {
            notes = new ArrayList<>();
        }
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

        Note note = notes.get(position);
        final NoteListItemView itemView = (NoteListItemView) row.findViewById(R.id.noteitem_text);
        ImageButton deleteBtn = (ImageButton)row.findViewById(R.id.noteitem_deleteBtn);
        itemView.setText(note.getText());
        deleteBtn.setVisibility((note.isEditable()) ? View.VISIBLE : View.GONE);
        deleteBtn.setTag((Integer) position);
        itemView.setEnabled((note.isEditable()) ? true : false);
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RelativeLayout iv = (RelativeLayout) v.getParent();
                iv.setVisibility(View.GONE);
            }
        });
        return row;
    }
}
