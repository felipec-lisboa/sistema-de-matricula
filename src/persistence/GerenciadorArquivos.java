package persistence;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

import model.*;

public class GerenciadorArquivos {

    private static final String PASTA_DATA = "data";

    // ==================================================
    // INICIALIZAÇÃO
    // ==================================================

    public static void inicializarArquivos() {

        try {

            Path pasta = Path.of(PASTA_DATA);

            if (!Files.exists(pasta)) {
                Files.createDirectories(pasta);
            }

            criarArquivoSeNaoExistir("usuarios.txt");
            criarArquivoSeNaoExistir("cursos.txt");
            criarArquivoSeNaoExistir("disciplinas.txt");
            criarArquivoSeNaoExistir("matriculas.txt");
            criarArquivoSeNaoExistir("curriculo.txt");

        } catch (IOException e) {

            System.out.println(
                    "Erro ao inicializar arquivos: "
                            + e.getMessage());
        }
    }

    private static void criarArquivoSeNaoExistir(
            String nomeArquivo) throws IOException {

        Path caminho = Path.of(
                PASTA_DATA,
                nomeArquivo);

        if (!Files.exists(caminho)) {
            Files.createFile(caminho);
        }
    }

    // ==================================================
    // OPERAÇÕES GENÉRICAS
    // ==================================================

    public static void sobrescreverArquivo(
            String nomeArquivo,
            List<String> linhas) {

        try {

            Path caminho = Path.of(
                    PASTA_DATA,
                    nomeArquivo);

            Files.write(
                    caminho,
                    linhas,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.TRUNCATE_EXISTING);

        } catch (IOException e) {

            System.out.println(
                    "Erro ao salvar "
                            + nomeArquivo
                            + ": "
                            + e.getMessage());
        }
    }

    public static List<String> lerArquivo(
            String nomeArquivo) {

        try {

            Path caminho = Path.of(
                    PASTA_DATA,
                    nomeArquivo);

            if (!Files.exists(caminho)) {
                return new ArrayList<>();
            }

            return Files.readAllLines(caminho);

        } catch (IOException e) {

            System.out.println(
                    "Erro ao ler "
                            + nomeArquivo
                            + ": "
                            + e.getMessage());

            return new ArrayList<>();
        }
    }

    // ==================================================
    // USUÁRIOS
    // ==================================================

    public static void salvarUsuarios(
            List<Usuario> usuarios) {

        List<String> linhas = new ArrayList<>();

        for (Usuario usuario : usuarios) {

            if (usuario instanceof Aluno) {

                Aluno aluno = (Aluno) usuario;

                linhas.add(
                        "ALUNO;"
                                + limpar(aluno.getNome()) + ";"
                                + limpar(aluno.getLogin()) + ";"
                                + limpar(aluno.getSenha()) + ";"
                                + limpar(aluno.getMatricula()));

            } else if (usuario instanceof Professor) {

                Professor professor = (Professor) usuario;

                linhas.add(
                        "PROFESSOR;"
                                + limpar(professor.getNome()) + ";"
                                + limpar(professor.getLogin()) + ";"
                                + limpar(professor.getSenha()) + ";"
                                + limpar(professor.getRegistro()));

            } else if (usuario instanceof Secretaria) {

                Secretaria secretaria = (Secretaria) usuario;

                linhas.add(
                        "SECRETARIA;"
                                + limpar(secretaria.getNome()) + ";"
                                + limpar(secretaria.getLogin()) + ";"
                                + limpar(secretaria.getSenha()) + ";"
                                + limpar(secretaria.getRegistro()));
            }
        }

        sobrescreverArquivo(
                "usuarios.txt",
                linhas);
    }

    public static List<Usuario> carregarUsuarios() {

        List<Usuario> usuarios = new ArrayList<>();

        for (String linha : lerArquivo("usuarios.txt")) {

            if (linha.isBlank()) {
                continue;
            }

            String[] dados = linha.split(";", -1);

            if (dados.length < 5) {
                continue;
            }

            switch (dados[0]) {

                case "ALUNO":

                    usuarios.add(
                            new Aluno(
                                    dados[1],
                                    dados[2],
                                    dados[3],
                                    dados[4]));

                    break;

                case "PROFESSOR":

                    usuarios.add(
                            new Professor(
                                    dados[1],
                                    dados[2],
                                    dados[3],
                                    dados[4]));

                    break;

                case "SECRETARIA":

                    usuarios.add(
                            new Secretaria(
                                    dados[1],
                                    dados[2],
                                    dados[3],
                                    dados[4]));

                    break;
            }
        }

        return usuarios;
    }

