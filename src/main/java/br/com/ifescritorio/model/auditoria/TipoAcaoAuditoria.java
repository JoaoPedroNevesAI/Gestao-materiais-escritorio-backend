package br.com.ifescritorio.model.auditoria;

public enum TipoAcaoAuditoria {
    CRIACAO,
    ATUALIZACAO,
    EXCLUSAO,
    SOLICITACAO,
    APROVACAO,
    REPROVACAO,
    CONCLUSAO,
    LOGIN_SUCESSO,
    LOGIN_FALHA,
    LEITURA,
    UPLOAD,
    DOWNLOAD,
    OUTRO
}
