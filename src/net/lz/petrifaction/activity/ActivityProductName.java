package net.lz.petrifaction.activity;

import java.util.Timer;
import java.util.TimerTask;

import net.lz.petrifaction.bean.MyConfig;
import net.lz.petrifaction.tool.CommonTools;
import net.lz.petrifaction.view.XListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.PendingIntent.OnFinished;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import cn.dh.resourclogin.R;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

public class ActivityProductName extends Activity {
	XListView mListView;
	Myadapter mAdapter;
	JSONArray jsarrdata = new JSONArray();
	String productid = "0";
	String productname = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.productname);

		ImageButton back = (ImageButton) findViewById(R.id.back);
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});

		mListView = (XListView) findViewById(R.id.xListView);
		mAdapter = new Myadapter();
		mListView.setAdapter(mAdapter);
		mListView.setPullLoadEnable(false);
		mListView.setPullRefreshEnable(false);
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Intent intent = new Intent();
				try {
					productid = jsarrdata.getJSONObject(arg2 - 1).getString(
							"productid");
					productname = jsarrdata.getJSONObject(arg2 - 1).getString(
							"productname");
					finish();
				} catch (JSONException e) {
					e.printStackTrace();
				}

			}
		});
		final EditText name = (EditText) findViewById(R.id.qiyemingcheng);
		name.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {
				getNews(name.getText().toString());
			}
		});
		Timer timer = new Timer();

		timer.schedule(new TimerTask()

		{

			public void run()

			{

				InputMethodManager inputManager =

				(InputMethodManager) name.getContext().getSystemService(
						Context.INPUT_METHOD_SERVICE);

				inputManager.showSoftInput(name, 0);

			}

		},

		300);

	}

	@Override
	public void finish() {
		EditText name = (EditText) findViewById(R.id.qiyemingcheng);
		if (productname.length() > 0) {
			Intent intent = new Intent();
			intent.putExtra("name", productname);
			intent.putExtra("id", productid);
			setResult(30, intent);
		} else if (name.getText().toString().length() > 0) {
			Intent intent = new Intent();
			intent.putExtra("name", name.getText().toString());
			intent.putExtra("id", "0");
			setResult(30, intent);
		}
		super.finish();
	}

	private class Myadapter extends BaseAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return jsarrdata.length();
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup arg2) {
			final int temp = position;
			LayoutInflater inflater = getLayoutInflater();

			convertView = inflater.inflate(R.layout.chanpinname_iterms, null);
			TextView tv = (TextView) convertView.findViewById(R.id.mark);
			try {
				tv.setText(jsarrdata.getJSONObject(position).getString(
						"productname"));
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return convertView;
		}
	}

	public void getNews(final String str) {
		AsyncHttpClient client = new AsyncHttpClient();
		com.loopj.android.http.RequestParams params = new com.loopj.android.http.RequestParams();
		params.put("q", str);
		params.put("pagesize", 40 + "");

		AsyncHttpResponseHandler hd = new AsyncHttpResponseHandler() {
			@Override
			public void onStart() {
				super.onStart();
			}

			@Override
			public void onSuccess(int statusCode, String content) {
				CommonTools.Log("content", content);
				try {
					jsarrdata = new JSONArray(content);
					mAdapter.notifyDataSetChanged();
				} catch (JSONException e) {
					e.printStackTrace();
				}

				super.onSuccess(statusCode, content);
			}

			@Override
			public void onFailure(Throwable error, String content) {
				super.onFailure(error, content);

			}
		};
		client.get(MyConfig.IP + "tool/autoproduct.do", params, hd);
	}
}