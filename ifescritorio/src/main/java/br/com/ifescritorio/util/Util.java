package br.com.ifescritorio.util;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.time.LocalDateTime;

import org.springframework.web.multipart.MultipartFile;

public class Util {

    public static final String LOCAL_ARMAZENAMENTO_IMAGENS =
            "C:/ifes-patrimonio/uploads/";

    public static String fazerUploadImagem(MultipartFile imagem) {

        boolean sucessoUpload = false;
        String nomeArquivoComDataHora = null;

        if (imagem != null && !imagem.isEmpty()) {

            String dataHora =
                    LocalDateTime.now().getYear() + "-"
                    + LocalDateTime.now().getMonthValue() + "-"
                    + LocalDateTime.now().getDayOfMonth() + "-"
                    + LocalDateTime.now().getHour() + "-"
                    + LocalDateTime.now().getMinute() + "-"
                    + LocalDateTime.now().getSecond() + "-";

            String nomeOriginalArquivo =
                    imagem.getOriginalFilename();

            nomeArquivoComDataHora =
                    dataHora + nomeOriginalArquivo;

            try {

                File dir = new File(
                        LOCAL_ARMAZENAMENTO_IMAGENS);

                if (!dir.exists()) {
                    dir.mkdirs();
                }

                File serverFile =
                        new File(dir.getAbsolutePath()
                                + File.separator
                                + nomeArquivoComDataHora);

                System.out.println(serverFile.getAbsolutePath());

                BufferedOutputStream stream =
                        new BufferedOutputStream(
                                new FileOutputStream(serverFile));

                stream.write(imagem.getBytes());
                stream.close();

                sucessoUpload = true;

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return sucessoUpload
                ? nomeArquivoComDataHora
                : null;
    }
}