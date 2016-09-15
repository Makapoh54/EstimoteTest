package estimote.com.estimotetest.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import estimote.com.estimotetest.R;
import estimote.com.estimotetest.adapter.BeaconsListAdapter;
import estimote.com.estimotetest.utils.Utils;

public class BeaconsFragment extends Fragment {

    public static final String ARG_PAGE = "ARG_PAGE";
    public static View.OnClickListener onBeaconClickListener;

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;

    private Unbinder mUnbinder;
    private BeaconsListAdapter mBeaconsListAdapter;

    public static BeaconsFragment newInstance() {
        BeaconsFragment fragment = new BeaconsFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_beacons, container, false);
        mUnbinder = ButterKnife.bind(this, rootView);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mBeaconsListAdapter = new BeaconsListAdapter(getContext(), Utils.getBeaconListFromSharedPreferences(getContext()));
        mRecyclerView.setAdapter(mBeaconsListAdapter);
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();

        if(mRecyclerView != null && mBeaconsListAdapter != null){
            ((BeaconsListAdapter) mRecyclerView.getAdapter()).setNewItems(Utils.getBeaconListFromSharedPreferences(getContext()));
        } else {
            mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            mBeaconsListAdapter = new BeaconsListAdapter(getContext(), Utils.getBeaconListFromSharedPreferences(getContext()));
            mRecyclerView.setAdapter(mBeaconsListAdapter);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
    }
}
