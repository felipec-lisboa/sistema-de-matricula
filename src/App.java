import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import model.*;
import persistence.GerenciadorArquivos;

public class App {

        private static final Scanner scanner = new Scanner(System.in);

        private static final List<Usuario> usuarios = new ArrayList<>();

        private static Secretaria secretaria;
        private static Curriculo curriculo;

        public static void main(String[] args) {

                GerenciadorArquivos
                                .inicializarArquivos();

                carregarSistema();

                boolean executando = true;

                while (executando) {

                        System.out.println(
                                        "\n==============================");
                        System.out.println(
                                        "    SISTEMA DE MATRÍCULAS");
                        System.out.println(
                                        "==============================");
                        System.out.println("1 - Login");
                        System.out.println("0 - Sair");
                        System.out.print("Escolha: ");

                        String opcao = scanner.nextLine();

                        switch (opcao) {

                                case "1":
                                        realizarLogin();
                                        break;

                                case "0":

                                        salvarSistema();

                                        executando = false;

                                        System.out.println(
                                                        "Sistema encerrado.");

                                        break;

                                default:
                                        System.out.println(
                                                        "Opção inválida.");
                        }
                }

                scanner.close();
        }

        // ==================================================
        // CARREGAMENTO / PERSISTÊNCIA
        // ==================================================

        private static void carregarSistema() {

                List<Usuario> carregados = GerenciadorArquivos
                                .carregarUsuarios();

                usuarios.addAll(carregados);

                if (usuarios.isEmpty()) {

                        criarUsuariosIniciais();

                        GerenciadorArquivos
                                        .salvarUsuarios(
                                                        usuarios);
                }

                for (Usuario usuario : usuarios) {

                        if (usuario instanceof Secretaria) {

                                secretaria = (Secretaria) usuario;

                                break;
                        }
                }

                if (secretaria == null) {

                        secretaria = new Secretaria(
                                        "Secretaria",
                                        "secretaria",
                                        "123",
                                        "S001");

                        usuarios.add(secretaria);
                }

                // Reassocia alunos e professores
                // carregados à Secretaria.
                for (Usuario usuario : usuarios) {

                        if (usuario instanceof Aluno) {

                                secretaria.cadastrarAluno(
                                                (Aluno) usuario);

                        } else if (usuario instanceof Professor) {

                                secretaria
                                                .cadastrarProfessor(
                                                                (Professor) usuario);
                        }
                }

                // Cursos
                List<Curso> cursos = GerenciadorArquivos
                                .carregarCursos();

                for (Curso curso : cursos) {
                        secretaria.cadastrarCurso(curso);
                }

                // Disciplinas
                List<Disciplina> disciplinas = GerenciadorArquivos
                                .carregarDisciplinas(
                                                usuarios, secretaria.getCursos());

                for (Disciplina disciplina : disciplinas) {

                        secretaria
                                        .cadastrarDisciplina(
                                                        disciplina);
                }

                // Currículo
                curriculo = GerenciadorArquivos
                                .carregarCurriculo(
                                                disciplinas);

                if (curriculo != null) {

                        secretaria.setCurriculoAtual(
                                        curriculo);
                }

                // Matrículas precisam ser carregadas
                // depois dos usuários e disciplinas.
                GerenciadorArquivos
                                .carregarMatriculas(
                                                usuarios,
                                                disciplinas);
        }

        private static void criarUsuariosIniciais() {

                secretaria = new Secretaria(
                                "Secretaria",
                                "secretaria",
                                "123",
                                "S001");

                Professor professor = new Professor(
                                "Professor Teste",
                                "professor",
                                "123",
                                "P001");

                Aluno aluno = new Aluno(
                                "Aluno Teste",
                                "aluno",
                                "123",
                                "A001");

                usuarios.add(secretaria);
                usuarios.add(professor);
                usuarios.add(aluno);
        }

        private static void salvarSistema() {

                GerenciadorArquivos.salvarTudo(
                                usuarios,
                                secretaria,
                                curriculo);
        }

        // ==================================================
        // LOGIN
        // ==================================================

