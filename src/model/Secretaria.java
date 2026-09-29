package model;

import java.util.ArrayList;
import java.util.List;

public class Secretaria extends Usuario {

    private String registro;

    private List<Curso> cursos;
    private List<Disciplina> disciplinas;
    private List<Professor> professores;
    private List<Aluno> alunos;

    private Curriculo curriculoAtual;

    public Secretaria(String nome, String login, String senha, String registro) {
        super(nome, login, senha);

        this.registro = registro;

        this.cursos = new ArrayList<>();
        this.disciplinas = new ArrayList<>();
        this.professores = new ArrayList<>();
        this.alunos = new ArrayList<>();
    }

    public void cadastrarCurso(Curso curso) {
        if (!cursos.contains(curso)) {
            cursos.add(curso);
        }
    }

    public void editarCurso(Curso curso) {
        // Os dados do curso são alterados
        // através dos setters da própria classe Curso.
    }

    public void removerCurso(Curso curso) {
        cursos.remove(curso);
    }


    public void cadastrarDisciplina(Disciplina disciplina) {
        if (!disciplinas.contains(disciplina)) {
            disciplinas.add(disciplina);
        }
    }

    public void editarDisciplina(Disciplina disciplina) {
        // Os dados da disciplina são alterados
        // através dos setters da própria classe Disciplina.
    }

    public void removerDisciplina(Disciplina disciplina) {
        disciplinas.remove(disciplina);
    }

    public void cadastrarProfessor(Professor professor) {
        if (!professores.contains(professor)) {
            professores.add(professor);
        }
    }

    public void removerProfessor(Professor professor) {
        professores.remove(professor);
    }

    public void cadastrarAluno(Aluno aluno) {
        if (!alunos.contains(aluno)) {
            alunos.add(aluno);
        }
    }

    public void removerAluno(Aluno aluno) {
        alunos.remove(aluno);
    }

    public Curriculo gerarCurriculo(String semestre) {

        Curriculo curriculo = new Curriculo(semestre);

        for (Disciplina disciplina : disciplinas) {
            curriculo.adicionarDisciplina(disciplina);
        }

        this.curriculoAtual = curriculo;

        return curriculo;
    }

    public void abrirPeriodoMatricula() {
        if (curriculoAtual != null) {
            curriculoAtual.abrirMatriculas();
        }
    }

    public void encerrarPeriodoMatricula() {
        if (curriculoAtual != null) {
            curriculoAtual.encerrarMatriculas();
        }
    }

    public String getRegistro() {
        return registro;
    }

    public void setRegistro(String registro) {
        this.registro = registro;
    }

    public List<Curso> getCursos() {
        return cursos;
    }

    public List<Disciplina> getDisciplinas() {
        return disciplinas;
    }

    public List<Professor> getProfessores() {
        return professores;
    }

    public List<Aluno> getAlunos() {
        return alunos;
    }

    public Curriculo getCurriculoAtual() {
        return curriculoAtual;
    }

    public void setCurriculoAtual(Curriculo curriculoAtual) {
        this.curriculoAtual = curriculoAtual;
    }
}