# 📌 Organizador – Sistema Completo de Gerenciamento de Tarefas

## 🌟 Visão Geral
### Sistema Java para organização pessoal/profissional com:
- ✅ Autenticação segura de usuários
- ✅ CRUD completo de tarefas
- ✅ Filtros inteligentes
- ✅ Interface intuitiva via terminal

## 🛠️ Stack Tecnológica
| Camada          | Tecnologias                 |
|-----------------|-----------------------------|
| **Backend**     | Java 11                     |
| **Banco**       | MySQL 8                     |
| **Arquitetura** | MVC + DAO                   |
| **Bibliotecas** | java.time, java.util.Scanner|

## 🏗️ Estrutura do Código
src/
└── com/todolist/
    ├── dao/               # Data Access Object
    │   ├── TaskDAO.java   # Operações: create/read/update/delete
    │   └── UserDAO.java   # Autenticação e CRUD users
    │
    ├── model/             # Entidades
    │   ├── Task.java      # Atributos: id, título, descrição, status
    │   └── User.java      # Atributos: id, nome, email, senha
    │
    └── ui/                # User Interface
        ├── Menu.java      # Lógica de navegação
        └── Main.java      # Ponto de entrada

## 📝 Gestão de Tarefas
Criação: Validação de campos obrigatórios

Atualização:

## 🚀 Execução
### Pré-requisitos:

MySQL configurado (script em /database)

Java JDK 11+

### Comandos:

# Compilar
javac src/com/todolist/Main.java

# Executar
java -cp src com.todolist.Main

## 📊 Fluxograma
### graph LR
    A[Menu Principal] --> B[Login/Cadastro]
    B --> C[Menu Tarefas]
    C --> D[Criar Tarefa]
    C --> E[Filtrar Tarefas]
    C --> F[Gerenciar Conta]

## 🔜 Roadmap
### CRUD básico

Testes automatizados (JUnit 5)

Docker compose (MySQL + App)

Dashboard web (Spring Boot)

## 📜 Licença
Apache License 2.0

## ✉️ Contatos
[João Paulo Ferreira] - [jpauloferreira2006@gmail.com]
https://www.linkedin.com/in/jo%C3%A3o-paulo-ferreira-6a1ab8328/