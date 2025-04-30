# Sistema de Controle de Defesa
> *Este sistema é responsável pelo controle das defesas, gerenciando e monitorando e-mails e documentos, garantindo a integridade e a organização das informações. A plataforma foi desenvolvida para apoiar a gestão de dados e otimizar o controle da comunicação interna.*

## 📄 Descrição
- **O que o sistema faz?**

  - Esse sistema é responsável por controlar toda a gestão de dados do mestrado, desde a coleta inicial até a data final da defesa do aluno.

- **Quem são os usuários alvo?**

  - Os usuários alvo deste projeto são coordenadores de curso, orientadores, membros da comissão e alunos. 

- **Principais funcionalidades?**
  - Envio e recebimento de e-mails;
  - Armazenamento de dados no banco de dados;
  - Análise de processos via BPMN;
  - Comunicação entre todos os envolvidos;

## 🚀 Tecnologias Utilizadas
- **Linguagem de programação**
  - Java - 17
    
- **Frameworks**
  - Spring Boot - 3.3.3
  - Camunda BPM - 7.22.0

- **Bibliotecas**
  - PostgreSQL
  - H2 Database
  - Spring JDBC
  - JavaMail API
  - Java Activation

## ⚙️ Instalação e Execução 

[GitHub do projeto](https://github.com/Davidsonnj/Defesa-Mestrado-BPMN)

#### ✅ Pré-requisitos
*Antes de começar, verifique se você tem instalado:*

- *Java 17 ou superior*
- *Maven 3.8+*
- *Git (para clonar o repositório)*
- *PostgreSQL (se for rodar com banco de dados local)*
  
### 📥 Como clonar o projeto

```bash
git clone https://github.com/Davidsonnj/Defesa-Mestrado-BPMN.git
```

Antes de tudo entre no path:

```bash
cd Defesa-Mestrado-BPMN/Defesa-Mestrado-Camunda/defesa-mestrado/
```

### 📦 Como instalar as dependências
O Maven irá gerenciar todas as dependências automaticamente. Basta rodar:

```bash
mvn clean install
```
### 🎯 Como rodar o servidor localmente

```bash
mvn spring-boot:run
```
*A aplicação estará disponível em:* http://localhost:8080

## 🗂️ Organização do Código

<pre> Defesa-Mestrado-BPMN/
└── Defesa-Mestrado-Camunda/
    ├── anexos/
    ├── defesa-mestrado/
    │   ├── camunda-h2-database.mv.db
    │   ├── Defesa-Mestrado.png
    │   ├── pom.xml
    │   ├── src/
    │   │   └── main/
    │   │       ├── java/
    │   │       │   └── br/
    │   │       │       └── edu/
    │   │       │           └── ifes/
    │   │       │               └── mestrado/
    │   │       │                   ├── Application.java
    │   │       │                   ├── camunda/
    │   │       │                   │   ├── controller/
    │   │       │                   │   │   └── delegates/
    │   │       │                   │   │       ├── Aluno/
    │   │       │                   │   │       ├── coordenacao/
    │   │       │                   │   │       ├── Orientador/
    │   │       │                   │   │       └── SistemaPrincipal/
    │   │       │                   │   ├── dao/
    │   │       │                   │   │   ├── implementations/
    │   │       │                   │   │   └── interfaceDAO/
    │   │       │                   │   ├── exception/
    │   │       │                   │   └── model/
    │   │       │                   ├── database/
    │   │       │                   └── emailAPI/
    │   │       │                       ├── controller/
    │   │       │                       ├── model/
    │   │       │                       ├── service/
    │   │       │                       └── view/
    │   │       └── resources/
    │   └── target/
    └── docs/
        └── defesa/
            ├── BPMNs/
            └── diagram_relacional/</pre>
