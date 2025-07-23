
package com.todolist.ui;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

import com.todolist.dao.TaskDAO;
import com.todolist.model.Task;
import com.todolist.model.User;

public class Menu {
    
    private final Scanner scanner;
    private final TaskDAO taskDAO;

    public Menu(Scanner scanner, TaskDAO taskDAO) {
        this.scanner = scanner;
        this.taskDAO = taskDAO;
    }

    public void exibir(User user) {
        int opcao;
        do {
            System.out.println("\n--- MENU ---");
            System.out.println("1. Criar Tarefa");
            System.out.println("2. Listar Tarefas");
            System.out.println("3. Atualizar Tarefa");
            System.out.println("4. Deletar Tarefa");
            System.out.println("5. Buscar tarefa por ID");
            System.out.println("6. Listar Tarefas por Status OK");
            System.out.println("7. Listar Tarefas por Período");
            System.out.println("0. Sair");
            System.out.print("Escolha: ");
            opcao = Integer.parseInt(scanner.nextLine().trim());            

            switch (opcao) {
                case 1:
                    criarTarefa(user);
                    break;
                case 2:
                    listarTarefas(user);
                    break;
                case 3:
                    atualizarTarefa(user);
                    break;
                case 4:
                    deletarTarefa(user);
                    break;
                case 5:
                    buscarPorId(user);
                    break;
                case 6:
                    listarPorStatus(user);
                    break;
                case 7:
                    listarPorPeriodo(user);
                    break;
                case 0:
                    System.out.println("Saindo...");
                    break;
                default:
                    System.out.println("[ERR] Opção inválida.");
            }            
        } while (opcao != 0);
    }

    private void criarTarefa(User user) {
        
        System.out.print("Título: ");
        String titulo = scanner.nextLine().trim();
        if (titulo.isEmpty()) {
            System.out.println("[ERR] O título não pode estar vazia.");
            return;
        }

        System.out.print("Descrição: ");
        String descricao = scanner.nextLine().trim();
        if (descricao.isEmpty()) {
            System.out.println("[ERR] A descrição não pode estar vazia.");
            return;
        }

        Task novaTarefa = new Task(titulo, descricao, false, user.getId());

        try {
            taskDAO.criar(novaTarefa);
            System.out.println("[OK] Tarefa criada com sucesso!.");
        } catch (Exception e){
            System.out.println("[ERR] Erro ao criar tarefa: " + e.getMessage());            
        }
    }

    private void listarTarefas(User user) {
        List<Task> tarefas = taskDAO.listarPorUsuario(user.getId());
        if (tarefas.isEmpty()) {
            System.out.println("Nenhuma tarefa encontrada.");
        } else {
            for (Task t : tarefas) {
                System.out.println("\nID: " + t.getId());
                System.out.println("Título: " + t.getTitulo());
                System.out.println("Descrição: " + t.getDescricao());
                System.out.println("Concluída: " + (t.isConcluida() ? "Sim" : "Não"));
                System.out.println("Criada em: " + t.getDataCriacao());
                System.out.println("Concluída em: " + (t.getDataConclusao() != null ?  t.getDataConclusao() : "Ainda não concluída"));
            }
        }
    }

    private void atualizarTarefa(User user) {
        System.out.print("ID da tarefa para atualizar: ");
        int id = scanner.nextInt();
        scanner.nextLine(); // Limpa buffer

        Task tarefa = taskDAO.buscarPorId(id);
        if (tarefa == null || tarefa.getUsuarioId() != user.getId()) {
            System.out.println("[ERR] Tarefa não encontrada ou não pertence ao usuário.");
            return;
        }

        System.out.print("Novo título: ");
        String novoTitulo = scanner.nextLine().trim();
        if (novoTitulo.isEmpty()){
            System.out.println("[ERR] O título não pode estar vazio.");
            return;
        }
        tarefa.setTitulo(novoTitulo);

        System.out.print("Novo descrição: ");
        String novaDescricao = scanner.nextLine().trim();
        if (novaDescricao.isEmpty()){
            System.out.println("[ERR] A descrição não pode estar vazia.");
            return;
        }
        tarefa.setDescricao(novaDescricao);

        System.out.print("Está concluída? (true/false): ");
        try{
            boolean concluida = Boolean.parseBoolean(scanner.nextLine().trim());
            tarefa.setConcluida(concluida);
        } catch (Exception e){
            System.out.println("[ERR] Valor inválido para conclusão. Use 'true' ou 'false'.");
        }
        
        try {
            taskDAO.atualizar(tarefa);
            System.out.println("[OK] Tarefa atualizada com sucesso!");
        } catch (Exception e) {
            System.out.println("[ERR] Erro ao atualizar tarefa: " + e.getMessage());
        }
    }

