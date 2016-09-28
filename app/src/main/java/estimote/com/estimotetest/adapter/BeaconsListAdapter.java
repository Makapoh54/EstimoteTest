package estimote.com.estimotetest.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import estimote.com.estimotetest.R;
import estimote.com.estimotetest.estimote.CustomBeacon;
import estimote.com.estimotetest.fragment.BeaconsFragment;

public class BeaconsListAdapter extends RecyclerView.Adapter<BeaconsListAdapter.ViewHolder> {

    private Context mContext;
    private List<CustomBeacon> mCustomBeaconList;
    private LayoutInflater mInflater;

    public BeaconsListAdapter(Context context, ArrayList<CustomBeacon> customBeaconList) {
        mCustomBeaconList = customBeaconList;
        mInflater = LayoutInflater.from(context);
        mContext = context;
    }

    public CustomBeacon getItem(int position) {
        return mCustomBeaconList.get(position);
    }

    public void setNewItems(List<CustomBeacon> newList) {
        mCustomBeaconList = newList;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.beacon_list_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final CustomBeacon customBeacon = mCustomBeaconList.get(position);
        switch (customBeacon.getColor()) {
            case ("Mint Cocktail"):
                Picasso.with(mContext)
                        .load(R.drawable.green_beacon)
                        .into(holder.mIcon);

                break;
            case ("Blueberry Pie"):
                Picasso.with(mContext).load(R.drawable.purple_beacon).into(holder.mIcon);
                break;
            case ("Icy Marshmallow"):
                Picasso.with(mContext).load(R.drawable.blue_beacon).into(holder.mIcon);
                break;
        }
        holder.mName.setText(customBeacon.getName());
        holder.mId.setText(customBeacon.getBeaconId().toString());

        holder.itemView.setOnClickListener(BeaconsFragment.onBeaconClickListener);
    }

    @Override
    public void onViewRecycled(ViewHolder holder) {
        super.onViewRecycled(holder);
    }

    @Override
    public int getItemCount() {
        return mCustomBeaconList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView mIcon;
        private TextView mName;
        private TextView mId;
        private TextView mLocation;

        public ViewHolder(View view) {
            super(view);

            mIcon = (ImageView) view.findViewById(R.id.iv_icon);
            mName = (TextView) view.findViewById(R.id.tv_name);
            mId = (TextView) view.findViewById(R.id.tv_beacon_id);
            mLocation = (TextView) view.findViewById(R.id.tv_location);
        }

    }
}