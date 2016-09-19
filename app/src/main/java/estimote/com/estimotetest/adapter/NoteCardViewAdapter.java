package estimote.com.estimotetest.adapter;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import estimote.com.estimotetest.R;
import estimote.com.estimotetest.database.FirebaseDb;
import estimote.com.estimotetest.model.Note;


public class NoteCardViewAdapter extends RecyclerView.Adapter<NoteCardViewAdapter.ViewHolder> {

    private LinkedHashMap<String, Note> mPostList;
    private Context mContext;
    private LayoutInflater mInflater;
    private OnItemClickListener mItemClickListener;

    public NoteCardViewAdapter(Context context, LinkedHashMap<String, Note> postList) {
        mPostList = postList;
        mInflater = LayoutInflater.from(context);
        mContext = context;
    }

    public Note getItem(int position) {
        return mPostList.get(position);
    }

    public LinkedHashMap<String, Note> getPostList() {
        return mPostList;
    }

    public String getItemKey(int position) {
        return new ArrayList<String>(mPostList.keySet()).get(position);
    }

    public void setNewItems(LinkedHashMap<String, Note> newList) {
        mPostList = newList;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.view_note_list_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Note note = (new ArrayList<Note>(mPostList.values())).get(position);

        holder.mTitle.setText(note.getTitle());
        holder.mContent.setText(note.getContent());
    }

    @Override
    public void onViewRecycled(ViewHolder holder) {
        super.onViewRecycled(holder);
    }

    @Override
    public int getItemCount() {
        return mPostList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private LinearLayout mPlaceHolder;
        private TextView mTitle;
        private TextView mContent;
        private Button mDelete;
        private Button mSetAlarm;

        public ViewHolder(View view) {
            super(view);

            mTitle = (TextView) view.findViewById(R.id.note_title_tv);
            mContent = (TextView) view.findViewById(R.id.note_content_tv);
            mPlaceHolder = (LinearLayout) view.findViewById(R.id.note_content);
            mDelete = (Button) view.findViewById(R.id.button_edit_note);
            mDelete.setOnClickListener(onClick -> {
                FirebaseDb.deletePost((new ArrayList<String>(mPostList.keySet())).get(getAdapterPosition()));
            });
            mPlaceHolder.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mItemClickListener != null) {
                mItemClickListener.onItemClick(v, getAdapterPosition());
            }
        }

    }

    public interface OnItemClickListener {
        public void onItemClick(View view, int position);
    }

    public void SetOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

}

