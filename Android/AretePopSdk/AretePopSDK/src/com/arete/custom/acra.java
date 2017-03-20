package com.arete.custom;

import android.app.Application;
import org.acra.*;
import org.acra.annotation.*;

@ReportsCrashes
(
	formKey = "", // will not be used
	mailTo = "jsyi@phychips.com",
	customReportContent ={
		ReportField.APP_VERSION_CODE, 
		ReportField.APP_VERSION_NAME, 
		ReportField.ANDROID_VERSION, 
		ReportField.PHONE_MODEL, 
		ReportField.CUSTOM_DATA, 
		ReportField.STACK_TRACE, 
		ReportField.LOGCAT }
)

public class acra extends Application 
{
	@Override
	public void onCreate() 
	{
		super.onCreate();
		// The following line triggers the initialization of ACRA
		ACRA.init(this);
	}
};
