package com.hiya.core;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;

/**
 * Created by Rajesh
 */
public abstract class AbstractProvider {

    protected String TAG;
    private ContentResolver mContentResolver;

    protected AbstractProvider(Context context) {
        TAG = getClass().getName();
        mContentResolver = context.getContentResolver();
    }



    protected <T extends Entity> Data<T> getContentTableData(Uri uri, Class<T> cls) {
        Cursor cursor = mContentResolver.query(uri, Entity.getColumns(cls), null, null, null /*BaseColumns._ID + " DESC " + " LIMIT 50" */);
        if (cursor == null) {
            return null;
        }

        Data<T> data = new Data<T>(cursor, cls);
        return data;
    }


}
