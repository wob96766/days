package com.mindspree.days.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.bumptech.glide.Glide;
import com.mindspree.days.R;
import com.mindspree.days.data.DBWrapper;
import com.mindspree.days.lib.AppConfig;
import com.mindspree.days.model.TimelineModel;

import java.util.ArrayList;


public class ImagePickActivity extends BaseActivity {

	private ArrayList<TextView> mSelectedImages = new ArrayList<TextView>();
	private ImageView mImageMain;
	private LinearLayout mHorizontalView;
    private TimelineModel mTimelineModel;
    private DBWrapper mDBWrapper;
    private String mFileName ="";

    public static void startActivity(Context context, TimelineModel timeline) {
		Intent intent = new Intent(context, ImagePickActivity.class);
		intent.putExtra(AppConfig.IntentParam.TIMELINE, timeline);
		context.startActivity(intent);
	}

    public static void startActivity(Context context,  TimelineModel timeline, String selectedFilename) {
        Intent intent = new Intent(context, ImagePickActivity.class);
        intent.putExtra(AppConfig.IntentParam.TIMELINE, timeline);
        intent.putExtra(AppConfig.IntentParam.FILENAME, selectedFilename);
        context.startActivity(intent);
    }
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		overridePendingTransition(R.anim.slide_in_right, R.anim.hold);
		setContentView(R.layout.activity_imagepick);
		
		initData();
		initView();
	}

	@Override
	public void finish(){
		super.finish();
		overridePendingTransition(R.anim.hold, R.anim.slide_out_right);
	}
	
	@Override
	public void onBackPressed() {
		finishActivity();
	}

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_save, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_save) {
            if(mSelectedImages.size() > 0) {
                final ArrayList<String> images = mTimelineModel.getImageList();
                for (int i=0 ; i<images.size() ; i++) {
                    mDBWrapper.setImageSequence(mTimelineModel.getCreateDate(), images.get(i), 20);
                }
                for (int i=0; i<mSelectedImages.size() ; i++) {
                    if(mSelectedImages.get(i).getTag() != null){
                        int imageIndex = (int)mSelectedImages.get(i).getTag();
                        mDBWrapper.setImageSequence(mTimelineModel.getCreateDate(), images.get(imageIndex), i);
                    }
                }
            }
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

	private void finishActivity()
	{
		finish();
	}
	
	private void initData(){
        mDBWrapper = new DBWrapper(mPreference.getUserUid());
		mTimelineModel = getIntent().getParcelableExtra(AppConfig.IntentParam.TIMELINE);
        mFileName = getIntent().getStringExtra(AppConfig.IntentParam.FILENAME);
	}
	
	private void initView(){

		AQuery aq = new AQuery(this);
		Toolbar toolbar = (Toolbar) aq.id(R.id.toolbar).getView();
		toolbar.setNavigationIcon(R.mipmap.ic_arrow_back);
		setSupportActionBar(toolbar);
		toolbar.setNavigationOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				finishActivity();
			}
		});
		getSupportActionBar().setDisplayShowTitleEnabled(false);
		getSupportActionBar().setDisplayShowCustomEnabled(true);
		mImageMain = aq.id(R.id.image_title).getImageView();
		mHorizontalView = (LinearLayout)aq.id(R.id.view_photo).getView();

        requestImage();


		aq.dismiss();
	}

    private void requestImage() {
        final ArrayList<String> images = mTimelineModel.getImageList();
        if(images.size() > 0) {
            if(!mFileName.equals("")) {
                Glide.with(getContext()).load(mFileName).into(mImageMain);
            } else {
                Glide.with(getContext()).load(images.get(0)).into(mImageMain);
            }


            for (int i = 0; i < images.size(); i++) {
                LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View view = inflater.inflate(R.layout.card_image_selection, null);
                ImageView imageView = (ImageView) view.findViewById(R.id.image_title);
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                view.setTag(i);
                Glide.with(getContext()).load(images.get(i)).into(imageView);
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                    if (view.getTag() != null) {
                        int imageIndex = (int) view.getTag();
                        TextView textPick = (TextView) view.findViewById(R.id.text_sort);
                        if (mSelectedImages.contains(textPick)) {
                            for (int j = 0; j < mSelectedImages.size(); j++) {
                                mSelectedImages.get(j).setText("");
                                mSelectedImages.get(j).setVisibility(View.INVISIBLE);
                            }
                            mSelectedImages.clear();
                        } else {
                            if (mSelectedImages.size() > 2) {
                                showToast(getAppText(R.string.message_invalid_picking));
                            } else {
                                textPick.setText(String.format("%d", mSelectedImages.size() + 1));
                                textPick.setTag(imageIndex);
                                textPick.setVisibility(View.VISIBLE);
                                mSelectedImages.add(textPick);
                                Glide.with(getContext()).load(images.get(imageIndex)).into(mImageMain);
                            }
                        }
                    }
                    }
                });
                mHorizontalView.addView(view);
            }
        }
    }


}
