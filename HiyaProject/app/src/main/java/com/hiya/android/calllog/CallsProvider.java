package com.hiya.android.calllog;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;

import com.hiya.core.AbstractProvider;
import com.hiya.core.Data;

@TargetApi(Build.VERSION_CODES.KITKAT)
public class CallsProvider extends AbstractProvider {

	public CallsProvider(Context context) {
		super(context);
	}

	/**
	 * Get all calls.
	 * 
	 * @return List of calls
	 */
	public Data<Call> getCalls() {
        Data<Call> calls = getContentTableData(Call.uri, Call.class);
		return calls;
	}

}
