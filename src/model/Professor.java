package model;

import java.util.List;

public class Professor extends Usuario {

    private String registro;

    public Professor(String nome, String login, String senha, String registro) {
        super(nome, login, senha);
        this.registro = registro;
    }

    public List<Aluno> consultarAlunos(Disciplina disciplina) {
        return null;
    }

    public String getRegistro() {
        return registro;
    }

    public void setRegistro(String registro) {
        this.registro = registro;
    }
}