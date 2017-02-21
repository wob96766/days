package com.mindspree.days.interfaces;

import com.mindspree.days.model.SearchModel;

import java.util.List;

public interface OnFinishSearchListener {
	public void onSuccess(List<SearchModel> itemList);
	public void onFail();
}
