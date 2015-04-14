package net.lz.petrifaction.fragment;

import java.util.ArrayList;

import net.lz.petrifaction.activity.DetailInfoActivity;
import net.lz.petrifaction.activity.MainActivity;
import net.lz.petrifaction.bean.MyConfig;
import net.lz.petrifaction.tool.CommonTools;
import net.lz.petrifaction.tool.JsonReader;
import net.lz.petrifaction.tool.UserInfoTools;
import net.lz.petrifaction.view.XListView;
import net.lz.petrifaction.view.XListView.IXListViewListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cn.dh.resourclogin.R;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;

public class SearchFragment extends Fragment implements IXListViewListener {
	XListView mListView;
	Myadapter mAdapter;
	JSONArray jsarrdata = new JSONArray();
	int page = 1;
	EditText content;
	Button sousuo;
	private boolean isgongying=true;
	private LinearLayout mTemp;
	private TextView gongying, qiugou, title;
    
	// 高级搜索
	TextView advancedSearch;
	Boolean showSearchBar = false;
	LinearLayout searchBar;
	Spinner mAreaSpinner, mProvinceSpinner;
	TextView mSpecification; // 规格
	private ArrayList<String[]> areas; // 所有地区
	private ArrayList<String[]> provinces;// 对应省份
	ArrayAdapter<String> spinnerAreaAdapter, spinnerProvinceAdapter;

