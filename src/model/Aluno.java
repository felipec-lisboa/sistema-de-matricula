package model;

import java.util.ArrayList;
import java.util.List;

import external.SistemaCobranca;
import external.SistemaCobrancaConsole;

public class Aluno extends Usuario {

    private String matricula;
    private List<Matricula> matriculas;
    private SistemaCobranca sistemaCobranca;

    public Aluno(String nome, String login, String senha, String matricula) {
        super(nome, login, senha);

        this.matricula = matricula;
        this.matriculas = new ArrayList<>();
        this.sistemaCobranca = new SistemaCobrancaConsole();
    }

    public List<Disciplina> visualizarDisciplinas() {
        List<Disciplina> disciplinas = new ArrayList<>();

        for (Matricula matricula : matriculas) {
            if (matricula.isAtiva()) {
                disciplinas.add(matricula.getDisciplina());
            }
        }

        return disciplinas;
    }

    public boolean matricular(Disciplina disciplina, Curriculo curriculo) {

        if (!curriculo.isPeriodoMatriculaAberto()) {
            return false;
        }

        if (!curriculo.getDisciplinas().contains(disciplina)) {
            return false;
        }

        if (!disciplina.possuiVaga()) {
            return false;
        }

        if (jaMatriculado(disciplina)) {
            return false;
        }

        if (disciplina.getTipo() == TipoDisciplina.OBRIGATORIA) {
            if (contarDisciplinas(TipoDisciplina.OBRIGATORIA) >= 4) {
                return false;
            }
        }

        if (disciplina.getTipo() == TipoDisciplina.OPTATIVA) {
            if (contarDisciplinas(TipoDisciplina.OPTATIVA) >= 2) {
                return false;
            }
        }

        Matricula novaMatricula = new Matricula(this, disciplina);

        if (novaMatricula.realizar()) {

            matriculas.add(novaMatricula);

            sistemaCobranca.notificarMatricula(this);

            return true;
        }

        return false;
    }

    public boolean cancelarMatricula(Disciplina disciplina, Curriculo curriculo) {

        if (!curriculo.isPeriodoMatriculaAberto()) {
            return false;
        }

        for (Matricula matricula : matriculas) {

            if (matricula.getDisciplina() == disciplina
                    && matricula.isAtiva()) {

                return matricula.cancelar();
            }
        }

        return false;
    }

    private int contarDisciplinas(TipoDisciplina tipo) {

        int quantidade = 0;

        for (Matricula matricula : matriculas) {
            if (matricula.isAtiva()&& matricula.getDisciplina().getTipo() == tipo) {
                quantidade++;
            }
        }

        return quantidade;
    }

    private boolean jaMatriculado(Disciplina disciplina) {

        for (Matricula matricula : matriculas) {

            if (matricula.isAtiva()
                    && matricula.getDisciplina() == disciplina) {

                return true;
            }
        }

        return false;
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public List<Matricula> getMatriculas() {
        return matriculas;
    }

    public void setMatriculas(List<Matricula> matriculas) {
        this.matriculas = matriculas;
    }
}