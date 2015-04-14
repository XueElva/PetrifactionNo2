package net.lz.petrifaction.fragment;

import net.lz.petrifaction.activity.ActivityHome;
import net.lz.petrifaction.activity.DetailInfoActivity;
import net.lz.petrifaction.bean.MyConfig;
import net.lz.petrifaction.tool.CommonTools;
import net.lz.petrifaction.tool.UserInfoTools;
import net.lz.petrifaction.view.XListView;
import net.lz.petrifaction.view.XListView.IXListViewListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.readystatesoftware.viewbadger.BadgeView;

import cn.dh.resourclogin.R;
import cn.trinea.android.view.autoscrollviewpager.AutoScrollViewPager;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class HomeFragment extends Fragment implements IXListViewListener,
		OnClickListener {
	private LinearLayout mAdvertisements;
	private boolean mIsSupply = true; // 默认是供应列表
	private TextView mSupply, mDemand;
	private XListView mListView;
	private int page = 1;
	private JSONArray jsarrdata = new JSONArray();
	private Myadapter mAdapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_home, null);

		initView(view);
		getNews(false);
		return view;
	}

	private void initView(View view) {
		// TODO Auto-generated method stub
		mAdvertisements = (LinearLayout) view.findViewById(R.id.advertisement);
		getTP(view);
		mSupply = (TextView) view.findViewById(R.id.supply);
		mDemand = (TextView) view.findViewById(R.id.demand);

		mSupply.setOnClickListener(this);
		mDemand.setOnClickListener(this);

		setStyle(mSupply, true);
		setStyle(mDemand, false);

		mListView = (XListView) view.findViewById(R.id.xListView);
		mAdapter = new Myadapter();
		mListView.setAdapter(mAdapter);
		mListView.setPullLoadEnable(true);
		mListView.setPullRefreshEnable(true);
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

					intent.setClass(getActivity(), DetailInfoActivity.class);
					startActivity(intent);

				} catch (JSONException e) {
					e.printStackTrace();
				}

			}
		});
	}

	/**
	 * 设置背景及字体颜色
	 * 
	 * @param v
	 */
	private void setStyle(TextView textView, boolean checked) {
		if (checked) {
			textView.setBackgroundColor(getResources().getColor(
					R.color.dark_red));
			textView.setTextColor(getResources().getColor(R.color.white));
		} else {
			textView.setBackgroundColor(getResources().getColor(R.color.white));
			textView.setTextColor(getResources().getColor(R.color.dark_red));
		}

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.supply: // 供应列表
			mIsSupply = true;
			setStyle(mSupply, true);
			setStyle(mDemand, false);
			getNews(false);
			break;
		case R.id.demand:// 需求列表
			mIsSupply = false;
			setStyle(mSupply, false);
			setStyle(mDemand, true);
			getNews(false);
			break;
		default:
			break;
		}

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
			LayoutInflater inflater = LayoutInflater.from(getActivity());

			ViewHolder vh = null;
			if (convertView == null) {
				convertView = inflater.inflate(R.layout.info_item, null);
				vh = new ViewHolder();
				vh.companyName = (TextView) convertView
						.findViewById(R.id.companyName);
				vh.productName = (TextView) convertView
						.findViewById(R.id.productName);
				vh.price = (TextView) convertView.findViewById(R.id.price);
				vh.collect = (LinearLayout) convertView
						.findViewById(R.id.collect);
				vh.attention = (LinearLayout) convertView
						.findViewById(R.id.attention);
				vh.share = (LinearLayout) convertView.findViewById(R.id.share);
				convertView.setTag(vh);
			} else {
				vh = (ViewHolder) convertView.getTag();
			}

			try {
				vh.productName.setText(jsarrdata.getJSONObject(position)
						.getString("cpname"));
				String price = jsarrdata.getJSONObject(position)
						.getString("price").equals("0") ? "面议" : jsarrdata
						.getJSONObject(position).getString("price");
				vh.price.setText("价格: " + price);
				vh.companyName.setText(jsarrdata.getJSONObject(position)
						.getString("corpname"));
				
				vh.companyName.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						Intent intent=new Intent(getActivity(),ActivityHome.class);
						startActivity(intent);
					}
				});
			} catch (JSONException e) {
				e.printStackTrace();
			}

			return convertView;
		}
	}

	class ViewHolder {
		TextView companyName, productName, price;
		LinearLayout collect, attention, share;

	}

	public void getNews(final boolean isLoadMore) {
		AsyncHttpClient client = new AsyncHttpClient();
		com.loopj.android.http.RequestParams params = new com.loopj.android.http.RequestParams();
		params.put("accessToken", UserInfoTools.getUserSH(getActivity())
				.getString("accessToken", ""));
		params.put("pagesize", "10");
		params.put("LastId", "");
		params.put("keyword", "");
		params.put("keytype", "0");
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
				mListView.stopRefresh();
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
		if (mIsSupply) {

			client.get(MyConfig.IP + "sd/supply.do", params, hd);
		} else {
			client.get(MyConfig.IP + "sd/demand.do", params, hd);
		}
	}

	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub
		getNews(false);
	}

	@Override
	public void onLoadMore() {
		getNews(true);
	}

	JSONArray tparr = new JSONArray();

	/**
	 * 广告
	 * 
	 * @param view
	 */
	public void getTP(final View view) {
		AsyncHttpClient client = new AsyncHttpClient();
		com.loopj.android.http.RequestParams params = new com.loopj.android.http.RequestParams();
		AsyncHttpResponseHandler hd = new AsyncHttpResponseHandler() {
			@Override
			public void onStart() {
				// CommonTools.createLoadingDialog(getActivity(),
				// getResources().getString(R.string.loading)).show();
				super.onStart();
			}

			@Override
			public void onSuccess(int statusCode, String content) {
				// CommonTools.cancleDialog();
				try {
					tparr = new JSONArray(content);
				} catch (JSONException e) {
					e.printStackTrace();
				}
				// ViewPager
				AutoScrollViewPager pagerpic = initPicPager();
				if (pagerpic == null) {
					return;
				}
				pagerpic.setInterval(4000);
				pagerpic.setSlideBorderMode(AutoScrollViewPager.SLIDE_BORDER_MODE_NONE);
				pagerpic.startAutoScroll();
				pagerpic.setCycle(true);
				pagerpic.setStopScrollWhenTouch(true);
				pagerpic.setLayoutParams(new RelativeLayout.LayoutParams(
						CommonTools.getScreenInfo(getActivity()).widthPixels,
						CommonTools.getScreenInfo(getActivity()).widthPixels * 320 / 640));

				RelativeLayout rlcontainer = new RelativeLayout(getActivity());

				final LinearLayout pointLinear = new LinearLayout(getActivity());
				RelativeLayout.LayoutParams paramsll = new RelativeLayout.LayoutParams(
						RelativeLayout.LayoutParams.MATCH_PARENT,
						RelativeLayout.LayoutParams.WRAP_CONTENT);
				paramsll.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
				paramsll.setMargins(0, 0, 5, 0);
				pointLinear.setLayoutParams(paramsll);
				pointLinear.setPadding(0, 0, 5, 8);
				pointLinear.setGravity(Gravity.CENTER_HORIZONTAL);
				for (int i = 0; i < (tparr.length() < 3 ? 3 : tparr.length()); i++) {
					ImageView pointView = new ImageView(getActivity());
					if (i == 0) {
						pointView
								.setBackgroundResource(R.drawable.home_banner_icon_hover);
					} else
						pointView
								.setBackgroundResource(R.drawable.home_banner_icon);
					LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
							LinearLayout.LayoutParams.WRAP_CONTENT,
							LinearLayout.LayoutParams.WRAP_CONTENT);
					params.setMargins(10, 0, 5, 0);
					pointView.setLayoutParams(params);
					pointLinear.addView(pointView);
				}

				rlcontainer
						.setLayoutParams(new AbsListView.LayoutParams(
								CommonTools.getScreenInfo(getActivity()).widthPixels,
								CommonTools.getScreenInfo(getActivity()).widthPixels * 320 / 640));
				rlcontainer.addView(pagerpic);
				rlcontainer.addView(pointLinear);
				pagerpic.setOnPageChangeListener(new OnPageChangeListener() {

					@Override
					public void onPageSelected(int arg0) {

					}

					@Override
					public void onPageScrolled(int arg0, float arg1, int arg2) {
						View view = pointLinear.getChildAt(arg2);
						View curView = pointLinear.getChildAt(arg0);
						if (view != null && curView != null) {
							ImageView pointView = (ImageView) view;
							ImageView curPointView = (ImageView) curView;
							for (int i = 0; i < pointLinear.getChildCount(); i++) {
								pointLinear.getChildAt(i)
										.setBackgroundResource(
												R.drawable.home_banner_icon);
							}
							curPointView
									.setBackgroundResource(R.drawable.home_banner_icon_hover);
						}
					}

					@Override
					public void onPageScrollStateChanged(int arg0) {

					}
				});

				mAdvertisements.addView(rlcontainer);
				super.onSuccess(statusCode, content);

			}

			@Override
			public void onFailure(Throwable error, String content) {
				CommonTools.Log("error", content);
				// Toast.makeText(getActivity(),
				// getResources().getString(R.string.faile),
				// Toast.LENGTH_SHORT).show();
				super.onFailure(error, content);

			}
		};
		client.get("http://gl.oilchem.net/oilchem/newspageadv/queryPadByPpo/2",
				params, hd);
	}

	PagerAdapter pagerAdapteradv;
	AutoScrollViewPager picviewpager;
	int[] img = { R.drawable.de1, R.drawable.de2, R.drawable.de3 };

	public AutoScrollViewPager initPicPager() {
		pagerAdapteradv = new PagerAdapter() {

			@Override
			public int getCount() {
				if (tparr.length() < 3) {
					return 3;
				}
				return tparr.length();
			}

			@Override
			public int getItemPosition(Object object) {

				return super.getItemPosition(object);
			}

			@Override
			public Object instantiateItem(ViewGroup container, int position) {
				final int temp = position;
				ImageView iv = new ImageView(getActivity());
				iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
				container.addView(iv);
				try {
					UrlImageViewHelper
							.setUrlDrawable(iv, tparr.getJSONObject(temp)
									.getString("PAD_IMG_PATH"), R.drawable.de1);
				} catch (JSONException e) {
					iv.setImageResource(img[position]);
				}
				try {
					iv.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							Intent intent = new Intent();
							intent.setAction("android.intent.action.VIEW");
							Uri content_url;
							try {
								content_url = Uri.parse(tparr.getJSONObject(
										temp).getString("PAD_URL"));
								intent.setData(content_url);
								startActivity(intent);
							} catch (JSONException e) {
								e.printStackTrace();
							}

						}
					});
				} catch (Exception e) {
					// TODO: handle exception
				}

				return iv;
			}

			@Override
			public boolean isViewFromObject(View arg0, Object arg1) {
				return arg0 == arg1;
			}

			@Override
			public void destroyItem(View container, int position, Object object) {
				((ViewGroup) container).removeView((View) object);

				object = null;
			}

		};
		try {

			picviewpager = new AutoScrollViewPager(getActivity());
			picviewpager.setAdapter(pagerAdapteradv);
			return picviewpager;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}

}
