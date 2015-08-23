package blood_cloud.com.osahub.app.bloodnew;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;

import blood_cloud.com.osahub.app.bloodnew.services.GCMNotificationIntentService;


public class GcmBroadcastReceiver extends WakefulBroadcastReceiver {
    ProgressDialog prgDialog;

	@Override
	public void onReceive(Context context, Intent intent) {
		ComponentName comp = new ComponentName(context.getPackageName(),
				GCMNotificationIntentService.class.getName());
		startWakefulService(context, (intent.setComponent(comp)));


		setResultCode(Activity.RESULT_OK);
	}

}
