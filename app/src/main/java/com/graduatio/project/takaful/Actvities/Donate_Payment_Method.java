package com.graduatio.project.takaful.Actvities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.graduatio.project.takaful.R;

public class Donate_Payment_Method extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.donate_payment_method);

    }
}
////    void showUserOptionsBottomSheet() {
//
//        messageAttachIv.setClickable(false);
//
//        final BottomSheetDialog bsd = new BottomSheetDialog(this, R.style.SheetDialog);
//        final View parentView = getLayoutInflater().inflate(R.layout.message_options_bsd, null);
//        parentView.setBackgroundColor(Color.TRANSPARENT);
//
//        parentView.findViewById(R.id.imageIv).setOnClickListener(view -> {
//
//            if (checkIsUploading()) {
//                return;
//            }
//
//            bsd.dismiss();
//
//            Files.startImageFetchIntent(MessagingActivity.this);
//        });
//
//        parentView.findViewById(R.id.audioIv).setOnClickListener(view -> {
//            if (checkIsUploading()) {
//                return;
//            }
////        bsd.dismiss();
////
////        Files.startImageFetchIntent(PrivateMessagingActivity.this);
//        });
//
//        parentView.findViewById(R.id.videoIv).setOnClickListener(view -> {
//            if (checkIsUploading()) {
//                return;
//            }
//            bsd.dismiss();
//            Files.startVideoFetchIntent(MessagingActivity.this);
//        });
//
//        parentView.findViewById(R.id.documentIv).setOnClickListener(view -> {
//            if (checkIsUploading()) {
//                return;
//            }
//            bsd.dismiss();
//            Files.startDocumentFetchIntent(MessagingActivity.this);
//        });
//
//        bsd.setOnDismissListener(dialogInterface -> messageAttachIv.setClickable(true));
//
//        bsd.setContentView(parentView);
//        bsd.show();
//
//    }