        private static void realizarLogin() {

                System.out.print("\nLogin: ");
                String login = scanner.nextLine();

                System.out.print("Senha: ");
                String senha = scanner.nextLine();

                Usuario usuarioEncontrado = null;

                for (Usuario usuario : usuarios) {

                        if (usuario.autenticar(
                                        login,
                                        senha)) {

                                usuarioEncontrado = usuario;

                                break;
                        }
                }

                if (usuarioEncontrado == null) {

                        System.out.println(
                                        "Login ou senha inválidos.");

                        return;
                }

                System.out.println(
                                "\nBem-vindo, "
                                                + usuarioEncontrado.getNome()
                                                + "!");

                if (usuarioEncontrado instanceof Aluno) {

                        menuAluno(
                                        (Aluno) usuarioEncontrado);

                } else if (usuarioEncontrado instanceof Professor) {

                        menuProfessor(
                                        (Professor) usuarioEncontrado);

                } else if (usuarioEncontrado instanceof Secretaria) {

                        menuSecretaria(
                                        (Secretaria) usuarioEncontrado);
                }
        }

        // ==================================================
        // MENU ALUNO
        // ==================================================

        private static void menuAluno(Aluno aluno) {

                boolean logado = true;

                while (logado) {

                        System.out.println("\n===== MENU ALUNO =====");
                        System.out.println("1 - Visualizar disciplinas");
                        System.out.println("2 - Realizar matrícula");
                        System.out.println("3 - Cancelar matrícula");
                        System.out.println("4 - Minhas disciplinas");
                        System.out.println("0 - Logout");
                        System.out.print("Escolha: ");

                        String opcao = scanner.nextLine();

                        switch (opcao) {
                                case "1":
                                        listarDisciplinas();
                                        break;
                                case "2":
                                        realizarMatricula(aluno);
                                        break;
                                case "3":
                                        cancelarMatricula(aluno);
                                        break;
                                case "4":
                                        listarDisciplinasAluno(aluno);
                                        break;
                                case "0":
                                        logado = false;
                                        break;
                                default:
                                        System.out.println("Opção inválida.");
                                        break;
                        }

                        if (logado) {
                                aguardarEnter();
                        }
                }
        }

        // ==================================================
        // MENU PROFESSOR
        // ==================================================

        private static void menuProfessor(Professor professor) {

                boolean logado = true;

                while (logado) {

                        System.out.println("\n===== MENU PROFESSOR =====");
                        System.out.println("1 - Consultar alunos");
                        System.out.println("0 - Logout");
                        System.out.print("Escolha: ");

                        String opcao = scanner.nextLine();

                        switch (opcao) {
                                case "1":
                                        consultarAlunosProfessor(professor);
                                        break;
                                case "0":
                                        logado = false;
                                        break;
                                default:
                                        System.out.println("Opção inválida.");
                                        break;
                        }

                        if (logado) {
                                aguardarEnter();
                        }
                }
        }

        // ==================================================
        // MENU SECRETARIA
        // ==================================================

