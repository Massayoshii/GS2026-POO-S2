package br.com.territory.dominio;

public abstract class ComponenteTerritorial {

    private Long id;
    private String nome;
    private String regiao;
    private boolean ativo;

    public ComponenteTerritorial(Long id, String nome, String regiao, boolean ativo) {
        this.id = id;
        this.nome = nome;
        this.regiao = regiao;
        this.ativo = ativo;
    }

    public abstract void verificarStatus();

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getRegiao() {
        return regiao;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setRegiao(String regiao) {
        this.regiao = regiao;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }
}