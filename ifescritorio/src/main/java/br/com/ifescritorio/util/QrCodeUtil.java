package br.com.ifescritorio.util;

import java.nio.file.FileSystems;
import java.nio.file.Path;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

public class QrCodeUtil {

    public static String gerarQRCode(
            String conteudo,
            String nomeArquivo) {

        try {

            String pasta =
                    "uploads/qrcodes/";

            Path path =
                    FileSystems.getDefault()
                            .getPath(
                                    pasta + nomeArquivo + ".png");

            QRCodeWriter qrCodeWriter =
                    new QRCodeWriter();

            BitMatrix bitMatrix =
                    qrCodeWriter.encode(
                            conteudo,
                            BarcodeFormat.QR_CODE,
                            300,
                            300);

            MatrixToImageWriter.writeToPath(
                    bitMatrix,
                    "PNG",
                    path);

            return nomeArquivo + ".png";

        } catch (Exception e) {

            throw new RuntimeException(
                    "Erro ao gerar QR Code",
                    e);
        }
    }
}