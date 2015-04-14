package net.lz.petrifaction.fragment;

import net.lz.petrifaction.view.XListView;
import cn.dh.resourclogin.R;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

public class OrderListFragment extends Fragment {

private TextView mMyOrder,mReceiveOrder;
private XListView mMyOrderLV,mReceiveOrderLV;
private Drawable mRight,mDown;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view=inflater.inflate(R.layout.orderlist, null);
		
		initView(view);
		return view;
	}
	private void initView(View view) {
		mMyOrder=(TextView) view.findViewById(R.id.myOrder);
		mReceiveOrder=(TextView) view.findViewById(R.id.receiveOrder);
		mMyOrderLV=(XListView) view.findViewById(R.id.myOrderListView);
		mReceiveOrderLV=(XListView) view.findViewById(R.id.receiveOrderListView);
		
		mMyOrderLV.setVisibility(View.GONE);
		mReceiveOrderLV.setVisibility(View.GONE);
		mRight = getResources().getDrawable( R.drawable.arrow1); // / 这一步必须要做,否则不会显示
		mRight.setBounds(0, 0, mRight.getMinimumWidth(), mRight.getMinimumHeight()); 
		mDown = getResources().getDrawable( R.drawable.arrow2); // / 这一步必须要做,否则不会显示
		mDown.setBounds(0, 0, mDown.getMinimumWidth(), mDown.getMinimumHeight()); 
		mMyOrder.setCompoundDrawables(null, null, mRight, null);
		mReceiveOrder.setCompoundDrawables(null, null, mRight, null);
		
		mMyOrder.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(mMyOrderLV.getVisibility()==View.GONE){
					//显示
					mMyOrderLV.setVisibility(View.VISIBLE);
					mMyOrder.setCompoundDrawables(null, null, mDown, null);
				}else{
					//隐藏
					mMyOrderLV.setVisibility(View.GONE);
					mMyOrder.setCompoundDrawables(null, null, mRight, null);
				}
			}
		});
		mReceiveOrder.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(mReceiveOrderLV.getVisibility()==View.GONE){
					//显示
					mReceiveOrderLV.setVisibility(View.VISIBLE);
					mReceiveOrder.setCompoundDrawables(null, null, mDown, null);
				}else{
					//隐藏
					mReceiveOrderLV.setVisibility(View.GONE);
					mReceiveOrder.setCompoundDrawables(null, null, mRight, null);
				}
			}
		});
		
	}
}
