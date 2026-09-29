package model;

import java.util.ArrayList;
import java.util.List;

public class Disciplina {

    private String codigo;
    private String nome;
    private TipoDisciplina tipo;
    private StatusDisciplina status;
    private Professor professor;
    private Curso curso;
    private List<Matricula> matriculas;

    public Disciplina(String codigo, String nome, TipoDisciplina tipo, StatusDisciplina status, Professor professor) {
        this.codigo = codigo;
        this.nome = nome;
        this.tipo = tipo;
        this.status = status;
        this.professor = professor;
        this.matriculas = new ArrayList<>();
    }

    public boolean possuiVaga() {
        return contarMatriculasAtivas() < 60;
    }

    public boolean verificarMinimoAlunos() {
        return contarMatriculasAtivas() >= 3;
    }

    public int contarMatriculasAtivas() {
        int quantidade = 0;

        for (Matricula matricula : matriculas) {
            if (matricula.isAtiva()) {
                quantidade++;
            }
        }

        return quantidade;
    }

    public List<Aluno> consultarAlunos() {
        List<Aluno> alunos = new ArrayList<>();

        for (Matricula matricula : matriculas) {
            if (matricula.isAtiva()) {
                alunos.add(matricula.getAluno());
            }
        }

        return alunos;
    }

    public void adicionarMatricula(Matricula matricula) {
        if (!matriculas.contains(matricula)) {
            matriculas.add(matricula);
        }

        if (!possuiVaga()) {
            status = StatusDisciplina.LOTADA;
        }
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

    public Curso getCurso() {
        return curso;
    }

    public void setCurso(Curso curso) {
        this.curso = curso;
    }

    public List<Matricula> getMatriculas() {
        return matriculas;
    }

    public void setMatriculas(List<Matricula> matriculas) {
        this.matriculas = matriculas;
    }
}