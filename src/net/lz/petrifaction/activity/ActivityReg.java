package net.lz.petrifaction.activity;

import net.lz.petrifaction.tool.CommonTools;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import cn.dh.resourclogin.R;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

public class ActivityReg extends Activity {
	EditText E_phone, E_pwd, E_ver_code;
	boolean canget = true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.reg);
		ImageButton back = (ImageButton) findViewById(R.id.back);
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
		E_phone = (EditText) this.findViewById(R.id.E_phone);

		E_pwd = (EditText) this.findViewById(R.id.E_pwd);

		Button zhuce = (Button) this.findViewById(R.id.zhuce);
		zhuce.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				zhuce();
			}
		});
		
	}

	private void GetVerCode() {

		final String phone = E_phone.getText() == null ? "" : E_phone.getText()
				+ "".trim();
		if ("".equals(phone) || phone.length() != 11) {
			Toast.makeText(this, "输入的手机号不合法", Toast.LENGTH_LONG)
					.show();
			return;
		}
		if (canget) {
			getcode();
		} else {
			Toast.makeText(this, "请一分钟后重新获取验证码", Toast.LENGTH_LONG).show();
		}
	}

	public void getcode() {
		AsyncHttpClient client = new AsyncHttpClient();
		AsyncHttpResponseHandler hd = new AsyncHttpResponseHandler() {
			@Override
			public void onStart() {
				CommonTools.createLoadingDialog(ActivityReg.this,
						getResources().getString(R.string.loading)).show();
				super.onStart();
			}

			@Override
			public void onSuccess(int statusCode, String content) {
				CommonTools.cancleDialog();
				CommonTools.Log("content", content);
				JSONObject js;
				try {
					if (new JSONObject(content).getJSONObject("data")
							.getString("getcode").equals("1")) {
						Toast.makeText(
								ActivityReg.this,
								new JSONObject(content).getJSONObject("data")
										.getString("message"),
								Toast.LENGTH_SHORT).show();
						canget = false;
						new Handler().postDelayed(new Runnable() {
							public void run() {
								canget = true;
							}
						}, 60000);
					} else {
						Toast.makeText(
								ActivityReg.this,
								new JSONObject(content).getJSONObject("data")
										.getString("message"),
								Toast.LENGTH_LONG).show();

					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
				super.onSuccess(statusCode, content);
			}

			@Override
			public void onFailure(Throwable error, String content) {
				CommonTools.Log("error", content);
				Toast.makeText(ActivityReg.this,
						getResources().getString(R.string.failed),
						Toast.LENGTH_SHORT).show();
				super.onFailure(error, content);

			}
		};
		client.get(
				"http://www.oilchem.net/reg/getcode/?clientid=GMall&UserName="
						+ E_phone.getText().toString(), hd);
	}

	public void zhuce() {

		String phone = E_phone.getText() == null ? "" : E_phone.getText()
				+ "".trim();
		if ("".equals(phone) || phone.length() != 11) {
			Toast.makeText(this, "输入的手机号不合法", Toast.LENGTH_LONG)
					.show();
			return;
		}
		String pew = E_pwd.getText() == null ? "" : E_pwd.getText() + "".trim();
		if ("".equals(pew)) {
			Toast.makeText(this, "请输入您的登录密码!", Toast.LENGTH_LONG).show();
			return;
		}
		if (pew.length() < 6) {
			Toast.makeText(this, "密码长度应该不少6位!", Toast.LENGTH_LONG).show();
			return;
		}
//		String pew_next = E_zzdm.getText() == null ? "" : E_zzdm.getText()
//				+ "".trim();
//		if ("".equals(pew_next)) {
//			Toast.makeText(this, "您两次输入的密码不一致，请确�?!", Toast.LENGTH_LONG).show();
//			return;
//		}

		AsyncHttpClient client = new AsyncHttpClient();
		com.loopj.android.http.RequestParams params = new com.loopj.android.http.RequestParams();
		params.put("action", "RegGMall");
		params.put("UserName", E_phone.getText().toString());
		params.put("PassWord", E_pwd.getText().toString());
		AsyncHttpResponseHandler hd = new AsyncHttpResponseHandler() {
			@Override
			public void onStart() {
				CommonTools.createLoadingDialog(ActivityReg.this,
						getResources().getString(R.string.loading)).show();
				super.onStart();
			}

			@Override
			public void onSuccess(int statusCode, String content) {
				CommonTools.cancleDialog();
				CommonTools.Log("content", content);
				JSONObject js;
//				02-10 16:11:00.985: D/content(6283): {"stat": "1","error": "","data": {"register": "1","message": "注册成功。请等待客服联系，为您开通功能�??"}}
				try {
					if(new JSONObject(content).getJSONObject("data").getString("register").equals("1")){
						Toast.makeText(ActivityReg.this,
								new JSONObject(content).getJSONObject("data").getString("message"),
								Toast.LENGTH_SHORT).show();
						finish();
					}else{
						Toast.makeText(ActivityReg.this,
								new JSONObject(content).getJSONObject("data").getString("message"),
								Toast.LENGTH_SHORT).show();
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
				super.onSuccess(statusCode, content);
			}

			@Override
			public void onFailure(Throwable error, String content) {
				CommonTools.cancleDialog();
				CommonTools.Log("error", content);
				Toast.makeText(ActivityReg.this,
						getResources().getString(R.string.failed),
						Toast.LENGTH_SHORT).show();
				super.onFailure(error, content);

			}
		};
		client.post("http://www.oilchem.net/reg/", params, hd);
	}
}