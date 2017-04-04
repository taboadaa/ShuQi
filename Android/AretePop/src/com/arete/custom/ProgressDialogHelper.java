package com.arete.custom;

import android.app.Activity;
import android.app.ProgressDialog;

public class ProgressDialogHelper
{
    private ProgressDialog progressDialog = null;    
    private static final ProgressDialogHelper mInstance = new ProgressDialogHelper();    
   
    private ProgressDialogHelper()
    {
	
    }

    public static ProgressDialogHelper getInstance()
    {
	return mInstance;
    }    
    
    synchronized public boolean isWorking()
    {
	if (progressDialog == null)
	    return false;
	
	return true;
    }
    
    synchronized public void showProgressDialog(Activity act, String title, String message)
    {
	if (title == null)
	{
	    title = "Waiting";
	}

	if (message == null)
	{
	    message = "Wait for process...";
	}

	if (progressDialog == null)
	{
	    progressDialog = new ProgressDialog(act);
	    progressDialog.setTitle(title);
	    progressDialog.setMessage(message);
	    progressDialog.setIndeterminate(true);
	    progressDialog.setCancelable(false);
	    progressDialog.setCanceledOnTouchOutside(false);
	    progressDialog.show();
	}
    }
    
    synchronized public void setMessage(String message)
    {
	if (progressDialog != null)
	{
	    progressDialog.setMessage(message);
	}
    }

    synchronized public void hideProgressDialog()
    {
	if (progressDialog != null)
	    progressDialog.dismiss();
	progressDialog = null;
    }
}
