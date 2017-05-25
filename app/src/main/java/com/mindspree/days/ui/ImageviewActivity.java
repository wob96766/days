package com.mindspree.days.ui;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Pattern;


import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.mindspree.days.R;
import com.mindspree.days.adapter.ImageFragmentAdapter;
import com.mindspree.days.lib.AppConfig;
import com.mindspree.days.lib.AppUtils;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


public class ImageviewActivity extends BaseActivity {

	ArrayList<String> mImages = new ArrayList<String>();
	private ImageFragmentAdapter mImageAdapter;
	private ViewPager mPager;
	private TextView pageText;
	private ImageView mBtnDownload;
	private String mFileName = "";

	public static void startActivity(Context context, String imageUrls) {
		Intent intent = new Intent(context, ImageviewActivity.class);
		intent.putExtra(AppConfig.IntentParam.IMAGE, imageUrls);
		context.startActivity(intent);
	}

	public static void startActivity(Context context, String imageUrls, String selectedFilename) {
		Intent intent = new Intent(context, ImageviewActivity.class);
		intent.putExtra(AppConfig.IntentParam.IMAGE, imageUrls);
		intent.putExtra(AppConfig.IntentParam.FILENAME, selectedFilename);
		context.startActivity(intent);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		overridePendingTransition(R.anim.slide_in_up, R.anim.hold);
		setContentView(R.layout.activity_imageview);
		
		initData();
		initView();
	}

	@Override
	public void finish(){
		super.finish();
		overridePendingTransition(R.anim.hold, R.anim.slide_out_up);
	}
	
	@Override
	public void onBackPressed() {
		finishActivity();
	}

	
	private void finishActivity()
	{
		finish();
		//overridePendingTransition(R.anim.hold, R.anim.slide_out_bottom);
	}
	
	private void initData(){
		String[] imgs = getIntent().getStringExtra(AppConfig.IntentParam.IMAGE).split(Pattern.quote(","));
		mImages = new ArrayList<String>(Arrays.asList(imgs));
		mFileName = getIntent().getStringExtra(AppConfig.IntentParam.FILENAME);
		
		mImageAdapter = new ImageFragmentAdapter(getSupportFragmentManager(), getContext());
		mImageAdapter.setDataSource(mImages);

	}
	
	private void initView(){
		
		AQuery aq = new AQuery(this);
		int position = 1;
		mPager = (ViewPager)aq.id(R.id.view_pager).getView();
		mPager.addOnPageChangeListener(mOnPageChangeListener);
		mImageAdapter.setDataSource(mImages);
		mPager.setAdapter(mImageAdapter);
		if(!mFileName.equals("")) {
			for(int i=0 ; i<mImages.size() ; i++) {
				if(mImages.get(i).equals(mFileName)) {
					mPager.setCurrentItem(i);
					position = i;
				}
			}
		}
		pageText = aq.id(R.id.text_pager).text(String.format("%d / %d", position, mImages.size())).getTextView();
		aq.id(R.id.btn_close).clicked(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finishActivity();
			}
		});
		mBtnDownload = aq.id(R.id.btn_download).clicked(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				downloadImage(mImages.get(mPager.getCurrentItem()));
			}
		}).getImageView();
		aq.dismiss();
	}

	private void downloadImage(String imageUrl){
		AQuery aq = new AQuery(this);
		File file = AppUtils.getTempImageFile(getContext());
		aq.download(imageUrl, file, new AjaxCallback<File>(){
			@Override
			public void callback(String url, File file, AjaxStatus status) {
				showToast(getAppText(R.string.message_complete_download));
			}
		});
		aq.dismiss();

	}

	ViewPager.OnPageChangeListener mOnPageChangeListener = new ViewPager.OnPageChangeListener()
	{

		@Override
		public void onPageScrollStateChanged(int arg0) {
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
		
		}

		@Override
		public void onPageSelected(int position) {
			pageText.setText((position + 1) + " / " + mImages.size());
			if(mImages.get(position).contains("http://") | mImages.get(position).contains("https://")){
				mBtnDownload.setVisibility(View.VISIBLE);
			} else {
				mBtnDownload.setVisibility(View.INVISIBLE);
			}
		}
		
	};



}
