package com.app.fixy.fragments;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.app.fixy.R;
import com.app.fixy.utils.Connection_Detector;
import com.app.fixy.utils.Encode;
import com.app.fixy.utils.LoadingDialog;
import com.app.fixy.utils.MarshMallowPermission;
import com.app.fixy.utils.Utils;
import com.google.gson.Gson;

import butterknife.ButterKnife;

public abstract class BaseFragment extends Fragment implements View.OnClickListener {

    public MarshMallowPermission mPermission;
    protected int mWidth, mHeight;
    protected Context mContext;
    protected String errorInternet;
    protected String platformStatus = "2";
    protected String errorAPI;
    protected String errorAccessToken;
    protected String terminateAccount;
    //    RoomDb mRoomDb;
    protected String deviceToken;
    //    protected Db db;
    Utils utils;
    Gson mGson = new Gson();
    Encode encode;
    private Snackbar mSnackbar;

    public static void hideKeyboard(Activity mContext) {
        // Check if no view has focus:
        View view = mContext.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        utils = new Utils(getActivity());
//        db = new Db(getActivity());
        encode = new Encode();
        getDefaults();
        mPermission = new MarshMallowPermission(getActivity());
        errorInternet = getResources().getString(R.string.internet);
        errorAPI = getResources().getString(R.string.error);
        errorAccessToken = getResources().getString(R.string.invalid_access_token);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(getContentView(), container, false);
        ButterKnife.bind(BaseFragment.this, view);
        mContext = getContext();
//        mRoomDb = Room.databaseBuilder(mContext.getApplicationContext(),
//                RoomDb.class, "nass-db").allowMainThreadQueries().fallbackToDestructiveMigration().build();
        deviceToken = Settings.Secure.getString(getContext().getContentResolver(),
                Settings.Secure.ANDROID_ID);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        onCreateStuff();
        initListeners();

    }

    protected abstract int getContentView();

    //onCreate
    protected abstract void onCreateStuff();

    protected abstract void initListeners();

    protected void getDefaults() {
        DisplayMetrics display = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(display);
        mWidth = display.widthPixels;
        mHeight = display.heightPixels;
        Log.e("Height = ", mHeight + " width " + mWidth);
        utils.setInt("width", mWidth);
        utils.setInt("height", mHeight);
    }

    protected void showProgress() {
        LoadingDialog.getLoader().showLoader(getActivity());
    }

    protected void hideProgress() {
        LoadingDialog.getLoader().dismissLoader();
    }

    protected void showSnakbarAlert(View view, String message) {
        mSnackbar = Snackbar.make(view, message, Snackbar.LENGTH_SHORT);
        mSnackbar.getView().setBackgroundColor(Color.RED);
        mSnackbar.show();
    }

    protected void showSnackBar(View view, String message) {
        mSnackbar = Snackbar.make(view, message, Snackbar.LENGTH_SHORT);
        mSnackbar.show();
    }

    public boolean connectedToInternet() {
        if ((new Connection_Detector(mContext)).isConnectingToInternet()) {
//            Consts.NO_INTERNET = false;
            return true;
        } else {
            return false;
        }
    }

    public boolean connectedToInternet(View view) {
        if ((new Connection_Detector(mContext)).isConnectingToInternet()) {
            return true;
        } else {
            showInternetAlert(view);
            return false;
        }
    }

    protected void showInternetAlert(View view) {
        mSnackbar = Snackbar.make(view, "Internet connection not available!", Snackbar.LENGTH_SHORT);
        mSnackbar.show();
    }
//
//    void showInternetDialog() {
//        if (!Consts.NO_INTERNET) {
//            AlertDialog.Builder builder1 = new AlertDialog.Builder(mContext);
//            builder1.setMessage(mContext.getResources().getString(R.string.internet));
//            builder1.setCancelable(true);
//            builder1.setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                public void onClick(DialogInterface dialog, int id) {
//                    Consts.NO_INTERNET = true;
//                    dialog.cancel();
//                }
//            });
//            AlertDialog alert11 = builder1.create();
//            alert11.show();
//        }
//    }

    public void toast(String message) {
        Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
    }


}
