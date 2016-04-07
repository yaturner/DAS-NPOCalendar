package com.das.yacalendar.dialog;

import android.os.Message;
import android.support.v4.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.das.yacalendar.R;
import com.das.yacalendar.notes.Note;
import com.das.yacalendar.yacalendar;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;

/**
 * Created by yaturner on 4/5/2016.
 */
public class AddNoteDialog extends DialogFragment {


    private EditText noteText = null;
    private Spinner prioritySpinner = null;
    private TimePicker timePicker = null;
    private Button doneButton = null;
    private Button cancelButton = null;
    private Note note = null;
    private Calendar date = null;
    private int priority = -1;
    public AddNoteDialog()
    {

    }

    /**
     * Create a new instance of AddNoteDialog, providing "date"
     * as an argument.
     */
    static public AddNoteDialog newInstance(final Calendar date) {
        AddNoteDialog f = new AddNoteDialog();

        // Supply num input as an argument.
        Bundle args = new Bundle();
        args.putSerializable(yacalendar.KEY_DATE, date);
        f.setArguments(args);

        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container,savedInstanceState);
        Bundle bundle = getArguments();
        if(bundle.containsKey(yacalendar.KEY_DATE))
        {
            this.date = (Calendar)bundle.getSerializable(yacalendar.KEY_DATE);
        }
        else
        {
            throw new RuntimeException("Internal Error in AddNoteDialog");
        }
        View view = inflater.inflate(R.layout.add_note_dlg, container);
        noteText = (EditText) view.findViewById(R.id.new_note_text);
        prioritySpinner = (Spinner) view.findViewById(R.id.new_note_priority_spinner);
        doneButton = (Button) view.findViewById(R.id.new_note_done_btn);
        cancelButton = (Button) view.findViewById(R.id.new_note_cancel_btn);

        getDialog().setTitle(getActivity().getResources().getString(R.string.new_note_title));

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
            }
        });

        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = noteText.getEditableText().toString();
                if (text == null || text.isEmpty()) {
                    Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.no_text_entered),
                            Toast.LENGTH_LONG).show();
                    return;
                }

                String prio = prioritySpinner.getSelectedItem().toString();
                ArrayList<String> priority_values = new ArrayList<String>(Arrays.asList(getActivity().getResources().getStringArray(R.array.priority_value_array)));

                if(priority_values.contains(prio))
                {
                    priority = priority_values.indexOf(prio);
                    if(priority == 0)
                    {
                    }
                }
                Note note = new Note(date, priority, text, true);
                yacalendar main = yacalendar.getInstance();
                Message msg = main.getMsgHandler().obtainMessage(yacalendar.HANDLER_MESSAGE_NEW_NOTE, note);
                main.getMsgHandler().sendMessage(msg);
                getDialog().dismiss();
            }
        });
        return view;
    }

}