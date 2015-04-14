package net.lz.petrifaction.tool;

import java.util.Stack;

import android.app.Activity;

public class ActivityManager {
	 
    private static Stack<Activity> activityStack;
    private static ActivityManager instance;
    private ActivityManager(){
         
    }
    public static ActivityManager getScreenManager(){
        if(instance==null){
            instance = new ActivityManager();
        }
        return instance;
    }
    //�?出栈�?
    public void popActivity(Activity activity){
        if(activity != null){
        	try {
            activity.finish();
				
			} catch (Exception e) {
				// TODO: handle exception
			}
            activityStack.remove(activity);
            activity=null;
        }
    }
    //获得当前栈顶
    public Activity currentActivity(){
        Activity activity = null;
        if(!activityStack.empty()){
            activity = (Activity) activityStack.lastElement();
        }
        return activity;
    }
    //当前Activity推入栈中
    public void pushActivity(Activity activity){
        if(activityStack == null){
            activityStack = new Stack<Activity>();
        }
        activityStack.add(activity);
    }
    //�?出所有Activity
    public void popAllActivityExceptionOne(){
        while(true){
            Activity activity = currentActivity();
            if(activity == null){
                break;
            }
//            if(activity.getClass().equals(cls)){
//                break;
//            }
            popActivity(activity);
        }
    }
}