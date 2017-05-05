package com.mindspree.days.data;

import android.database.Cursor;

import com.google.firebase.database.DataSnapshot;
import com.mindspree.days.AppApplication;
import com.mindspree.days.model.Daily;
import com.mindspree.days.model.DatelineModel;
import com.mindspree.days.model.DailyModel;
import com.mindspree.days.model.Location;
import com.mindspree.days.model.Photo;
import com.mindspree.days.model.PhotoInfoModel;
import com.mindspree.days.model.PhotosGridModel;
import com.mindspree.days.model.TimelineModel;
import com.mindspree.days.model.SentenceModel;

import java.util.ArrayList;


/**
 * Created by vision51 on 2016. 12. 14..
 */

public class DBWrapper {
    private DBHelper mDbHelper;
    private String mUserUid;

    public DBWrapper(String userUid){
        mUserUid = userUid;
        mDbHelper = AppApplication.getDbHelper();
    }

    public boolean isAvailable(String photoPath){
        return mDbHelper.isAvailable(mUserUid, photoPath);
    }

    public long insertNew(String fileLocation, String fileName, String fileUpdateDate, int photo_size) {
        return mDbHelper.insertNew(mUserUid, fileLocation, fileName, fileUpdateDate, photo_size);
    }

    public int deleteOld(String filePath) {
        return mDbHelper.deleteOld(mUserUid, filePath);
    }



    // request locations with no name.
    public ArrayList<TimelineModel> getNonameTimelineList() {
        return mDbHelper.getNonameTimelineList(mUserUid);
    }

    // request clustered images.
    public ArrayList<PhotosGridModel> getClusteredImages() {
        return mDbHelper.getClusteredImages(mUserUid);
    }
    // create user's location
    public void insertLocation(double latitude, double longitude) {
        mDbHelper.insertLocation(mUserUid, latitude, longitude);
    }

    // create user's location
    public void insertLocationAminute(double latitude, double longitude) {
        mDbHelper.insertLocation(mUserUid, latitude, longitude);
    }

    // create user's location
    public void updateLocation(double latitude, double longitude) {
        mDbHelper.updateLocation(mUserUid, latitude, longitude);
    }

    // create user's location
    public void updateMeasureLocation(double latitude, double longitude) {
        mDbHelper.updateMeasureLocation(mUserUid, latitude, longitude);
    }

    // request sentence for TimelineActivity
    public SentenceModel getSentence() {
        return mDbHelper.getSentence(mUserUid);
    }

    // request sentence for TimelineActivity
    public SentenceModel getSentence(String dateString) {
        return mDbHelper.getSentence(mUserUid, dateString);
    }

    // request timelines for TodayFragement
    public ArrayList<TimelineModel> getTiemlineList(){
        return mDbHelper.getTimelinelist(mUserUid);
    }

    // request timelines for TimelineActivity
    public ArrayList<TimelineModel> getTiemlineList(String dateString){
        return mDbHelper.getTimelinelist(mUserUid, dateString);
    }

    // request emotions for SummarizeFragment
    public ArrayList<DailyModel> getDailyList(){
        return mDbHelper.getDailyList(mUserUid);
    }

    // request datelines for JournalFragment
    public ArrayList<DatelineModel> getDatelineList(int nextpage){
        return mDbHelper.getDatelineList(mUserUid, nextpage);
    }

    // request datelines for JournalFragment
    public ArrayList<DatelineModel> getDatelineList(){
        return mDbHelper.getDatelineList(mUserUid);
    }

    public ArrayList<DatelineModel> getDatelineList(String searchText, int nextpage) {
        return mDbHelper.getDatelineList(mUserUid, searchText, nextpage);
    }

    public DatelineModel getDateline(String dateString) {
        return mDbHelper.getDateline(mUserUid, dateString);
    }

    // request last location & time for LocationLoggingService
    public TimelineModel getLastTimeline(){
        return mDbHelper.getLastTimeline(mUserUid);
    }

    // request last location & time for LocationLoggingService
    public TimelineModel getLastTimelineToday(){
        return mDbHelper.getLastTimelineToday(mUserUid);
    }

    // request close last timeline
    public void setLastTimeline(){
        mDbHelper.setLastTimeline(mUserUid);
    }

    //update the mood of the day.
    public void setMood(String mood){
        mDbHelper.setMood(mUserUid, mood);
    }

    //update the weather of the day.
    public void setWeather(String weather){
        mDbHelper.setWeather(mUserUid, weather);
    }

    public String getWeatherToday(){
        return mDbHelper.getWeatherToday(mUserUid);
    }

    // update the set location information.
    public void setLocation(int locationId, String title) {
        mDbHelper.setLocation(mUserUid, locationId, title);
    }

    // update the set location information.
    public void setLocation(int locationId, String title, String category) {
        mDbHelper.setLocation(mUserUid, locationId, title, category);
    }


    // update the set location information.
    public void setLocationLog(int locationId , int islock) {
        mDbHelper.setLocationLog(mUserUid, islock, locationId);
    }

    // update the set location information.
    public void setLocationLog(int locationId, String name, double latitude, double longitude) {
        mDbHelper.setLocationLog(locationId, mUserUid, name, latitude, longitude);
    }


    //update the set location information.
    public void setLocation(int locationId, String title, double latitude, double longitude){
        mDbHelper.setLocation(mUserUid, locationId, title, latitude, longitude);
    }

    // update the display order of your photos.
    public void setImageSequence(String dateString, String filename, int sequence){
        mDbHelper.setImageSequence(mUserUid, dateString, filename, sequence);
    }

    public ArrayList<Photo> getPhotoDatalist(String start, String end) {
        return mDbHelper.getPhotoDatalist(mUserUid, start, end);
    }

    public ArrayList<Location> getLocationList(String start, String end) {
        return mDbHelper.getLocationDatalist(mUserUid, start, end);
    }

    public ArrayList<Daily> getDailyDatalist(String start, String end) {
        return mDbHelper.getDailyDatalist(mUserUid, start, end);
    }


    public void setPhoto(String fileindex, String imageUrl) {
        mDbHelper.setPhoto(mUserUid, fileindex, imageUrl);
    }

    public void restoreLocation(DataSnapshot list) {
        for (DataSnapshot datasnapshot : list.getChildren()) {
            Location item = datasnapshot.getValue(Location.class);
            mDbHelper.restoreLocation(mUserUid, item);
        }
    }

    public void restoreDaily(DataSnapshot list) {
        for (DataSnapshot datasnapshot : list.getChildren()) {
            Daily item = datasnapshot.getValue(Daily.class);
            mDbHelper.restoreDaily(mUserUid, item);
        }
    }

    public void restorePhoto(DataSnapshot list) {
        for (DataSnapshot datasnapshot : list.getChildren()) {
            Photo item = datasnapshot.getValue(Photo.class);
            mDbHelper.restorePhoto(mUserUid, item);
        }
    }

    public void setSentence(String date, String dateString) {
        mDbHelper.setSentence(mUserUid, date, dateString);
    }

    public PhotoInfoModel getPhotoInfo(String file_index) {
        return mDbHelper.getPhotoInfo(mUserUid, file_index);
    }

    public int getClusteredImageSize (String cluster_id) {
        return mDbHelper.getClusteredImageSize(mUserUid, cluster_id);
    }

    public String getCreateTime (String name) {
        return mDbHelper.getCreateTime(mUserUid, name);
    }

    public int getNewPhotoCount() {
        return mDbHelper.getNewPhotoCount(mUserUid);
    }

    public int updateNewPhotoFlag() {
        return mDbHelper.updateNewPhotoFlag(mUserUid);
    }


}
