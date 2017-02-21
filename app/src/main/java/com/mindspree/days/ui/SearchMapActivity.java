package com.mindspree.days.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.mindspree.days.R;
import com.mindspree.days.adapter.AutoCompleteAdapter;
import com.mindspree.days.data.DBWrapper;
import com.mindspree.days.interfaces.OnFinishSearchListener;
import com.mindspree.days.lib.AppConfig;
import com.mindspree.days.lib.Searcher;
import com.mindspree.days.model.SearchModel;

import net.daum.mf.map.api.MapLayout;
import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by vision51 on 2016. 12. 27..
 */

public class SearchMapActivity extends BaseActivity implements  MapView.MapViewEventListener, MapView.OpenAPIKeyAuthenticationResultListener {
    private ListView mListHistory;
    private EditText mEditSearchText;
    private AutoCompleteAdapter mAutoCompleteAdapter;
    private MapView mMapView;

    public int mLocationId = 0;
    public double mLatitude = 0;
    public double mLongitude = 0;
    public String mMode = "";

    private ArrayList<SearchModel> mSearchResultList = new ArrayList<SearchModel>();
    private SearchModel mSelectedResult = new SearchModel();
    private TextView mTextLocationPick;

    private DBWrapper mDBWrapper;

    public static void startActivity(Context context, int locationId, double latitude, double longitude){
        Intent intent = new Intent(context, SearchMapActivity.class);
        intent.putExtra(AppConfig.IntentParam.ID, locationId);
        intent.putExtra(AppConfig.IntentParam.LATITUDE, latitude);
        intent.putExtra(AppConfig.IntentParam.LONGITUDE, longitude);
        intent.putExtra(AppConfig.IntentParam.MODE, "");
        context.startActivity(intent);
    }

