package br.com.ifescritorio.util;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

public class QrCodeUtil {

    /**
     * Gera um QR Code em formato PNG.
     *
     * @param conteudo conteúdo que será armazenado no QR Code
     * @param nomeArquivo nome do arquivo sem extensão
     * @return nome do arquivo gerado
     */
    public static String gerarQRCode(
            String conteudo,
            String nomeArquivo) {

        try {

            String pasta =
                    Util.LOCAL_ARMAZENAMENTO_IMAGENS
                    + "qrcodes"
                    + File.separator;

            File diretorio =
                    new File(pasta);

            if (!diretorio.exists()
                    && !diretorio.mkdirs()) {

                throw new RuntimeException(
                        "Não foi possível criar a pasta dos QR Codes."
                );
            }

            Path path =
                    Paths.get(
                            pasta,
                            nomeArquivo + ".png"
                    );

            QRCodeWriter qrCodeWriter =
                    new QRCodeWriter();

            BitMatrix bitMatrix =
                    qrCodeWriter.encode(
                            conteudo,
                            BarcodeFormat.QR_CODE,
                            300,
                            300
                    );

            MatrixToImageWriter.writeToPath(
                    bitMatrix,
                    "PNG",
                    path
            );

            return nomeArquivo + ".png";

        } catch (Exception e) {

            throw new RuntimeException(
                    "Erro ao gerar QR Code.",
                    e
            );
        }
    }

    /**
     * Remove o arquivo físico de um QR Code.
     *
     * @param nomeArquivo nome do arquivo sem extensão
     */
    public static void excluirQRCode(
            String nomeArquivo) {

        try {

            String pasta =
                    Util.LOCAL_ARMAZENAMENTO_IMAGENS
                    + "qrcodes"
                    + File.separator;

            Path path =
                    Paths.get(
                            pasta,
                            nomeArquivo + ".png"
                    );

            Files.deleteIfExists(path);

        } catch (Exception e) {

            throw new RuntimeException(
                    "Erro ao excluir QR Code.",
                    e
            );
        }
    }

    /**
     * Verifica se o arquivo físico do QR Code existe.
     */
    public static boolean existeQRCode(
            String nomeArquivo) {

        String pasta =
                Util.LOCAL_ARMAZENAMENTO_IMAGENS
                + "qrcodes"
                + File.separator;

        Path path =
                Paths.get(
                        pasta,
                        nomeArquivo + ".png"
                );

        return Files.exists(path);
    }
}