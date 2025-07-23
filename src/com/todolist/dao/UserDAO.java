package com.todolist.dao;

import java.sql.*;
import java.util.List;
import java.util.ArrayList;

import com.todolist.model.User;
import com.todolist.database.ConnectionFactory;



public class UserDAO {

    // Classe de autenticação
    public User autenticar(String email, String senha) {
        try {
            Connection conn = ConnectionFactory.getConnection();

            String sql = "SELECT * FROM usuarios WHERE email = ? AND senha = SHA2(?, 256)";
            PreparedStatement stmt = conn.prepareStatement(sql);

            stmt.setString(1, email);
            stmt.setString(2, senha);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                User user = new User();
                user.setId(rs.getInt("id"));
                user.setNome(rs.getString("nome"));
                user.setEmail(rs.getString("email"));
                user.setSenha(rs.getString("senha"));
                return user;
            }

            return null;
        } catch (Exception e) {
            throw new RuntimeException("Erro ao autenticar", e);
        }
    }
    
    // Classe para salavar o usuário
    public void criar(User user) {
        String sql = "INSERT INTO usuarios (nome, email, senha) VALUES (?, ?, SHA2(?, 256))";

        try (Connection conn = ConnectionFactory.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, user.getNome());
            stmt.setString(2, user.getEmail());
            stmt.setString(3, user.getSenha());
            stmt.executeUpdate();

            System.out.println("[OK] Usuário criado com sucesso.");
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao criar usuário", e);
        }
    }


    // Comando para listar todos os usuarios 
    public List<User> listarTodos() {
        List<User> usuarios = new ArrayList<>();
        String sql = "SELECT * FROM usuarios";

        try (Connection conn = ConnectionFactory.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                User u = new User();
                u.setId(rs.getInt("id"));
                u.setNome(rs.getString("nome"));
                u.setEmail(rs.getString("email"));
                usuarios.add(u);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar usuários", e);
        }

        return usuarios;
    }


    // Classe para atualizar o usuário
    public void atualizar(User user) {
        String sql = "UPDATE usuarios SET nome = ?, email = ?, senha = SHA2(?, 256) WHERE id = ?";

        try (Connection conn = ConnectionFactory.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, user.getNome());
            stmt.setString(2, user.getEmail());
            stmt.setString(3, user.getSenha());
            stmt.setInt(4, user.getId());
            stmt.executeUpdate();

            System.out.println("[OK] Usuário atualizado com sucesso.");
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar usuário", e);
        }
    }


    public void deletar(int id) {
        String sql = "DELETE FROM usuarios WHERE id = ?"; 

        try (Connection conn = ConnectionFactory.getConnection();            
            PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();

            System.out.println("[OK] Usuário deletado com sucesso.");
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao deletar usuário", e);
        }
    }

    public void recuperarSenha(String email, String novaSenha){
        String sql = "UPDATE usuarios SET senha = SHA2(?, 256) WHERE email = ?";

        try (Connection conn = ConnectionFactory.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {

                stmt.setString(1, novaSenha);
                stmt.setString(2, email);
                int linhasAfetadas = stmt.executeUpdate();

                if (linhasAfetadas > 0){
                    System.out.println("[OK] Senha atualizada com sucesso.");
                } else {
                    System.out.println("[ERR] Nenhum usuário encontrado com esse email.");
                } 

        } catch (SQLException e){
            throw new RuntimeException("Erro ao atualizar senha", e);
        }
    }

    public void atualizarNome(int id, String novoNome){
        String sql = "UPDATE usuarios SET nome = ? WHERE id = ?";

        try (Connection conn = ConnectionFactory.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {

                stmt.setString(1, novoNome);
                stmt.setInt(2, id);
                stmt.executeUpdate();

                System.out.println("[OK] Nome atualizada com sucesso."); 
        } catch (SQLException e){
            throw new RuntimeException("Erro ao atualizar nome do usuário", e);
        }
    }

    public void atualizarSenha(int id, String novaSenha){
        String sql = "UPDATE usuarios SET senha = SHA2(?, 256) WHERE id = ?";

        try (Connection conn = ConnectionFactory.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {

                stmt.setString(1, novaSenha);
                stmt.setInt(2, id);
                stmt.executeUpdate();

                System.out.println("[OK] Senha atualizada com sucesso."); 
        } catch (SQLException e){
            throw new RuntimeException("Erro ao atualizar senha do usuário", e);
        }
    }
}
