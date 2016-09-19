package estimote.com.estimotetest.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import estimote.com.estimotetest.Constant;
import estimote.com.estimotetest.NoteDetailsActivity;
import estimote.com.estimotetest.R;
import estimote.com.estimotetest.adapter.NoteCardViewAdapter;
import estimote.com.estimotetest.database.FirebaseDb;
import estimote.com.estimotetest.model.Note;
import estimote.com.estimotetest.utils.ISO8601DateTime;

import static estimote.com.estimotetest.NoteDetailsActivity.NOTE;
import static estimote.com.estimotetest.database.Snapshot.toPosts;

public class NoteListFragment extends Fragment {
    public static final String ARG_PAGE = "ARG_PAGE";

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;


    private LinearLayoutManager mLayoutManager;
    private Unbinder mUnbinder;
    private String mEditableItemKey;

    public static NoteListFragment newInstance() {
        NoteListFragment fragment = new NoteListFragment();
        return fragment;
    }

    @OnClick(R.id.create_new_post_button)
    public void createNewPost() {
        Intent intent = new Intent(getActivity(), NoteDetailsActivity.class);
        intent.putExtra(Constant.REQUEST_CODE, Constant.NOTE_CREATE_REQUEST);
        startActivityForResult(intent, Constant.NOTE_CREATE_REQUEST);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View rootView = inflater.inflate(R.layout.fragment_note_list, container, false);
        mUnbinder = ButterKnife.bind(this, rootView);
        setRecyclerView();
        return rootView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == Constant.NOTE_CREATE_REQUEST) {
            if (resultCode == Activity.RESULT_OK) {
                FirebaseDb.createPost((Note) data.getSerializableExtra(NOTE));
            }
        }
        if (requestCode == Constant.NOTE_EDIT_REQUEST) {
            if (resultCode == Activity.RESULT_OK) {
                FirebaseDb.updatePost(mEditableItemKey, (Note) data.getSerializableExtra(NOTE));
            }
        }
    }

    private void setRecyclerView() {
        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        FirebaseDb.getAllPosts(snapshot -> {
            NoteCardViewAdapter adapter = new NoteCardViewAdapter(getContext(), toPosts(snapshot));
            adapter.SetOnItemClickListener((View v, int position) -> {
                Log.d("TAG", String.valueOf(position));
                mEditableItemKey = adapter.getItemKey(position);
                Intent intent = new Intent(getActivity(), NoteDetailsActivity.class);
                intent.putExtra(Constant.REQUEST_CODE, Constant.NOTE_EDIT_REQUEST);
                intent.putExtra(NOTE, adapter.getItem(position));
                startActivityForResult(intent, Constant.NOTE_EDIT_REQUEST);
            });
            mRecyclerView.setAdapter(adapter);
        });
    }


    private void createPost(String title, String content) {
        Note note = new Note(title, content, ISO8601DateTime.now(), "ads");
        FirebaseDb.createPost(note);
    }
}

