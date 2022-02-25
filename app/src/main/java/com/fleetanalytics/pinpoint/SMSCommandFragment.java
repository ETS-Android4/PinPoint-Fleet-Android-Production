package com.fleetanalytics.pinpoint;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.telephony.gsm.SmsManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

public class SMSCommandFragment extends Fragment {

	private static View view;

	EditText etPhNo, etTimeInterval;
	Spinner spOt1, spOt2, spx;
	Button btnSendSMS;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		if (view != null) {
			ViewGroup parent = (ViewGroup) view.getParent();
			if (parent != null) {
				parent.removeView(view);
			}
		}
		view = inflater
				.inflate(R.layout.fragment_sms_command, container, false);
		init(view);

		btnSendSMS.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {

				try{
				String sPhNo = etPhNo.getText().toString();
				int iTime = Integer.parseInt(etTimeInterval.getText()
						.toString());
				int iOt1 = spOt1.getSelectedItemPosition();
				int iOt2 = spOt2.getSelectedItemPosition();
				int iX = spx.getSelectedItemPosition();
				if (!sPhNo.equals(null)) {
					
					//-------
					int count = 0;
					for (int i = 0; i < sPhNo.length(); i++) {
					    if (Character.isDigit(sPhNo.charAt(i))) {
					        count++;  
					    }
					}
					//------------
					if(count <=10){
					if (iTime == 0 || (iTime > 5 && iTime <= 1440)) {
						// sendSMS
						String message = "+XT:7005,"+iOt1+","+(iX + 1)+"[,"+iOt2+
								","+iTime+"]";
						sendSMSMethod(message, sPhNo);
						
						etPhNo.setText("");
						etTimeInterval.setText("");
						
					} else {
						ToastOnUI(getResources().getString(
								R.string.min_should_gre_five));
					}
					}else{
						ToastOnUI(getResources().getString(
								R.string.invalid_above_data));
					}
				} else {
					ToastOnUI(getResources().getString(
							R.string.enter_abv_data_first));
				}
				}catch(Exception e){
					ToastOnUI(getResources().getString(
							R.string.enter_abv_data_first));
				}
			}
		});

		return view;
	}

	private void init(View v) {

		etPhNo = (EditText) v.findViewById(R.id.etPhNo);
		etTimeInterval = (EditText) v.findViewById(R.id.etTimeInterval);
		spOt1 = (Spinner) v.findViewById(R.id.spSetOutputPort);
		spOt2 = (Spinner) v.findViewById(R.id.spSetOutputPortAfterExpired);
		spx = (Spinner) v.findViewById(R.id.spResponseVia);
		btnSendSMS = (Button) v.findViewById(R.id.btnSendSMS);
	}

	protected void ToastOnUI(final String text) {
		getActivity().runOnUiThread(new Runnable() {
			@Override
			public void run() {
				new Utils().showToast(getActivity(), text);
			}
		});
	}
	
	public void sendSMSMethod(String message,String phNo) {
		SmsManager sms = SmsManager.getDefault();
		sms.sendTextMessage(phNo, null, message,null, null);
		
		//ToastOnUI(phNo+"\n\n"+message);
		ToastOnUI(getResources().getString(R.string.success));
	}
}
