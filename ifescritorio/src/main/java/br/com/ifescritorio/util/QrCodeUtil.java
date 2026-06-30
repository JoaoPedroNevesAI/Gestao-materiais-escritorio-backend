package br.com.ifescritorio.util;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

public class QrCodeUtil {

    public static String gerarQRCode(
            String conteudo,
            String nomeArquivo) {

        try {

            // Pasta onde os QR Codes serão salvos
            String pasta =
                    Util.LOCAL_ARMAZENAMENTO_IMAGENS + "qrcodes" + File.separator;

            // Cria a pasta caso ela não exista
            File diretorio = new File(pasta);

            if (!diretorio.exists()) {
                diretorio.mkdirs();
            }

            // Caminho completo do arquivo
            Path path = Paths.get(
                    pasta,
                    nomeArquivo + ".png");

            // Geração do QR Code
            QRCodeWriter qrCodeWriter = new QRCodeWriter();

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

            // Retorna apenas o nome do arquivo salvo
            return nomeArquivo + ".png";

        } catch (Exception e) {

            e.printStackTrace();

            throw new RuntimeException(
                    "Erro ao gerar QR Code",
                    e);
        }
    }
}