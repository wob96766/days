package com.mindspree.days.services;

import android.app.job.JobInfo;
import android.app.job.JobParameters;
import android.app.job.JobScheduler;
import android.app.job.JobService;
import android.content.ComponentName;
import android.content.Context;
import android.database.Cursor;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.widget.Toast;

import com.mindspree.days.data.DBWrapper;
import com.mindspree.days.lib.AppPreference;
import com.mindspree.days.lib.AppUtils;
import com.mindspree.days.model.TimelineModel;
import com.mindspree.days.model.WeatherModel;
import com.mindspree.days.network.AsyncHttpResponse;
import com.mindspree.days.network.HttpAdapter;
import com.mindspree.days.network.RequestCode;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cz.msebera.android.httpclient.Header;

/**
 * Created by vision51 on 2017. 5. 5..
 */


@RequiresApi(api = Build.VERSION_CODES.N)
public class FileJobService extends JobService {

    private AppPreference mPreference;
    private Context mContext;
    private DBWrapper mDBWrapper;

    // The root URI of the media provider, to monitor for generic changes to its content.
    static final Uri MEDIA_URI = Uri.parse("content://" + MediaStore.AUTHORITY + "/");

    // Path segments for image-specific URIs in the provider.
    static final List<String> EXTERNAL_PATH_SEGMENTS
            = MediaStore.Images.Media.EXTERNAL_CONTENT_URI.getPathSegments();

    // The columns we want to retrieve about a particular image.
    static final String[] PROJECTION = new String[] {
            MediaStore.Images.ImageColumns._ID, MediaStore.Images.ImageColumns.DATA
    };
    static final int PROJECTION_ID = 0;
    static final int PROJECTION_DATA = 1;

    // This is the external storage directory where cameras place pictures.
    static final String DCIM_DIR = Environment.getExternalStoragePublicDirectory(
            Environment.DIRECTORY_DCIM).getPath();

    // A pre-built JobInfo we use for scheduling our job.
    static final JobInfo JOB_INFO;

    static {
        JobInfo.Builder builder = new JobInfo.Builder(777,
                new ComponentName("com.mindspree.days", FileJobService.class.getName()));
        // Look for specific changes to images in the provider.
        builder.addTriggerContentUri(new JobInfo.TriggerContentUri(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                JobInfo.TriggerContentUri.FLAG_NOTIFY_FOR_DESCENDANTS));
        // Also look for general reports of changes in the overall provider.
        builder.addTriggerContentUri(new JobInfo.TriggerContentUri(MEDIA_URI, 0));
        JOB_INFO = builder.build();
    }

    // Fake job work.  A real implementation would do some work on a separate thread.
    final Handler mHandler = new Handler();
    final Runnable mWorker = new Runnable() {
        @Override public void run() {
            scheduleJob(FileJobService.this);
            jobFinished(mRunningParams, false);
        }
    };

    JobParameters mRunningParams;

    // Schedule this job, replace any existing one.
    public static void scheduleJob(Context context) {
        JobScheduler js = context.getSystemService(JobScheduler.class);
        js.schedule(JOB_INFO);
        Log.i("PhotosContentJob", "JOB SCHEDULED!");
    }

    // Check whether this job is currently scheduled.
    public static boolean isScheduled(Context context) {
        JobScheduler js = context.getSystemService(JobScheduler.class);
        List<JobInfo> jobs = js.getAllPendingJobs();
        if (jobs == null) {
            return false;
        }
        for (int i=0; i<jobs.size(); i++) {
            if (jobs.get(i).getId() == 777) {
                return true;
            }
        }
        return false;
    }

    // Cancel this job, if currently scheduled.
    public static void cancelJob(Context context) {
        JobScheduler js = context.getSystemService(JobScheduler.class);
        js.cancel(777);
    }

