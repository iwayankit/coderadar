package com.kayulab.android.coderadar.view;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;

import com.kayulab.android.coderadar.data.ContestContract;
import com.kayulab.android.coderadar.model.OnlineJudge;

/**
 * Created by kevinyu on 5/24/15.
 */
public class UpcomingContestFragment extends ContestListFragment {

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String whereClause = ContestContract.ContestEntry.COLUMN_START_TIME+" > ?";


        Context context = getActivity();
        SharedPreferences sharedPref = context.getSharedPreferences(ContestFragment.FILTER_PREFERENCE_FILE_KEY,Context.MODE_PRIVATE);

        whereClause += (" AND " + ContestContract.ContestEntry.COLUMN_SOURCE+" IN (");

        for (int i=0;i< OnlineJudge.OJ_NUMBER;i++) {
            boolean isShown = sharedPref.getBoolean(OnlineJudge.OJ_NAME[i],true);
            if (isShown) {
                whereClause += ("'"+OnlineJudge.OJ_NAME[i] + "', ");
            }
        }
        whereClause = whereClause.substring(0,whereClause.length()-2);
        whereClause += ")";

        String[] whereArgs = {
                Long.toString(System.currentTimeMillis())
        };

        return new CursorLoader(getActivity(),
                ContestContract.ContestEntry.CONTENT_URI,
                CONTEST_SUMMARY_COLUMNS,
                whereClause,
                whereArgs,
                ContestContract.ContestEntry.COLUMN_START_TIME+" ASC");
    }
}
