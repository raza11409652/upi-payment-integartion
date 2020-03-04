package com.hackdroid.upiapptest;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class UPIPayment extends AppCompatActivity {
    Uri.Builder payUri = new Uri.Builder();
    String upi = null, amounttobepaid = String.valueOf(1.0);
    String TAG = UPIPayment.class.getSimpleName();
    RecyclerView listRecycler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upipayment);
        upi = getIntent().getStringExtra("upi");

        listRecycler = findViewById(R.id.upiapp);
        Log.d(TAG, "onCreate: " + upi);
        payUri.scheme("upi").authority("pay");
        payUri.appendQueryParameter("pa", upi);
        payUri.appendQueryParameter("pn", "Vendor Name");
        Long tsLong = System.currentTimeMillis() / 1000;
        String transaction_ref_id = tsLong.toString();
      payUri.appendQueryParameter("tid", transaction_ref_id);
        payUri.appendQueryParameter("mc", "");
       payUri.appendQueryParameter("tr", transaction_ref_id);
        payUri.appendQueryParameter("tn", "Order Ref 74657475858" );
        payUri.appendQueryParameter("am", amounttobepaid);
        payUri.appendQueryParameter("cu", "INR");

        Uri uri = payUri.build();

        Intent intent = new Intent(Intent.ACTION_VIEW);


        intent.setData(uri);
        Log.d(TAG, "onCreate: "+uri);
        if (intent.resolveActivity(getPackageManager()) != null) {
            List<ResolveInfo> list = getPackageManager().queryIntentActivities(intent, 0);
            showApp(list, intent, getApplicationContext());
            Log.d(TAG, "" + list);
        } else {
            Toast.makeText(getApplicationContext(), "No UPI App found", Toast.LENGTH_LONG).show();
            Log.d("IN", "error");

        }
    }

    private void showApp(List<ResolveInfo> list, Intent intent, Context applicationContext) {
        Log.d(TAG, "showApp: " + list);
        listRecycler.setHasFixedSize(true);
        UpiAdapter adapter = new UpiAdapter(this, applicationContext, list, intent);
        listRecycler.setAdapter(adapter);
        listRecycler.setLayoutManager(new LinearLayoutManager(this));


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult: " + data);
        if (data != null) {
            switch (requestCode) {
                case 10001:
                    Log.d(TAG, "onActivityResult: " + data);
                    String response = data.getStringExtra("response");
                    Log.d("Response", response);

                    ArrayList<String> dataList = new ArrayList<>();
                    dataList.add(response);
                    upiPaymentDataOperation(dataList);
                    break;
            }
        }
    }

    private void upiPaymentDataOperation(ArrayList<String> dataList) {


        String str = dataList.get(0);
        Log.d(TAG, "upiPaymentDataOperation: " + str);
        Log.d("UPIPAY", "upiPaymentDataOperation: " + str);
        String paymentCancel = "";
        if (str == null) str = "discard";
        String status = "";
        String approvalRefNo = "";
        String response[] = str.split("&");
        for (int i = 0; i < response.length; i++) {
            String equalStr[] = response[i].split("=");
            if (equalStr.length >= 2) {
                if (equalStr[0].toLowerCase().equals("Status".toLowerCase())) {
                    status = equalStr[1].toLowerCase();
                } else if (equalStr[0].toLowerCase().equals("ApprovalRefNo".toLowerCase()) || equalStr[0].toLowerCase().equals("txnRef".toLowerCase())) {
                    approvalRefNo = equalStr[1];
                }
            } else {
                paymentCancel = "Payment cancelled by user.";
            }
        }

        if (status.equals("success")) {
            //Code to handle successful transaction here.
            //Toast.makeText(getApplicationContext(), "Transaction successful.", Toast.LENGTH_SHORT).show();
            Log.d("UPI", "responseStr: " + approvalRefNo);
                /*Intent payconfirmation = new Intent(getApplicationContext(), PaymentConfirmation.class);
                String s = "responseStr: " + approvalRefNo;
                payconfirmation.putExtra("payref", s);
                payconfirmation.putExtra("amount", Constant.AMOUNT_TO_BE_PAID);
                startActivity(payconfirmation);
                */
            //Payment Done
            //1.update order status create transaction
            //for wallet if discount id provided
            //updtae trasaction
            //
//                String approvalno = "UPI : " + approvalRefNo;
//                String mode = "UPI APP " + getString(R.string.upi_vendor_vpa);
//                createTransaction(orderid, orderuid, approvalno, mode);
            Toast.makeText(getApplicationContext() , ""+approvalRefNo , Toast.LENGTH_SHORT).show();

        } else if ("Payment cancelled by user.".equals(paymentCancel)) {
            Toast.makeText(getApplicationContext(), "Payment cancelled by user.", Toast.LENGTH_SHORT).show();
//                showChocoBar.showAlert("Payment cancelled by user");
        } else {
//                showChocoBar.showAlert("Payment cancelled by user");
            Toast.makeText(getApplicationContext(), "Payment cancel", Toast.LENGTH_SHORT).show();
        }

    }
}
