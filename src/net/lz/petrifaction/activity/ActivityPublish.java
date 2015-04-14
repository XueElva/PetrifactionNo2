package net.lz.petrifaction.activity;

import net.lz.petrifaction.bean.MyConfig;
import net.lz.petrifaction.tool.CommonTools;
import net.lz.petrifaction.tool.UserInfoTools;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import cn.dh.resourclogin.R;
//发布或查看供求
public class ActivityPublish extends Activity {
	Spinner mSpinner;
	Spinner mSpinnerhuobi;
	Spinner mSpinnerdanwei;
	String[] mItems;
	String[] id = {};
	EditText chanpinmingcheng;
	EditText lianxiren;
	EditText chanpinguige;
	EditText producer;
	EditText price;
	EditText shuliang;
	EditText shoujied;
	EditText xiaoshoubanjing ;
	EditText quhao;
	EditText zuoji;
   
	String[] huobi = {};
	String[] danwei = {};

	String productid = "0";
	String productname = "";

	JSONObject lianxirenjsobj = new JSONObject();
	String type = "11";
    private boolean mIsMine;
    private TextView mDelete,mTitle;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_publish);

		mIsMine=getIntent().getBooleanExtra("ismy", true);
		mDelete=(TextView) findViewById(R.id.shanchu);
		mTitle=(TextView) findViewById(R.id.title);
		
		if(mIsMine){
			mDelete.setVisibility(View.VISIBLE);
			mTitle.setText("我的供求");
		}else{
			mDelete.setVisibility(View.GONE);
			mTitle.setText("发布供求");
		}
		
		mDelete.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				shanchu();
			}
		});
		ImageButton back = (ImageButton) findViewById(R.id.back);
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});

		final TextView gongying = (TextView) findViewById(R.id.tigong);

		final TextView xuqiu = (TextView) findViewById(R.id.xuqiu);

		gongying.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				gongying.setTextColor(getResources().getColor(R.color.red));
				xuqiu.setTextColor(getResources().getColor(R.color.gray));
				type = "11";
			}
		});
		xuqiu.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				xuqiu.setTextColor(getResources().getColor(R.color.red));
				gongying.setTextColor(getResources().getColor(R.color.gray));
				type = "10";
			}
		});
		try {
			if (getIntent().getExtras().getString("tb").equals("11")) {
				gongying.performClick();
				
			} else {
				xuqiu.performClick();
				
			}
		} catch (Exception e) {
		}
		xiaoshoubanjing = (EditText) findViewById(R.id.xiaoshoubanjing);
		mSpinnerhuobi = (Spinner) findViewById(R.id.huobi);
		mSpinnerdanwei = (Spinner) findViewById(R.id.danwei);

		chanpinmingcheng = (EditText) findViewById(R.id.chanpinmingcheng);

		quhao = (EditText) findViewById(R.id.quhao);

		zuoji = (EditText) findViewById(R.id.zuoji);

		shoujied = (EditText) findViewById(R.id.shoujied);
		
		chanpinguige = (EditText) findViewById(R.id.chanpinguige);

		producer=(EditText) findViewById(R.id.producer);
		chanpinmingcheng.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				Intent intent = new Intent();
				intent.setClass(ActivityPublish.this, ActivityProductName.class);
				startActivityForResult(intent, 20);

			}
		});

		lianxiren = (EditText) findViewById(R.id.lianxiren);
		
	

		lianxiren.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				Intent intent = new Intent();
				intent.setClass(ActivityPublish.this, ActivityLianxiren.class);
				startActivityForResult(intent, 20);

			}
		});
		price = (EditText) findViewById(R.id.price);
		shuliang = (EditText) findViewById(R.id.shuliang);

		View save = (View) findViewById(R.id.save);
		save.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				getNews(type);
			}
		});

		getHuobi();
		getAccountInfo("list");;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == 30) {
			productname = data.getExtras().getString("name");
			productid = data.getExtras().getString("id");
			chanpinmingcheng.setText(productname);
		}
		if (resultCode == 40) {
			try {
				lianxirenjsobj = new JSONObject(data.getExtras().getString(
						"lianxiren"));
				lianxiren.setText(lianxirenjsobj.getString("lxrname"));
				quhao.setText(lianxirenjsobj.getString("lxrareano"));
				zuoji.setText(lianxirenjsobj.getString("lxrphone"));
				shoujied.setText(lianxirenjsobj.getString("lxrmobile"));
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		if (resultCode == 50) {
			// lianxirenjsobj = new JSONObject(
			// data.getExtras().getString("lianxiren"));
			lianxiren.setText(data.getExtras().getString("name"));
		}
		super.onActivityResult(requestCode, resultCode, data);

	}

	
	//发布
	public void getNews(final String type) {

		if (chanpinmingcheng.getText().length() == 0) {
			Toast.makeText(ActivityPublish.this, "请填写产品名称", Toast.LENGTH_SHORT).show();
			return;
		}
		if (lianxiren.getText().length() == 0) {
			Toast.makeText(ActivityPublish.this, "请填写联系人名称", Toast.LENGTH_SHORT).show();
			return;
		}
		if ((shoujied.getText().length() == 0)
				&& (quhao.getText().toString().length() == 0 || zuoji.getText()
						.toString().length() == 0)) {
			Toast.makeText(ActivityPublish.this, "请至少填写一种联系方式", Toast.LENGTH_SHORT).show();
			return;
		}
		AsyncHttpClient client = new AsyncHttpClient();
		com.loopj.android.http.RequestParams params = new com.loopj.android.http.RequestParams();
		params.put("accessToken",
				UserInfoTools.getUserSH(this).getString("accessToken", ""));
		params.put("act", "update");
		try {
			params.put("sdid", getIntent().getExtras().getString("id"));
		} catch (Exception e) {
			params.put("sdid", "0");
		}
		
		params.put("tb", type);
		try {
			params.put("lxrname", lianxirenjsobj.getString("lxrname"));
			params.put("lxrid", lianxirenjsobj.getString("lxrid"));
			params.put("lxrareano", lianxirenjsobj.getString("lxrareano"));
			params.put("lxrphone", lianxirenjsobj.getString("lxrphone"));
			params.put("lxrmobile", lianxirenjsobj.getString("lxrmobile"));
			params.put("lxrjobtitle", lianxirenjsobj.getString("lxrjobtitle"));
			
		} catch (JSONException e1) {
			e1.printStackTrace();
		}
		if (lianxirenjsobj.length() == 0) {
			params.put("lxrname", lianxiren.getText().toString());

			params.put("lxrareano", quhao.getText().toString());
			params.put("lxrphone", zuoji.getText().toString());
			params.put("lxrmobile", shoujied.getText().toString());

		}
		params.put("cpname", productname.length()==0?chanpinmingcheng.getText().toString():productname);
		params.put("productid", productid);
		params.put("salesArea", xiaoshoubanjing.getText().toString());
		params.put("model", chanpinguige.getText().toString());
		params.put("producer", producer.getText().toString());
		params.put("price", price.getText().toString());
		params.put("coin", huobi[mSpinnerhuobi.getSelectedItemPosition()]);
		params.put("unit", danwei[mSpinnerdanwei.getSelectedItemPosition()]);
		try {
			params.put("producer", lianxirenjsobj.getString("corpid"));
		} catch (JSONException e1) {
			e1.printStackTrace();
		}
		params.put("stock", shuliang.getText().toString());
		params.put("pickupAddr", "");
		AsyncHttpResponseHandler hd = new AsyncHttpResponseHandler() {
			@Override
			public void onStart() {
				CommonTools.createLoadingDialog(ActivityPublish.this,
						getResources().getString(R.string.loading)).show();
				super.onStart();
			}

			@Override
			public void onSuccess(int statusCode, String content) {
				CommonTools.cancleDialog();
				CommonTools.Log("content", content);
				JSONObject js;
				if (CommonTools.CheckJson(true, content, ActivityPublish.this)) {
					try {
						js = new JSONObject(content).getJSONObject("data");
						if (js.getString("succeed").equals("1")) {
							Toast.makeText(ActivityPublish.this, "发布成功！", Toast.LENGTH_SHORT)
									.show();
							finish();
						} else {
							Toast.makeText(ActivityPublish.this, js.getString("message"),
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
				Toast.makeText(ActivityPublish.this,
						getResources().getString(R.string.failed),
						Toast.LENGTH_SHORT).show();
				super.onFailure(error, content);

			}
		};
		client.post(MyConfig.IP + "sd/sdpost.do", params, hd);
	}

	// tb=11&
	// cpname=船舶制造&
	// lxrid=17138&
	// lxrname=测试&
	// coin=元&
	// price=23&
	// unit=吨&
	// pickupAddr=&
	// act=update&
	// lxrjobtitle=&
	// lxrmobile=13002714165&
	// sdid=0&
	// model=咯啦咯啦咯了&
	// producer=607411&
	// productid=3868&
	// accessToken=a385f59ea8931d605c28d9c7a7b34856&
	// stock=55&
	// lxrphone=&
	// lxrareano=533
	public void getHuobi() {
		AsyncHttpClient client = new AsyncHttpClient();
		com.loopj.android.http.RequestParams params = new com.loopj.android.http.RequestParams();

		AsyncHttpResponseHandler hd = new AsyncHttpResponseHandler() {
			@Override
			public void onStart() {
				CommonTools.createLoadingDialog(ActivityPublish.this,
						getResources().getString(R.string.loading)).show();
				super.onStart();
			}

			@Override
			public void onSuccess(int statusCode, String content) {
				JSONArray arr;
				try {
					arr = new JSONArray(content);
					huobi = new String[arr.length()];
					for (int i = 0; i < arr.length(); i++) {
						huobi[i] = arr.getJSONObject(i).getString("coin");
					}
					ArrayAdapter<String> _Adapter = new ArrayAdapter<String>(
							ActivityPublish.this,
							android.R.layout.simple_spinner_dropdown_item,
							huobi);
					// 绑定 Adapter到控件
					mSpinnerhuobi.setAdapter(_Adapter);
				} catch (JSONException e) {
					e.printStackTrace();
				}
				getDanwei();
				super.onSuccess(statusCode, content);
			}

			@Override
			public void onFailure(Throwable error, String content) {
				CommonTools.Log("error", content);
				Toast.makeText(ActivityPublish.this,
						getResources().getString(R.string.failed),
						Toast.LENGTH_SHORT).show();
				super.onFailure(error, content);

			}
		};
		client.get(MyConfig.IP + "tool/coin.do", params, hd);
	}

	public void getDanwei() {
		AsyncHttpClient client = new AsyncHttpClient();
		com.loopj.android.http.RequestParams params = new com.loopj.android.http.RequestParams();

		AsyncHttpResponseHandler hd = new AsyncHttpResponseHandler() {
			@Override
			public void onStart() {
				super.onStart();
			}

			@Override
			public void onSuccess(int statusCode, String content) {
				CommonTools.cancleDialog();
				JSONArray arr;
				try {
					arr = new JSONArray(content);
					danwei = new String[arr.length()];
					for (int i = 0; i < arr.length(); i++) {
						danwei[i] = arr.getJSONObject(i).getString("unit");
					}
					ArrayAdapter<String> _Adapter = new ArrayAdapter<String>(
							ActivityPublish.this,
							android.R.layout.simple_spinner_dropdown_item,
							danwei);
					// 绑定 Adapter到控件
					mSpinnerdanwei.setAdapter(_Adapter);
				} catch (JSONException e) {
					e.printStackTrace();
				}
				try {
					if(getIntent().getExtras().getString("id").length()>0){
						getInfo();
					}
				} catch (Exception e) {
					// TODO: handle exception
				}
				
				super.onSuccess(statusCode, content);
			}

			@Override
			public void onFailure(Throwable error, String content) {
				CommonTools.Log("error", content);
				Toast.makeText(ActivityPublish.this,
						getResources().getString(R.string.failed),
						Toast.LENGTH_SHORT).show();
				super.onFailure(error, content);

			}
		};
		client.get(MyConfig.IP + "tool/unit.do", params, hd);
	}

	
	//得到我的供求信息
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
				CommonTools.createLoadingDialog(ActivityPublish.this,
						getResources().getString(R.string.loading)).show();
				super.onStart();
			}

			@Override
			public void onSuccess(int statusCode, String content) {
				CommonTools.cancleDialog();
				CommonTools.Log("content", content);
				JSONObject js;
				if (CommonTools.CheckJson(true, content, ActivityPublish.this)) {
					try {
						js = new JSONObject(content).getJSONObject("data");
						if (js.getString("login").equals("1")) {
							final JSONObject temp = js.getJSONArray("message")
									.getJSONObject(0);
                          Log.d("value","temp=="+temp.toString());
							chanpinmingcheng.setText(temp.getString("cpname"));
							xiaoshoubanjing.setText(temp.getString("salesarea"));
							chanpinguige.setText(temp.getString("model"));
							producer.setText(temp.getString("producer"));
							price.setText(temp.getString("price"));
							shuliang.setText(temp.getString("stock"));
							lianxiren.setText(temp.getString("lxrname"));
							shoujied.setText(temp.getString("lxrmobile"));
							quhao.setText(temp.getString("lxrareano"));
							zuoji.setText(temp.getString("lxrphone"));
							
					for(int i=0;i<danwei.length;i++){
						if(danwei[i].equals(temp.getString("unit"))){
							mSpinnerdanwei.setSelection(i);
						}
					}
					for(int i=0;i<huobi.length;i++){
						if(huobi[i].equals(temp.getString("coin"))){
							mSpinnerhuobi.setSelection(i);
						}
					}

						} else {
							Toast.makeText(ActivityPublish.this, js.getString("message"),
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
				Toast.makeText(ActivityPublish.this,
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
				CommonTools.createLoadingDialog(ActivityPublish.this,
						getResources().getString(R.string.loading)).show();
				super.onStart();
			}

			@Override
			public void onSuccess(int statusCode, String content) {
				CommonTools.cancleDialog();
				CommonTools.Log("content", content);
				JSONObject js;
				if (CommonTools.CheckJson(true, content, ActivityPublish.this)) {
					try {
						js = new JSONObject(content).getJSONObject("data");
						if (js.getString("login").equals("1")) {
							Toast.makeText(ActivityPublish.this, js.getString("msg"),
									Toast.LENGTH_SHORT).show();
							finish();
							
						} else {
							Toast.makeText(ActivityPublish.this, js.getString("msg"),
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
				Toast.makeText(ActivityPublish.this,
						getResources().getString(R.string.failed),
						Toast.LENGTH_SHORT).show();
				super.onFailure(error, content);

			}
		};
		client.get(MyConfig.IP + "sd/sddel.do", params, hd);
	}
	
	
	public void getAccountInfo(final String type) {
		AsyncHttpClient client = new AsyncHttpClient();
		com.loopj.android.http.RequestParams params = new com.loopj.android.http.RequestParams();
		params.put("accessToken",
				UserInfoTools.getUserSH(this).getString("accessToken", ""));
		params.put("act", type);
		params.put("corpname", "");
		params.put("corptype","");
		params.put("corplxr", "");
		params.put("cropareano","");
		params.put("corpphone", "");
		params.put("corpmobile","");

		AsyncHttpResponseHandler hd = new AsyncHttpResponseHandler() {
			@Override
			public void onStart() {
//				CommonTools.createLoadingDialog(context,
//						getResources().getString(R.string.loading)).show();
				super.onStart();
			}

			@Override
			public void onSuccess(int statusCode, String content) {
//				CommonTools.cancleDialog();
				CommonTools.Log("content", content);
				JSONObject js;
				if (CommonTools.CheckJson(false, content, ActivityPublish.this)) {
					try {
						js = new JSONObject(content).getJSONObject("data").getJSONArray("message").getJSONObject(0);
							shoujied.setText(js.getString("corpmobile"));
							quhao.setText(js.getString("cropareano"));
							zuoji.setText(js.getString("corpphone"));
							lianxiren.setText(js.getString("corplxr"));
							
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}

				super.onSuccess(statusCode, content);
			}

			@Override
			public void onFailure(Throwable error, String content) {
//				CommonTools.Log("error", content);
//				Toast.makeText(context,
//						getResources().getString(R.string.faile),
//						Toast.LENGTH_SHORT).show();
				super.onFailure(error, content);

			}
		};
		client.get(MyConfig.IP + "sd/corp.do", params, hd);
	}
}