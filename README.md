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

* **JDK 17 ou superior**
* Maven Wrapper (`mvnw`)

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
