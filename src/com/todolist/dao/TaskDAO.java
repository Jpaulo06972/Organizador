
package com.todolist.dao;

import com.todolist.model.Task;
import com.todolist.database.ConnectionFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDateTime; // <- ESTE AQUI

public class TaskDAO {
    
    public void criar(Task tarefa) {
        String sql = "INSERT INTO tarefas (titulo, descricao, concluida, usuario_id, data_criacao, data_conclusao) VALUES (?, ?, ?, ?, ?, ?)";


        try (Connection conn = ConnectionFactory.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, tarefa.getTitulo());
            stmt.setString(2, tarefa.getDescricao());
            stmt.setBoolean(3, tarefa.isConcluida());
            stmt.setInt(4, tarefa.getUsuarioId());

            // Verificação e definição da data de criação
            if (tarefa.getDataCriacao() == null) {
                tarefa.setDataCriacao(LocalDateTime.now());
            }
            
            stmt.setTimestamp(5, Timestamp.valueOf(tarefa.getDataCriacao()));

            if (tarefa.getDataConclusao() != null){
                stmt.setTimestamp(6, Timestamp.valueOf(tarefa.getDataConclusao()));
            } else {
                stmt.setNull(6, java.sql.Types.TIMESTAMP);
            }

            stmt.executeUpdate(); // <-- Aqui é o correto!
            
        } catch (SQLException e) {
            System.err.println("[ERR] Detalhes do erro SQL: " + e.getMessage());
            e.printStackTrace(); // Mostra onde está o problema de verdade
            throw new RuntimeException("Erro ao criar tarefa", e);
        }
    }


    public List<Task> listarPorUsuario(int usuarioId) {
        List<Task> tarefas = new ArrayList<>();
        String sql = "SELECT * FROM tarefas WHERE usuario_id = ?";

        try (Connection conn = ConnectionFactory.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, usuarioId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Task t = new Task(
                    rs.getString("titulo"),
                    rs.getString("descricao"),
                    rs.getBoolean("concluida"),
                    rs.getInt("usuario_id")
                );
                t.setId(rs.getInt("id"));

                // Adicionando data de criação
                Timestamp dataCriacaoTS = rs.getTimestamp("data_criacao");
                if (dataCriacaoTS != null) {
                    t.setDataCriacao(dataCriacaoTS.toLocalDateTime());
                }

                // Adicionando data de conclusão (se houver)
                Timestamp dataConclusaoTS = rs.getTimestamp("data_conclusao");
                if (dataConclusaoTS != null) {
                    t.setDataConclusao(dataConclusaoTS.toLocalDateTime());
                }

                tarefas.add(t);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar tarefas", e);
        }

        return tarefas;
    }


    public void atualizar(Task tarefa) {
        String sql = "UPDATE tarefas SET titulo = ?, descricao = ?, concluida = ?, data_conclusao = ? WHERE id = ?";

        try (Connection conn = ConnectionFactory.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, tarefa.getTitulo());
            stmt.setString(2, tarefa.getDescricao());
            stmt.setBoolean(3, tarefa.isConcluida());
            stmt.setTimestamp(4, tarefa.getDataConclusao() != null ? Timestamp.valueOf(tarefa.getDataConclusao()) : null);            
            stmt.setInt(5, tarefa.getId());

            stmt.executeUpdate(); // <- Correto aqui também

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar tarefa", e);
        }
    }


    public void deletar(int id) {
        String sql = "DELETE FROM tarefas WHERE id = ?";

        try (Connection conn = ConnectionFactory.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();
            System.out.println("[OK] Tarefa deletada com sucesso.");

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao deletar tarefa", e);
        }
    }

    public Task buscarPorId(int id) {
        try (Connection conn = ConnectionFactory.getConnection()){
            String sql = "SELECT * FROM tarefas WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql); 
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Task task = new Task();
                task.setId(rs.getInt("id"));
                task.setTitulo(rs.getString("titulo"));
                task.setDescricao(rs.getString("descricao"));
                task.setConcluida(rs.getBoolean("concluida"));
                task.setUsuarioId(rs.getInt("usuario_id"));

                Timestamp dataCriacao = rs.getTimestamp("data_criacao");
                if (dataCriacao != null){
                    task.setDataCriacao(dataCriacao.toLocalDateTime());
                }

                Timestamp dataConclusao = rs.getTimestamp("data_conclusao");
                if (dataConclusao != null){
                    task.setDataConclusao(dataConclusao.toLocalDateTime());
                }

                return task;

            }
        } catch (SQLException e) {
            System.out.println("[ERR] Erro ao buscar tarefa por ID: " + e.getMessage());
        }
        return null;
    }

    public List<Task> listarPorStatus(int usuarioId, boolean concluida) {
        List<Task> tarefas = new ArrayList<>();
        String sql = "SELECT * FROM tarefas WHERE usuario_id = ? AND concluida = ?";

        try (Connection conn = ConnectionFactory.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, usuarioId);
            stmt.setBoolean(2, concluida);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Task tarefa = new Task();
                tarefa.setId(rs.getInt("id"));
                tarefa.setTitulo(rs.getString("titulo"));
                tarefa.setDescricao(rs.getString("descricao"));
                tarefa.setConcluida(rs.getBoolean("concluida"));
                tarefa.setUsuarioId(rs.getInt("usuario_id"));
                tarefas.add(tarefa);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar tarefas por status:" + e.getMessage());
        }

        return tarefas;
    }
    
    public List<Task> listarPorPeriodo(int usuarioId, LocalDateTime inicio, LocalDateTime fim) {
        List<Task> tarefas = new ArrayList<>();
        String sql = "SELECT * FROM tarefas WHERE usuario_id = ? AND data_criacao BETWEEN ? AND ?";

        try (Connection conn = ConnectionFactory.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, usuarioId);
            stmt.setTimestamp(2, Timestamp.valueOf(inicio));
            stmt.setTimestamp(3, Timestamp.valueOf(fim));

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Task t = new Task(
                    rs.getString("titulo"),
                    rs.getString("descricao"),
                    rs.getBoolean("concluida"),
                    rs.getInt("usuario_id")
                );
                t.setId(rs.getInt("id"));

                Timestamp dataCriacaoTS = rs.getTimestamp("data_criacao");
                if (dataCriacaoTS != null){
                    t.setDataCriacao(dataCriacaoTS.toLocalDateTime());
                }

                Timestamp dataConclusaoTS = rs.getTimestamp("data_conclusao");
                if (dataConclusaoTS!= null){
                    t.setDataConclusao(dataConclusaoTS.toLocalDateTime());
                }
                
                tarefas.add(t);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar tarefas por perído" + e);
        }

        return tarefas;
    }
}