	private String mAreaId, mProvinceId, mSpecificationValue;

	

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.search, null);

		mTemp = (LinearLayout) view.findViewById(R.id.temp1);
		mListView = (XListView) view.findViewById(R.id.xListView);
		content = (EditText) view.findViewById(R.id.content);
		title = (TextView) view.findViewById(R.id.title);
        
		sousuo = (Button) view.findViewById(R.id.sousuo);
		sousuo.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mListView.setPullLoadEnable(true);
				jsarrdata = new JSONArray();
				page = 1;
				getNews(false);

			}
		});

		initSearch(view);
		mAdapter = new Myadapter();
		mListView.setAdapter(mAdapter);
		mListView.setPullLoadEnable(false);
		mListView.setPullRefreshEnable(false);
		mListView.setXListViewListener(this);
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Intent intent = new Intent();
				try {
					intent.putExtra("id", jsarrdata.getJSONObject(arg2 - 1)
							.getString("sdid"));
					intent.putExtra("tb", jsarrdata.getJSONObject(arg2 - 1)
							.getString("tb"));

					intent.setClass(getActivity(),
							DetailInfoActivity.class);
					startActivity(intent);

				} catch (JSONException e) {
					e.printStackTrace();
				}

			}
		});

		gongying = (TextView) view.findViewById(R.id.zuixinshangji);

		qiugou = (TextView) view.findViewById(R.id.mubiaokehu);

		gongying.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				showSearchBar=false;
				searchBar.setVisibility(View.GONE);
				qiugou.setBackgroundColor(getResources().getColor(
						android.R.color.white));
				qiugou.setTextColor(getResources().getColor(R.color.red));
				gongying.setBackgroundColor(getResources()
						.getColor(R.color.red));
				gongying.setTextColor(getResources().getColor(
						android.R.color.white));
				isgongying = true;
				page = 1;
				jsarrdata = new JSONArray();
				mAdapter.notifyDataSetChanged();
				mListView.setPullLoadEnable(true);
				getNews(false);
			}
		});
		qiugou.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				showSearchBar=false;
				searchBar.setVisibility(View.GONE);
				gongying.setBackgroundColor(getResources().getColor(
						android.R.color.white));
				gongying.setTextColor(getResources().getColor(R.color.red));
				qiugou.setBackgroundColor(getResources().getColor(R.color.red));
				qiugou.setTextColor(getResources().getColor(
						android.R.color.white));
				isgongying = false;
				page = 1;
				jsarrdata = new JSONArray();
				mAdapter.notifyDataSetChanged();
				mListView.setPullLoadEnable(true);
				getNews(false);
			}
		});


		return view;
	}

	// 初始化高级搜索
	private void initSearch(View view) {
		advancedSearch = (TextView) view.findViewById(R.id.advancedSearch);
		searchBar = (LinearLayout) view.findViewById(R.id.searchCriteria);
		mAreaSpinner = (Spinner) view.findViewById(R.id.area);
		mProvinceSpinner = (Spinner) view.findViewById(R.id.province);
		mSpecification = (TextView) view.findViewById(R.id.specification);
		searchBar.setVisibility(View.GONE);

		advancedSearch.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (showSearchBar) {

					searchBar.setVisibility(View.GONE);
				} else {
					searchBar.setVisibility(View.VISIBLE);
				}
				showSearchBar = !showSearchBar;
			}
		});

		areas = JsonReader.getAreas(getActivity());
		final String[] saleAreaArr = getNameList(areas);
		spinnerAreaAdapter = new ArrayAdapter<String>(getActivity(),
				R.layout.spinner_item_layout, saleAreaArr);

		mAreaSpinner.setAdapter(spinnerAreaAdapter);
		mAreaSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {

				mAreaId = areas.get(position)[0];
				provinces = JsonReader.getProvinces(mAreaId, getActivity());
				spinnerProvinceAdapter = new ArrayAdapter<String>(
						getActivity(), R.layout.spinner_item_layout,
						getNameList(provinces));

				mProvinceSpinner.setAdapter(spinnerProvinceAdapter);
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub
				mAreaId = null;
			}
		});

		mProvinceSpinner
				.setOnItemSelectedListener(new OnItemSelectedListener() {

					@Override
					public void onItemSelected(AdapterView<?> parent,
							View view, int position, long id) {
						mProvinceId = provinces.get(position)[0];
					}

					@Override
					public void onNothingSelected(AdapterView<?> parent) {
						// TODO Auto-generated method stub
						mProvinceId = null;
					}
				});

	}

	/**
	 * 获得地区或省份名称列表
	 * 
	 * @param list
	 * @return
	 */
	private String[] getNameList(ArrayList<String[]> list) {

		String[] areaName = new String[list.size()];
		for (int i = 0; i < list.size(); i++) {
			areaName[i] = list.get(i)[1];
		}

		return areaName;
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
			LayoutInflater inflater = LayoutInflater.from(getActivity());

			convertView = inflater.inflate(R.layout.search_iterms, null);

			TextView kind = (TextView) convertView.findViewById(R.id.kind);
			TextView price = (TextView) convertView.findViewById(R.id.price);
			TextView name = (TextView) convertView.findViewById(R.id.name);

			try {
				kind.setText(jsarrdata.getJSONObject(position).getString(
						"cpname"));
				price.setText(jsarrdata.getJSONObject(position)
						.getString("price").equals("0") ? "面议" : jsarrdata
						.getJSONObject(position).getString("price"));
				name.setText(jsarrdata.getJSONObject(position).getString(
						"corpname"));
			} catch (JSONException e) {
				e.printStackTrace();
			}

			return convertView;
		}
	}

	public void getNews(final boolean isLoadMore) {
		AsyncHttpClient client = new AsyncHttpClient();
		com.loopj.android.http.RequestParams params = new com.loopj.android.http.RequestParams();
		params.put("accessToken", UserInfoTools.getUserSH(getActivity())
				.getString("accessToken", ""));
		params.put("pagesize", "10");
		params.put("LastId", "");
		params.put("keyword", content.getText().toString());
		params.put("keytype", "0");

		if (showSearchBar) {
			// 高级搜索
			page = 1;
			if (mAreaId != null && !mAreaId.equals("0")) {

				params.put("dqid", mAreaId);
			}
			if (mProvinceId != null && !mProvinceId.equals("0")) {
				params.put("provid", mProvinceId);
			}
			mSpecificationValue=mSpecification.getText().toString();
        	if(!mSpecificationValue.equals("")){
				params.put("ggxh", mSpecificationValue);
			}
		}
		params.put("page", page + "");

		AsyncHttpResponseHandler hd = new AsyncHttpResponseHandler() {
			@Override
			public void onStart() {
				CommonTools.createLoadingDialog(getActivity(),
						getResources().getString(R.string.loading)).show();
				super.onStart();
			}

			@Override
			public void onSuccess(int statusCode, String content) {
				CommonTools.cancleDialog();
				if (CommonTools.CheckJson(true, content, getActivity())) {
					try {
						JSONArray jsarr = new JSONObject(content)
								.getJSONObject("data").getJSONArray("message");
						if (jsarr.length() == 0) {
							Toast.makeText(getActivity(), "没有匹配的内容", 1).show();
						}
						if (jsarr.length() < 10) {
							mListView.setPullLoadEnable(false);
						}
						if (!isLoadMore) {
							jsarrdata = new JSONArray();
						}
						jsarrdata = CommonTools.joinJSONArray(jsarrdata, jsarr);
						mAdapter.notifyDataSetChanged();
						page++;
					} catch (JSONException e) {
						e.printStackTrace();
					}
				} else {
					mListView.stopLoadMore();
				}
				;

				super.onSuccess(statusCode, content);
			}

			@Override
			public void onFailure(Throwable error, String content) {
				CommonTools.Log("error", content);
				Toast.makeText(getActivity(),
						getResources().getString(R.string.failed),
						Toast.LENGTH_SHORT).show();
				super.onFailure(error, content);

			}
		};
		if (isgongying) {

			client.get(MyConfig.IP + "sd/supply.do", params, hd);
		} else {
			client.get(MyConfig.IP + "sd/demand.do", params, hd);
		}
	}

	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onLoadMore() {
		getNews(true);
	}
}
