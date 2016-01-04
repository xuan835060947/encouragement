package com.person.xuan.encouragement.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.person.xuan.encouragement.R;
import com.person.xuan.encouragement.entity.History;
import com.person.xuan.encouragement.entity.Plan;
import com.person.xuan.encouragement.util.ShareValueUtil;
import com.person.xuan.encouragement.widget.EncouragementWrapper;

/**
 * Created by chenxiaoxuan1 on 15/12/17.
 */
public class HistoryFragment extends BaseFragment implements View.OnClickListener {
    private ListView mLvPlans;
    private PlansListAdapter mAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_history, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        if(!hidden){
            refreshView();
        }
    }

    private void initView() {
        mLvPlans = findViewById(R.id.lv_history_plans);

    }

    private void refreshView(){
        EncouragementWrapper.getHistory(new EncouragementWrapper.OnGetHistory() {
            @Override
            public void onGet(History history) {
                Log.e("onGet", "history: " + ShareValueUtil.GSON.toJson(history));
                mAdapter = new PlansListAdapter();
                mAdapter.setHistory(history);
                mLvPlans.setAdapter(mAdapter);
                mAdapter.notifyDataSetChanged();
            }

        });
    }

    private class PlansListAdapter extends BaseAdapter {
        private History mHistory;
        private LayoutInflater inflater = LayoutInflater.from(HistoryFragment.this.getActivity());

        public void setHistory(History history) {
            this.mHistory = history;
        }

        @Override
        public int getCount() {
            return mHistory.getPlans().size();
        }

        @Override
        public Object getItem(int position) {
            return mHistory.getPlans().get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                View view = inflater.inflate(R.layout.item_history_plan,null);
                ViewHolder viewHolder = new ViewHolder();
                viewHolder.tvContent = (TextView) view.findViewById(R.id.tv_plan);
                viewHolder.ivReward = (ImageView) view.findViewById(R.id.iv_reward);
                refreshItem(viewHolder,position);
                view.setTag(viewHolder);
                return view;
            }else {
                ViewHolder viewHolder = (ViewHolder) convertView.getTag();
                refreshItem(viewHolder,position);
                return convertView;
            }
        }

        private void refreshItem(ViewHolder viewHolder,int position){
            Plan plan = mHistory.getPlans().get(position);
            viewHolder.tvContent.setText(plan.getContent());
            if(plan.getEncouragement()!=0){
                viewHolder.ivReward.setVisibility(View.VISIBLE);
                viewHolder.ivReward.setImageResource(R.drawable.reward);
            }else {
                viewHolder.ivReward.setVisibility(View.INVISIBLE);
            }
        }

    }



    static class ViewHolder {
        TextView tvContent;
        ImageView ivReward;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_finish:

                break;
        }
    }

}
