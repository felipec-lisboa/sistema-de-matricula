package external;

import model.Aluno;

public class SistemaCobrancaConsole implements SistemaCobranca {

    @Override
    public void notificarMatricula(Aluno aluno) {
        System.out.println(
            "Sistema de Cobranças notificado para o aluno: "
            + aluno.getNome()
        );
    }
}