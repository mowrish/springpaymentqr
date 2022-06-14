package xyz.mowrish.springpaymentqr.controller;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import xyz.mowrish.springpaymentqr.model.PaymentRequest;

import java.net.InetAddress;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.Base64;
import java.util.UUID;

@RestController("/api")
public class PaymentsController {

    @Value("${server.port}")
    public Long serverPort;

    @SneakyThrows
    @PostMapping("/createPaymentQr")
    public ResponseEntity<String> createPaymentQr(@RequestBody PaymentRequest paymentRequest) {
        String uuid = UUID.randomUUID().toString();
        String url = "http://" + InetAddress.getLocalHost().getHostAddress() + ":" + serverPort + "/makePayment/" + uuid;
        QRCodeWriter barcodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = barcodeWriter.encode(url, BarcodeFormat.QR_CODE, 200, 200);
        Path path = FileSystems.getDefault().getPath(uuid + ".png");
        MatrixToImageWriter.writeToPath(bitMatrix, "PNG", path);
        return new ResponseEntity<>(url, HttpStatus.OK);
    }

    @GetMapping("/makePayment/{transactionId}")
    public ResponseEntity<String> makePayment(@PathVariable String transactionId) {
        return new ResponseEntity<>(transactionId, HttpStatus.OK);
    }

}
