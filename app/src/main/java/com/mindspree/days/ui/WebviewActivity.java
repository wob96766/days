package com.mindspree.days.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebView;

import com.androidquery.AQuery;
import com.mindspree.days.R;
import com.mindspree.days.lib.AppConfig;

public class WebviewActivity extends BaseActivity {
    private WebView mWebView;
    private String mUrl = "";

    public static void startActivity(Context context, String url) {
        Intent intent = new Intent(context, WebviewActivity.class);
        intent.putExtra(AppConfig.IntentParam.URL, url);
        context.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        mScreenName = getClass().toString();
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.slide_in_up, R.anim.hold);
        setContentView(R.layout.activity_webview);

        initData();
        initView();
    }

    @Override
    public void finish(){
        super.finish();
        overridePendingTransition(R.anim.hold, R.anim.slide_out_up);
    }

    private void initData() {
        mUrl = getIntent().getStringExtra(AppConfig.IntentParam.URL);
    }

    private void initView() {
        AQuery aq = new AQuery(this);

        Toolbar toolbar = (Toolbar) aq.id(R.id.toolbar).getView();
        toolbar.setNavigationIcon(R.mipmap.ic_arrow_back);
        if(mUrl.equals(AppConfig.WebUrl.TERMS)) {
            aq.id(R.id.toolbar_title).text(getAppText(R.string.text_terms));
        }else if(mUrl.equals(AppConfig.WebUrl.POLICY)) {
            aq.id(R.id.toolbar_title).text(getAppText(R.string.text_policy));
        }

        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finishActivity();
            }
        });
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayShowCustomEnabled(true);

        mWebView = aq.id(R.id.webview).getWebView();
        mWebView.setVerticalScrollbarOverlay(true);
        mWebView.setHorizontalScrollbarOverlay(true);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setLoadsImagesAutomatically(true);

        mWebView.loadUrl(mUrl);

        aq.dismiss();
    }

    private void finishActivity(){
        finish();
    }

    @Override
    public void onBackPressed() {
        finishActivity();
    }

    View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()){
            }
        }
    };
}
