package model;

public class Secretaria extends Usuario {

    private String registro;

    public Secretaria(String nome, String login, String senha, String registro) {
        super(nome, login, senha);
        this.registro = registro;
    }

    public void cadastrarCurso(Curso curso) {
        // TODO: implementar na Sprint 3
    }

    public void editarCurso(Curso curso) {
        // TODO: implementar na Sprint 3
    }

    public void removerCurso(Curso curso) {
        // TODO: implementar na Sprint 3
    }

    public void cadastrarDisciplina(Disciplina disciplina) {
        // TODO: implementar na Sprint 3
    }

    public void editarDisciplina(Disciplina disciplina) {
        // TODO: implementar na Sprint 3
    }

    public void removerDisciplina(Disciplina disciplina) {
        // TODO: implementar na Sprint 3
    }

    public void cadastrarProfessor(Professor professor) {
        // TODO: implementar na Sprint 3
    }

    public void removerProfessor(Professor professor) {
        // TODO: implementar na Sprint 3
    }

    public void cadastrarAluno(Aluno aluno) {
        // TODO: implementar na Sprint 3
    }

    public void removerAluno(Aluno aluno) {
        // TODO: implementar na Sprint 3
    }

    public Curriculo gerarCurriculo(String semestre) {
        return null;
    }

    public void abrirPeriodoMatricula() {
        // TODO: implementar na Sprint 3
    }

    public void encerrarPeriodoMatricula() {
        // TODO: implementar na Sprint 3
    }

    public String getRegistro() {
        return registro;
    }

    public void setRegistro(String registro) {
        this.registro = registro;
    }
}