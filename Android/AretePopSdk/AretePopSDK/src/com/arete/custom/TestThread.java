package com.arete.custom;

import java.util.Calendar;

//import org.acra.ACRA;
import android.os.Build;
import android.os.Handler;
import android.os.Message;

import com.phychips.rcp.RcpApi;
import com.phychips.rcp.RcpException;
import com.phychips.rcp.iRcpEvent;
import com.phychips.utility.Logger;

public class TestThread extends Thread implements iRcpEvent 
{

	private int ferCount = 0;
	StringBuffer sb;
	Handler progressHandler;

	public TestThread(Handler progHandler, StringBuffer sb) 
	{
		// TODO Auto-generated constructor stub
		RcpApi.setRcpEvent(this);
		this.progressHandler = progHandler;
		this.sb = sb;
	}

	private void setProgress(int percent)
	{
		Message msg = progressHandler.obtainMessage();
		msg.what = 7;
		msg.arg1 = percent;
		msg.arg2 = 0;
		msg.obj = null;								
		progressHandler.sendMessage(msg);
	}
	
	@Override
	public void run() 
	{		
		int[] threshold = { 500, 1000, 5000, 10000, 20000};
		
		int ferMax = 0;
		int ferMaxThreshold = threshold[0];	
		boolean ferMaxFiler = true;
		StringBuilder sb = new StringBuilder();
		
		try 
		{
			try
			{
			    RcpApi.setBeep(true);
			}
			catch (RcpException e)
			{
			    // TODO Auto-generated catch block
			    e.printStackTrace();
			}
			Thread.sleep(500);

			int progCount = 0;

			CaptureStart("FER");
			RcpApi.audioIo.setFilter(true);			
			for(int i = 0; i < threshold.length; i++)
			{
				ferCount = 0;
				RcpApi.audioIo.setThreshold(threshold[i]);

				try
				{
				    RcpApi.FERtest();
				}
				catch (RcpException e)
				{
				    // TODO Auto-generated catch block
				    e.printStackTrace();
				}				
				Thread.sleep(40000);
				m_log.write("Filtering," + threshold[i] + "," + this.ferCount + "\n");	
				sb.append("Filtering," + threshold[i] + "," + this.ferCount + "\n");				
				
				progCount = progCount + 5;				
				setProgress(progCount);
				
				if(ferMax < this.ferCount)
				{
					ferMax = this.ferCount;
					ferMaxThreshold = threshold[i];
					ferMaxFiler = true;
				}
			}	

			RcpApi.audioIo.setFilter(false);			
			for(int i = 0; i < threshold.length; i++)
			{
				ferCount = 0;
				RcpApi.audioIo.setThreshold(threshold[i]);

				try
				{
				    RcpApi.FERtest();
				}
				catch (RcpException e)
				{
				    // TODO Auto-generated catch block
				    e.printStackTrace();
				}				
				Thread.sleep(40000);
				m_log.write("Non-filtering," + threshold[i] + "," + this.ferCount + "\n");
				sb.append("Non-filtering," + threshold[i] + "," + this.ferCount + "\n");
												
				if(ferMax < this.ferCount)
				{
					ferMax = this.ferCount;
					ferMaxThreshold = threshold[i];
					ferMaxFiler = false;
				}
				
				progCount = progCount + 5;				
				setProgress(progCount);
			}
			
			
			m_log.write("ferMax," + ferMax + "\n");
			m_log.write("ferMaxThreshold," + ferMaxThreshold + "\n");
			m_log.write("ferMaxFiler," + ferMaxFiler + "\n");			
			
			sb.append("ferMax," + ferMax + "\n");
			sb.append("ferMaxThreshold," + ferMaxThreshold + "\n");
			sb.append("ferMaxFiler," + ferMaxFiler + "\n");
			
						
			CaptureStop();						
			
			for(int i = 0; i < threshold.length; i++)
			{
				RcpApi.audioIo.setThreshold(threshold[i]);
				RcpApi.audioIo.startCaptureWave("LOOPBACK_" + threshold[i]);
				try
				{
				    RcpApi.loopBackTest();
				}
				catch (RcpException e)
				{
				    // TODO Auto-generated catch block
				    e.printStackTrace();
				}
				Thread.sleep(2000);				
				RcpApi.audioIo.stopCaptureWave();
				
				progCount = progCount + 4;				
				setProgress(progCount);
			}


			
			byte[] period = new byte[] { 2, 4, 6, 8, 10, 12, 14, 16, 18, 20,
					22, 24, 26, 28, 30, 32, 34, 36, 40, 44, 48, 54, 62, 72, 88,
					110};
			
			RcpApi.audioIo.setThreshold(ferMaxThreshold);			
			for (int i = 0; i < period.length; i++) 
			{	
				RcpApi.audioIo.startCaptureWave("TONE_"
						+ Integer.toString((int)period[i] + 1000).substring(1));

				try
				{
				    RcpApi.waveTest(period[i]);
				}
				catch (RcpException e)
				{
				    // TODO Auto-generated catch block
				    e.printStackTrace();
				}
				Thread.sleep(3000);
				progCount = progCount + 1;				
				setProgress(progCount);				
			}
			
			setProgress(100);
			progressHandler.sendEmptyMessage(0);			
			//ACRA.getErrorReporter().putCustomData("FER", sb.toString());
			//ACRA.getErrorReporter().handleSilentException(null);
			
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	Logger m_log = null;
	public void CaptureStart(String fileName) 
	{
		long time = System.currentTimeMillis();
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(time);

		String f = fileName + "_"
				+ Build.MODEL
				+ "_"
				+ Integer.toString(c.get(Calendar.YEAR))
				+ Integer.toString(c.get(Calendar.MONTH) + 101).substring(1)
				+ Integer.toString(c.get(Calendar.DATE) + 100).substring(1)
				+ Integer.toString(c.get(Calendar.HOUR_OF_DAY) + 100)
				.substring(1)
				+ Integer.toString(c.get(Calendar.MINUTE) + 100).substring(1)
				+ Integer.toString(c.get(Calendar.SECOND) + 100).substring(1)
				+ ".csv";

		m_log = new Logger();
		m_log.initFile("AudioTest",f);		
	}		

	public void CaptureStop()
	{
		m_log.releaseFile();
		m_log = null;
	}



	@Override
	public void onTagReceived(int[] data) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onReaderInfoReceived(int[] data) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onRegionReceived(int[] data) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onSelectParamReceived(int[] data) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onQueryParamReceived(int[] data) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onChannelReceived(int[] data) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onFhLbtReceived(int[] data) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onTxPowerLevelReceived(int[] data) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onTagMemoryReceived(int[] data) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onHoppingTableReceived(int[] data) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onModulationParamReceived(int[] data) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onAnticolParamReceived(int[] data) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onTempReceived(int[] data) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onRssiReceived(int[] data) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onRegistryItemReceived(int[] data) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onResetReceived(int[] data) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onSuccessReceived(int[] data) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onFailureReceived(int[] data) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onAuthenticat(int[] dest) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onBeepStateReceived(int[] dest) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onTestFerPacketReceived(int[] dest) {
		// TODO Auto-generated method stub
		ferCount++;
	}

	@Override
	public void onAdcReceived(int[] dest)
	{
	    // TODO Auto-generated method stub
	    
	}

}