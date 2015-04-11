package net.lz.petrifaction.tool;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cn.dh.resourclogin.R;
import android.app.Activity;
import android.app.Dialog;
import android.app.LocalActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class CommonTools {
	public static JSONArray joinJSONArray(JSONArray mData, JSONArray array) {
		if (mData.length() == 0) {
			mData = array;
			return array;
		}
		StringBuffer buffer = new StringBuffer();
		try {
			int len = mData.length();
			for (int i = 0; i < len; i++) {
				JSONObject obj1 = (JSONObject) mData.get(i);
				if (i == len - 1)
					buffer.append(obj1.toString());
				else
					buffer.append(obj1.toString()).append(",");
			}
			len = array.length();
			if (len > 0)
				buffer.append(",");
			for (int i = 0; i < len; i++) {
				JSONObject obj1 = (JSONObject) array.get(i);
				if (i == len - 1)
					buffer.append(obj1.toString());
				else
					buffer.append(obj1.toString()).append(",");
			}
			buffer.insert(0, "[").append("]");
			return new JSONArray(buffer.toString());
		} catch (Exception e) {
		}
		return null;
	}

	public static boolean CheckJson(boolean isneedtoshow, String content,
			Context context) {

		if (null!=content && content.contains("stat")) {
			JSONObject js = null;
			try {
				js = new JSONObject(content);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			try {
				if(js==null){
					return false;
				}
				if (js.getString("stat").equals("1")) {
					return true;
				} else {
					if(isneedtoshow){
						Toast.makeText(context, js.getString("error"),
								Toast.LENGTH_SHORT).show();
					}
					
					CommonTools.Log("error",
							"errcode:" + js.getString("errcode"));
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		} else {
			if (isneedtoshow)
				Toast.makeText(context, "网络连接失败，请检查网络连接", Toast.LENGTH_SHORT)
						.show();
		}
		return false;

	}

	public static DisplayMetrics getScreenInfo(Context context) {
		DisplayMetrics dm = new DisplayMetrics();
		((Activity) context).getWindowManager().getDefaultDisplay()
				.getMetrics(dm);
		return dm;
	}

	public static void SetFontSize(TextView tv, Context context) {
		// float sc= (float)
		// (context.getResources().getDisplayMetrics().scaledDensity/2.5);
		int width = context.getResources().getDisplayMetrics().widthPixels;
		if (width <= 480) {

			tv.setTextSize((px2sp(context, tv.getTextSize())) - 4);
		}
		if (width > 480 && width < 800) {

			tv.setTextSize((px2sp(context, tv.getTextSize())) - 2);
		}
		if (width >= 800 && width < 1000) {

			tv.setTextSize((px2sp(context, tv.getTextSize())) - 1);
		}
	}

	public static void SetFontSize(Button btn, Context context) {
		int width = context.getResources().getDisplayMetrics().widthPixels;
		if (width <= 480) {

			btn.setTextSize((px2sp(context, btn.getTextSize())) - 4);
		}
		if (width > 480 && width < 800) {

			btn.setTextSize((px2sp(context, btn.getTextSize())) - 2);
		}
		if (width >= 800 && width < 1000) {

			btn.setTextSize((px2sp(context, btn.getTextSize())) - 1);
		}
	}

	public static int px2sp(Context context, float pxValue) {
		final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
		Log.e("scaledDensity", fontScale + "");
		return (int) (pxValue / fontScale + 0.5f);
	}

	// 04-29 09:35:07.460: E/AndroidRuntime(3174): java.lang.RuntimeException:
	// Unable to start activity
	// ComponentInfo{com.labthink.code/com.labthink.code.MainActivity}:
	// java.lang.IllegalStateException: Activities can't be added until the
	// containing group has been created.

	@SuppressWarnings("deprecation")
	public static View activityToView(Bundle savedInstanceState,
			Context parent, Intent intent) {
		@SuppressWarnings("deprecation")
		LocalActivityManager mLocalActivityManager = new LocalActivityManager(
				(Activity) parent, true);
		mLocalActivityManager.dispatchCreate(savedInstanceState);

		final Window w = mLocalActivityManager.startActivity("TagName", intent);
		final View wd = w != null ? w.getDecorView() : null;
		if (wd != null) {
			wd.setVisibility(View.VISIBLE);
			wd.setFocusableInTouchMode(true);
			((ViewGroup) wd)
					.setDescendantFocusability(ViewGroup.FOCUS_AFTER_DESCENDANTS);
		}
		return wd;

	}

	public static void setPullLvHeight(ListView pull) {
		int totalHeight = 0;
		ListAdapter adapter = pull.getAdapter();
		for (int i = 0, len = adapter.getCount(); i < len; i++) { // listAdapter.getCount()返回数据项的数目
			View listItem = adapter.getView(i, null, pull);
			try {

				listItem.measure(0, 0); // 计算子项View 的宽�?
				totalHeight += listItem.getMeasuredHeight(); // 统计�?有子项的总高�?
			} catch (Exception e) {
				// TODO: handle exception
			}
		}

		ViewGroup.LayoutParams params = pull.getLayoutParams();
		params.height = totalHeight
				+ (pull.getDividerHeight() * (pull.getCount() - 1));
		pull.setLayoutParams(params);
	}

	/**
	 * 得到自定义的progressDialog
	 * 
	 * @param context
	 * @param msg
	 * @return
	 */
	public static Dialog LoadingDialog;

	public static Dialog createLoadingDialog(Context context, String msg) {

		LayoutInflater inflater = LayoutInflater.from(context);
		View v = inflater.inflate(R.layout.loading_dialog, null);// 得到加载view
		LinearLayout layout = (LinearLayout) v.findViewById(R.id.dialog_view);// 加载布局
		// main.xml中的ImageView
		ImageView spaceshipImage = (ImageView) v.findViewById(R.id.img);
		TextView tipTextView = (TextView) v.findViewById(R.id.tipTextView);// 提示文字
		// 加载动画
		Animation hyperspaceJumpAnimation = AnimationUtils.loadAnimation(
				context, R.anim.loading_animation);
		// 使用ImageView显示动画
		spaceshipImage.startAnimation(hyperspaceJumpAnimation);
		tipTextView.setText(msg);// 设置加载信息

		Dialog loadingDialog = new Dialog(context, R.style.loading_dialog);// 创建自定义样式dialog

		loadingDialog.setCancelable(true);// 不可以用“返回键”取�?
		loadingDialog.setContentView(layout, new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.FILL_PARENT,
				LinearLayout.LayoutParams.FILL_PARENT));// 设置布局
		LoadingDialog = loadingDialog;
		return loadingDialog;
	}

	public static void cancleDialog() {
		try {
			LoadingDialog.cancel();
		} catch (Exception e) {
		}
	}

	public static void showAlertDialog(Context context, String content) {

//		showAlertDialog(context, null, content, null, null, null, null);

//		try {
//
//			Dialog alertDialog = new AlertDialog.Builder(context)
//					.setTitle("温馨提示�?")
//					.setMessage(content)
//					.setIcon(R.drawable.abc_ic_go)
//					.setPositiveButton("确定",
//							new DialogInterface.OnClickListener() {
//
//								@Override
//								public void onClick(DialogInterface dialog,
//										int which) {
//									dialog.cancel();
//								}
//							}).create();
//			alertDialog.show();
//		} catch (Exception e) {
//		}
	}

	public static void cancleButtonDialog() {
		try {
			loadingbtnDialog.cancel();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	static Dialog loadingbtnDialog;

//	public static TextView showAlertDialog(Context context, String title,
//			String content, String btnok, String btncancle,
//			final AlertCallBack btnokcallback,
//			final AlertCallBack btncanclecallback) {
//		LayoutInflater inflater = LayoutInflater.from(context);
//		View v = inflater.inflate(R.layout.my_dialog, null);// 得到加载view
//		LinearLayout layout = (LinearLayout) v.findViewById(R.id.dialog_view);// 加载布局
//		// main.xml中的ImageView
//		// ImageView spaceshipImage = (ImageView) v.findViewById(R.id.img);
//		// TextView tipTextView = (TextView) v.findViewById(R.id.tipTextView);//
//		// 提示文字
//		// // 加载动画
//		// Animation hyperspaceJumpAnimation = AnimationUtils.loadAnimation(
//		// context, R.anim.loading_animation);
//		// // 使用ImageView显示动画
//		// spaceshipImage.startAnimation(hyperspaceJumpAnimation);
//		// // tipTextView.setText(msg);// 设置加载信息
//		TextView tipTextView = (TextView) v.findViewById(R.id.tipTextView);
//		TextView contenttb = (TextView) v.findViewById(R.id.content);
//		Button btnokbt = (Button) v.findViewById(R.id.btnok);
//		Button btncanclebt = (Button) v.findViewById(R.id.btncancle);
//		if (title == null) {
//			tipTextView.setText(context.getResources().getString(
//					R.string.wenxintishi));
//		} else {
//			tipTextView.setText(title);
//		}
//		contenttb.setText(content);
//		if (btnok == null) {
//			btnokbt.setText(context.getResources().getString(R.string.ok));
//			btncanclebt.setText(context.getResources().getString(
//					R.string.cancle));
//		}
//		if (btnok != null && btncancle == null) {
//			btnokbt.setText(btnok);
//			btncanclebt.setVisibility(View.GONE);
//		}
//		if (btnok == null && btncancle == null) {
//			btnokbt.setText(context.getResources().getString(R.string.ok));
//			btncanclebt.setVisibility(View.GONE);
//		}
//		if (btnok != null && btncancle != null) {
//			btnokbt.setText(btnok);
//			btncanclebt.setText(btncancle);
//		}
//
//		final Dialog loadingDialog = new Dialog(context,
//				R.style.loading_dialog1);// 创建自定义样式dialog
//		loadingbtnDialog = loadingDialog;
//		loadingDialog.setCancelable(false);// 不可以用“返回键”取�?
//		loadingDialog.setContentView(layout, new LinearLayout.LayoutParams(
//				LinearLayout.LayoutParams.FILL_PARENT,
//				LinearLayout.LayoutParams.FILL_PARENT));// 设置布局
//		btnokbt.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				if (btnokcallback != null) {
//
//					btnokcallback.callback();
//				}
//				loadingDialog.cancel();
//			}
//		});
//		btncanclebt.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				if (btncanclecallback != null) {
//
//					btncanclecallback.callback();
//				}
//
//				loadingDialog.cancel();
//			}
//		});
//
//		// LoadingDialog = loadingDialog;
//		// LoadingDialog.show();
//		try {
//
//			loadingDialog.show();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return contenttb;
//	}

	public static void Log(String tag, String content) {
		if (content == null) {
			return;
		}
//		Log.d(tag, content);
	}

	public static int dip2px(Context context, float dipValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dipValue * scale + 0.5f);
	}

	public static int px2dip(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}

	public static HashMap<String, String> getAppInfo(Context context) {
		HashMap<String, String> map = new HashMap<String, String>();
		try {
			String pkName = context.getPackageName();
			String versionName = context.getPackageManager().getPackageInfo(
					pkName, 0).versionName;
			int versionCode = context.getPackageManager().getPackageInfo(
					pkName, 0).versionCode;
			map.put("name", versionName);
			map.put("code", versionCode + "");
		} catch (Exception e) {
		}
		return map;
	}

	// public static void addShortcut(Context context) {
	// try {
	// Intent shortcut = new Intent(
	// "com.android.launcher.action.INSTALL_SHORTCUT");
	// // 不允许重�?
	// shortcut.putExtra("duplicate", false);
	// // 设置名字
	// shortcut.putExtra(Intent.EXTRA_SHORTCUT_NAME, context
	// .getResources().getString(R.string.app_name));
	// // 设置图标
	// shortcut.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE,
	// Intent.ShortcutIconResource.fromContext(context,
	// R.drawable.a055));
	// // 设置意图和快捷方式关联程�?
	// shortcut.putExtra(Intent.EXTRA_SHORTCUT_INTENT, new Intent(context,
	// context.getClass()).setAction(Intent.ACTION_MAIN));
	// // 发�?�广�?
	// context.sendBroadcast(shortcut);
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	//
	// }

	public static void ShowToast(Context context, String str) {
		Toast.makeText(context, str, Toast.LENGTH_LONG).show();
	}

	// 判断email格式是否正确
	public static boolean isEmail(String email) {
		String str = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
		Pattern p = Pattern.compile(str);
		Matcher m = p.matcher(email);
		return m.matches();
	}

	public static String getTelePhone(Context context) {
		SharedPreferences sharedPreferences = context.getSharedPreferences(
				"phonenumber", Activity.MODE_PRIVATE);
		return sharedPreferences.getString("number", "");
	}

	public static void SaveTelePhone(Context context, String number) {
		SharedPreferences sharedPreferences = context.getSharedPreferences(
				"phonenumber", Activity.MODE_PRIVATE);
		sharedPreferences.edit().putString("number", number).commit();
	}
}
