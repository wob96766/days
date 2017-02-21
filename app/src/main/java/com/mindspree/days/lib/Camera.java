package com.mindspree.days.lib;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.widget.ImageView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;


public class Camera {
	
	private Context mContext;
	
	private int CAMERA_STEP_NONE = -1;
	private int CAMERA_STEP_ING = 1;
	private int CAMERA_STEP_DONE = 2;
	
	private int mCurrentStep=CAMERA_STEP_NONE;
	
	private int REQ_CODE_PICK_GALLERY = 9001;
	private int REQ_CODE_PICK_CAMERA = 9002;
	private int REQ_CODE_PICK_CROP = 9003;
	
	private boolean mCropRequested = false;

	private int mCropAspectWidth = 1, mCropAspectHeight = 1;
	private int mImageSizeBoundary = 500;
	
	private int mImageViewResourceID=-1;
	private File mFile;

	private Fragment mFragment;
	
	public Camera(Context context, int galleryCode, int cameraCode, int cropCode) {
		mContext = context;
		initCamera();
		setCameraCode(galleryCode, cameraCode, cropCode);
	}
	
	public Camera(Context context, Fragment fragment, int galleryCode, int cameraCode, int cropCode) {
		mContext = context;
		mFragment = fragment;
		initCamera();
		setCameraCode(galleryCode, cameraCode, cropCode);
	}

	
	public Camera(Context context, int resource, int galleryCode, int cameraCode, int cropCode) {
		mContext = context;
		mImageViewResourceID = resource;
		
		initCamera();
		setCameraCode(galleryCode, cameraCode, cropCode);
	}
	
	public Camera(Context context, Fragment fragment, int resource, int galleryCode, int cameraCode, int cropCode) {
		mContext = context;
		mFragment = fragment;
		mImageViewResourceID = resource;
		
		initCamera();
		setCameraCode(galleryCode, cameraCode, cropCode);
	}
	
	public void initCamera(){
		mFile = null;
	}

	public void setImageSizeBoundary(int sizePixel) {
		mImageSizeBoundary = sizePixel;
	}
	
	public void setCameraCode(int galleryCode, int cameraCode, int cropCode){
		REQ_CODE_PICK_GALLERY = galleryCode;
		REQ_CODE_PICK_CAMERA = cameraCode;
		REQ_CODE_PICK_CROP = cropCode;
	}
	
	public int getCurrentCameraStep(){
		// -1 == no step
		// 1 == ing
		// 2 == done
		return mCurrentStep;
	}

	public void deleteFile(){
		if( mFile != null ){
			mFile.delete();
		}
	}
	
	private boolean checkWriteExternalPermission() {
		String permission = "android.permission.WRITE_EXTERNAL_STORAGE";
		int res = mContext.checkCallingOrSelfPermission(permission);
		return (res == PackageManager.PERMISSION_GRANTED);
	}
	
	private boolean checkSDisAvailable() {
		String state = Environment.getExternalStorageState();
		return state.equals(Environment.MEDIA_MOUNTED);
	}

	public boolean checkCameraPermission() {
		
		if (!checkWriteExternalPermission()) {
			showAlert("we need android.permission.WRITE_EXTERNAL_STORAGE");
			return false;
		}
		
		if (!checkSDisAvailable()) {
			showAlert("Check External Storage.");
			return false;
		}
		
		return true;
	}
	
	private void showAlert( String msg) {
	}

	public void cameraActivityResult(int requestCode, int resultCode, Intent data) {

		if( resultCode == Activity.RESULT_OK ){
			if (requestCode == REQ_CODE_PICK_GALLERY ) {
				Uri uri = data.getData();
				copyUriToFile(uri, getTempImageFile());
				if (mCropRequested) {
					cropImage();
				} else {
					doFinalProcess();
				}
			} else if (requestCode == REQ_CODE_PICK_CAMERA ) {
				correctCameraOrientation(getTempImageFile());
				if (mCropRequested) {
					cropImage();
				} else {
					doFinalProcess();
				}
			} else if (requestCode == REQ_CODE_PICK_CROP ) {
				doFinalProcess();
			}
		}
		
	}
	
