package com.person.xuan.encouragement.data;

import com.person.xuan.encouragement.entity.History;
import com.person.xuan.encouragement.entity.Plan;
import com.person.xuan.encouragement.file.FileTool;
import com.person.xuan.encouragement.util.ShareValueUtil;

/**
 * Created by chenxiaoxuan1 on 16/11/19.
 */

public class HistoryData {
    private int mStartLine;
    private int mEndLine;
    private boolean mIsGotAllData;

    private HistoryData() {
    }

    public static HistoryData getInstance(boolean reset) {
        if (reset) {
            InstanceHolder.mHistoryData.mStartLine = 0;
            InstanceHolder.mHistoryData.mEndLine = 0;
            InstanceHolder.mHistoryData.mIsGotAllData = false;
        }
        return InstanceHolder.mHistoryData;
    }

    public static void addHistoryPlan(Plan plan) {
        FileTool.addLine(ShareValueUtil.HISTORY_FILE, ShareValueUtil.GSON.toJson(plan));
    }

    public void getHistory(int amount, final OnGetHistory onGetHistory) {
        if (mIsGotAllData) {
            onGetHistory.onGet(null);
        }
        mStartLine = mEndLine;
        mEndLine += amount;
        getHistoryFromFile(new OnGetHistoryFromFile() {
            @Override
            public void onGet(History history) {
                onGetHistory.onGet(history);
            }
        });
    }

    private void getHistoryFromFile(final OnGetHistoryFromFile onGetHistory) {
        FileTool.getLines(ShareValueUtil.HISTORY_FILE, mStartLine, mEndLine, new FileTool.OnGetLine() {
            History history = new History();

            @Override
            public void onGetLine(String line, int lineNum) {
                if (lineNum >= 0) {
                    Plan plan = ShareValueUtil.GSON.fromJson(line, Plan.class);
                    if (plan != null) {
                        history.addPlan(plan);
                    }
                } else {
                    if (lineNum == FileTool.STATE_GET_FILE_END) {
                        mIsGotAllData = true;
                    }
                    onGetHistory.onGet(history);
                }
            }
        });
    }

    private static class InstanceHolder {
        private static final HistoryData mHistoryData = new HistoryData();
    }

    private interface OnGetHistoryFromFile {
        void onGet(History history);
    }

    public interface OnGetHistory {
        void onGet(History history);
    }
}
