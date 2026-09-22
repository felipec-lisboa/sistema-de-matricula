package model;

import java.util.List;

public class Aluno extends Usuario {

    private String matricula;

    public Aluno(String nome, String login, String senha, String matricula) {
        super(nome, login, senha);
        this.matricula = matricula;
    }

    public List<Disciplina> visualizarDisciplinas() {
        return null;
    }

    public boolean matricular(Disciplina disciplina) {
        return false;
    }

    public boolean cancelarMatricula(Disciplina disciplina) {
        return false;
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }
}