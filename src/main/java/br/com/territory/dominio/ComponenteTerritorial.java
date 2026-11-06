
package br.com.territory.dominio;

import br.com.territory.exceptions.DadosInvalidosException;

public abstract class ComponenteTerritorial {

    private String id;
    private String nome;
    private String regiao;
    private boolean ativo;

    public ComponenteTerritorial(String id, String nome, String regiao)
            throws DadosInvalidosException {

        if (id == null || id.trim().isEmpty()) {
            throw new DadosInvalidosException(
                    "O ID do componente territorial é obrigatório.");
        }

        if (nome == null || nome.trim().isEmpty()) {
            throw new DadosInvalidosException(
                    "O nome do componente territorial é obrigatório.");
        }

        if (regiao == null || regiao.trim().isEmpty()) {
            throw new DadosInvalidosException(
                    "A região do componente territorial é obrigatória.");
        }

        this.id = id.trim();
        this.nome = nome.trim();
        this.regiao = regiao.trim();
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
