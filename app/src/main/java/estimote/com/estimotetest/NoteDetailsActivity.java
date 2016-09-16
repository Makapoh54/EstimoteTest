package estimote.com.estimotetest;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import estimote.com.estimotetest.model.Note;

public class NoteDetailsActivity extends AppCompatActivity {

    public final static String NOTE = "NOTE";

    @BindView(R.id.et_note_title)
    EditText mPostTitle;
    @BindView(R.id.et_note_content)
    EditText mPostContent;

    private Unbinder mUnbinder;
    private Note mNote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_details);
        mUnbinder = ButterKnife.bind(this);

        mNote = (Note) getIntent().getSerializableExtra(NOTE);
    }

    private void initViews(){
        mPostTitle.setText(mNote.getTitle());
        mPostTitle.setText(mNote.getContent());
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        mUnbinder.unbind();
    }

}
