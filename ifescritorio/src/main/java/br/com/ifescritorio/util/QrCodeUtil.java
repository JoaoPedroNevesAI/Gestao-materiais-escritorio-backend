package br.com.ifescritorio.util;

import java.io.File;
import java.nio.file.FileSystems;
import java.nio.file.Files;
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
            String pastaDiretorio = "uploads/qrcodes/";
            
            // SOLUÇÃO: Garante que a árvore de diretórios exista fisicamente
            File diretorio = new File(pastaDiretorio);
            if (!diretorio.exists()) {
                diretorio.mkdirs(); // Cria as pastas 'uploads' e 'qrcodes' automaticamente
            }

            Path path = FileSystems.getDefault()
                    .getPath(pastaDiretorio + nomeArquivo + ".png");

            QRCodeWriter qrCodeWriter = new QRCodeWriter();

            BitMatrix bitMatrix = qrCodeWriter.encode(
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