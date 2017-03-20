package com.arete.custom;

import java.lang.ref.WeakReference;
import android.os.Handler;
import android.os.Message;

public class WeakRefHandler extends Handler{
	private final WeakReference<IOnHandlerMessage> mHandlerActivity;
	
	public WeakRefHandler(IOnHandlerMessage popSettingActivity){
		mHandlerActivity = new WeakReference<IOnHandlerMessage>(popSettingActivity);
	}

	@Override
	public void handleMessage(Message msg){
		super.handleMessage(msg);
		IOnHandlerMessage activity=(IOnHandlerMessage) mHandlerActivity.get();
		if(activity == null) return;
			activity.handlerMessage(msg);
	}
}