        private static void menuSecretaria(Secretaria secretaria) {

                boolean logado = true;

                while (logado) {

                        System.out.println("\n===== MENU SECRETARIA =====");
                        System.out.println("1 - Cadastrar aluno");
                        System.out.println("2 - Editar aluno");
                        System.out.println("3 - Remover aluno");
                        System.out.println("4 - Listar alunos");
                        System.out.println("5 - Cadastrar professor");
                        System.out.println("6 - Editar professor");
                        System.out.println("7 - Remover professor");
                        System.out.println("8 - Listar professores");
                        System.out.println("9 - Cadastrar disciplina");
                        System.out.println("10 - Editar disciplina");
                        System.out.println("11 - Remover disciplina");
                        System.out.println("12 - Listar disciplinas");
                        System.out.println("13 - Cadastrar curso");
                        System.out.println("14 - Editar curso");
                        System.out.println("15 - Remover curso");
                        System.out.println("16 - Listar cursos");
                        System.out.println("17 - Gerar currículo");
                        System.out.println("18 - Abrir período de matrícula");
                        System.out.println("19 - Encerrar período de matrícula");
                        System.out.println("20 - Ver status do período");
                        System.out.println("0 - Logout");
                        System.out.print("Escolha: ");

                        String opcao = scanner.nextLine();

                        switch (opcao) {
                                case "1":
                                        cadastrarAluno(secretaria);
                                        break;
                                case "2":
                                        editarAluno(secretaria);
                                        break;
                                case "3":
                                        removerAluno(secretaria);
                                        break;
                                case "4":
                                        listarAlunos(secretaria);
                                        break;
                                case "5":
                                        cadastrarProfessor(secretaria);
                                        break;
                                case "6":
                                        editarProfessor(secretaria);
                                        break;
                                case "7":
                                        removerProfessor(secretaria);
                                        break;
                                case "8":
                                        listarProfessores(secretaria);
                                        break;
                                case "9":
                                        cadastrarDisciplina(secretaria);
                                        break;
                                case "10":
                                        editarDisciplina(secretaria);
                                        break;
                                case "11":
                                        removerDisciplina(secretaria);
                                        break;
                                case "12":
                                        listarDisciplinasSecretaria(secretaria);
                                        break;
                                case "13":
                                        cadastrarCurso(secretaria);
                                        break;
                                case "14":
                                        editarCurso(secretaria);
                                        break;
                                case "15":
                                        removerCurso(secretaria);
                                        break;
                                case "16":
                                        listarCursos(secretaria);
                                        break;
                                case "17":
                                        gerarCurriculo(secretaria);
                                        break;
                                case "18":
                                        if (curriculo == null) {
                                                System.out.println("Gere um currículo primeiro.");
                                                break;
                                        }
                                        secretaria.abrirPeriodoMatricula();
                                        salvarSistema();
                                        System.out.println("Período de matrícula aberto.");
                                        break;
                                case "19":
                                        if (curriculo == null) {
                                                System.out.println("Nenhum currículo disponível.");
                                                break;
                                        }
                                        secretaria.encerrarPeriodoMatricula();
                                        salvarSistema();
                                        System.out.println("Período de matrícula encerrado.");
                                        break;
                                case "20":
                                        if (curriculo == null) {
                                                System.out.println("Nenhum currículo disponível.");
                                        } else if (curriculo.isPeriodoMatriculaAberto()) {
                                                System.out.println("Período de matrícula: ABERTO");
                                        } else {
                                                System.out.println("Período de matrícula: FECHADO");
                                        }
                                        break;
                                case "0":
                                        logado = false;
                                        break;
                                default:
                                        System.out.println("Opção inválida.");
                                        break;
                        }

                        if (logado) {
                                aguardarEnter();
                        }
                }
        }

        // ==================================================
        // ALUNO
        // ==================================================

        private static void listarDisciplinas() {

                if (curriculo == null) {

                        System.out.println(
                                        "Nenhum currículo disponível.");

                        return;
                }

                System.out.println(
                                "\n===== DISCIPLINAS =====");

                if (curriculo
                                .getDisciplinas()
                                .isEmpty()) {

                        System.out.println(
                                        "Nenhuma disciplina disponível.");

                        return;
                }

                for (Disciplina disciplina : curriculo.getDisciplinas()) {

                        System.out.println(
                                        disciplina.getCodigo()
                                                        + " - "
                                                        + disciplina.getNome()
                                                        + " | "
                                                        + disciplina.getTipo()
                                                        + " | "
                                                        + disciplina.getStatus()
                                                        + " | Alunos: "
                                                        + disciplina
                                                                        .contarMatriculasAtivas()
                                                        + "/60");
                }
        }

        private static void realizarMatricula(
                        Aluno aluno) {

                if (curriculo == null) {

                        System.out.println(
                                        "Nenhum currículo disponível.");

                        return;
                }

                if (!curriculo
                                .isPeriodoMatriculaAberto()) {

                        System.out.println(
                                        "O período de matrícula "
                                                        + "está fechado.");

                        return;
                }

                listarDisciplinas();

                System.out.print(
                                "\nCódigo da disciplina: ");

                String codigo = scanner.nextLine();

                Disciplina disciplina = buscarDisciplina(codigo);

                if (disciplina == null) {

                        System.out.println(
                                        "Disciplina não encontrada.");

                        return;
                }

                if (aluno.matricular(
                                disciplina,
                                curriculo)) {

                        salvarSistema();

                        System.out.println(
                                        "Matrícula realizada "
                                                        + "com sucesso.");

                } else {

                        System.out.println(
                                        "Não foi possível "
                                                        + "realizar a matrícula.");
                }
        }

