package net.lz.petrifaction.activity;

import net.lz.petrifaction.bean.MyConfig;
import net.lz.petrifaction.tool.ActivityManager;
import net.lz.petrifaction.tool.CommonTools;
import net.lz.petrifaction.tool.MD5;
import net.lz.petrifaction.tool.UserInfoTools;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import cn.dh.resourclogin.R;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

public class ActivityLogin extends Activity {
	EditText phone;
	EditText password;
	public String IMEI;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);

		TelephonyManager telephonyManager = (TelephonyManager) this
				.getSystemService(Context.TELEPHONY_SERVICE);
		String imei = telephonyManager.getDeviceId();
		IMEI = imei;
		ActivityManager.getScreenManager().pushActivity(this);
		phone = (EditText) findViewById(R.id.phone);
		password = (EditText) findViewById(R.id.password);
		Button login = (Button) findViewById(R.id.login);
		login.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				getNews();
			}
		});
		TextView zhuce = (TextView) findViewById(R.id.zhuce);
		zhuce.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(ActivityLogin.this, ActivityReg.class);
				startActivity(intent);
			}
		});
		SharedPreferences mySharedPreferences = getSharedPreferences(
				"userinfo", Activity.MODE_PRIVATE);
		phone.setText(mySharedPreferences.getString("phonenumber", ""));
		TextView wangjimima = (TextView) findViewById(R.id.wangjimima);
		wangjimima.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				try {
					Intent intent = new Intent();
					intent.setAction("android.intent.action.VIEW");
					Uri content_url = Uri.parse("http://i.oilchem.net");
					intent.setData(content_url);
					startActivity(intent);
				} catch (Exception e) {
				}

			}
		});
	}

	public void getNews() {
		AsyncHttpClient client = new AsyncHttpClient();
		com.loopj.android.http.RequestParams params = new com.loopj.android.http.RequestParams();
		if (phone.getText().length() == 0) {
			Toast.makeText(ActivityLogin.this, "请输入资讯帐号", Toast.LENGTH_SHORT).show();
			return;
		}
		if (password.getText().length() == 0) {
			Toast.makeText(ActivityLogin.this, "请输入密码", Toast.LENGTH_SHORT).show();
			return;
		}
		params.put("username", phone.getText().toString());
		params.put("password", MD5.GetMD5Code(password.getText().toString()));
		params.put("imei", IMEI);
		params.put("accessToken", "");
		params.put("ver", CommonTools.getAppInfo(this).get("name"));

		AsyncHttpResponseHandler hd = new AsyncHttpResponseHandler() {
			@Override
			public void onStart() {
				CommonTools.createLoadingDialog(ActivityLogin.this,
						getResources().getString(R.string.loading)).show();
				super.onStart();
			}

			@Override
			public void onSuccess(int statusCode, String content) {
				// CommonTools.cancleDialog();
				CommonTools.Log("content", content);
				CommonTools.cancleDialog();
				JSONObject js;
				try {
					js = new JSONObject(content).getJSONObject("data");
					if (js.getString("login").equals("1")) {
						Toast.makeText(ActivityLogin.this, "登录成功", Toast.LENGTH_SHORT)
								.show();
						SharedPreferences mySharedPreferences = getSharedPreferences(
								"userinfo", Activity.MODE_PRIVATE);
						SharedPreferences.Editor editor = mySharedPreferences
								.edit();
						editor.putBoolean("islogin", true);
						editor.putString("accessToken",
								js.getString("accessToken"));
						editor.putString("username", js.getString("username"));
						editor.putString("corpname", js.getString("corpname"));
						editor.putString("phonenumber", phone.getText()
								.toString());
						editor.commit();

						getaccountinfo("list");

					} else {
						Toast.makeText(ActivityLogin.this, js.getString("message"),
								Toast.LENGTH_SHORT).show();

					}
				} catch (JSONException e) {
					e.printStackTrace();
				}

				super.onSuccess(statusCode, content);
			}

			@Override
			public void onFailure(Throwable error, String content) {
				CommonTools.Log("error", content);
				CommonTools.cancleDialog();
				Toast.makeText(ActivityLogin.this,
						getResources().getString(R.string.failed),
						Toast.LENGTH_SHORT).show();
				super.onFailure(error, content);

			}
		};
		client.get(MyConfig.IP + "user/userLogin.do", params, hd);
	}

	
	//获取账户信息
	public void getaccountinfo(final String type) {
		AsyncHttpClient client = new AsyncHttpClient();
		com.loopj.android.http.RequestParams params = new com.loopj.android.http.RequestParams();
		params.put("accessToken",
				UserInfoTools.getUserSH(this).getString("accessToken", ""));
		params.put("act", type);
		params.put("corpname", "");
		params.put("corptype", "");
		params.put("corplxr", "");
		params.put("cropareano", "");
		params.put("corpphone", "");
		params.put("corpmobile", "");

		AsyncHttpResponseHandler hd = new AsyncHttpResponseHandler() {
			@Override
			public void onStart() {
				// CommonTools.createLoadingDialog(context,
				// getResources().getString(R.string.loading)).show();
				super.onStart();
			}

			@Override
			public void onSuccess(int statusCode, String content) {
				CommonTools.cancleDialog();
				CommonTools.Log("content", content);
				JSONObject js;
				if (CommonTools.CheckJson(true, content, ActivityLogin.this)) {
					try {
						js = new JSONObject(content).getJSONObject("data");
						if (js.getString("login").equals("1")) {
							JSONObject temp = js.getJSONArray("message")
									.getJSONObject(0);
							if (temp.getString("corpname").length() == 0
									|| temp.getString("corplxr").length() == 0) {

								Intent intent = new Intent();
								intent.putExtra("cango", false);
								intent.setClass(ActivityLogin.this, ActivityAccount.class);
								startActivity(intent);
								finish();
								Toast.makeText(ActivityLogin.this, "请先完善账户信息",
										Toast.LENGTH_SHORT).show();
							} else {
								Intent intent = new Intent();
								intent.setClass(ActivityLogin.this, MainActivity.class);
								startActivity(intent);
								finish();
							}

						} else {
							 Toast.makeText(ActivityLogin.this, js.getString("message"),
							 Toast.LENGTH_SHORT).show();

						}
					} catch (JSONException e) {
					}
				}

				super.onSuccess(statusCode, content);
			}

			@Override
			public void onFailure(Throwable error, String content) {
				CommonTools.Log("error", content);
				Toast.makeText(ActivityLogin.this,
						getResources().getString(R.string.failed),
						Toast.LENGTH_SHORT).show();
				super.onFailure(error, content);

			}
		};
		client.get(MyConfig.IP + "sd/corp.do", params, hd);
	}
}