package com.hiya.sample.fragments;

import android.provider.CallLog;
import android.text.format.DateFormat;
import android.widget.TextView;

import com.hiya.android.calllog.Call;
import com.hiya.android.calllog.CallsProvider;
import com.hiya.sample.R;
import com.hiya.sample.base.GetCursorTask;
import com.hiya.sample.base.RecycleViewCursorFragment;


/**
 * Created by rajesh.
 */
public class CallsFragment extends RecycleViewCursorFragment<Call> {

    private String[] mColumns = new String[]{
            CallLog.Calls.NUMBER,
            CallLog.Calls.TYPE,
            CallLog.Calls.CACHED_NAME,
            CallLog.Calls.DATE
    };

    @Override
    protected String getTitle() {
        return getResources().getString(R.string.callog);
    }


    @Override
    protected void bindEntity(Call call, TextView title, TextView details, TextView name, TextView date) {
        try {
            title.setText(call.number);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            details.setText(call.type.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            if (call.name.length() == 0) {
                name.setText(getResources().getString(R.string.unknown));
            } else {
                name.setText(call.name);
            }
        } catch (Exception e) {
            name.setText("Unknown");
            e.printStackTrace();
        }
        try {
            date.setText(getTime(call.callDate + ""));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    String getTime(String dateInMilliseconds) {
        String array[] = {"hh", "mm", "dd", "MM"};
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < array.length; i++) {

            stringBuilder.append(replaceChar(convertDate(dateInMilliseconds, array[i])));
            if (i == 0) {
                stringBuilder.append(":");
            } else if (i == 1) {
                stringBuilder.append(" ");
                stringBuilder.append(convertDate(dateInMilliseconds, "aa"));
                stringBuilder.append(" ");
            } else if (i == 3) {
                stringBuilder.append("/");
                stringBuilder.append(convertDate(dateInMilliseconds, "yyyy"));
            } else {
                stringBuilder.append("/");
            }
        }
        return stringBuilder.toString();
    }

    String replaceChar(String input) {

        Character ch = input.charAt(0);

        if ((ch + "").equalsIgnoreCase("0")) {
            input = input.substring(1);
        }
        return input;

    }

    public static String convertDate(String dateInMilliseconds, String dateFormat) {
        return DateFormat.format(dateFormat, Long.parseLong(dateInMilliseconds)).toString();
    }

    @Override
    protected String[] getProjectionColumns() {
        return mColumns;
    }

    @Override
    protected GetCursorTask.DataFetcher<Call> getFetcher() {
        return () -> {
            CallsProvider callsProvider = new CallsProvider(getApplicationContext());
            return callsProvider.getCalls();
        };
    }


}
