package com.person.xuan.encouragement.entity;

/**
 * Created by chenxiaoxuan1 on 15/12/11.
 */
public class Plan {
    private boolean isFinish;
    private String content;
    private int encouragement = 0;

    public boolean isFinish() {
        return isFinish;
    }

    public String getContent() {
        return content;
    }

    public int getEncouragement() {
        return encouragement;
    }

    public void setIsFinish(boolean isFinish) {
        this.isFinish = isFinish;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setEncouragement(int encouragement) {
        this.encouragement = encouragement;
    }
}
