package com.das.yacalendar.dialog;

import android.support.v4.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.das.yacalendar.R;
import com.das.yacalendar.notes.Note;

/**
 * Created by yaturner on 4/5/2016.
 */
public class AddNoteDialog extends DialogFragment {


    private EditText noteText = null;
    private Spinner prioritySpinner = null;
    private Button doneButton = null;
    private Button cancelButton = null;
    private Note note = null;

    public AddNoteDialog() {
        // Empty constructor required for DialogFragment
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.add_note_dlg, container);
        noteText = (EditText) view.findViewById(R.id.new_note_text);
        prioritySpinner = (Spinner)view.findViewById(R.id.new_note_priority_spinner);
        doneButton = (Button)view.findViewById(R.id.new_note_done_btn);
        cancelButton = (Button)view.findViewById(R.id.new_note_cancel_btn);

        getDialog().setTitle(getActivity().getResources().getString(R.string.new_note_title));

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
                getDialog().cancel();
            }
        });

        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        return view;
    }
}