    // ==================================================
    // CURSOS
    // ==================================================

    public static void salvarCursos(
            List<Curso> cursos) {

        List<String> linhas = new ArrayList<>();

        for (Curso curso : cursos) {

            linhas.add(
                    limpar(curso.getNome())
                            + ";"
                            + curso.getNumeroCreditos());
        }

        sobrescreverArquivo(
                "cursos.txt",
                linhas);
    }

    public static List<Curso> carregarCursos() {

        List<Curso> cursos = new ArrayList<>();

        for (String linha : lerArquivo("cursos.txt")) {

            if (linha.isBlank()) {
                continue;
            }

            String[] dados = linha.split(";", -1);

            if (dados.length < 2) {
                continue;
            }

            try {

                cursos.add(
                        new Curso(
                                dados[0],
                                Integer.parseInt(dados[1])));

            } catch (NumberFormatException e) {

                System.out.println(
                        "Curso inválido no arquivo: "
                                + linha);
            }
        }

        return cursos;
    }

    // ==================================================
    // DISCIPLINAS
    // ==================================================

    public static void salvarDisciplinas(
            List<Disciplina> disciplinas) {

        List<String> linhas = new ArrayList<>();

        for (Disciplina disciplina : disciplinas) {

            String registroProfessor = "";

            if (disciplina.getProfessor() != null) {

                registroProfessor = disciplina
                        .getProfessor()
                        .getRegistro();
            }

            String nomeCurso = "";

            if (disciplina.getCurso() != null) {
                nomeCurso = disciplina.getCurso().getNome();
            }

            linhas.add(
                    limpar(disciplina.getCodigo()) + ";"
                            + limpar(disciplina.getNome()) + ";"
                            + disciplina.getTipo() + ";"
                            + disciplina.getStatus() + ";"
                            + limpar(registroProfessor) + ";"
                            + limpar(nomeCurso));
        }

        sobrescreverArquivo(
                "disciplinas.txt",
                linhas);
    }

    public static List<Disciplina> carregarDisciplinas(
            List<Usuario> usuarios,
            List<Curso> cursos) {

        List<Disciplina> disciplinas = new ArrayList<>();

        for (String linha : lerArquivo("disciplinas.txt")) {

            if (linha.isBlank()) {
                continue;
            }

            String[] dados = linha.split(";", -1);

            if (dados.length < 5) {
                continue;
            }

            Professor professor = buscarProfessor(
                    usuarios,
                    dados[4]);

            try {

                Disciplina disciplina = new Disciplina(
                        dados[0],
                        dados[1],
                        TipoDisciplina.valueOf(
                                dados[2]),
                        StatusDisciplina.valueOf(
                                dados[3]),
                        professor);

                if (dados.length >= 6 && !dados[5].isBlank()) {
                    Curso curso = buscarCurso(cursos, dados[5]);

                    if (curso != null) {
                        disciplina.setCurso(curso);
                        curso.adicionarDisciplina(disciplina);
                    }
                }

                disciplinas.add(
                        disciplina);

            } catch (IllegalArgumentException e) {

                System.out.println(
                        "Disciplina inválida "
                                + "no arquivo: "
                                + linha);
            }
        }

        return disciplinas;
    }

    // ==================================================
    // CURRÍCULO
    // ==================================================

    public static void salvarCurriculo(
            Curriculo curriculo) {

        List<String> linhas = new ArrayList<>();

        if (curriculo != null) {

            linhas.add(
                    limpar(curriculo.getSemestre())
                            + ";"
                            + curriculo
                                    .isPeriodoMatriculaAberto());

            for (Disciplina disciplina : curriculo.getDisciplinas()) {

                linhas.add(
                        limpar(
                                disciplina.getCodigo()));
            }
        }

        sobrescreverArquivo(
                "curriculo.txt",
                linhas);
    }

