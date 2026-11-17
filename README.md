# events
# Plataforma de Gestão de Eventos

##  Sobre o Projeto

Este é o projeto integrador da disciplina de **Programação Orientada a Objetos II**.

O objetivo é desenvolver uma plataforma extensível e testável para a gestão completa de eventos acadêmicos e profissionais, consolidando em um único sistema processos de:

* Divulgação
* Inscrição
* Controle de frequência
* Avaliação

O desafio central não é apenas a implementação das funcionalidades, mas também a aplicação de princípios sólidos de **Programação Orientada a Objetos (POO)**, como:

* Encapsulamento de invariantes
* Alta coesão
* Baixo acoplamento
* Uso de padrões de projeto para resolver problemas reais do domínio

---

##  Glossário do Domínio

Termos fundamentais extraídos da especificação para garantir um vocabulário consistente entre o código e as regras de negócio.

| Termo            | Descrição                                                                                                               |
| ---------------- | ----------------------------------------------------------------------------------------------------------------------- |
| **Evento**       | Contexto principal da plataforma, definido por título, descrição, período e regras gerais de visibilidade.              |
| **Atividade**    | Unidade programática (palestra, oficina, mesa-redonda) vinculada a um evento, com horários e locais próprios.           |
| **Participante** | Usuário que consome o conteúdo, realiza inscrições e gera sua própria agenda.                                           |
| **Organizador**  | Ator responsável pela configuração de eventos, atividades e critérios de presença.                                      |
| **Inscrição**    | Vínculo entre um Participante e um Evento/Atividade, sujeito a regras de vagas e prazos.                                |
| **Frequência**   | Registro da presença efetiva de um participante em uma atividade.                                                       |
| **Invariante**   | Regra de negócio que deve ser protegida pelo modelo. Exemplo: não permitir inscrição em atividade com lotação esgotada. |

---

##  Mapa de Responsabilidades

Conforme definido nos requisitos funcionais e atores do sistema:

| Ator              | Responsabilidades                                                                                   |
| ----------------- | --------------------------------------------------------------------------------------------------- |
| **Administrador** | Gerencia usuários organizadores e parâmetros administrativos globais.                               |
| **Organizador**   | Cria e mantém eventos, atividades e critérios de frequência, além de gerar relatórios operacionais. |
| **Participante**  | Realiza cadastro, inscreve-se em atividades, monta sua agenda pessoal e avalia atividades.          |
| **Visitante**     | Acessa o site público para consultar informações e iniciar o processo de inscrição.                 |

---

##  Arquitetura e Decisões Técnicas

O projeto segue as diretrizes da **Arquitetura Hexagonal (Portas e Adaptadores)** para manter as regras de domínio independentes de tecnologias externas, como bancos de dados ou interfaces de usuário.

### Linguagem

* **Java** — obrigatório para o núcleo de domínio.

### Estrutura Lógica

#### Domínio

Responsável pelos:

* Objetos
* Regras de negócio
* Proteção de invariantes

#### Aplicação

Responsável pelos:

* Casos de uso
* Coordenação das operações do sistema

#### Adaptadores

Responsáveis pela comunicação com tecnologias externas, incluindo:

* Interface Desktop (Swing/JavaFX)
* API REST
* Persistência

---

## ▶ Como Executar

### Pré-requisitos

* **JDK 21 ou superior**
* Maven Wrapper (`mvnw`)

### Criar a primeira conta de administrador local

1. Crie o banco `eventplatform` no MariaDB, se ele ainda não existir. Na raiz do projeto, configure o arquivo `.env` com os dados da sua instalação local:

   ```dotenv
   DB_HOST=localhost
   DB_PORT=3306
   DB_NAME=eventplatform
   DB_USER=seu_usuario_mariadb
   DB_PASSWORD=sua_senha_mariadb
   JWT_SECRET=troque-por-um-segredo-local-aleatorio
   ```

   O usuário do MariaDB precisa ter permissão para rodar as migrações do Flyway e atualizar a tabela `users`.

2. Inicie o Spring Boot na raiz do projeto com `./mvnw spring-boot:run` (no Windows, `mvnw.cmd spring-boot:run`). Abra [Criar conta](http://localhost:3000/events.html#cadastro) e cadastre o e-mail fictício `admin.demo@example.com` com uma senha de sua escolha de pelo menos 8 caracteres. Guarde essa senha para o login.

3. Em outro terminal, conecte-se ao banco. Substitua o usuário pelos dados do seu `.env` e informe a senha do MariaDB quando solicitado:

   ```bash
   mariadb -h localhost -P 3306 -u seu_usuario_mariadb -p eventplatform
   ```

   Execute o SQL abaixo (ajuste o e-mail caso tenha cadastrado outro). O valor `0` corresponde à role `ADMIN`. A consulta deve retornar uma linha com o e-mail cadastrado e a role `0`.

   ```sql
   UPDATE users SET role = 0 WHERE email = 'admin.demo@example.com';
   SELECT email, role FROM users WHERE email = 'admin.demo@example.com';
   ```

4. Entre pelo aplicativo desktop com o e-mail e a senha do cadastro. Para testar pela API, substitua `SENHA_DO_CADASTRO` pela senha que você escolheu. O login retorna um token JWT:

   ```bash
   curl -fsS http://localhost:3000/auth/desktop/login \
     -H 'Content-Type: application/json' \
     -d '{"email":"admin.demo@example.com","password":"SENHA_DO_CADASTRO"}'
   ```

   A tela de login do site é exclusiva para participantes. Para confirmar a role pela API, envie o token retornado no passo anterior:

   ```bash
   curl -fsS http://localhost:3000/auth/me -H 'Authorization: Bearer SEU_TOKEN'
   ```

   A resposta deve mostrar `"role":"ADMIN"`.

### Executar os testes

Na raiz do projeto, execute:

```bash
./mvnw test
```

> **Windows:** caso necessário, utilize:
>
> ```bash
> mvnw.cmd test
> ```

---

##  Status do Projeto

Projeto em desenvolvimento como atividade integradora da disciplina de **Programação Orientada a Objetos II**.
