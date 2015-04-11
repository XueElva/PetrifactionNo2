package net.lz.petrifaction.tool;


import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
public class UserInfoTools {
	public static SharedPreferences getUserSH(Context context){
		SharedPreferences mySharedPreferences=context. getSharedPreferences("userinfo", 
				Activity.MODE_PRIVATE); 
		return mySharedPreferences;
	}
}