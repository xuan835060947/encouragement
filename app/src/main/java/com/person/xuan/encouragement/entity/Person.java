package com.person.xuan.encouragement.entity;

import android.graphics.Color;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chenxiaoxuan1 on 15/12/11.
 */
public class Person {
    private int curEncouragements;
    private int usedEncouragements;
    private List<Plan> plans = new ArrayList<>();
    private int padingHeight = 2;
    private int textSize = 20;
    private int textColor = Color.WHITE;

    public int getCurEncouragements() {
        return curEncouragements;
    }

    public int getUsedEncouragements() {
        return usedEncouragements;
    }

    public List<Plan> getPlans() {
        return plans;
    }

    public Plan getPlan(long id) {
        for (Plan plan : plans) {
            if (plan.getId() == id) {
                return plan;
            }
        }
        return null;
    }

    public void addPlan(Plan plan) {
        plans.add(plan);
    }

    public void updatePlan(Plan plan) {
        Plan old = getPlan(plan.getId());
        if (old == null) {
            addPlan(plan);
        } else {
            old.setEncouragement(plan.getEncouragement());
            old.setContent(plan.getContent());
            old.setIsFinish(plan.isFinish());
        }
    }

    public void finish(long id) {
        for (int i = 0; i < plans.size(); i++) {
            Plan plan = plans.get(i);
            if (plan.getId() == id) {
                plan.setIsFinish(true);
                curEncouragements += plan.getEncouragement();
                plans.remove(plan);
            }
        }
    }

    public boolean usedOneEncouragements() {
        if (curEncouragements > 0) {
            --curEncouragements;
            ++usedEncouragements;
            return true;
        } else {
            return false;
        }
    }

    public int getPadingHeight() {
        return padingHeight;
    }

    public int getTextSize() {
        return textSize;
    }

    public void setPadingHeight(int padingHeight) {
        this.padingHeight = padingHeight;
    }

    public void setTextSize(int textSize) {
        this.textSize = textSize;
    }

    public int getTextColor() {
        return textColor;
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
    }
}
