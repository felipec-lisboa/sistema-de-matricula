package model;

import java.util.ArrayList;
import java.util.List;

public class Curriculo {

    private String semestre;
    private boolean periodoMatriculaAberto;
    private List<Disciplina> disciplinas;

    public Curriculo(String semestre) {
        this.semestre = semestre;
        this.periodoMatriculaAberto = false;
        this.disciplinas = new ArrayList<>();
    }

    public void adicionarDisciplina(Disciplina disciplina) {
        // TODO: implementar na Sprint 3
    }

    public void removerDisciplina(Disciplina disciplina) {
        // TODO: implementar na Sprint 3
    }

    public void abrirMatriculas() {
        // TODO: implementar na Sprint 3
    }

    public void encerrarMatriculas() {
        // TODO: implementar na Sprint 3
    }

    public void verificarDisciplinas() {
        // TODO: implementar na Sprint 3
    }

    public String getSemestre() {
        return semestre;
    }

    public void setSemestre(String semestre) {
        this.semestre = semestre;
    }

    public boolean isPeriodoMatriculaAberto() {
        return periodoMatriculaAberto;
    }

    public void setPeriodoMatriculaAberto(boolean periodoMatriculaAberto) {
        this.periodoMatriculaAberto = periodoMatriculaAberto;
    }

    public List<Disciplina> getDisciplinas() {
        return disciplinas;
    }

    public void setDisciplinas(List<Disciplina> disciplinas) {
        this.disciplinas = disciplinas;
    }
}