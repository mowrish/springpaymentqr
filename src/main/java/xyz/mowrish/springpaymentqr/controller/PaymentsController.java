package xyz.mowrish.springpaymentqr.controller;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;
import xyz.mowrish.springpaymentqr.model.PaymentRequest;

import java.net.InetAddress;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController("/api")
public class PaymentsController {

    @Value("${server.port}")
    public Long serverPort;

    @SneakyThrows
    @PostMapping("/createPaymentQr")
    public ResponseEntity<String> createPaymentQr(@RequestBody PaymentRequest paymentRequest) {
        String txId = UUID.randomUUID().toString();

        MultiValueMap<String, String> urlParams = new LinkedMultiValueMap<>();
        urlParams.add("transactionId", txId);
        urlParams.add("merchantName", paymentRequest.getMerchantName());
        urlParams.add("merchantUpi", paymentRequest.getMerchantUpiId());
        urlParams.add("amount", String.valueOf(paymentRequest.getAmount()));

        String url = UriComponentsBuilder.newInstance()
                .scheme("http").host(InetAddress.getLocalHost().getHostAddress())
                .path("/makePayment/").queryParams(urlParams).build().toUriString();

        QRCodeWriter barcodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = barcodeWriter.encode(url, BarcodeFormat.QR_CODE, 200, 200);
        Path path = FileSystems.getDefault().getPath(txId + ".png");
        MatrixToImageWriter.writeToPath(bitMatrix, "PNG", path);
        return new ResponseEntity<>(url, HttpStatus.OK);
    }

    @GetMapping("/makePayment")
    public ResponseEntity<String> makePayment(@RequestParam(name = "transactionId") String transactionId,
                                              @RequestParam(name = "merchantName") String merchantName,
                                              @RequestParam(name = "merchantUpi") String merchantUpi,
                                              @RequestParam(name = "amount") String amount)
    {
        return new ResponseEntity<>(transactionId+ "-" +merchantUpi, HttpStatus.OK);

    }

}
