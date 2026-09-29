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
        if (!disciplinas.contains(disciplina)) {
            disciplinas.add(disciplina);
        }
    }

    public void removerDisciplina(Disciplina disciplina) {
        disciplinas.remove(disciplina);
    }

    public void abrirMatriculas() {
        periodoMatriculaAberto = true;

        for (Disciplina disciplina : disciplinas) {
            if (disciplina.getStatus() != StatusDisciplina.CANCELADA) {
                disciplina.setStatus(StatusDisciplina.ABERTA);
            }
        }
    }

    public void encerrarMatriculas() {
        periodoMatriculaAberto = false;
        verificarDisciplinas();
    }

    public void verificarDisciplinas() {
        for (Disciplina disciplina : disciplinas) {

            if (disciplina.verificarMinimoAlunos()) {
                disciplina.setStatus(StatusDisciplina.ATIVA);
            } else {
                disciplina.setStatus(StatusDisciplina.CANCELADA);
            }
        }
    }

    public boolean isPeriodoMatriculaAberto() {
        return periodoMatriculaAberto;
    }

    public String getSemestre() {
        return semestre;
    }

    public void setSemestre(String semestre) {
        this.semestre = semestre;
    }

    public List<Disciplina> getDisciplinas() {
        return disciplinas;
    }

    public void setDisciplinas(List<Disciplina> disciplinas) {
        this.disciplinas = disciplinas;
    }

    public void setPeriodoMatriculaAberto(boolean periodoMatriculaAberto) {
        this.periodoMatriculaAberto = periodoMatriculaAberto;
    }
}