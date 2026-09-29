package model;

public class Matricula {

    private Aluno aluno;
    private Disciplina disciplina;
    private boolean ativa;

    public Matricula(Aluno aluno, Disciplina disciplina) {
        this.aluno = aluno;
        this.disciplina = disciplina;
        this.ativa = true;
    }

    public boolean realizar() {
        if(ativa) return false;

        if(!disciplina.possuiVaga()) return false;

        ativa = true;
        disciplina.adicionarMatricula(this);

        return  true;
    }

    public boolean cancelar() {
        if(!ativa) return false;

        ativa = false;

        if(disciplina.getStatus() == StatusDisciplina.LOTADA) {
            disciplina.setStatus(StatusDisciplina.ABERTA);
        }

        return true;
     }

    public Aluno getAluno() {
        return aluno;
    }

    public void setAluno(Aluno aluno) {
        this.aluno = aluno;
    }

    public Disciplina getDisciplina() {
        return disciplina;
    }

    public void setDisciplina(Disciplina disciplina) {
        this.disciplina = disciplina;
    }

    public boolean isAtiva() {
        return ativa;
    }

    public void setAtiva(boolean ativa) {
        this.ativa = ativa;
    }
}