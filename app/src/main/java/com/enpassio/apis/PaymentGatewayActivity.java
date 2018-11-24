package com.enpassio.apis;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

import com.paytm.pgsdk.PaytmOrder;
import com.paytm.pgsdk.PaytmPGService;

import java.util.HashMap;
import java.util.Map;

public class PaymentGatewayActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_gateway);
        readSmsFromUsersDeviceForOtpVerification();

        PaytmPGService Service = PaytmPGService.getStagingService();
        setupOrderFromAppToPaytmSdk(Service);
    }

    private void setupOrderFromAppToPaytmSdk(PaytmPGService service) {
        Map<String, String> paramMap = new HashMap<String, String>();
        paramMap.put("MID", "rxazcv89315285244163");
        // Key in your staging and production MID available in your dashboard
        paramMap.put("ORDER_ID", "order1");
        paramMap.put("CUST_ID", "cust123");
        paramMap.put("MOBILE_NO", "7777777777");
        paramMap.put("EMAIL", "username@emailprovider.com");
        paramMap.put("CHANNEL_ID", "WAP");
        paramMap.put("TXN_AMOUNT", "100.12");
        paramMap.put("WEBSITE", "WEBSTAGING");
        // This is the staging value. Production value is available in your dashboard
        paramMap.put("INDUSTRY_TYPE_ID", "Retail");
        // This is the staging value. Production value is available in your dashboard
        paramMap.put("CALLBACK_URL", "https://securegw-stage.paytm.in/theia/paytmCallback?ORDER_ID=order1");
        paramMap.put("CHECKSUMHASH",
                "w2QDRMgp1234567JEAPCIOmNgQvsi+BhpqijfM9KvFfRiPmGSt3Ddzw+oTaGCLneJwxFFq5mqTMwJXdQE2EzK4px2xruDqKZjHupz9yXev4=");
        PaytmOrder Order = new PaytmOrder((HashMap<String, String>) paramMap);
//        String paytmChecksum = CheckSumServiceHelper.getCheckSumServiceHelper().genrateCheckSum(merchantKey, paytmParams);
    }

    private void readSmsFromUsersDeviceForOtpVerification() {
        if (ContextCompat.checkSelfPermission(PaymentGatewayActivity.this,
                Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(PaymentGatewayActivity.this,
                    new String[]{Manifest.permission.READ_SMS, Manifest.permission.RECEIVE_SMS},
                    101);
        }
    }

}