        private static void cancelarMatricula(
                        Aluno aluno) {

                if (curriculo == null) {

                        System.out.println(
                                        "Nenhum currículo disponível.");

                        return;
                }

                if (!curriculo
                                .isPeriodoMatriculaAberto()) {

                        System.out.println(
                                        "O período de matrícula "
                                                        + "está fechado.");

                        return;
                }

                listarDisciplinasAluno(aluno);

                System.out.print(
                                "\nCódigo da disciplina: ");

                String codigo = scanner.nextLine();

                Disciplina disciplina = buscarDisciplina(codigo);

                if (disciplina == null) {

                        System.out.println(
                                        "Disciplina não encontrada.");

                        return;
                }

                if (aluno.cancelarMatricula(
                                disciplina,
                                curriculo)) {

                        salvarSistema();

                        System.out.println(
                                        "Matrícula cancelada "
                                                        + "com sucesso.");

                } else {

                        System.out.println(
                                        "Não foi possível "
                                                        + "cancelar a matrícula.");
                }
        }

        private static void listarDisciplinasAluno(
                        Aluno aluno) {

                System.out.println(
                                "\n===== MINHAS DISCIPLINAS =====");

                List<Disciplina> disciplinas = aluno.visualizarDisciplinas();

                if (disciplinas.isEmpty()) {

                        System.out.println(
                                        "Nenhuma matrícula ativa.");

                        return;
                }

                for (Disciplina disciplina : disciplinas) {

                        System.out.println(
                                        disciplina.getCodigo()
                                                        + " - "
                                                        + disciplina.getNome()
                                                        + " | "
                                                        + disciplina.getTipo());
                }
        }

        // ==================================================
        // PROFESSOR
        // ==================================================

        private static void consultarAlunosProfessor(
                        Professor professor) {

                if (curriculo == null) {

                        System.out.println(
                                        "Nenhum currículo disponível.");

                        return;
                }

                System.out.println(
                                "\n===== SUAS DISCIPLINAS =====");

                boolean encontrou = false;

                for (Disciplina disciplina : curriculo.getDisciplinas()) {

                        if (disciplina.getProfessor() == professor) {

                                encontrou = true;

                                System.out.println(
                                                disciplina.getCodigo()
                                                                + " - "
                                                                + disciplina.getNome());
                        }
                }

                if (!encontrou) {

                        System.out.println(
                                        "Nenhuma disciplina encontrada.");

                        return;
                }

                System.out.print(
                                "\nCódigo da disciplina: ");

                String codigo = scanner.nextLine();

                Disciplina disciplina = buscarDisciplina(codigo);

                if (disciplina == null
                                || disciplina.getProfessor() != professor) {

                        System.out.println(
                                        "Disciplina inválida.");

                        return;
                }

                List<Aluno> alunos = professor.consultarAlunos(
                                disciplina);

                System.out.println(
                                "\n===== ALUNOS MATRICULADOS =====");

                if (alunos.isEmpty()) {

                        System.out.println(
                                        "Nenhum aluno matriculado.");

                        return;
                }

                for (Aluno aluno : alunos) {

                        System.out.println(
                                        aluno.getMatricula()
                                                        + " - "
                                                        + aluno.getNome());
                }
        }

        // ==================================================
        // CADASTROS
        // ==================================================

        private static void cadastrarAluno(
                        Secretaria secretaria) {

                System.out.println(
                                "\n===== CADASTRAR ALUNO =====");

                System.out.print("Nome: ");
                String nome = scanner.nextLine();

                System.out.print("Login: ");
                String login = scanner.nextLine();

                System.out.print("Senha: ");
                String senha = scanner.nextLine();

                System.out.print("Matrícula: ");
                String matricula = scanner.nextLine();

                Aluno aluno = new Aluno(
                                nome,
                                login,
                                senha,
                                matricula);

                secretaria.cadastrarAluno(
                                aluno);

                usuarios.add(aluno);

                salvarSistema();

                System.out.println(
                                "Aluno cadastrado com sucesso.");
        }

