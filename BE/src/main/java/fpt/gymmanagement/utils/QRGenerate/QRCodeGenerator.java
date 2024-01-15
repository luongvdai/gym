package fpt.gymmanagement.utils.QRGenerate;

import com.google.zxing.*;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;


public class QRCodeGenerator {
    private static final int WIDTH = 100;
    private static final int HEIGHT = 100;
    public static String generateBase64QR(String data) throws WriterException, IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        // Tạo mã QR code
        BitMatrix bitMatrix = new QRCodeWriter().encode(data, BarcodeFormat.QR_CODE, WIDTH, HEIGHT);

        // Ghi hình ảnh vào ByteArrayOutputStream
        MatrixToImageWriter.writeToStream(bitMatrix, "PNG", baos);

        // Chuyển đổi hình ảnh thành chuỗi Base64
        String base64Image = Base64.getEncoder().encodeToString(baos.toByteArray());

        baos.close();

        return base64Image;
    }

}