	private int exifOrientationToDegrees(int exifOrientation) {
		if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_90) {
			return 90;
		} else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_180) {
			return 180;
		} else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_270) {
			return 270;
		}
		return 0;
	}
	
	private Bitmap rotateImage(Bitmap bitmap, int degrees) {
		if (degrees != 0 && bitmap != null) {
			Matrix m = new Matrix();
			m.setRotate(degrees, (float) bitmap.getWidth() / 2, (float) bitmap.getHeight() / 2);
			try {
				Bitmap converted = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), m, true);
				if (bitmap != converted) {
					bitmap.recycle();
					bitmap = converted;
				}
			} catch (Exception ex) {
			}
		}
		return bitmap;
	}
	
	private void correctCameraOrientation(File imgFile) {
		Bitmap bitmap = loadImageWithSampleSize(imgFile);
		try {
			ExifInterface exif = new ExifInterface(imgFile.getAbsolutePath());
			int exifOrientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
			int exifRotateDegree = exifOrientationToDegrees(exifOrientation);
			bitmap = rotateImage(bitmap, exifRotateDegree);
			saveBitmapToFile(bitmap);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void saveBitmapToFile(Bitmap bitmap) {
		File target = getTempImageFile();
		try {
			FileOutputStream fos = new FileOutputStream(target, false);
			bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
			fos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private Bitmap loadImageWithSampleSize(File file) {
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(file.getAbsolutePath(), options);
		int width = options.outWidth;
		int height = options.outHeight;
		int longSide = Math.max(width, height);
		int sampleSize = 1;
		if (longSide > mImageSizeBoundary) {
		//	sampleSize = longSide / mImageSizeBoundary;
		}
		options.inJustDecodeBounds = false;
		options.inSampleSize = sampleSize;
		options.inPurgeable = true;
		options.inDither = false;

		Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath(), options);
		return bitmap;
	}
	
	public File getSelectedImageFile() {
		return mFile;
	}
	
	public String getSelectedImagePath() {
		
		File imageFile = getTempImageFile();

		return imageFile.getAbsolutePath();
	}
	
	public Bitmap getSelectedImageBitmap() {
		
		File imageFile = getTempImageFile();

		// show image on ImageView
		Bitmap bm = BitmapFactory.decodeFile(imageFile.getAbsolutePath());
		
		return bm;
	}
	
	private void doFinalProcess() {
		Bitmap bitmap = loadImageWithSampleSize(getTempImageFile());

		bitmap = resizeImageWithinBoundary(bitmap);

		saveBitmapToFile(bitmap);
		
		if( mImageViewResourceID > 0 ){
			File imageFile = getTempImageFile();
			
			String filePath = imageFile.getAbsolutePath();

			// show image on ImageView
			Bitmap bm = BitmapFactory.decodeFile(filePath);
			ImageView imageView = (ImageView)((Activity) mContext).findViewById(mImageViewResourceID);
			imageView.setImageBitmap(bm);
		}
		
		mCurrentStep = CAMERA_STEP_DONE;
	}
	
	private Bitmap resizeBitmapWithWidth(Bitmap source, int wantedWidth) {
		if (source == null)
			return null;

		int width = source.getWidth();
		int height = source.getHeight();

		float resizeFactor = wantedWidth * 1f / width;

		int targetWidth, targetHeight;
		targetWidth = (int) (width * resizeFactor);
		targetHeight = (int) (height * resizeFactor);

		Bitmap resizedBitmap = Bitmap.createScaledBitmap(source, targetWidth, targetHeight, true);

		return resizedBitmap;
	}
	
	private Bitmap resizeBitmapWithHeight(Bitmap source, int wantedHeight) {
		if (source == null)
			return null;

		int width = source.getWidth();
		int height = source.getHeight();

		float resizeFactor = wantedHeight * 1f / height;

		int targetWidth, targetHeight;
		targetWidth = (int) (width * resizeFactor);
		targetHeight = (int) (height * resizeFactor);

		Bitmap resizedBitmap = Bitmap.createScaledBitmap(source, targetWidth, targetHeight, true);

		return resizedBitmap;
	}
	
	private Bitmap resizeImageWithinBoundary(Bitmap bitmap) {
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();

		if (width > height) {
			if (width > mImageSizeBoundary) {
				bitmap = resizeBitmapWithWidth(bitmap, mImageSizeBoundary);
			}
		} else {
			if (height > mImageSizeBoundary) {
				bitmap = resizeBitmapWithHeight(bitmap, mImageSizeBoundary);
			}
		}
		return bitmap;
	}
	
	public void setCropOption(int aspectX, int aspectY) {
		mCropRequested = true;
		
		mCropAspectWidth = aspectX;
		mCropAspectHeight = aspectY;
	}
	
	private void copyUriToFile(Uri srcUri, File target) {
		FileInputStream inputStream = null;
		FileOutputStream outputStream = null;
		FileChannel fcin = null;
		FileChannel fcout = null;
		try {
			inputStream = (FileInputStream) mContext.getContentResolver().openInputStream(srcUri);
			outputStream = new FileOutputStream(target);

			fcin = inputStream.getChannel();
			fcout = outputStream.getChannel();

			long size = fcin.size();
			fcin.transferTo(0, size, fcout);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				fcout.close();
			} catch (IOException ioe) {
			}
			try {
				fcin.close();
			} catch (IOException ioe) {
			}
			try {
				outputStream.close();
			} catch (IOException ioe) {
			}
			try {
				inputStream.close();
			} catch (IOException ioe) {
			}
		}
	}
	
	public static String sha1(String s) {
		MessageDigest digest = null;
		try {
			digest = MessageDigest.getInstance("SHA-1");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		digest.reset();
		byte[] data = digest.digest(s.getBytes());
		return String.format("%0" + (data.length*2) + "X", new BigInteger(1, data));
	}
	
	private File getTempImageFile() {
		
		if( mFile != null && mFile.exists()){
			return mFile;
		}
		
		String mFileName = sha1(String.format("%d", System.currentTimeMillis()))+".png";

		File path = new File(Environment.getExternalStorageDirectory() + "/menupick/");
		if (!path.exists()) {
			path.mkdirs();
		}
		mFile = new File(path, mFileName);
		
		return mFile;
	}

	public static String getDeviceModel(){
		return android.os.Build.MODEL;
	}
	
	private void cropImage() {
		String device = getDeviceModel();
		if( device.contains(AppConfig.deviceModel.NEXUS) ){
			cropImage_Ver02();
		}
		else {
			cropImage_Ver01();
		}
	}

	private void cropImage_Ver02() {

		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(Uri.fromFile(getTempImageFile()), "image/*");

		intent.putExtra("crop", "true");
		
		intent.putExtra("aspectX", mCropAspectWidth);
		intent.putExtra("aspectY", mCropAspectHeight);
		if(mFragment == null)
			((Activity)mContext).startActivityForResult(intent, REQ_CODE_PICK_CROP);
		else
			mFragment.startActivityForResult(intent, REQ_CODE_PICK_CROP);
	}
	
	private void cropImage_Ver01() {

		
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setType("image/*");
		List<ResolveInfo> cropToolLists = mContext.getPackageManager().queryIntentActivities(intent, 0);
		int size = cropToolLists.size();
		if (size == 0) {
			doFinalProcess();
		} else {
			intent.setData(Uri.fromFile(getTempImageFile()));
			
			intent.putExtra("crop", "true");
			intent.putExtra("aspectX", mCropAspectWidth);
			intent.putExtra("aspectY", mCropAspectHeight);
//			intent.putExtra("outputX", mImageSizeBoundary);
//			intent.putExtra("outputY", 200);
			intent.putExtra("output", Uri.fromFile(getTempImageFile()));
//			intent.putExtra("return-data", true);
			Intent i = new Intent(intent);
			ResolveInfo res = cropToolLists.get(0);
			i.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
			if(mFragment == null)
				((Activity)mContext).startActivityForResult(i, REQ_CODE_PICK_CROP);
			else
				mFragment.startActivityForResult(i, REQ_CODE_PICK_CROP);
			
		}
	}
	
	public void showGallery(){
		
		mCurrentStep = CAMERA_STEP_ING;
		
		Intent i = new Intent(Intent.ACTION_PICK);
		i.setType(MediaStore.Images.Media.CONTENT_TYPE);
		i.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		if(mFragment == null)
			((Activity)mContext).startActivityForResult(i, REQ_CODE_PICK_GALLERY);
		else
			mFragment.startActivityForResult(i, REQ_CODE_PICK_GALLERY);
		
	}
	
	public void showCamera(){
		
		mCurrentStep = CAMERA_STEP_ING;
		
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(getTempImageFile()));
		intent.putExtra("return-data", true);
		if(mFragment == null)
			((Activity)mContext).startActivityForResult(intent, REQ_CODE_PICK_CAMERA);
		else
			mFragment.startActivityForResult(intent, REQ_CODE_PICK_CAMERA);
	}
}
