package net.lz.petrifaction.activity;

import net.lz.petrifaction.bean.MyConfig;
import net.lz.petrifaction.tool.CommonTools;
import net.lz.petrifaction.tool.UserInfoTools;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;
import cn.dh.resourclogin.R;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

public class ActivityAccount extends Activity {
	Spinner mSpinner;
	String[] mItems;
	String[] id = {};
	EditText qiyemingcheng;
	EditText lianxirenxingming;
	EditText quhao;
	EditText zuoji;
	EditText shouji;
	boolean cango = false;

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ((keyCode == KeyEvent.KEYCODE_BACK)) {
			if (cango==false) {
				Toast.makeText(ActivityAccount.this, "请完善账户信息！", Toast.LENGTH_SHORT).show();
			}

			return false;
		} else {
			return super.onKeyDown(keyCode, event);
		}

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.account);

		ImageButton back = (ImageButton) findViewById(R.id.back);
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (cango==false) {
					Toast.makeText(ActivityAccount.this, "请完善账户信息！", Toast.LENGTH_SHORT)
							.show();
					return;
				}
				finish();
			}
		});

		mSpinner = (Spinner) findViewById(R.id.qiyeleixing);
		// 建立数据
		String[] mItems = { "生产企业", "贸易企业", "工贸一体", "其他" };
		String[] idtemp = { "1", "2", "0", "3" };

		id = idtemp;

		// 建立Adapter并且绑定数据源
		ArrayAdapter<String> _Adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_dropdown_item, mItems);

		// 绑定 Adapter到控件
		mSpinner.setAdapter(_Adapter);

		qiyemingcheng = (EditText) findViewById(R.id.qiyemingcheng);
		lianxirenxingming = (EditText) findViewById(R.id.lianxirenxingming);
		quhao = (EditText) findViewById(R.id.quhao);
		zuoji = (EditText) findViewById(R.id.zuoji);
		shouji = (EditText) findViewById(R.id.shouji);

		getNews("list");

		View save = (View) findViewById(R.id.save);
		save.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				getNews("update");
			}
		});

	}

	public void getNews(final String type) {
		if (qiyemingcheng.getText().length() == 0 && type.equals("update")) {
			Toast.makeText(ActivityAccount.this, "请填写企业名称", Toast.LENGTH_SHORT).show();
			return;
		}
		if (lianxirenxingming.getText().length() == 0 && type.equals("update")) {
			Toast.makeText(ActivityAccount.this, "请填写联系人姓名", Toast.LENGTH_SHORT).show();
			return;
		}
		AsyncHttpClient client = new AsyncHttpClient();
		com.loopj.android.http.RequestParams params = new com.loopj.android.http.RequestParams();
		params.put("accessToken",
				UserInfoTools.getUserSH(this).getString("accessToken", ""));
		params.put("act", type);
		params.put("corpname", qiyemingcheng.getText().toString());
		params.put("corptype", id[mSpinner.getSelectedItemPosition()]);
		params.put("corplxr", lianxirenxingming.getText().toString());
		params.put("cropareano", quhao.getText().toString());
		params.put("corpphone", zuoji.getText().toString());
		params.put("corpmobile", shouji.getText().toString());

		AsyncHttpResponseHandler hd = new AsyncHttpResponseHandler() {
			@Override
			public void onStart() {
				CommonTools.createLoadingDialog(ActivityAccount.this,
						getResources().getString(R.string.loading)).show();
				super.onStart();
			}

			@Override
			public void onSuccess(int statusCode, String content) {
				CommonTools.cancleDialog();
				CommonTools.Log("content", content);
				JSONObject js;
				if (CommonTools.CheckJson(true, content, ActivityAccount.this)) {
					try {
						js = new JSONObject(content).getJSONObject("data");
						if (js.getString("login").equals("1")) {
							if (type.equals("update")) {
								Toast.makeText(ActivityAccount.this, "修改成功!",
										Toast.LENGTH_SHORT).show();
								cango = true;
							}
							JSONObject temp = js.getJSONArray("message")
									.getJSONObject(0);
							qiyemingcheng.setText(temp.getString("corpname"));
							lianxirenxingming
									.setText(temp.getString("corplxr"));
							quhao.setText(temp.getString("cropareano"));
							zuoji.setText(temp.getString("corpphone"));
							shouji.setText(temp.getString("corpmobile"));
							if (qiyemingcheng.getText().length() > 0) {
								cango = true;
							}
							if (temp.getString("corptype").endsWith("1")) {

								mSpinner.setSelection(0);
							}
							if (temp.getString("corptype").endsWith("2")) {

								mSpinner.setSelection(1);
							}
							if (temp.getString("corptype").endsWith("3")) {

								mSpinner.setSelection(3);
							}
							if (temp.getString("corptype").endsWith("0")) {

								mSpinner.setSelection(2);
							}

						} else {
							Toast.makeText(ActivityAccount.this, js.getString("message"),
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
				cango = true;
				Toast.makeText(ActivityAccount.this,
						getResources().getString(R.string.failed),
						Toast.LENGTH_SHORT).show();
				super.onFailure(error, content);

			}
		};
		client.get(MyConfig.IP + "sd/corp.do", params, hd);
	}
}