package estimote.com.estimotetest.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import estimote.com.estimotetest.NoteDetailsActivity;
import estimote.com.estimotetest.R;
import estimote.com.estimotetest.adapter.NoteCardViewAdapter;
import estimote.com.estimotetest.database.FirebaseDb;
import estimote.com.estimotetest.model.Note;
import estimote.com.estimotetest.utils.ISO8601DateTime;

import static estimote.com.estimotetest.database.Snapshot.toPosts;

public class NoteListFragment extends Fragment {
    public static final String ARG_PAGE = "ARG_PAGE";

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;


    private LinearLayoutManager mLayoutManager;
    private Unbinder mUnbinder;

    public static NoteListFragment newInstance() {
        NoteListFragment fragment = new NoteListFragment();
        return fragment;
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

    private void setRecyclerView() {
        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        FirebaseDb.getAllPosts(snapshot -> {
            NoteCardViewAdapter adapter = new NoteCardViewAdapter(getContext(), toPosts(snapshot));
            adapter.SetOnItemClickListener((View v, int position) -> {
                Log.d("TAG", String.valueOf(position));
                Intent intent = new Intent(getActivity(), NoteDetailsActivity.class);
                intent.putExtra(NoteDetailsActivity.NOTE, adapter.getItem(position));
                Pair<View, String> p1 = Pair.create((View) v.findViewById(R.id.note_title_tv), "note_title");
                Pair<View, String> p2 = Pair.create((View) v.findViewById(R.id.note_content_tv), "note_content");
                ActivityOptionsCompat options = ActivityOptionsCompat.
                        makeSceneTransitionAnimation((Activity) getActivity(), p1, p2);
                startActivity(intent);
            });
            mRecyclerView.setAdapter(adapter);
        });
    }


    private void createPost(String title, String content) {
        Note note = new Note(title, content, ISO8601DateTime.now(), "ads");
        FirebaseDb.createPost(note);
    }
}