        private static void cadastrarProfessor(
                        Secretaria secretaria) {

                System.out.println(
                                "\n===== CADASTRAR PROFESSOR =====");

                System.out.print("Nome: ");
                String nome = scanner.nextLine();

                System.out.print("Login: ");
                String login = scanner.nextLine();

                System.out.print("Senha: ");
                String senha = scanner.nextLine();

                System.out.print("Registro: ");
                String registro = scanner.nextLine();

                Professor professor = new Professor(
                                nome,
                                login,
                                senha,
                                registro);

                secretaria.cadastrarProfessor(
                                professor);

                usuarios.add(professor);

                salvarSistema();

                System.out.println(
                                "Professor cadastrado "
                                                + "com sucesso.");
        }

        private static void cadastrarDisciplina(Secretaria secretaria) {

                System.out.println("\n===== CADASTRAR DISCIPLINA =====");

                if (secretaria.getProfessores().isEmpty()) {
                        System.out.println("Cadastre um professor primeiro.");
                        return;
                }

                if (secretaria.getCursos().isEmpty()) {
                        System.out.println("Cadastre um curso primeiro.");
                        return;
                }

                System.out.print("Código: ");
                String codigo = scanner.nextLine();

                System.out.print("Nome: ");
                String nome = scanner.nextLine();

                System.out.println("1 - Obrigatória");
                System.out.println("2 - Optativa");
                System.out.print("Tipo: ");

                String opcaoTipo = scanner.nextLine();

                TipoDisciplina tipo;

                if (opcaoTipo.equals("1")) {
                        tipo = TipoDisciplina.OBRIGATORIA;
                } else if (opcaoTipo.equals("2")) {
                        tipo = TipoDisciplina.OPTATIVA;
                } else {
                        System.out.println("Tipo inválido.");
                        return;
                }

                // ==============================
                // PROFESSOR
                // ==============================

                System.out.println("\n===== PROFESSORES =====");

                for (Professor professor : secretaria.getProfessores()) {
                        System.out.println(
                                        professor.getRegistro()
                                                        + " - "
                                                        + professor.getNome());
                }

                System.out.print("Registro do professor: ");
                String registro = scanner.nextLine();

                Professor professorEncontrado = null;

                for (Professor professor : secretaria.getProfessores()) {

                        if (professor.getRegistro().equalsIgnoreCase(registro)) {
                                professorEncontrado = professor;
                                break;
                        }
                }

                if (professorEncontrado == null) {
                        System.out.println("Professor não encontrado.");
                        return;
                }

                // ==============================
                // CURSO
                // ==============================

                System.out.println("\n===== CURSOS =====");

                for (Curso curso : secretaria.getCursos()) {
                        System.out.println(
                                        curso.getNome()
                                                        + " | Créditos: "
                                                        + curso.getNumeroCreditos());
                }

                System.out.print("Nome do curso: ");
                String nomeCurso = scanner.nextLine();

                Curso cursoSelecionado = null;

                for (Curso curso : secretaria.getCursos()) {

                        if (curso.getNome().equalsIgnoreCase(nomeCurso)) {
                                cursoSelecionado = curso;
                                break;
                        }
                }

                if (cursoSelecionado == null) {
                        System.out.println("Curso não encontrado.");
                        return;
                }

                // ==============================
                // CRIA DISCIPLINA
                // ==============================

                Disciplina disciplina = new Disciplina(
                                codigo,
                                nome,
                                tipo,
                                StatusDisciplina.ABERTA,
                                professorEncontrado);

                // Associação Curso -> Disciplina
                cursoSelecionado.adicionarDisciplina(disciplina);

                // Associação Disciplina -> Curso
                disciplina.setCurso(cursoSelecionado);

                secretaria.cadastrarDisciplina(disciplina);

                salvarSistema();

                System.out.println("Disciplina cadastrada com sucesso.");
        }

        private static void cadastrarCurso(
                        Secretaria secretaria) {

                System.out.println(
                                "\n===== CADASTRAR CURSO =====");

                System.out.print("Nome: ");
                String nome = scanner.nextLine();

                System.out.print(
                                "Número de créditos: ");

                int creditos;

                try {

                        creditos = Integer.parseInt(
                                        scanner.nextLine());

                } catch (NumberFormatException e) {

                        System.out.println(
                                        "Número de créditos inválido.");

                        return;
                }

                Curso curso = new Curso(
                                nome,
                                creditos);

                secretaria.cadastrarCurso(
                                curso);

                salvarSistema();

                System.out.println(
                                "Curso cadastrado com sucesso.");
        }

