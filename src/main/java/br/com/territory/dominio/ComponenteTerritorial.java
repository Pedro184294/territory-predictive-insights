package br.com.territory.dominio;

public abstract class ComponenteTerritorial {

    private String id;
    private String nome;
    private String regiao;
    private boolean ativo;

    public ComponenteTerritorial(String id, String nome, String regiao) {
        this.id = id;
        this.nome = nome;
        this.regiao = regiao;
        this.ativo = true;
    }

    public abstract String verificarStatus();

    public String getId() {
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

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }

    public void exibirInformacoes() {
        System.out.println("ID: " + id);
        System.out.println("Nome: " + nome);
        System.out.println("Região: " + regiao);
        System.out.println("Ativo: " + (ativo ? "Sim" : "Não"));
    }
}