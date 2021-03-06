package com.app.fixy.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.app.fixy.R;
import com.app.fixy.activities.PendingDetailActivity;
import com.app.fixy.adapters.PendingAdapter;
import com.app.fixy.interfaces.InterConst;
import com.app.fixy.interfaces.InterfacesCall;
import com.app.fixy.models.RequestModel;
import com.app.fixy.network.RetrofitClient;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class PendingFragment extends BaseFragment {

    @SuppressLint("StaticFieldLeak")
    static PendingFragment fragment;
    @SuppressLint("StaticFieldLeak")
    static Context mContext;

    @BindView(R.id.ll_main)
    LinearLayout llMain;
    @BindView(R.id.recycleview)
    RecyclerView rvPast;

    PendingAdapter mAdapter;
    ArrayList<RequestModel.ResponseBean> mData = new ArrayList<>();



    InterfacesCall.IndexClick click = new InterfacesCall.IndexClick() {
        @Override
        public void clickIndex(int pos) {
            Intent intent = new Intent(getActivity(), PendingDetailActivity.class);
            startActivity(intent);
            getActivity().overridePendingTransition(R.anim.in, R.anim.out);
        }
    };

    public static PendingFragment newInstance(Context context) {
        fragment = new PendingFragment();
        mContext = context;
        return fragment;
    }

    @Override
    protected int getContentView() {
        return R.layout.fragment_pending;
    }

    @Override
    protected void onCreateStuff() {
        rvPast.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        rvPast.setNestedScrollingEnabled(false);

        mAdapter = new PendingAdapter(mContext, mData, click);
        rvPast.setAdapter(mAdapter);

//        hitApi();
    }
    public void updateAdater(){

//        mAdapter = new NewRequestAdapter(mContext,click, mList);
//        rvPast.setAdapter(mAdapter);
        hitApi();
    }
    void hitApi() {
        if (connectedToInternet(llMain)) {
            Call<RequestModel> call = RetrofitClient.getInstance().request_history(
                    utils.getString(InterConst.ACCESS_TOKEN, ""),
                    utils.getString(InterConst.DEVICE_ID, ""), InterConst.STATUS_PENDING_REQUEST);
            call.enqueue(new Callback<RequestModel>() {
                @Override
                public void onResponse(@NonNull Call<RequestModel> call, @NonNull Response<RequestModel> response) {
                    if (response.body().getCode().equals(InterConst.SUCCESS_RESULT)) {
                        notifyAdapter(response.body().getResponse());
                    } else if (response.body().getCode().equals(InterConst.ERROR_RESULT)) {
                        showSnackBar(llMain, response.body().getMessage());
                    }
                }

                @Override
                public void onFailure(@NonNull Call<RequestModel> call, @NonNull Throwable t) {
                    t.printStackTrace();
                }
            });
        }
    }

    void notifyAdapter(List<RequestModel.ResponseBean> response) {
        mData = new ArrayList<>();
        mData.addAll(response);
        mAdapter = new PendingAdapter(mContext, mData, click);
        rvPast.setAdapter(mAdapter);
    }

    @Override
    protected void initListeners() {
    }

    @Override
    public void onClick(View view) {

    }



}