        // ==================================================
        // LISTAGENS
        // ==================================================

        private static void listarAlunos(
                        Secretaria secretaria) {

                System.out.println(
                                "\n===== ALUNOS =====");

                if (secretaria
                                .getAlunos()
                                .isEmpty()) {

                        System.out.println(
                                        "Nenhum aluno cadastrado.");

                        return;
                }

                for (Aluno aluno : secretaria.getAlunos()) {

                        System.out.println(
                                        aluno.getMatricula()
                                                        + " - "
                                                        + aluno.getNome());
                }
        }

        private static void listarProfessores(
                        Secretaria secretaria) {

                System.out.println(
                                "\n===== PROFESSORES =====");

                if (secretaria
                                .getProfessores()
                                .isEmpty()) {

                        System.out.println(
                                        "Nenhum professor cadastrado.");

                        return;
                }

                for (Professor professor : secretaria.getProfessores()) {

                        System.out.println(
                                        professor.getRegistro()
                                                        + " - "
                                                        + professor.getNome());
                }
        }

        private static void listarCursos(
                        Secretaria secretaria) {

                System.out.println(
                                "\n===== CURSOS =====");

                if (secretaria
                                .getCursos()
                                .isEmpty()) {

                        System.out.println(
                                        "Nenhum curso cadastrado.");

                        return;
                }

                for (Curso curso : secretaria.getCursos()) {

                        System.out.println(
                                        curso.getNome()
                                                        + " | Créditos: "
                                                        + curso
                                                                        .getNumeroCreditos());
                }
        }

        // ==================================================
        // CURRÍCULO
        // ==================================================

        private static void gerarCurriculo(
                        Secretaria secretaria) {

                if (secretaria
                                .getDisciplinas()
                                .isEmpty()) {

                        System.out.println(
                                        "Cadastre pelo menos "
                                                        + "uma disciplina primeiro.");

                        return;
                }

                System.out.print(
                                "\nSemestre (ex: 2026/2): ");

                String semestre = scanner.nextLine();

                curriculo = secretaria.gerarCurriculo(
                                semestre);

                salvarSistema();

                System.out.println(
                                "Currículo "
                                                + semestre
                                                + " gerado com sucesso.");
        }

        // ==================================================
        // BUSCAS
        // ==================================================

        private static Disciplina buscarDisciplina(
                        String codigo) {

                if (curriculo == null) {
                        return null;
                }

                for (Disciplina disciplina : curriculo.getDisciplinas()) {

                        if (disciplina
                                        .getCodigo()
                                        .equalsIgnoreCase(
                                                        codigo)) {

                                return disciplina;
                        }
                }

                return null;
        }

        private static void listarDisciplinasSecretaria(
                        Secretaria secretaria) {

                System.out.println(
                                "\n===== DISCIPLINAS CADASTRADAS =====");

                if (secretaria
                                .getDisciplinas()
                                .isEmpty()) {

                        System.out.println(
                                        "Nenhuma disciplina cadastrada.");

                        return;
                }

                for (Disciplina disciplina : secretaria.getDisciplinas()) {

                        String professor = "Sem professor";

                        if (disciplina.getProfessor() != null) {
                                professor = disciplina
                                                .getProfessor()
                                                .getNome();
                        }

                        System.out.println(
                                        disciplina.getCodigo()
                                                        + " - "
                                                        + disciplina.getNome()
                                                        + " | "
                                                        + disciplina.getTipo()
                                                        + " | "
                                                        + disciplina.getStatus()
                                                        + " | Professor: "
                                                        + professor
                                                        + " | Alunos: "
                                                        + disciplina
                                                                        .contarMatriculasAtivas()
                                                        + "/60");
                }
        }

        // ==================================================
        // EDIÇÃO / REMOÇÃO - CRUD SECRETARIA
        // ==================================================