    @Override
    public boolean onStartJob(JobParameters params) {
        Log.i("PhotosContentJob", "JOB STARTED!");
        mRunningParams = params;

        // Instead of real work, we are going to build a string to show to the user.
        StringBuilder sb = new StringBuilder();

        // Did we trigger due to a content change?
        if (params.getTriggeredContentAuthorities() != null) {
            boolean rescanNeeded = false;

            if (params.getTriggeredContentUris() != null) {
                // If we have details about which URIs changed, then iterate through them
                // and collect either the ids that were impacted or note that a generic
                // change has happened.
                ArrayList<String> ids = new ArrayList<>();
                for (Uri uri : params.getTriggeredContentUris()) {
                    List<String> path = uri.getPathSegments();
                    if (path != null && path.size() == EXTERNAL_PATH_SEGMENTS.size()+1) {
                        // This is a specific file.
                        ids.add(path.get(path.size()-1));
                    } else {
                        // Oops, there is some general change!
                        rescanNeeded = true;
                    }
                }

                if (ids.size() > 0) {
                    // If we found some ids that changed, we want to determine what they are.
                    // First, we do a query with content provider to ask about all of them.
                    StringBuilder selection = new StringBuilder();
                    for (int i=0; i<ids.size(); i++) {
                        if (selection.length() > 0) {
                            selection.append(" OR ");
                        }
                        selection.append(MediaStore.Images.ImageColumns._ID);
                        selection.append("='");
                        selection.append(ids.get(i));
                        selection.append("'");
                    }

                    // Now we iterate through the query, looking at the filenames of
                    // the items to determine if they are ones we are interested in.
                    Cursor cursor = null;
                    boolean haveFiles = false;
                    try {
                        cursor = getContentResolver().query(
                                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                                PROJECTION, selection.toString(), null, null);
                        while (cursor.moveToNext()) {
                            // We only care about files in the DCIM directory.
                            String dir = cursor.getString(PROJECTION_DATA);
                            if (dir.startsWith(DCIM_DIR)) {
                                if (!haveFiles) {
                                    haveFiles = true;
                                    sb.append("New photos:\n");
                                }
                                sb.append(cursor.getInt(PROJECTION_ID));
                                sb.append(": ");
                                sb.append(dir);
                                sb.append("\n");
                            }
                        }
                    } catch (SecurityException e) {
                        sb.append("Error: no access to media!");
                    } finally {
                        if (cursor != null) {
                            cursor.close();
                        }
                    }
                }

            } else {
                // We don't have any details about URIs (because too many changed at once),
                // so just note that we need to do a full rescan.
                rescanNeeded = true;
            }

            if (rescanNeeded) {
                sb.append("Photos rescan needed!");
            }
        } else {
            sb.append("(No photos content)");
        }
        //Toast.makeText(this, sb.toString(), Toast.LENGTH_LONG).show();
        mPreference = AppPreference.getInstance();
        mDBWrapper = new DBWrapper(mPreference.getUserUid());

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date now = new Date();

        TimelineModel model = mDBWrapper.getLastTimelineToday();

        Location dbLocation = new Location("dbLocation");
        dbLocation.setLatitude(model.getMeasureLatitude());
        dbLocation.setLongitude(model.getMeasureLogitude());

        Location location = new Location("deviceLocation");
        location.setLatitude(mPreference.getLatitude());
        location.setLongitude(mPreference.getLongitude());


        try {

            if (AppUtils.datediffinminutes(now, dateFormat.parse(model.getMeasureDate())) >= (mPreference.getDuration()/2)) {
                if (dbLocation.distanceTo(location) > mPreference.getDistance()) {
                    sendAnalyticsEvent(mPreference.getUserUid(), "photo_location", String.format("%f,%f",location.getLatitude(), location.getLongitude()));
                    mDBWrapper.insertLocationAminute(location.getLatitude(), location.getLongitude());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        // We will emulate taking some time to do this work, so we can see batching happen.
        mHandler.postDelayed(mWorker, 10*1000);
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        mHandler.removeCallbacks(mWorker);
        return false;
    }

    private void sendAnalyticsEvent(String id, String name, String content){
        /*Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, id);
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, name);
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, content);

        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.VIEW_ITEM, bundle);*/
        HttpAdapter httpClient = HttpAdapter.getInstance(FileJobService.this);
        httpClient.RequestAnalytics(FileJobService.this, id, name, content, mAsyncHttpResponse);
    }

    // 서버 응답 처리
    AsyncHttpResponse mAsyncHttpResponse = new AsyncHttpResponse()
    {
        @Override
        public void onHttpStart(int reqCode) {
            switch(reqCode)
            {
                case RequestCode.GET_WEATHER:
                    break;
            }
        }

        @Override
        public void onHttpFinish(int reqCode) {
            switch(reqCode)
            {
                case RequestCode.GET_WEATHER:
                    break;
            }
        }

        @Override
        public void onHttpSuccess(int reqCode, int code, Header[] headers, byte[] response, Object tag) {
            String data = new String(response);
            try {
                switch(reqCode) {
                    case RequestCode.GET_WEATHER:
                        try {
                            WeatherModel model = WeatherModel.parseData(data);
                            sendAnalyticsEvent(mPreference.getUserUid(), "weather", model.mWeather);
                            mDBWrapper.setWeather(model.mWeather);

                        } catch (Exception e) {
                            sendAnalyticsEvent(mPreference.getUserUid(), "weather", "exception");
                        }
                        break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onHttpFailure(int reqCode, int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
            try {
                switch(reqCode)
                {
                    case RequestCode.GET_WEATHER:
                        sendAnalyticsEvent(mPreference.getUserUid(), "weather", "error");
                        break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    };
}
