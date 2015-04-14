package net.lz.petrifaction.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.TextView;
import cn.dh.resourclogin.R;

public class MessageFragment extends Fragment implements OnClickListener {

	private ImageButton mSet;
	private TextView mMessage, mOrder;
	private MessageListFragment mMessageListFragment;
	private OrderListFragment mOrderListFragment;
	private boolean isMessage = true;
	private FragmentManager mManager;
	private FragmentTransaction mTransaction;
	private PopupWindow window;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_message, null);

		init(view);
		return view;
	}

	private void init(View view) {
		mManager = getFragmentManager();
		mMessage = (TextView) view.findViewById(R.id.message);
		mOrder = (TextView) view.findViewById(R.id.order);
		mMessage.setOnClickListener(this);
		mOrder.setOnClickListener(this);

		setStyle(mMessage, true);
		setStyle(mOrder, false);

		mMessageListFragment = new MessageListFragment();
		mOrderListFragment = new OrderListFragment();

		mTransaction = mManager.beginTransaction();
		mTransaction.replace(R.id.content1, mMessageListFragment);
		mTransaction.commit();

		// 菜单
		LayoutInflater inflater = LayoutInflater.from(getActivity());
		View viewWindow = inflater.inflate(R.layout.message_set, null);
		window = new PopupWindow(getActivity());
		window.setContentView(viewWindow);
		window.setWidth(250);
		window.setHeight(LayoutParams.WRAP_CONTENT);
		window.setOutsideTouchable(true);
		window.setFocusable(true);

		mSet = (ImageButton) view.findViewById(R.id.set);
		mSet.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				window.showAsDropDown(mSet);
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
		case R.id.message:// 消息
			isMessage = true;
			setStyle(mMessage, true);
			setStyle(mOrder, false);
			mTransaction = mManager.beginTransaction();
			mTransaction.replace(R.id.content1, mMessageListFragment);
			mTransaction.commit();
			break;
		case R.id.order: // 订单
			isMessage = false;
			setStyle(mMessage, false);
			setStyle(mOrder, true);
			mTransaction = mManager.beginTransaction();
			mTransaction.replace(R.id.content1, mOrderListFragment);
			mTransaction.commit();
			break;

		default:
			break;
		}

	}
}