        private static void editarAluno(Secretaria secretaria) {
                listarAlunos(secretaria);
                System.out.print("Matrícula do aluno: ");
                Aluno aluno = buscarAlunoSecretaria(secretaria, scanner.nextLine());
                if (aluno == null) {
                        System.out.println("Aluno não encontrado.");
                        return;
                }

                System.out.print("Novo nome (Enter mantém): ");
                String valor = scanner.nextLine();
                if (!valor.isBlank())
                        aluno.setNome(valor);
                System.out.print("Novo login (Enter mantém): ");
                valor = scanner.nextLine();
                if (!valor.isBlank())
                        aluno.setLogin(valor);
                System.out.print("Nova senha (Enter mantém): ");
                valor = scanner.nextLine();
                if (!valor.isBlank())
                        aluno.setSenha(valor);
                System.out.print("Nova matrícula (Enter mantém): ");
                valor = scanner.nextLine();
                if (!valor.isBlank())
                        aluno.setMatricula(valor);

                salvarSistema();
                System.out.println("Aluno editado com sucesso.");
        }

        private static void removerAluno(Secretaria secretaria) {
                listarAlunos(secretaria);
                System.out.print("Matrícula do aluno: ");
                Aluno aluno = buscarAlunoSecretaria(secretaria, scanner.nextLine());
                if (aluno == null) {
                        System.out.println("Aluno não encontrado.");
                        return;
                }

                for (Matricula matricula : aluno.getMatriculas()) {
                        if (matricula.isAtiva()) {
                                System.out.println("Não é possível remover aluno com matrícula ativa.");
                                return;
                        }
                }
                secretaria.removerAluno(aluno);
                usuarios.remove(aluno);
                salvarSistema();
                System.out.println("Aluno removido com sucesso.");
        }

        private static void editarProfessor(Secretaria secretaria) {
                listarProfessores(secretaria);
                System.out.print("Registro do professor: ");
                Professor professor = buscarProfessorSecretaria(secretaria, scanner.nextLine());
                if (professor == null) {
                        System.out.println("Professor não encontrado.");
                        return;
                }

                System.out.print("Novo nome (Enter mantém): ");
                String valor = scanner.nextLine();
                if (!valor.isBlank())
                        professor.setNome(valor);
                System.out.print("Novo login (Enter mantém): ");
                valor = scanner.nextLine();
                if (!valor.isBlank())
                        professor.setLogin(valor);
                System.out.print("Nova senha (Enter mantém): ");
                valor = scanner.nextLine();
                if (!valor.isBlank())
                        professor.setSenha(valor);
                System.out.print("Novo registro (Enter mantém): ");
                valor = scanner.nextLine();
                if (!valor.isBlank())
                        professor.setRegistro(valor);

                salvarSistema();
                System.out.println("Professor editado com sucesso.");
        }

        private static void removerProfessor(Secretaria secretaria) {
                listarProfessores(secretaria);
                System.out.print("Registro do professor: ");
                Professor professor = buscarProfessorSecretaria(secretaria, scanner.nextLine());
                if (professor == null) {
                        System.out.println("Professor não encontrado.");
                        return;
                }

                for (Disciplina disciplina : secretaria.getDisciplinas()) {
                        if (disciplina.getProfessor() == professor) {
                                System.out.println("Não é possível remover professor associado a uma disciplina.");
                                return;
                        }
                }
                secretaria.removerProfessor(professor);
                usuarios.remove(professor);
                salvarSistema();
                System.out.println("Professor removido com sucesso.");
        }

        private static void editarDisciplina(Secretaria secretaria) {
                listarDisciplinasSecretaria(secretaria);
                System.out.print("Código da disciplina: ");
                Disciplina disciplina = buscarDisciplinaSecretaria(secretaria, scanner.nextLine());
                if (disciplina == null) {
                        System.out.println("Disciplina não encontrada.");
                        return;
                }

                System.out.print("Novo código (Enter mantém): ");
                String valor = scanner.nextLine();
                if (!valor.isBlank())
                        disciplina.setCodigo(valor);
                System.out.print("Novo nome (Enter mantém): ");
                valor = scanner.nextLine();
                if (!valor.isBlank())
                        disciplina.setNome(valor);
                System.out.print("Tipo [1 Obrigatória / 2 Optativa / Enter mantém]: ");
                valor = scanner.nextLine();
                if (valor.equals("1"))
                        disciplina.setTipo(TipoDisciplina.OBRIGATORIA);
                else if (valor.equals("2"))
                        disciplina.setTipo(TipoDisciplina.OPTATIVA);
                else if (!valor.isBlank()) {
                        System.out.println("Tipo inválido.");
                        return;
                }

                listarProfessores(secretaria);
                System.out.print("Novo registro do professor (Enter mantém): ");
                valor = scanner.nextLine();
                if (!valor.isBlank()) {
                        Professor professor = buscarProfessorSecretaria(secretaria, valor);
                        if (professor == null) {
                                System.out.println("Professor não encontrado.");
                                return;
                        }
                        disciplina.setProfessor(professor);
                }

                salvarSistema();
                System.out.println("Disciplina editada com sucesso.");
        }

