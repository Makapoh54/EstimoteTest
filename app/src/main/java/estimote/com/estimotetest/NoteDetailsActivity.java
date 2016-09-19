package estimote.com.estimotetest;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import estimote.com.estimotetest.fragment.NoteListFragment;
import estimote.com.estimotetest.model.Note;
import estimote.com.estimotetest.utils.ISO8601DateTime;

public class NoteDetailsActivity extends AppCompatActivity {

    public final static String NOTE = "NOTE";

    @BindView(R.id.et_note_title)
    EditText mPostTitle;
    @BindView(R.id.et_note_content)
    EditText mPostContent;
    @BindView(R.id.tv_time_stamp)
    TextView mTimeStamp;
    @BindView(R.id.tv_assigned_beacon)
    TextView mBeacon;

    private Unbinder mUnbinder;
    private Note mNote;
    private int mRequestCode;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_details);
        mUnbinder = ButterKnife.bind(this);

        Intent d = getIntent();
        mRequestCode = getIntent().getIntExtra(Constant.REQUEST_CODE, 0);
        if (mRequestCode == (Constant.NOTE_CREATE_REQUEST)) {
            mNote = new Note();
        } else if (mRequestCode == (Constant.NOTE_EDIT_REQUEST)) {
            mNote = (Note) getIntent().getSerializableExtra(NOTE);
            initViews();
        }
    }

    private void initViews() {
        mPostTitle.setText(mNote.getTitle());
        mPostContent.setText(mNote.getContent());
        mTimeStamp.setText(mNote.getCreationTime());
        //mTimeStamp.setText(mNote.getBeaconId());
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        saveNote();
    }

    private void saveNote() {
        if (mRequestCode == Constant.NOTE_CREATE_REQUEST) {
            mNote.setTitle(mPostTitle.getText().toString());
            mNote.setContent(mPostContent.getText().toString());
            mNote.setCreationTime(ISO8601DateTime.now());

            Intent returnIntent = new Intent(this, NoteListFragment.class);
            returnIntent.putExtra(NOTE, mNote);
            setResult(Activity.RESULT_OK, returnIntent);
            finish();
        } else if (mRequestCode == (Constant.NOTE_EDIT_REQUEST)) {
            mNote.setTitle(mPostTitle.getText().toString());
            mNote.setContent(mPostContent.getText().toString());

            Intent returnIntent = new Intent(this, NoteListFragment.class);
            returnIntent.putExtra(NOTE, mNote);
            setResult(Activity.RESULT_OK, returnIntent);
            finish();

        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mUnbinder.unbind();
    }

}
