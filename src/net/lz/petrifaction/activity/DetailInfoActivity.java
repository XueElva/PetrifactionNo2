package net.lz.petrifaction.activity;

import net.lz.petrifaction.bean.MyConfig;
import net.lz.petrifaction.tool.CommonTools;
import net.lz.petrifaction.tool.UserInfoTools;

import org.json.JSONException;
import org.json.JSONObject;

import cn.dh.resourclogin.R;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class DetailInfoActivity extends Activity {
boolean ismy=false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ismy=getIntent().getExtras().getBoolean("ismy",false);
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		setContentView(R.layout.gongqiuxiangqing);

		ImageButton back = (ImageButton) findViewById(R.id.back);
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});

		getInfo();
		if(ismy){
			TextView shanchu=(TextView)findViewById(R.id.shanchu);
			shanchu.setVisibility(View.VISIBLE);
			shanchu.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					shanchu();
				}
			});
		}
	
	}

	public void getInfo() {
		AsyncHttpClient client = new AsyncHttpClient();
		com.loopj.android.http.RequestParams params = new com.loopj.android.http.RequestParams();
		params.put("accessToken",
				UserInfoTools.getUserSH(this).getString("accessToken", ""));
		params.put("sdid", getIntent().getExtras().getString("id"));
		params.put("tb", getIntent().getExtras().getString("tb"));

		AsyncHttpResponseHandler hd = new AsyncHttpResponseHandler() {
			@Override
			public void onStart() {
				CommonTools.createLoadingDialog(DetailInfoActivity.this,
						getResources().getString(R.string.loading)).show();
				super.onStart();
			}

			@Override
			public void onSuccess(int statusCode, String content) {
				Log.d("value","content=="+content);
				CommonTools.cancleDialog();
				CommonTools.Log("content", content);
				JSONObject js;
				if (CommonTools.CheckJson(true, content, DetailInfoActivity.this)) {
					try {
						js = new JSONObject(content).getJSONObject("data");
						if (js.getString("login").equals("1")) {
							final JSONObject temp=js.getJSONArray("message").getJSONObject(0);
							
							TextView qiyemingcheng=(TextView)findViewById(R.id.qiyemingcheng);
							TextView lianxiren=(TextView)findViewById(R.id.lianxirenxingming);
							EditText lianxidianhua=(EditText)findViewById(R.id.lianxidianhua);
							TextView gongqiuleixing=(TextView)findViewById(R.id.gongqiuleixing);
							TextView chanpinmingcheng=(TextView)findViewById(R.id.chanpinmingcheng);
							TextView chanpinguige=(TextView)findViewById(R.id.chanpinguige);
							TextView chanpinjiage=(TextView)findViewById(R.id.chanpinjiage);
							TextView jiagedanwei=(TextView)findViewById(R.id.jiagedanwei);
							TextView producer=(TextView) findViewById(R.id.producer);
							TextView area=(TextView) findViewById(R.id.area);
							
							qiyemingcheng.setText(temp.getString("corpname"));
							lianxiren.setText(temp.getString("lxrname"));
							lianxidianhua.setText(temp.getString("lxrmobile"));
							gongqiuleixing.setText(temp.getString("tb").equals("11")?"供应":"求购");
							chanpinmingcheng.setText(temp.getString("cpname"));
							chanpinguige.setText(temp.getString("model"));
							chanpinjiage.setText(temp.getString("price").equals("0")?"面议":(temp.getString("price")+temp.getString("coin")));
							jiagedanwei.setText(temp.getString("stock").equals("0")?"面议":(temp.getString("stock")+temp.getString("unit")));
							producer.setText(temp.getString("producer"));
							area.setText(temp.getString("pickupaddr"));
							
							lianxidianhua.setEnabled(false);
							

							View call = (View) findViewById(R.id.call);
							call.setOnClickListener(new OnClickListener() {

								@Override
								public void onClick(View v) {
									Uri uri;
									try {
										uri = Uri.parse("tel:"+temp.getString("lxrmobile"));
										Intent intent = new Intent(Intent.ACTION_DIAL, uri);
										startActivity(intent);
									} catch (JSONException e) {
										e.printStackTrace();
									}
									
								}
							});
							
						} else {
							Toast.makeText(DetailInfoActivity.this, js.getString("message"),
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
				Toast.makeText(DetailInfoActivity.this,
						getResources().getString(R.string.failed),
						Toast.LENGTH_SHORT).show();
				super.onFailure(error, content);

			}
		};
		client.get(MyConfig.IP + "sd/sd.do", params, hd);
	}
	public void shanchu() {
		AsyncHttpClient client = new AsyncHttpClient();
		com.loopj.android.http.RequestParams params = new com.loopj.android.http.RequestParams();
		params.put("accessToken",
				UserInfoTools.getUserSH(this).getString("accessToken", ""));
		params.put("sdid", getIntent().getExtras().getString("id"));

		AsyncHttpResponseHandler hd = new AsyncHttpResponseHandler() {
			@Override
			public void onStart() {
				CommonTools.createLoadingDialog(DetailInfoActivity.this,
						getResources().getString(R.string.loading)).show();
				super.onStart();
			}

			@Override
			public void onSuccess(int statusCode, String content) {
				CommonTools.cancleDialog();
				CommonTools.Log("content", content);
				JSONObject js;
				if (CommonTools.CheckJson(true, content, DetailInfoActivity.this)) {
					try {
						js = new JSONObject(content).getJSONObject("data");
						if (js.getString("login").equals("1")) {
							Toast.makeText(DetailInfoActivity.this, js.getString("msg"),
									Toast.LENGTH_SHORT).show();
							finish();
							
						} else {
							Toast.makeText(DetailInfoActivity.this, js.getString("msg"),
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
				Toast.makeText(DetailInfoActivity.this,
						getResources().getString(R.string.failed),
						Toast.LENGTH_SHORT).show();
				super.onFailure(error, content);

			}
		};
		client.get(MyConfig.IP + "sd/sddel.do", params, hd);
	}
}
