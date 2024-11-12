package com.example.mobile.ui.service;

import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.mobile.R;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link QRCodeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class QRCodeFragment extends Fragment {

    private static final String ARG_SERVICE_ID = "service_id";
    private static final String ARG_SERVICE_NAME = "service_name";
    private static final String ARG_SERVICE_DESCRIPTION = "service_description";
    private static final String ARG_SERVICE_PHONE = "service_phone";
    private static final String ARG_SERVICE_PLACE = "service_place";
    private static final String ARG_SERVICE_START_DATE = "service_start_date";
    private static final String ARG_SERVICE_END_DATE = "service_end_date";
    private static final String ARG_SERVICE_PRICE = "service_price";

    public static QRCodeFragment newInstance(int serviceId, String name, String description, String phone, String place, String startDate, String endDate, String price) {
        QRCodeFragment fragment = new QRCodeFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SERVICE_ID, serviceId);
        args.putString(ARG_SERVICE_NAME, name);
        args.putString(ARG_SERVICE_DESCRIPTION, description);
        args.putString(ARG_SERVICE_PHONE, phone);
        args.putString(ARG_SERVICE_PLACE, place);
        args.putString(ARG_SERVICE_START_DATE, startDate);
        args.putString(ARG_SERVICE_END_DATE, endDate);
        args.putString(ARG_SERVICE_PRICE, price);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_q_r_code, container, false);
        ImageView qrCodeImageView = view.findViewById(R.id.qrCodeImageView);

        if (getArguments() != null) {
            int serviceId = getArguments().getInt(ARG_SERVICE_ID);
            String name = getArguments().getString(ARG_SERVICE_NAME);
            String description = getArguments().getString(ARG_SERVICE_DESCRIPTION);
            String phone = getArguments().getString(ARG_SERVICE_PHONE);
            String place = getArguments().getString(ARG_SERVICE_PLACE);
            String startDate = getArguments().getString(ARG_SERVICE_START_DATE);
            String endDate = getArguments().getString(ARG_SERVICE_END_DATE);
            String price = getArguments().getString(ARG_SERVICE_PRICE);

            generateQRCode(serviceId, name, description, phone, place, startDate, endDate, price, qrCodeImageView);
        }

        return view;
    }

    private void generateQRCode(int serviceId, String name, String description, String phone, String place, String startDate, String endDate, String price, ImageView imageView) {
        String qrText = "Service Details:\n" +
                "ID: " + serviceId + "\n" +
                "Name: " + name + "\n" +
                "Description: " + description + "\n" +
                "Phone: " + phone + "\n" +
                "Place: " + place + "\n" +
                "Start Date: " + startDate + "\n" +
                "End Date: " + endDate + "\n" +
                "Price: " + price;

        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        try {
            BitMatrix bitMatrix = qrCodeWriter.encode(qrText, BarcodeFormat.QR_CODE, 200, 200);
            Bitmap bitmap = Bitmap.createBitmap(bitMatrix.getWidth(), bitMatrix.getHeight(), Bitmap.Config.RGB_565);
            for (int x = 0; x < bitMatrix.getWidth(); x++) {
                for (int y = 0; y < bitMatrix.getHeight(); y++) {
                    bitmap.setPixel(x, y, bitMatrix.get(x, y) ? 0xFF000000 : 0xFFFFFFFF);
                }
            }
            imageView.setImageBitmap(bitmap);
        } catch (WriterException e) {
            e.printStackTrace();
        }
    }
}
