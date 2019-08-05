package com.hiya.sample.base;

import android.database.Cursor;
import android.os.Bundle;
import android.provider.CallLog;
import android.util.Log;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.hiya.core.Data;
import com.hiya.core.Entity;
import com.hiya.sample.R;

import java.util.Date;

/**
 * Created by Rajesh.
 */
public abstract class RecycleViewCursorFragment<T extends Entity> extends BaseFragment {

    private Data<T> mData;

    private EntitiesAdapter mAdapter;

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;

    @BindView(R.id.swipeContainer)
    SwipeRefreshLayout swipeContainer;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public void onRefresh() {
        // load data
        swipeContainer.setRefreshing(true);
        getDataFromCallApp();
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recycle, container, false);
        ButterKnife.bind(this, view);
        setToolbarTitle(getTitle());
        LinearLayoutManager llm = new LinearLayoutManager(getApplicationContext());
        mRecyclerView.setLayoutManager(llm);
        mAdapter = new EntitiesAdapter();
        mRecyclerView.setAdapter(mAdapter);
        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(this::onRefresh);

        return view;
    }


    @Override
    public void onResume() {
        super.onResume();
        swipeContainer.setRefreshing(true);
        getDataFromCallApp();
    }


    void getDataFromCallApp() {
        GetCursorTask<T> getCursorTask = new GetCursorTask.Builder()
                .setFetcher(getFetcher())
                .setCallback(data -> {
                    mData = data;
                    mAdapter.notifyDataSetChanged();
                    swipeContainer.setRefreshing(false);
                })
                .build();
        getCursorTask.execute();
    }

    private int getCount() {
        return mData == null ? 0 : (mData.getCursor().getCount() <= 50 ? mData.getCursor().getCount() : 50);
    }

    protected abstract String getTitle();

    protected abstract void bindEntity(T entity, TextView title, TextView details, TextView name, TextView date);

    protected abstract GetCursorTask.DataFetcher<T> getFetcher();


    protected String[] getProjectionColumns() {
        return null;
    }


    static class RowViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.title)
        TextView mTitle;
        @BindView(R.id.details)
        TextView mDetails;

        @BindView(R.id.name)
        TextView name;
        @BindView(R.id.date)
        TextView date;

        public RowViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

    }

    private class EntitiesAdapter extends RecyclerView.Adapter<RowViewHolder> {

        @Override
        public RowViewHolder onCreateViewHolder(ViewGroup parent, int pos) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_rowitem, parent, false);
            return new RowViewHolder(view);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public int getItemViewType(int position) {
            return position;
        }

        @Override
        public void onBindViewHolder(RowViewHolder rowViewHolder, int pos) {
            Cursor cursor = mData.getCursor();
            cursor.moveToPosition(pos);
            String[] projectionColumns = getProjectionColumns();
            T entity;
            if (projectionColumns == null) {
                entity = mData.fromCursor(cursor);
            } else {
                entity = mData.fromCursor(cursor, projectionColumns);
            }
            try {
                bindEntity(entity, rowViewHolder.mTitle, rowViewHolder.mDetails, rowViewHolder.name, rowViewHolder.date);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public int getItemCount() {
            return getCount();
        }
    }

}