    public static Curriculo carregarCurriculo(
            List<Disciplina> disciplinas) {

        List<String> linhas = lerArquivo("curriculo.txt");

        if (linhas.isEmpty()) {
            return null;
        }

        String[] cabecalho = linhas.get(0).split(";", -1);

        if (cabecalho.length < 2) {
            return null;
        }

        Curriculo curriculo = new Curriculo(
                cabecalho[0]);

        for (int i = 1; i < linhas.size(); i++) {

            String codigo = linhas.get(i).trim();

            Disciplina disciplina = buscarDisciplina(
                    disciplinas,
                    codigo);

            if (disciplina != null) {

                curriculo
                        .adicionarDisciplina(
                                disciplina);
            }
        }

        boolean periodoAberto = Boolean.parseBoolean(
                cabecalho[1]);

        curriculo.setPeriodoMatriculaAberto(periodoAberto);

        return curriculo;
    }

    // ==================================================
    // MATRÍCULAS
    // ==================================================

    public static void salvarMatriculas(
            List<Usuario> usuarios) {

        List<String> linhas = new ArrayList<>();

        for (Usuario usuario : usuarios) {

            if (!(usuario instanceof Aluno)) {
                continue;
            }

            Aluno aluno = (Aluno) usuario;

            for (Matricula matricula : aluno.getMatriculas()) {

                linhas.add(
                        limpar(
                                aluno.getMatricula())
                                + ";"
                                + limpar(
                                        matricula
                                                .getDisciplina()
                                                .getCodigo())
                                + ";"
                                + matricula.isAtiva());
            }
        }

        sobrescreverArquivo(
                "matriculas.txt",
                linhas);
    }

    public static void carregarMatriculas(
            List<Usuario> usuarios,
            List<Disciplina> disciplinas) {

        for (String linha : lerArquivo("matriculas.txt")) {

            if (linha.isBlank()) {
                continue;
            }

            String[] dados = linha.split(";", -1);

            if (dados.length < 3) {
                continue;
            }

            Aluno aluno = buscarAluno(
                    usuarios,
                    dados[0]);

            Disciplina disciplina = buscarDisciplina(
                    disciplinas,
                    dados[1]);

            if (aluno == null
                    || disciplina == null) {

                continue;
            }

            boolean ativa = Boolean.parseBoolean(
                    dados[2]);

            Matricula matricula = new Matricula(
                    aluno,
                    disciplina);

            matricula.setAtiva(ativa);

            aluno.getMatriculas()
                    .add(matricula);

            disciplina
                    .adicionarMatricula(
                            matricula);
        }
    }

    // ==================================================
    // SALVAR TUDO
    // ==================================================

    public static void salvarTudo(
            List<Usuario> usuarios,
            Secretaria secretaria,
            Curriculo curriculo) {

        salvarUsuarios(usuarios);

        if (secretaria != null) {

            salvarCursos(
                    secretaria.getCursos());

            salvarDisciplinas(
                    secretaria.getDisciplinas());
        }

        salvarMatriculas(usuarios);

        salvarCurriculo(curriculo);
    }

    // ==================================================
    // BUSCAS AUXILIARES
    // ==================================================

    private static Professor buscarProfessor(
            List<Usuario> usuarios,
            String registro) {

        for (Usuario usuario : usuarios) {

            if (usuario instanceof Professor) {

                Professor professor = (Professor) usuario;

                if (professor
                        .getRegistro()
                        .equalsIgnoreCase(
                                registro)) {

                    return professor;
                }
            }
        }

        return null;
    }

    private static Aluno buscarAluno(
            List<Usuario> usuarios,
            String matricula) {

        for (Usuario usuario : usuarios) {

            if (usuario instanceof Aluno) {

                Aluno aluno = (Aluno) usuario;

                if (aluno
                        .getMatricula()
                        .equalsIgnoreCase(
                                matricula)) {

                    return aluno;
                }
            }
        }

        return null;
    }

    private static Disciplina buscarDisciplina(
            List<Disciplina> disciplinas,
            String codigo) {

        for (Disciplina disciplina : disciplinas) {

            if (disciplina
                    .getCodigo()
                    .equalsIgnoreCase(
                            codigo)) {

                return disciplina;
            }
        }

        return null;
    }

    private static Curso buscarCurso(
            List<Curso> cursos,
            String nome) {

        for (Curso curso : cursos) {
            if (curso.getNome().equalsIgnoreCase(nome)) {
                return curso;
            }
        }

        return null;
    }

    private static String limpar(
            String valor) {

        if (valor == null) {
            return "";
        }

        return valor.replace(
                ";",
                ",");
    }
}