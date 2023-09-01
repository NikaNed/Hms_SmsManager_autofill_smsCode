package com.example.huaweiproject

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.huawei.hms.common.api.CommonStatusCodes
import com.huawei.hms.support.api.client.Status
import com.huawei.hms.support.sms.common.ReadSmsConstant
import com.huawei.hms.support.sms.common.ReadSmsConstant.READ_SMS_BROADCAST_ACTION

class SmsBroadcastReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        Log.d("LOL", "${intent?.action}")
        val bundle = intent!!.extras
        if (READ_SMS_BROADCAST_ACTION == intent.action) {
            if (bundle != null) {
                val status: Status? = bundle.getParcelable(ReadSmsConstant.EXTRA_STATUS)
                if (status?.statusCode == CommonStatusCodes.TIMEOUT) {
                } else if (status?.statusCode == CommonStatusCodes.SUCCESS) {
                    if (bundle.containsKey(ReadSmsConstant.EXTRA_SMS_MESSAGE)) {
                        bundle.getString(ReadSmsConstant.EXTRA_SMS_MESSAGE)?.let {
                            Log.d("LOL", it)
                            val local = Intent()
                            local.action = "service.to.activity.transfer"
                            local.putExtra("sms", it)
                            context!!.sendBroadcast(local)
                        }
                    }
                }
            }
        }
    }
}