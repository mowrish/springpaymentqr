package xyz.mowrish.springpaymentqr.controller;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import lombok.SneakyThrows;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import xyz.mowrish.springpaymentqr.model.PaymentRequest;

import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.Base64;
import java.util.UUID;

@RestController("/api")
public class PaymentsController {

    @SneakyThrows
    @PostMapping("/createPaymentQr")
    public ResponseEntity<String> createPaymentQr(@RequestBody PaymentRequest paymentRequest) {
        QRCodeWriter barcodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = barcodeWriter.encode(paymentRequest.toString(), BarcodeFormat.QR_CODE, 200, 200);
        String uuid = UUID.randomUUID().toString();
        Path path = FileSystems.getDefault().getPath(uuid + ".png");
        MatrixToImageWriter.writeToPath(bitMatrix, "PNG", path);
        return new ResponseEntity<>(uuid, HttpStatus.OK);
    }

}
