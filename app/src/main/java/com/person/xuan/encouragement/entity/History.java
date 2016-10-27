package com.person.xuan.encouragement.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chenxiaoxuan1 on 15/12/17.
 */
public class History {
    private int encouragement;
    private List<Plan> plans = new ArrayList<>();

    public List<Plan> getPlans() {
        return plans;
    }

    public void addPlan(Plan plan){
        if(plan == null){
            throw new AssertionError();
        }
        plans.add(plan);
        encouragement += plan.getEncouragement();
    }
}