        private static void removerDisciplina(Secretaria secretaria) {
                listarDisciplinasSecretaria(secretaria);
                System.out.print("Código da disciplina: ");
                Disciplina disciplina = buscarDisciplinaSecretaria(secretaria, scanner.nextLine());
                if (disciplina == null) {
                        System.out.println("Disciplina não encontrada.");
                        return;
                }
                if (disciplina.contarMatriculasAtivas() > 0) {
                        System.out.println("Não é possível remover disciplina com matrícula ativa.");
                        return;
                }

                for (Aluno aluno : secretaria.getAlunos()) {
                        aluno.getMatriculas().removeIf(m -> m.getDisciplina() == disciplina);
                }
                for (Curso curso : secretaria.getCursos())
                        curso.removerDisciplina(disciplina);
                if (curriculo != null)
                        curriculo.removerDisciplina(disciplina);
                secretaria.removerDisciplina(disciplina);
                salvarSistema();
                System.out.println("Disciplina removida com sucesso.");
        }

        private static void editarCurso(Secretaria secretaria) {
                listarCursos(secretaria);
                System.out.print("Nome atual do curso: ");
                Curso curso = buscarCursoSecretaria(secretaria, scanner.nextLine());
                if (curso == null) {
                        System.out.println("Curso não encontrado.");
                        return;
                }

                System.out.print("Novo nome (Enter mantém): ");
                String valor = scanner.nextLine();
                if (!valor.isBlank())
                        curso.setNome(valor);
                System.out.print("Novo número de créditos (Enter mantém): ");
                valor = scanner.nextLine();
                if (!valor.isBlank()) {
                        try {
                                curso.setNumeroCreditos(Integer.parseInt(valor));
                        } catch (NumberFormatException e) {
                                System.out.println("Número de créditos inválido.");
                                return;
                        }
                }
                salvarSistema();
                System.out.println("Curso editado com sucesso.");
        }

        private static void removerCurso(Secretaria secretaria) {
                listarCursos(secretaria);
                System.out.print("Nome do curso: ");
                Curso curso = buscarCursoSecretaria(secretaria, scanner.nextLine());
                if (curso == null) {
                        System.out.println("Curso não encontrado.");
                        return;
                }
                secretaria.removerCurso(curso);
                salvarSistema();
                System.out.println("Curso removido com sucesso.");
        }

        private static Aluno buscarAlunoSecretaria(Secretaria secretaria, String matricula) {
                for (Aluno aluno : secretaria.getAlunos())
                        if (aluno.getMatricula().equalsIgnoreCase(matricula))
                                return aluno;
                return null;
        }

        private static Professor buscarProfessorSecretaria(Secretaria secretaria, String registro) {
                for (Professor professor : secretaria.getProfessores())
                        if (professor.getRegistro().equalsIgnoreCase(registro))
                                return professor;
                return null;
        }

        private static Disciplina buscarDisciplinaSecretaria(Secretaria secretaria, String codigo) {
                for (Disciplina disciplina : secretaria.getDisciplinas())
                        if (disciplina.getCodigo().equalsIgnoreCase(codigo))
                                return disciplina;
                return null;
        }

        private static Curso buscarCursoSecretaria(Secretaria secretaria, String nome) {
                for (Curso curso : secretaria.getCursos())
                        if (curso.getNome().equalsIgnoreCase(nome))
                                return curso;
                return null;
        }

        private static void aguardarEnter() {
                System.out.println("\nPressione Enter para voltar ao menu...");
                scanner.nextLine();
        }

}