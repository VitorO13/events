# events
--Plataforma de Gestão de Eventos--
    Sobre o Projeto
Este é o projeto integrador da disciplina de Programação Orientada a Objetos II. O objetivo é desenvolver uma plataforma extensível e testável para a gestão completa de eventos acadêmicos e profissionais, consolidando em um único sistema processos de divulgação, inscrição, controle de frequência e avaliações
.
O desafio central não é apenas a funcionalidade, mas a aplicação de princípios sólidos de POO, como o encapsulamento de invariantes, alta coesão, baixo acoplamento e o uso de padrões de projeto para resolver problemas reais do domínio
.
    Glossário do Domínio
Termos fundamentais extraídos da especificação para garantir um vocabulário consistente entre o código e as regras de negócio
.
Evento: Contexto principal da plataforma, definido por título, descrição, período e regras gerais de visibilidade
.
Atividade: Unidade programática (palestra, oficina, mesa-redonda) vinculada a um evento, com horários e locais próprios
.
Participante: Usuário que consome o conteúdo, realiza inscrições e gera sua própria agenda
.
Organizador: Ator responsável pela configuração de eventos, atividades e critérios de presença
.
Inscrição: Vínculo entre um Participante e um Evento/Atividade, sujeito a regras de vagas e prazos
.
Frequência: Registro da presença efetiva de um participante em uma atividade
.
Invariante: Regra de negócio que deve ser protegida pelo modelo (ex: não permitir inscrição em atividade com lotação esgotada)
.
    Mapa de Responsabilidades
Conforme definido nos requisitos funcionais e atores do sistema
.
Administrador: Gerencia usuários organizadores e parâmetros administrativos globais.
Organizador: Cria e mantém eventos, atividades, critérios de frequência e gera relatórios operacionais.
Participante: Realiza cadastro, inscreve-se em atividades, monta sua agenda pessoal e avalia atividades.
Visitante: Acessa o site público para consultar informações e iniciar o processo de inscrição.
    Arquitetura e Decisões Técnicas
O projeto segue as diretrizes de Arquitetura Hexagonal (Portas e Adaptadores) para manter as regras de domínio independentes de tecnologias externas como bancos de dados ou interfaces de usuário
.
Linguagem: Java (obrigatório para o núcleo de domínio)
.
Estrutura Lógica:
Domínio: Objetos, regras e proteção de invariantes
.
Aplicação: Casos de uso e coordenação
.
Adaptadores: Interface Desktop (Swing/JavaFX), API REST e Persistência
.
    Como Executar (Configuração Inicial)
.
Pré-requisitos: JDK 17+.
.
.
.
Comando para testes: ./mvnw test (exemplo).