    public static void startActivityForResult(Activity context, int index, double latitude, double longitude){
        Intent intent = new Intent(context, SearchMapActivity.class);
        intent.putExtra(AppConfig.IntentParam.LATITUDE, latitude);
        intent.putExtra(AppConfig.IntentParam.LONGITUDE, longitude);
        intent.putExtra(AppConfig.IntentParam.MODE, "all");
        if(index == 1) {
            context.startActivityForResult(intent, AppConfig.IntentCode.ACTION_MAPPICK1);
        } else {
            context.startActivityForResult(intent, AppConfig.IntentCode.ACTION_MAPPICK2);
        }
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.slide_in_right, R.anim.hold);
        setContentView(R.layout.activity_searchmap);
        initData();
        initView();
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.hold, R.anim.slide_out_right);
    }

    private void initData() {
        mDBWrapper = new DBWrapper(mPreference.getUserUid());
        Intent i = getIntent();
        mMode = i.getStringExtra(AppConfig.IntentParam.MODE);
        mLocationId = i.getIntExtra(AppConfig.IntentParam.ID, 0);
        mLatitude = i.getDoubleExtra(AppConfig.IntentParam.LATITUDE, 0);
        mLongitude = i.getDoubleExtra(AppConfig.IntentParam.LONGITUDE, 0);
    }

    private void initView() {
        AQuery aq = new AQuery(this);

        Toolbar toolbar = (Toolbar) aq.id(R.id.toolbar).getView();
        toolbar.setNavigationIcon(R.mipmap.ic_arrow_back);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        mListHistory = aq.id(R.id.list_autocomplete).gone().getListView();
        mAutoCompleteAdapter = new AutoCompleteAdapter(this);

        setHistoryAdapter();
        mListHistory.setAdapter(mAutoCompleteAdapter);
        mListHistory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                mSelectedResult = mAutoCompleteAdapter.getItem(position);
                mListHistory.setVisibility(View.GONE);
                mEditSearchText.setText(mSelectedResult.title);
                mEditSearchText.clearFocus();
                moveMapPosition();
                mTextLocationPick.setVisibility(View.VISIBLE);
            }
        });

        mEditSearchText = aq.id(R.id.edit_searchtext).getEditText();
        mEditSearchText.setFocusableInTouchMode(true);
        mEditSearchText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    search();
                    return true;
                }
                return false;
            }
        });
        aq.id(R.id.image_deletetext).clicked(mOnClickListener);
        mTextLocationPick = aq.id(R.id.text_locationpick).clicked(mOnClickListener).getTextView();
        MapLayout mapLayout = new MapLayout(this);
        mMapView = mapLayout.getMapView();
        mMapView.setOpenAPIKeyAuthenticationResultListener(this);
        mMapView.setDaumMapApiKey(getAppText(R.string.daummap_key));
        mMapView.setMapViewEventListener(this);

        ViewGroup mapViewContainer = (ViewGroup) aq.id(R.id.map_container).getView();
        mapViewContainer.removeAllViews();
        mapViewContainer.addView(mapLayout);


        aq.dismiss();
    }

    private void moveMapPosition()
    {
        if(mSelectedResult != null) {
            MapPOIItem poiItem = new MapPOIItem();
            poiItem.setItemName(mSelectedResult.title);
            poiItem.setMapPoint(MapPoint.mapPointWithGeoCoord(mSelectedResult.latitude, mSelectedResult.longitude));
            poiItem.setMarkerType(MapPOIItem.MarkerType.BluePin);
            poiItem.setShowAnimationType(MapPOIItem.ShowAnimationType.DropFromHeaven);
            poiItem.setShowCalloutBalloonOnTouch(true);
            poiItem.setDraggable(true);
            poiItem.setTag(153);
            mMapView.addPOIItem(poiItem);

            mMapView.setMapCenterPointAndZoomLevel(MapPoint.mapPointWithGeoCoord(mSelectedResult.latitude, mSelectedResult.longitude), 1, true);
        }
    }

    @Override
    public void onMapViewInitialized(MapView mapView) {
        // MapView had loaded. Now, MapView APIs could be called safely.

        MapPOIItem poiItem = new MapPOIItem();
        poiItem.setItemName("Test");
        poiItem.setMapPoint(MapPoint.mapPointWithGeoCoord(mLatitude,mLongitude));
        poiItem.setMarkerType(MapPOIItem.MarkerType.BluePin);
        poiItem.setShowAnimationType(MapPOIItem.ShowAnimationType.DropFromHeaven);
        poiItem.setShowCalloutBalloonOnTouch(true);
        poiItem.setDraggable(true);
        poiItem.setTag(153);
        mapView.addPOIItem(poiItem);

        mapView.setMapCenterPointAndZoomLevel(MapPoint.mapPointWithGeoCoord(mLatitude,mLongitude), 1, true);
        //hideLoadingProgress();
    }

    @Override
    public void onMapViewCenterPointMoved(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewZoomLevelChanged(MapView mapView, int i) {

    }

    @Override
    public void onMapViewSingleTapped(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewDoubleTapped(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewLongPressed(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewDragStarted(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewDragEnded(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewMoveFinished(MapView mapView, MapPoint mapPoint) {

    }

    private void search() {
        if(mEditSearchText.getText().length() > 0) {
            Searcher searcher = new Searcher(); // net.daum.android.map.openapi.search.Searcher
            hideSoftKeyboard(mEditSearchText);
            if(mMode.equals("")) {
                searcher.searchKeyword(getApplicationContext(), mEditSearchText.getText().toString()
                        , mLatitude, mLongitude, 10000, 1, getAppText(R.string.daummap_key), new OnFinishSearchListener() {
                            @Override
                            public void onSuccess(List<SearchModel> itemList) {
                                mMapView.removeAllPOIItems(); // 기존 검색 결과 삭제
                                if (itemList.size() > 0) {
                                    mSearchResultList = new ArrayList<SearchModel>(itemList);
                                    runOnUiThread(new Runnable() {
                                        public void run() {
                                            mListHistory.setVisibility(View.VISIBLE);
                                            setHistoryAdapter();
                                        }
                                    });
                                } else {
                                    runOnUiThread(new Runnable() {
                                        public void run() {
                                            mListHistory.setVisibility(View.GONE);
                                            setHistoryAdapter();
                                        }
                                    });
                                }

                            }

                            @Override
                            public void onFail() {
                                runOnUiThread(new Runnable() {
                                    public void run() {
                                        showToast(getAppText(R.string.message_daummap_request_limit));
                                    }
                                });
                            }
                        });

            } else {
                searcher.searchKeywordAnoymous(getApplicationContext(), mEditSearchText.getText().toString()
                        , mLatitude, mLongitude, 10000, 1, getAppText(R.string.daummap_key), new OnFinishSearchListener() {
                            @Override
                            public void onSuccess(List<SearchModel> itemList) {
                                mMapView.removeAllPOIItems(); // 기존 검색 결과 삭제
                                if (itemList.size() > 0) {
                                    mSearchResultList = new ArrayList<SearchModel>(itemList);
                                    runOnUiThread(new Runnable() {
                                        public void run() {
                                            mListHistory.setVisibility(View.VISIBLE);
                                            setHistoryAdapter();
                                        }
                                    });
                                } else {
                                    runOnUiThread(new Runnable() {
                                        public void run() {
                                            mListHistory.setVisibility(View.GONE);
                                            setHistoryAdapter();
                                        }
                                    });
                                }

                            }

                            @Override
                            public void onFail() {
                                runOnUiThread(new Runnable() {
                                    public void run() {
                                        showToast(getAppText(R.string.message_daummap_request_limit));
                                    }
                                });

                            }
                        });
            }
        } else {
            showToast(getAppText(R.string.message_search_entertext));
        }
        mTextLocationPick.setVisibility(View.GONE);
    }

    @Override
    public void onDaumMapOpenAPIKeyAuthenticationResult(MapView mapView, int i, String s) {

    }

    private void setHistoryAdapter() {

        mAutoCompleteAdapter.clear();
        if(mSearchResultList.size() > 0) {
            for (int i = 0; i < mSearchResultList.size(); i++) {
                mAutoCompleteAdapter.add(mSearchResultList.get(i));
            }
        }
        mAutoCompleteAdapter.notifyDataSetChanged();

    }

    View.OnClickListener mOnClickListener = new View.OnClickListener(){

        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.text_locationpick:
                    if(mLocationId > 0) {
                        mDBWrapper.setLocation(mLocationId, mSelectedResult.title, mSelectedResult.latitude, mSelectedResult.longitude);
                        finish();
                    } else {
                        Intent intent = new Intent();
                        intent.putExtra(AppConfig.IntentParam.TITLE, mSelectedResult.title);
                        intent.putExtra(AppConfig.IntentParam.LATITUDE, mSelectedResult.latitude);
                        intent.putExtra(AppConfig.IntentParam.LONGITUDE, mSelectedResult.longitude);
                        setResult(1, intent);
                        finish();
                    }
                    sendBroadcast(new Intent(AppConfig.Broadcast.REFRESH_DATA));
                    break;
                case R.id.image_deletetext:
                    mEditSearchText.setText("");
                    break;
            }
        }
    };
}
