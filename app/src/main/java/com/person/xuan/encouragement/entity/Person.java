package com.person.xuan.encouragement.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chenxiaoxuan1 on 15/12/11.
 */
public class Person {
    private int curEncouragements;
    private int usedEncouragements;
    private List<Plan> plans = new ArrayList<>();

    public int getCurEncouragements() {
        return curEncouragements;
    }

    public int getUsedEncouragements() {
        return usedEncouragements;
    }

    public List<Plan> getPlans() {
        return plans;
    }

    public void addPlan(Plan plan) {
        plans.add(plan);
    }

    public void finish(Plan plan) {
        curEncouragements += plan.getEncouragement();
        plan.setIsFinish(true);
        plans.remove(plan);
    }

    public void finish(int index) {
        Plan plan = plans.get(index);
        plan.setIsFinish(true);
        curEncouragements += plan.getEncouragement();
        plans.remove(index);
    }

    public boolean usedOneEncouragements() {
        if (curEncouragements > 0) {
            --curEncouragements;
            ++usedEncouragements;
            return true;
        }else {
            return false;
        }
    }

}
