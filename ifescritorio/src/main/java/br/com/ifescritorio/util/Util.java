package br.com.ifescritorio.util;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.time.LocalDateTime;

import org.springframework.web.multipart.MultipartFile;

public class Util {

    /**
     * Diretório utilizado para armazenamento de arquivos.
     *
     * Em desenvolvimento, utiliza:
     * C:/ifes-patrimonio/uploads/
     *
     * Em produção/Docker, pode ser definido através
     * da variável de ambiente:
     *
     * PATRIMONIO_UPLOAD_DIR
     */
    public static final String LOCAL_ARMAZENAMENTO_IMAGENS =
            System.getenv().getOrDefault(
                    "PATRIMONIO_UPLOAD_DIR",
                    "C:/ifes-patrimonio/uploads/"
            );

    public static String fazerUploadImagem(
            MultipartFile imagem) {

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

                File dir =
                        new File(
                                LOCAL_ARMAZENAMENTO_IMAGENS
                        );

                if (!dir.exists()) {

                    if (!dir.mkdirs()) {

                        throw new RuntimeException(
                                "Não foi possível criar o diretório de uploads."
                        );
                    }
                }

                File serverFile =
                        new File(
                                dir.getAbsolutePath()
                                + File.separator
                                + nomeArquivoComDataHora
                        );

                System.out.println(
                        "Arquivo salvo em: "
                        + serverFile.getAbsolutePath()
                );

                try (
                        BufferedOutputStream stream =
                                new BufferedOutputStream(
                                        new FileOutputStream(serverFile)
                                )
                ) {

                    stream.write(
                            imagem.getBytes()
                    );
                }

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