    private void deletarTarefa(User user){
        System.out.print("ID da tarefa para deletar: ");
        int id = scanner.nextInt();
        
        Task tarefa = taskDAO.buscarPorId(id);
        if (tarefa == null || tarefa.getUsuarioId() != user.getId()) {
            System.out.println("[INFO] Tarefa não encontrada ou não pertence ao usuário.");
            return;
        }

        try {
            taskDAO.deletar(id);
            System.out.println("[OK] Tarefa deletada com sucesso!");
        } catch (Exception e) {
            System.out.println("[ERR] Erro ao deletar tarefa: " + e.getMessage());
        }
    }

    private void buscarPorId(User user){
        System.out.print("Digite o ID da tarefa: ");
        int id = scanner.nextInt();
        scanner.nextLine(); // Limpar o buffer
        
        // TaskDAO taskDAO = new TaskDAO();
        Task tarefa = taskDAO.buscarPorId(id);

        if (tarefa != null && tarefa.getUsuarioId() == user.getId()){
            System.out.println("=== Tarefa Encontrada ===");
            System.out.println("ID: " + tarefa.getId());
            System.out.println("Título: " + tarefa.getTitulo());
            System.out.println("Descrição: " + tarefa.getDescricao());
            System.out.println("Concluída: " + (tarefa.isConcluida() ? "Sim" : "Não"));
            System.out.println("Data de Criação: " + tarefa.getDataCriacao());
            System.out.println("Data de Criação: " +
                (tarefa.getDataConclusao() != null ? tarefa.getDataConclusao(): "Não concluída"));
        } else {
            System.out.println("[INFO] Nenhuma tarefa encontrada com esse ID para este usuário.");
        }
    }

    private void listarPorStatus(User user){
        System.out.print("Deseja ver tarefas concluídas? (true/false): ");
        boolean concluida = scanner.nextBoolean();
        scanner.nextLine();

        List<Task> tarefas = taskDAO.listarPorStatus(user.getId(), concluida);

        if (tarefas.isEmpty()){
            System.out.println("[INFO] Nenhuma tarefa encontrada com esse status.");
        } else {
            for (Task tarefa : tarefas){
                System.out.println("ID: " + tarefa.getId() + " | Título: " + tarefa.getTitulo() + " | Concluída: " + tarefa.isConcluida());
            }
        }
    }

    private void listarPorPeriodo(User user){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        try{
            System.out.print("Data início (yyyy-MM-dd HH:mm): ");
            String dataInicioStr = scanner.nextLine();
            LocalDateTime dataInicio = LocalDateTime.parse(dataInicioStr, formatter);

            System.out.print("Data fim (yyyy-MM-dd HH:mm): ");
            String dataFimStr = scanner.nextLine();
            LocalDateTime dataFim = LocalDateTime.parse(dataFimStr, formatter);

            List<Task> tarefas = taskDAO.listarPorPeriodo(user.getId(), dataInicio, dataFim);

            if (tarefas.isEmpty()){
                System.out.println("[INFO] Nenhuma tarefa encontrada no período.");
            } else {
                for (Task t : tarefas){
                    System.out.println(t);
                }
            }
        } catch (Exception e) {
            System.out.println("[ERR] Formato de data inválido ou erro: " + e.getMessage());

        }
    }

    
}
