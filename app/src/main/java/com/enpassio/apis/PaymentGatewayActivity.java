package com.enpassio.apis;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.paytm.pg.merchant.CheckSumServiceHelper;
import com.paytm.pgsdk.PaytmOrder;
import com.paytm.pgsdk.PaytmPGService;
import com.paytm.pgsdk.PaytmPaymentTransactionCallback;

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

    private void setupOrderFromAppToPaytmSdk(PaytmPGService Service) {
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
        String paytmChecksum = CheckSumServiceHelper.getCheckSumServiceHelper().genrateCheckSum(merchantKey, paytmParams);
        Service.initialize(Order, null);
        Service.startPaymentTransaction(this,
                true,
                true,
                new PaytmPaymentTransactionCallback() {
                    /*Call Backs*/
                    public void someUIErrorOccurred(String inErrorMessage) {
                        /*Display the error message as below */
                        Log.d("my_tag", "UI Error " + inErrorMessage);
                        Toast.makeText(getApplicationContext(),
                                "UI Error " + inErrorMessage,
                                Toast.LENGTH_LONG).show();

                    }

                    public void onTransactionResponse(Bundle inResponse) {
                        /*Display the message as below */
                        Log.d("my_tag", "Payment Transaction response " + inResponse.toString());
                        Toast.makeText(getApplicationContext(),
                                "Payment Transaction response " + inResponse.toString(),
                                Toast.LENGTH_LONG).show();
                    }

                    public void networkNotAvailable() {
                        /*Display the message as below */
                        Log.d("my_tag", "Network connection error: Check your internet connectivity");
                        Toast.makeText(getApplicationContext(),
                                "Network connection error: Check your internet connectivity",
                                Toast.LENGTH_LONG).show();

                    }

                    public void clientAuthenticationFailed(String inErrorMessage) {
                        /*Display the message as below */
                        Log.d("my_tag", "Authentication failed: Server error" + inErrorMessage.toString());
                        Toast.makeText(getApplicationContext(),
                                "Authentication failed: Server error" + inErrorMessage.toString(),
                                Toast.LENGTH_LONG).show();

                    }

                    public void onErrorLoadingWebPage(int iniErrorCode, String inErrorMessage, String inFailingUrl) {
                        /*Display the message as below */
                        Log.d("my_tag", "Unable to load webpage " + inErrorMessage.toString());
                        Toast.makeText(getApplicationContext(),
                                "Unable to load webpage " + inErrorMessage.toString(),
                                Toast.LENGTH_LONG).show();

                    }

                    public void onBackPressedCancelTransaction() {
                        /*Display the message as below */
                        Log.d("my_tag", "Transaction cancelled");
                        Toast.makeText(getApplicationContext(),
                                "Transaction cancelled",
                                Toast.LENGTH_LONG).show();

                    }

                    public void onTransactionCancel(String inErrorMessage, Bundle inResponse) {
                        /*Display the message as below */
                        Log.d("my_tag", "Transaction Cancelled" + inResponse.toString());
                        Toast.makeText(getApplicationContext(),
                                "Transaction Cancelled" + inResponse.toString(),
                                Toast.LENGTH_LONG).show();

                    }
                });
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
