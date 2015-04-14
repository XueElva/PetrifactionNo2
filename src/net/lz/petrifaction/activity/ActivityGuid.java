package net.lz.petrifaction.activity;

import java.util.ArrayList;

import cn.dh.resourclogin.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.AnimationSet;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;


public class ActivityGuid extends Activity {
	ViewPager viewPager;
	ArrayList<View> viewList;
	View view1;
	View view2;
	View view3;
	RelativeLayout bg;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.guid);
		final ImageView lybottom = (ImageView) findViewById(R.id.lybottom);
		final Button start = (Button) findViewById(R.id.start);
		LayoutInflater lf = getLayoutInflater().from(this);
		view1 = lf.inflate(R.layout.lyll01, null);
		view2 = lf.inflate(R.layout.lyll01, null);
		view3 = lf.inflate(R.layout.lyll01, null);
		viewList = new ArrayList<View>();
		viewList.add(view1);
		viewList.add(view2);
		viewList.add(view3);
		viewPager = (ViewPager) findViewById(R.id.viewpager);
		viewPager.setAdapter(pagerAdapter);
		ImageView temp = (ImageView) viewList.get(0).findViewById(
				R.id.imageView);
		temp.setImageResource(R.drawable.guide_text_01);
		ImageView temp1 = (ImageView) viewList.get(1).findViewById(
				R.id.imageView);
		temp1.setImageResource(R.drawable.guide_text_02);
		ImageView temp2 = (ImageView) viewList.get(2).findViewById(
				R.id.imageView);
		temp2.setImageResource(R.drawable.guide_text_03);
		viewPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {
				switch (arg0) {
//				case 0:
//					lybottom.setImageResource(R.drawable.point_01);
//					break;
//				case 1:
//					lybottom.setImageResource(R.drawable.point_02);
//					break;
				case 2:
//					lybottom.setImageResource(R.drawable.point_03);
//					start.setVisibility(View.VISIBLE);
					break;

				default:
					break;
				}
				ImageView img = (ImageView) viewList.get(arg0).findViewById(
						R.id.imageView);
				AnimationSet animationSet = new AnimationSet(true);
				// 鍒涘缓锟�?涓狝lphaAnimation瀵硅薄锛堝弬鏁拌〃绀轰粠瀹屽叏涓嶏拷?锟芥槑鍒板畬鍏拷?锟芥槑锟�?
				AlphaAnimation alphaAnimation = new AlphaAnimation(0, 1);
				// 璁剧疆鍔ㄧ敾鎵ц鐨勬椂闂达紙鍗曚綅锛氭绉掞級
				alphaAnimation.setDuration(1000);
				// 灏咥lphaAnimation瀵硅薄娣诲姞鍒癆nimationSet褰撲腑
				animationSet.addAnimation(alphaAnimation);
				animationSet.setInterpolator(new AccelerateDecelerateInterpolator());
				// 浣跨敤ImageView鐨剆tartAnimation鏂规硶锟�?濮嬫墽琛屽姩锟�?
				img.startAnimation(animationSet);
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// lastvalue = arg2;
			}

			@Override
			public void onPageScrollStateChanged(int arg0) {

			}
		});
		start.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
			
			}
		});
		start.setVisibility(View.GONE);
		bg=(RelativeLayout)findViewById(R.id.bg);
//		bg.setLayoutParams(new RelativeLayout.LayoutParams(CommonTools.getScreenInfo(context).widthPixels*8, CommonTools.getScreenInfo(context).heightPixels*4));
	}

	PagerAdapter pagerAdapter = new PagerAdapter() {

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {

			return arg0 == arg1;
		}

		@Override
		public int getCount() {

			return viewList.size();
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView(viewList.get(position));

		}

		@Override
		public int getItemPosition(Object object) {

			return super.getItemPosition(object);
		}

		@Override
		public CharSequence getPageTitle(int position) {

			return "";
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			container.addView(viewList.get(position));
			if(position==viewList.size()-1){
				viewList.get(position).setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						Intent mainIntent = new Intent(ActivityGuid.this,
								ActivityLogin.class);
						ActivityGuid.this.startActivity(mainIntent);
						ActivityGuid.this.finish();
					}
				});
			}
			return viewList.get(position);
		}

	};
}