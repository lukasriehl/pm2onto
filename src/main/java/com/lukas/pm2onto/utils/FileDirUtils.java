package com.lukas.pm2onto.utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 *
 * @author Lukas Riehl
 */
public class FileDirUtils {

    public static boolean OsIsWindows() {
        String osName = System.getProperty("os.name");
        return osName != null && osName.toLowerCase().contains("win");
    }

    public static File getDiretorioTemporario() {
        File diretorioTemporario = new File(OsIsWindows()
                ? "C:\\tmpPm2Onto" : "tmpPm2Onto");

        return diretorioTemporario;
    }

    /**
     * Retorna um arquivo em um diretório temporário criado na função. Esse
     * arquivo tem o nome informado como parâmetro.
     *
     * @param nomeArquivo
     * @return
     */
    public static File geraArquivo(String nomeArquivo) {
        File diretorioTemporario = getDiretorioTemporario();

        if (!diretorioTemporario.exists()) {
            diretorioTemporario.setExecutable(true);
            diretorioTemporario.setReadable(true);
            
            diretorioTemporario.mkdir();
        }

        String caminhoDiretorioGerado = diretorioTemporario.getPath().concat(OsIsWindows() ? "\\" : "/");

        File arquivo = new File(caminhoDiretorioGerado.concat(nomeArquivo));

        return arquivo;
    }

    /**
     * Remove um arquivo/diretório informado como parâmetro. Caso o parâmetro
     * seja um diretório, então esse método é executado de maneira recursiva
     * para limpar subpasstas/subarquivos
     *
     * @param arqDir
     * @throws IOException
     */
    public static void removeArquivoOuDiretorio(File arqDir) throws IOException {
        if (arqDir.isDirectory()) {
            if (arqDir.list().length == 0) {
                removeArquivoEmDiretorio(getDiretorioTemporario().getPath(), null);
            } else {
                String arquivosDiretorios[] = arqDir.list();

                for (String tempArqDir : arquivosDiretorios) {
                    File arqDirParaRemover = new File(arqDir, tempArqDir);

                    removeArquivoOuDiretorio(arqDirParaRemover);
                }

                if (arqDir.list().length == 0) {
                    removeArquivoEmDiretorio(getDiretorioTemporario().getPath(), null);
                }
            }
        } else if (arqDir.isFile()) {
            removeArquivoEmDiretorio(getDiretorioTemporario().getPath(), arqDir.getName());
        }
    }

    /**
     * Remove um arquivo, baseado em seu nome informado como parâmetro, de um
     * diretório, cujo caminho também é informado como parâmetro.
     *
     * @param caminhoDiretorio
     * @param nomeArquivo
     * @throws IOException
     */
    private static void removeArquivoEmDiretorio(String caminhoDiretorio, String nomeArquivo) throws IOException {
        Path filePath = nomeArquivo == null ? FileSystems.getDefault().getPath(caminhoDiretorio)
                : FileSystems.getDefault().getPath(caminhoDiretorio,
                        nomeArquivo);
        Files.delete(filePath);
    }
}
