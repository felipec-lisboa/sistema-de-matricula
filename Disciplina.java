package model;

import java.util.List;

public class Disciplina {

    private String codigo;
    private String nome;
    private TipoDisciplina tipo;
    private StatusDisciplina status;
    private Professor professor;

    public Disciplina(
        String codigo,
        String nome,
        TipoDisciplina tipo,
        StatusDisciplina status,
        Professor professor
    ) {
        this.codigo = codigo;
        this.nome = nome;
        this.tipo = tipo;
        this.status = status;
        this.professor = professor;
    }

    public boolean possuiVaga() {
        return false;
    }

    public boolean verificarMinimoAlunos() {
        return false;
    }

    public List<Aluno> consultarAlunos() {
        return null;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public TipoDisciplina getTipo() {
        return tipo;
    }

    public void setTipo(TipoDisciplina tipo) {
        this.tipo = tipo;
    }

    public StatusDisciplina getStatus() {
        return status;
    }

    public void setStatus(StatusDisciplina status) {
        this.status = status;
    }

    public Professor getProfessor() {
        return professor;
    }

    public void setProfessor(Professor professor) {
        this.professor = professor;
    }
}