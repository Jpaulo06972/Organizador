package com.todolist;

import java.io.Console;
import java.util.List;
import java.util.Scanner;

import com.todolist.dao.UserDAO;
import com.todolist.dao.TaskDAO;
import com.todolist.model.User;
import com.todolist.ui.Menu;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        UserDAO userDAO = new UserDAO();
        TaskDAO taskDAO = new TaskDAO();
        boolean executando = true;

        while (executando){
            System.out.println("\\n====== ToDo List ======"); 
            System.out.println("1. Fazer Login");
            System.out.println("2. Criar Nova Conta");
            System.out.println("3. Recuperar Senha");
            System.out.println("0. Sair");
            System.out.print("Escolha: ");
            int opcao = Integer.parseInt(scanner.nextLine().trim());

            switch (opcao) {
                case 1:
                    login(scanner, userDAO, taskDAO);               
                    break;
                case 2:
                    criarConta(scanner, userDAO);              
                    break;
                case 3:
                    recuperarSenha(scanner, userDAO);
                    break;
                case 0:
                    executando = false;
                    break;
                default:
                    System.out.println("[ERR] Opção inválida.");                    
            } 
        }
    }

    private static void login(Scanner scanner, UserDAO userDAO, TaskDAO taskDAO){
        
        String email = "";
        String senha = "";

        // Validação do email
        while(email.isBlank()){
            System.out.print("Email: ");
            email = scanner.nextLine().trim();
            if (email.isBlank()){
                System.out.println("[ERR] O email não pode estar vazio.");
            }
        }

        // Validação da senha
        while(senha.isBlank()){
            System.out.print("Senha: ");
            senha = scanner.nextLine().trim();
            if (senha .isBlank()){
                System.out.println("[ERR] A senha não pode estar vazia.");
            }
        }

        User user = userDAO.autenticar(email, senha);

        if (user == null) {
            System.out.println("[ERR] Email ou senha inválido.");
            return;
        }

        System.out.println("[OK] Login realizado com sucesso!");
        System.out.println("Bem-vindo, " + user.getNome());

        boolean logado = true;
        Menu menu = new Menu(scanner, taskDAO);

        while (logado) {
            System.out.println("\n====== Menu Principal ======");
            System.out.println("1. Gerenciar Tarefas");
            System.out.println("2. Gerenciar Minha Conta");
            System.out.println("0. Sair");
            System.out.print("Escolha: ");
            int escolha = Integer.parseInt(scanner.nextLine().trim());
            
            switch (escolha) {
                case 1:  
                    menu.exibir(user);                  
                    break;
                case 2:
                    gerenciarConta(scanner, userDAO, user);
                    break;
                case 0:
                    logado = false;
                    break;
                default:
                    System.out.println("[ERR] Opção inválida.");                    
            }
        }      
    }    

    
    public static void criarConta(Scanner scanner, UserDAO userDAO){
        String nome = "";
        String email = "";
        String senha = "";

        // Validação do nome
        while(nome.isBlank()){
            System.out.print("Nome: ");
            nome = scanner.nextLine().trim();
            if (nome.isBlank()){
                System.out.println("[ERR] O nome não pode estar vazio.");
            }
        }

        // Validação do email
        while(email.isBlank()){
            System.out.print("Email: ");
            email = scanner.nextLine().trim();
            if (email.isBlank()){
                System.out.println("[ERR] O email não pode estar vazio.");
            }
        }

        // Validação da senha
        while(senha.isBlank()){  
            System.out.print("Senha: ");
            senha = scanner.nextLine().trim();
            if (senha .isBlank()){
                System.out.println("[ERR] A senha não pode estar vazia.");
            }
        }
        

        User novo = new User();
        novo.setNome(nome);
        novo.setEmail(email);
        novo.setSenha(senha);

        try {
            userDAO.criar(novo);
            System.out.println("[OK] Conta criada com sucesso! Agora você pode fazer login.");
        } catch (Exception e){
            System.out.println("[ERR] Erro ao criar conta:" + e.getMessage());
        }   
    }

    public static void recuperarSenha(Scanner scanner, UserDAO userDAO){
        String email = "";
        String senha = "";

        // Validação do email
        while(email.isBlank()){
            System.out.print("Email: ");
            email = scanner.nextLine().trim();
            if (email.isBlank()){
                System.out.println("[ERR] O email não pode estar vazio.");
            }
        }

        // Validação da senha
        while(senha.isBlank()){
            System.out.print("Senha: ");
            senha = scanner.nextLine().trim();            
            if (senha .isBlank()){
                System.out.println("[ERR] A senha não pode estar vazia.");
            }
        }

        try {
            userDAO.recuperarSenha(email, senha);
            System.out.println("[OK] Senha recuperada com sucesso! Agora você pode fazer login.");
        } catch (Exception e){
            System.out.println("[ERR] Erro ao recuperar senha:" + e.getMessage());
        }   
    }

    public static void gerenciarConta(Scanner scanner, UserDAO userDAO, User user){
        boolean gerenciar = true;

        while(gerenciar){
            System.out.println("\n--- Gerenciar Conta ---");
            System.out.println("1. Atualizar Nome");
            System.out.println("2. Atualizar Senha");
            System.out.println("3. Deletar Conta");
            System.out.println("0. Sair");
            System.out.print("Escolha: ");
            int funcao = Integer.parseInt(scanner.nextLine().trim());
            
            switch (funcao) {
                case 1:
                    System.out.print("Novo Nome: ");
                    String novoNome = scanner.nextLine().trim();
                    if (novoNome.isBlank()){
                        System.out.println("[ERR] O nome não pode estar vazio.");
                    } else {
                        userDAO.atualizarNome(user.getId(), novoNome);
                        user.setNome(novoNome);
                        System.out.println("[OK] Nome atualizado com sucesso.");
                    }
                    break;

                case 2:
                    String novaSenha = "";
                    // Validação da senha
                    while(novaSenha.isBlank()){
                        System.out.print("Senha: ");
                        novaSenha = scanner.nextLine().trim();                
                        if (novaSenha.isBlank()){
                            System.out.println("[ERR] A senha não pode estar vazia.");
                        } else {
                            userDAO.atualizarSenha(user.getId(), novaSenha);
                            System.out.println("[OK] Senha atualizado com sucesso.");   
                            break;                        
                        }
                    }
                    break; 
                case 3:
                    System.out.print("Tem certeza que deseja deletar sua conta? (s/n): ");
                    String confirmacao = scanner.nextLine().trim().toLowerCase();
                    if (confirmacao.equals("s")){
                        userDAO.deletar(user.getId());
                        System.out.println("[OK] Conta deletada com sucesso.");
                    } else {
                        System.out.println("[INFO] Ação cancelada.");
                    }
                    break;

                case 0:  
                    gerenciar = false; 
                    break;  

                default:
                    System.out.println("[ERR] Opção inválida.");  
            }
        }
    }   
}
