package net.lz.petrifaction.activity;

import cn.dh.resourclogin.R;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;

public class ActivitySplash extends Activity {
		public static int time = 1500;

		@Override
		protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.splash);
			new Handler().postDelayed(new Runnable() {

				@Override
				public void run() {

					
					SharedPreferences prefs = PreferenceManager
							.getDefaultSharedPreferences(ActivitySplash.this);
					boolean firstTime = prefs.getBoolean("first_time", true);
					if (firstTime) {// 第一次启动
						Editor pEdit = prefs.edit();
						pEdit.putBoolean("first_time", false);
						pEdit.commit();
						Intent intent = new Intent();
						intent.setClass(ActivitySplash.this, ActivityGuid.class);
						startActivity(intent);
						finish();
					} else {
						
						SharedPreferences mySharedPreferences = getSharedPreferences(
								"userinfo", Activity.MODE_PRIVATE);
						if(mySharedPreferences.getBoolean("islogin", false)){  //已登录
							Intent intent = new Intent();
							intent.setClass(ActivitySplash.this, MainActivity.class);
							startActivity(intent);
							finish();
							return;
						}
						//未登录
						Intent intent = new Intent();
						intent.setClass(ActivitySplash.this, ActivityLogin.class);
						startActivity(intent);
						finish();
					}

				}

			}, time);
		}

	}
