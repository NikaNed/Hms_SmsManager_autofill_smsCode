package com.example.huaweiproject

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.telephony.SmsManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.huaweiproject.databinding.ActivityMainBinding
import com.huawei.hms.support.sms.ReadSmsManager
import com.huawei.hms.support.sms.common.ReadSmsConstant.READ_SMS_BROADCAST_ACTION
import kotlin.random.Random


class MainActivity : AppCompatActivity() {
    private lateinit var mainActivityViewModel: MainActivityViewModel
    private lateinit var binding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainActivityViewModel = ViewModelProvider(this)[MainActivityViewModel::class.java]
        binding = DataBindingUtil.setContentView(
            this,
            R.layout.activity_main
        )
        binding.viewModel = mainActivityViewModel
        binding.lifecycleOwner = this@MainActivity


        binding.appCompatButton.setOnClickListener {
            generateSmsCode()
        }

    }



    private fun initSmsManager() {
        val task = ReadSmsManager.start(this@MainActivity)
        task.addOnCompleteListener {
            if (task.isSuccessful) {
                Toast.makeText(this, "Verification code was sent successfully", Toast.LENGTH_LONG)
                    .show()
            } else
                Toast.makeText(this, "The service failed to be enabled.", Toast.LENGTH_LONG).show()
        }
    }

    private fun generateSmsCode() {
        initSmsManager()
        registerSmsBroadcastReceiver()
        sendSms()
        registerOtpBroadcastReceiver()
    }


    private fun registerSmsBroadcastReceiver() {
        val intentFilter = IntentFilter(READ_SMS_BROADCAST_ACTION)
        registerReceiver(SmsBroadcastReceiver(), intentFilter)
    }

    private fun sendSms() {

        val hashValue = HashManager().getHashValue(applicationContext)
        val otp = Random.nextInt(
            100000,
            999999
        ).toString()

            val number = binding.appCompatEditText.text.toString()
            val msg = "[#] Your verification code is $otp $hashValue"
            if (!number.isNullOrEmpty()) {
                val smsManager = SmsManager.getDefault()
                smsManager.sendTextMessage(number, null, msg, null, null)
                Toast.makeText(applicationContext, "Message Sent", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(applicationContext, "Some fields is Empty", Toast.LENGTH_LONG)
                    .show()
            }
    }

    private fun registerOtpBroadcastReceiver() {
        val filter = IntentFilter()
        filter.addAction("service.to.activity.transfer")
        val updateUIReceiver = object : BroadcastReceiver() {
            override fun onReceive(
                context: Context,
                intent: Intent
            ) {
                intent.getStringExtra("sms")?.let {
                    mainActivityViewModel.otp.value = "Otp : " + it.split(" ")[4]
                }
            }
        }
        registerReceiver(updateUIReceiver, filter)
    }
}




//class MainActivity : AppCompatActivity() {
//
//
//    private lateinit var binding: ActivityMain2Binding
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        binding = ActivityMain2Binding.inflate(layoutInflater)
//        setContentView(binding.root)
//
//        binding.button.setOnClickListener(View.OnClickListener {
//            val number = binding.editText.text.toString()
//            val msg = binding.editText2.text.toString()
//            try {
//                val smsManager = SmsManager.getDefault()
//                smsManager.sendTextMessage(number, null, msg, null, null)
//                Toast.makeText(applicationContext, "Message Sent", Toast.LENGTH_LONG).show()
//            } catch (e: Exception) {
//                Toast.makeText(applicationContext, "Some fields is Empty", Toast.LENGTH_LONG)
//                    .show()
//            }
//        })
//    }
//}


//    override fun onRequestPermissionsResult(
//        requestCode: Int,
//        permissions: Array<String>,
//        grantResults: IntArray
//    ) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
//        if (requestCode == SMS_PERMISSION_REQUEST_CODE) {
//            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                generateSmsCode()
//            } else {
//                Toast.makeText(this, "PERMISSION NOT GRANTED", Toast.LENGTH_LONG).show()
//            }
//        }
//    }

//    private fun observe() {
//        mainActivityViewModel.clickObserver.observe(this, Observer {
//            when (it) {
//                "generate_code" -> {
//                    val permissionCheck =
//                        ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS)
//                    if (permissionCheck == PackageManager.PERMISSION_GRANTED)
//                        generateSmsCode()
//                    else
//                        ActivityCompat.requestPermissions(
//                            this,
//                            arrayOf(Manifest.permission.SEND_SMS), 111
//                        )
//                }
//            }
//        })
//    }

//    override fun onRequestPermissionsResult(
//        requestCode: Int,
//        permissions: Array<out String>,
//        grantResults: IntArray
//    ) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
//        when (requestCode) {
//            111 -> {
//                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)
//                    generateSmsCode()
//                else
//                    Toast.makeText(
//                        this,
//                        "Permissions has been refused",
//                        Toast.LENGTH_LONG
//                    ).show()
//            }
//        }
//    }