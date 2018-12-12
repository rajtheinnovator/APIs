package com.enpassio.apis;

import android.support.v7.app.AppCompatActivity;

public class PaymentGatewayActivity extends AppCompatActivity {
/*
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_gateway);
        readSmsFromUsersDeviceForOtpVerification();

 //       PaytmPGService Service = PaytmPGService.getStagingService();
        //step 1 and 2: Collect transaction info and generate order details
        //generate checksum
        String checkSum=  generateCheckSumForPaytm();
        Log.d("my_tag", "checksum is: "+checkSum);
//        PaytmOrder paytmOrder = setupOrderFromAppToPaytmSdk(checkSum);
//        Service.initialize(paytmOrder, null);
//        setupPaytmCallback(Service);
    }

    private String generateCheckSumForPaytm() {
        String paytmChecksum = null;
        String merchantMid = "rxazcv89315285244163";
        // Key in your staging and production MID available in your dashboard
        String merchantKey = "gKpu7IKaLSbkchFS";
        // Key in your staging and production MID available in your dashboard
        String orderId = "order1";
        String channelId = "WAP";
        String custId = "cust123";
        String mobileNo = "7777777777";
        String email = "username@emailprovider.com";
        String txnAmount = "100.12";
        String website = "WEBSTAGING";
        // This is the staging value. Production value is available in your dashboard
        String industryTypeId = "Retail";
        // This is the staging value. Production value is available in your dashboard
        String callbackUrl = "https://securegw-stage.paytm.in/theia/paytmCallback?ORDER_ID=order1";
        TreeMap<String, String> paytmParams = new TreeMap<String, String>();
        paytmParams.put("MID", merchantMid);
        paytmParams.put("ORDER_ID", orderId);
        paytmParams.put("CHANNEL_ID", channelId);
        paytmParams.put("CUST_ID", custId);
        paytmParams.put("MOBILE_NO", mobileNo);
        paytmParams.put("EMAIL", email);
        paytmParams.put("TXN_AMOUNT", txnAmount);
        paytmParams.put("WEBSITE", website);
        paytmParams.put("INDUSTRY_TYPE_ID", industryTypeId);
        paytmParams.put("CALLBACK_URL", callbackUrl);
        try {
            paytmChecksum = CheckSumServiceHelper.getCheckSumServiceHelper().genrateCheckSum(merchantKey, paytmParams);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return paytmChecksum;
    }

//    private void setupPaytmCallback(PaytmPGService service) {
//        service.startPaymentTransaction(this,
//                true,
//                true,
//                new PaytmPaymentTransactionCallback() {
//                    /*Call Backs*/
//                    public void someUIErrorOccurred(String inErrorMessage) {
//                        /*Display the error message as below */
//                        Log.d("my_tag", "UI Error " + inErrorMessage);
//                        Toast.makeText(getApplicationContext(),
//                                "UI Error " + inErrorMessage,
//                                Toast.LENGTH_LONG).show();
//
//                    }
//
//                    public void onTransactionResponse(Bundle inResponse) {
//                        /*Display the message as below */
//                        Log.d("my_tag", "Payment Transaction response " + inResponse.toString());
//                        Toast.makeText(getApplicationContext(),
//                                "Payment Transaction response " + inResponse.toString(),
//                                Toast.LENGTH_LONG).show();
//                    }
//
//                    public void networkNotAvailable() {
//                        /*Display the message as below */
//                        Log.d("my_tag", "Network connection error: Check your internet connectivity");
//                        Toast.makeText(getApplicationContext(),
//                                "Network connection error: Check your internet connectivity",
//                                Toast.LENGTH_LONG).show();
//
//                    }
//
//                    public void clientAuthenticationFailed(String inErrorMessage) {
//                        /*Display the message as below */
//                        Log.d("my_tag", "Authentication failed: Server error" + inErrorMessage.toString());
//                        Toast.makeText(getApplicationContext(),
//                                "Authentication failed: Server error" + inErrorMessage.toString(),
//                                Toast.LENGTH_LONG).show();
//
//                    }
//
//                    public void onErrorLoadingWebPage(int iniErrorCode, String inErrorMessage, String inFailingUrl) {
//                        /*Display the message as below */
//                        Log.d("my_tag", "Unable to load webpage " + inErrorMessage.toString());
//                        Toast.makeText(getApplicationContext(),
//                                "Unable to load webpage " + inErrorMessage.toString(),
//                                Toast.LENGTH_LONG).show();
//
//                    }
//
//                    public void onBackPressedCancelTransaction() {
//                        /*Display the message as below */
//                        Log.d("my_tag", "Transaction cancelled");
//                        Toast.makeText(getApplicationContext(),
//                                "Transaction cancelled",
//                                Toast.LENGTH_LONG).show();
//
//                    }
//
//                    public void onTransactionCancel(String inErrorMessage, Bundle inResponse) {
//                        /*Display the message as below */
//                        Log.d("my_tag", "Transaction Cancelled" + inResponse.toString());
//                        Toast.makeText(getApplicationContext(),
//                                "Transaction Cancelled" + inResponse.toString(),
//                                Toast.LENGTH_LONG).show();
//
//                    }
//                });
//    }
//
//    private PaytmOrder setupOrderFromAppToPaytmSdk(String checkSum) {
//
//        Map<String, String> paramMap = new HashMap<String, String>();
//        paramMap.put("MID", "rxazcv89315285244163");
//        // Key in your staging and production MID available in your dashboard
//        paramMap.put("ORDER_ID", "order1");
//        paramMap.put("CUST_ID", "cust123");
//        paramMap.put("MOBILE_NO", "7777777777");
//        paramMap.put("EMAIL", "username@emailprovider.com");
//        paramMap.put("CHANNEL_ID", "WAP");
//        paramMap.put("TXN_AMOUNT", "100.12");
//        paramMap.put("WEBSITE", "WEBSTAGING");
//        // This is the staging value. Production value is available in your dashboard
//        paramMap.put("INDUSTRY_TYPE_ID", "Retail");
//        // This is the staging value. Production value is available in your dashboard
//        paramMap.put("CALLBACK_URL", "https://securegw-stage.paytm.in/theia/paytmCallback?ORDER_ID=order1");
//        paramMap.put("CHECKSUMHASH", checkSum);
//        return new PaytmOrder((HashMap<String, String>) paramMap);
//    }
//
//    private void readSmsFromUsersDeviceForOtpVerification() {
//        if (ContextCompat.checkSelfPermission(PaymentGatewayActivity.this,
//                Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(PaymentGatewayActivity.this,
//                    new String[]{Manifest.permission.READ_SMS, Manifest.permission.RECEIVE_SMS},
//                    101);
//        }
//    }

}
