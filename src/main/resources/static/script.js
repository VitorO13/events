
/* =========================================================
   PLATAFORMA DE EVENTOS ACADÊMICOS
   ========================================================= */


/* =========================================================
   1. DADOS DOS EVENTOS
   ========================================================= */

const DEMO_EVENTS = [
    {
        id: 1,
        nome: 'Semana da Computação',
        categoria: 'Tecnologia',
        data: '2026-10-14',
        fim: '2026-10-16',
        local: 'Auditório Central',
        remoto: false,
        icone: '⌘',
        cores: ['#15365c', '#316eb8'],
        descricao: 'Três dias de palestras, oficinas e encontros sobre desenvolvimento de software, pesquisa e inovação.',
        atividades: [
            {
                id: 11,
                hora: '09:00',
                dia: '14/10',
                nome: 'Abertura e tendências em tecnologia',
                tipo: 'Palestra',
                local: 'Auditório Central'
            },
            {
                id: 12,
                hora: '14:00',
                dia: '14/10',
                nome: 'Oficina prática de APIs REST',
                tipo: 'Oficina',
                local: 'Laboratório 2'
            },
            {
                id: 13,
                hora: '10:00',
                dia: '15/10',
                nome: 'Mesa-redonda: carreiras em TI',
                tipo: 'Mesa-redonda',
                local: 'Auditório Central'
            }
        ]
    },
    {
        id: 2,
        nome: 'Workshop de Java e Spring Boot',
        categoria: 'Oficina',
        data: '2026-10-22',
        fim: '2026-10-22',
        local: 'Laboratório de Informática',
        remoto: false,
        icone: '{ }',
        cores: ['#4b3c96', '#8a67d5'],
        descricao: 'Introdução prática ao desenvolvimento de APIs com Java, Spring Boot e persistência de dados.',
        atividades: [
            {
                id: 21,
                hora: '14:00',
                dia: '22/10',
                nome: 'Primeira API com Spring Boot',
                tipo: 'Oficina',
                local: 'Laboratório de Informática'
            }
        ]
    },
    {
        id: 3,
        nome: 'Jornada de Pesquisa e Inovação',
        categoria: 'Pesquisa',
        data: '2026-11-05',
        fim: '2026-11-06',
        local: 'Online',
        remoto: true,
        icone: '✧',
        cores: ['#0e6473', '#23a6b0'],
        descricao: 'Apresentações de projetos científicos e discussões sobre pesquisa aplicada e inovação.',
        atividades: [
            {
                id: 31,
                hora: '19:00',
                dia: '05/11',
                nome: 'Apresentação de projetos',
                tipo: 'Palestra',
                local: 'Sala virtual'
            },
            {
                id: 32,
                hora: '19:00',
                dia: '06/11',
                nome: 'Inovação na universidade',
                tipo: 'Mesa-redonda',
                local: 'Sala virtual'
            }
        ]
    },
    {
        id: 4,
        nome: 'Encontro de Ciência e Sustentabilidade',
        categoria: 'Ciências',
        data: '2026-11-12',
        fim: '2026-11-12',
        local: 'Centro de Convenções',
        remoto: false,
        icone: '❋',
        cores: ['#276950', '#57aa80'],
        descricao: 'Debates e apresentações sobre os desafios ambientais e as aplicações da ciência na sociedade.',
        atividades: [
            {
                id: 41,
                hora: '08:30',
                dia: '12/11',
                nome: 'Ciência e desafios ambientais',
                tipo: 'Palestra',
                local: 'Centro de Convenções'
            }
        ]
    }
];


/* =========================================================
   2. VARIÁVEIS E ELEMENTOS DA PÁGINA
   ========================================================= */

let events = [...DEMO_EVENTS];

let currentEvent = null;

let agenda = new Set();

const app = document.getElementById('app');

const nav = document.getElementById('nav');

const toast = document.getElementById('toast');


/* =========================================================
   3. FUNÇÕES AUXILIARES
   ========================================================= */

// Protege os textos inseridos dinamicamente no HTML.

const esc = s =>
    String(s ?? '').replace(
        /[&<>"']/g,
        c => ({
            '&': '&amp;',
            '<': '&lt;',
            '>': '&gt;',
            '"': '&quot;',
            "'": '&#39;'
        }[c])
    );


// Formata as datas para o padrão brasileiro.

const date = s =>
    new Date(`${s}T12:00:00`).toLocaleDateString('pt-BR', {
        day: '2-digit',
        month: 'short',
        year: 'numeric'
    });


// Exibe mensagens temporárias.

function notify(message) {
    toast.textContent = message;

    toast.classList.add('show');

    setTimeout(() => {
        toast.classList.remove('show');
    }, 4200);
}


// Cria o cabeçalho das páginas internas.

function pageHeader(title, subtitle) {
    return `
        <section class="page-head">
            <div class="container">
                <span
                    class="eyebrow"
                    style="color:#3268e7"
                >
                    PLATAFORMA DE EVENTOS ACADÊMICOS
                </span>

                <h1>${title}</h1>

                <p>${subtitle}</p>
            </div>
        </section>
    `;
}


/* =========================================================
   4. CARD DE EVENTO
   ========================================================= */

function card(e) {
    return `
        <article class="card">
            <div
                class="card-art"
                style="--c1:${e.cores[0]};--c2:${e.cores[1]}"
            >
                <span class="art-label">
                    ${esc(e.categoria)}
                </span>

                ${esc(e.icone)}
            </div>

            <div class="card-body">
                <span class="pill ${e.remoto ? 'blue' : ''}">
                    ${e.remoto ? '◉ Online' : '● Presencial'}
                </span>

                <h3>${esc(e.nome)}</h3>

                <p>${esc(e.descricao)}</p>

                <div class="card-meta">
                    <span>
                        ▦ ${date(e.data)}
                    </span>

                    <span>
                        ⌖ ${esc(e.local)}
                    </span>
                </div>

                <div class="card-foot">
                    <span
                        class="muted"
                        style="font-size:12px"
                    >
                        ${e.atividades.length} atividade(s)
                    </span>

                    <a
                        class="link"
                        href="#evento/${e.id}"
                    >
                        Ver detalhes →
                    </a>
                </div>
            </div>
        </article>
    `;
}


/* =========================================================
   5. PÁGINA INICIAL
   ========================================================= */

function home() {
    app.innerHTML = `
        <section class="hero">
            <div class="hero-inner">
                <div>
                    <span class="eyebrow">
                        SEU PRÓXIMO EVENTO COMEÇA AQUI
                    </span>

                    <h1>
                        Conhecimento que
                        <span>conecta pessoas.</span>
                    </h1>

                    <p>
                        Descubra palestras, oficinas e encontros
                        acadêmicos. Explore a programação e organize
                        suas próximas experiências.
                    </p>

                    <div class="hero-actions">
                        <a
                            class="primary"
                            href="#eventos"
                        >
                            Explorar eventos →
                        </a>

                        <a
                            class="secondary"
                            href="#programacao"
                        >
                            Ver programação
                        </a>
                    </div>
                </div>

                <div class="hero-art">
                    <span class="big">✦</span>

                    <span class="floating float-a">
                        ▦ Programação acadêmica
                    </span>

                    <span class="floating float-b">
                        ✧ Aprenda e conecte-se
                    </span>
                </div>
            </div>
        </section>

        <div class="container">
            <div class="stats">
                <div class="stat">
                    <strong>
                        ${events.length}
                    </strong>

                    <span>
                        Eventos disponíveis
                    </span>
                </div>

                <div class="stat">
                    <strong>
                        ${events.reduce(
                            (n, e) => n + e.atividades.length,
                            0
                        )}
                    </strong>

                    <span>
                        Atividades na programação
                    </span>
                </div>

                <div class="stat">
                    <strong>
                        ${events.filter(e => e.remoto).length}
                    </strong>

                    <span>
                        Eventos online
                    </span>
                </div>
            </div>

            <section class="section">
                <div class="section-head">
                    <div>
                        <span
                            class="eyebrow"
                            style="color:#3268e7"
                        >
                            DESCUBRA
                        </span>

                        <h2>
                            Próximos eventos
                        </h2>

                        <p>
                            Explore oportunidades de aprendizado
                            e networking.
                        </p>
                    </div>

                    <a
                        class="link"
                        href="#eventos"
                    >
                        Ver todos →
                    </a>
                </div>

                <div class="cards">
                    ${events.slice(0, 3).map(card).join('')}
                </div>
            </section>

            <section
                class="section"
                style="padding-top:0"
            >
                <div class="panel">
                    <h2>
                        Como funciona?
                    </h2>

                    <p>
                        Explore os eventos disponíveis, consulte
                        as atividades e os horários e organize
                        sua agenda acadêmica.
                    </p>

                    <a
                        class="primary"
                        style="display:inline-block"
                        href="#eventos"
                    >
                        Encontrar um evento
                    </a>
                </div>
            </section>
        </div>
    `;
}


/* =========================================================
   6. CATÁLOGO DE EVENTOS
   ========================================================= */

function catalog() {
    app.innerHTML =
        pageHeader(
            'Explore os eventos',
            'Encontre palestras, oficinas e encontros acadêmicos.'
        ) + `
            <div class="container">
                <div class="filters">
                    <input
                        class="field search"
                        id="search"
                        aria-label="Buscar eventos"
                        placeholder="⌕ Buscar por nome ou descrição..."
                    >

                    <select
                        class="field"
                        id="category"
                        aria-label="Categoria"
                    >
                        <option value="">
                            Todas as categorias
                        </option>

                        ${[...new Set(events.map(e => e.categoria))]
                            .map(c => `
                                <option>
                                    ${esc(c)}
                                </option>
                            `)
                            .join('')}
                    </select>

                    <select
                        class="field"
                        id="mode"
                        aria-label="Modalidade"
                    >
                        <option value="">
                            Todas as modalidades
                        </option>

                        <option value="online">
                            Online
                        </option>

                        <option value="presencial">
                            Presencial
                        </option>
                    </select>
                </div>

                <div
                    id="results"
                    class="cards"
                ></div>
            </div>
        `;


    // Filtra os eventos por nome, categoria e modalidade.

    function filter() {
        let q = document
            .getElementById('search')
            .value.toLocaleLowerCase('pt-BR');

        let c = document
            .getElementById('category')
            .value;

        let m = document
            .getElementById('mode')
            .value;

        let filtered = events.filter(e =>
            (e.nome + ' ' + e.descricao)
                .toLocaleLowerCase('pt-BR')
                .includes(q) &&

            (!c || e.categoria === c) &&

            (!m || (m === 'online') === e.remoto)
        );

        document.getElementById('results').innerHTML =
            filtered.length
                ? filtered.map(card).join('')
                : `
                    <div class="empty">
                        Nenhum evento encontrado.
                        Experimente outros filtros.
                    </div>
                `;
    }


    // Atualiza os resultados sempre que um filtro muda.

    ['search', 'category', 'mode'].forEach(id => {
        document
            .getElementById(id)
            .addEventListener('input', filter);
    });

    filter();
}


/* =========================================================
   7. DETALHES DE UM EVENTO
   ========================================================= */

function detail(id) {
    const e = events.find(
        e => e.id === Number(id)
    );

    if (!e) {
        app.innerHTML =
            pageHeader(
                'Evento não encontrado',
                'Volte ao catálogo para escolher outro evento.'
            ) + `
                <div class="container">
                    <a
                        class="back"
                        href="#eventos"
                    >
                        ← Voltar aos eventos
                    </a>
                </div>
            `;

        return;
    }

    currentEvent = e;

    app.innerHTML = `
        <div class="container">
            <a
                class="back"
                href="#eventos"
            >
                ← Todos os eventos
            </a>

            <div
                class="detail-hero"
                style="--c1:${e.cores[0]};--c2:${e.cores[1]}"
            >
                <div class="symbol">
                    ${esc(e.icone)}
                </div>

                <div>
                    <span class="chip">
                        ${esc(e.categoria)} •
                        ${e.remoto ? 'Online' : 'Presencial'}
                    </span>

                    <h1>
                        ${esc(e.nome)}
                    </h1>

                    <p>
                        ${date(e.data)}

                        ${e.fim !== e.data
                            ? '— ' + date(e.fim)
                            : ''}

                        · ${esc(e.local)}
                    </p>
                </div>
            </div>

            <div class="detail-grid">
                <div>
                    <section class="panel">
                        <h2>
                            Sobre o evento
                        </h2>

                        <p>
                            ${esc(e.descricao)}
                        </p>
                    </section>

                    <section class="panel">
                        <h2>
                            Programação
                        </h2>

                        ${e.atividades.map(a => `
                            <div class="schedule-item">
                                <span class="time">
                                    ${esc(a.hora)}
                                </span>

                                <div>
                                    <strong>
                                        ${esc(a.nome)}
                                    </strong>

                                    <small>
                                        ${esc(a.dia)} ·
                                        ${esc(a.tipo)} ·
                                        ${esc(a.local)}
                                    </small>
                                </div>

                                <button
                                    class="secondary activity-action"
                                    data-agenda="${a.id}"
                                >
                                    ${agenda.has(a.id)
                                        ? '✓ Na agenda'
                                        : '+ Agenda'}
                                </button>
                            </div>
                        `).join('')}
                    </section>
                </div>

                <aside>
                    <section class="panel">
                        <h2>
                            Informações
                        </h2>

                        <p>
                            ▦ ${date(e.data)}
                            <br>

                            ⌖ ${esc(e.local)}
                            <br>

                            ◉ ${e.remoto
                                ? 'Evento online'
                                : 'Evento presencial'}
                        </p>

                        <a
                            href="#inscricao/${e.id}"
                            class="primary"
                            style="display:block;text-align:center"
                        >
                            Iniciar inscrição →
                        </a>
                    </section>
                </aside>
            </div>
        </div>
    `;


    // Adiciona ou remove atividades da agenda.

    app.querySelectorAll('[data-agenda]').forEach(b => {
        b.onclick = () => {
            let aid = Number(b.dataset.agenda);

            if (agenda.has(aid)) {
                agenda.delete(aid);
            } else {
                agenda.add(aid);
            }

            detail(id);

            notify('Agenda atualizada.');
        };
    });
}


/* =========================================================
   8. PROGRAMAÇÃO
   ========================================================= */

function programming() {
    let items = events.flatMap(e =>
        e.atividades.map(a => ({
            ...a,
            event: e
        }))
    );

    app.innerHTML =
        pageHeader(
            'Programação',
            'Consulte as atividades disponíveis nos eventos.'
        ) + `
            <div class="container">
                <section
                    class="panel"
                    style="margin-top:25px"
                >
                    <h2>
                        Atividades programadas
                    </h2>

                    ${items.map(a => `
                        <div class="agenda-row">
                            <div>
                                <span class="chip">
                                    ${esc(a.dia)} ·
                                    ${esc(a.hora)}
                                </span>

                                <h3 style="margin-top:10px">
                                    ${esc(a.nome)}
                                </h3>

                                <p>
                                    ${esc(a.event.nome)} ·
                                    ${esc(a.local)}
                                </p>
                            </div>

                            <div>
                                <button
                                    class="secondary"
                                    data-add="${a.id}"
                                >
                                    ${agenda.has(a.id)
                                        ? '✓ Na agenda'
                                        : '+ Minha agenda'}
                                </button>

                                <a
                                    class="link"
                                    href="#evento/${a.event.id}"
                                >
                                    Evento →
                                </a>
                            </div>
                        </div>
                    `).join('')}
                </section>
            </div>
        `;


    // Atualiza a agenda a partir da programação.

    app.querySelectorAll('[data-add]').forEach(b => {
        b.onclick = () => {
            let id = Number(b.dataset.add);

            if (agenda.has(id)) {
                agenda.delete(id);
            } else {
                agenda.add(id);
            }

            programming();

            notify('Agenda atualizada.');
        };
    });
}


/* =========================================================
   9. MINHA AGENDA
   ========================================================= */

function myAgenda() {
    let items = events
        .flatMap(e =>
            e.atividades.map(a => ({
                ...a,
                event: e
            }))
        )
        .filter(a => agenda.has(a.id));

    app.innerHTML =
        pageHeader(
            'Minha agenda',
            'Organize as atividades que deseja acompanhar.'
        ) + `
            <div class="container">
                <section
                    class="panel"
                    style="margin-top:25px"
                >
                    <h2>
                        Atividades salvas
                    </h2>

                    ${items.length
                        ? items.map(a => `
                            <div class="agenda-row">
                                <div>
                                    <span class="chip">
                                        ${esc(a.dia)} ·
                                        ${esc(a.hora)}
                                    </span>

                                    <h3 style="margin-top:9px">
                                        ${esc(a.nome)}
                                    </h3>

                                    <p>
                                        ${esc(a.event.nome)}
                                    </p>
                                </div>

                                <button
                                    class="secondary"
                                    data-remove="${a.id}"
                                >
                                    Remover
                                </button>
                            </div>
                        `).join('')
                        : `
                            <div class="empty">
                                Sua agenda está vazia.

                                <br><br>

                                <a
                                    class="primary"
                                    href="#programacao"
                                >
                                    Explorar programação
                                </a>
                            </div>
                        `}
                </section>
            </div>
        `;


    // Remove uma atividade da agenda.

    app.querySelectorAll('[data-remove]').forEach(b => {
        b.onclick = () => {
            agenda.delete(
                Number(b.dataset.remove)
            );

            myAgenda();
        };
    });
}


/* =========================================================
   10. FORMULÁRIO DE INSCRIÇÃO
   ========================================================= */

function enrollment(id) {
    let e = events.find(
        e => e.id === Number(id)
    );

    app.innerHTML =
        pageHeader(
            'Iniciar inscrição',
            'Preencha seus dados para realizar sua inscrição.'
        ) + `
            <div
                class="container"
                style="max-width:830px"
            >
                <section
                    class="panel"
                    style="margin-top:24px"
                >
                    <h2>
                        Dados do participante
                    </h2>

                    <form
                        id="enroll-form"
                        novalidate
                    >
                        <div class="form-grid">
                            <div class="form-group full">
                                <label for="full-name">
                                    Nome completo *
                                </label>

                                <input
                                    id="full-name"
                                    required
                                    autocomplete="name"
                                >

                                <span
                                    class="error"
                                    id="err-name"
                                ></span>
                            </div>

                            <div class="form-group">
                                <label for="email">
                                    E-mail *
                                </label>

                                <input
                                    id="email"
                                    type="email"
                                    required
                                    autocomplete="email"
                                >

                                <span
                                    class="error"
                                    id="err-email"
                                ></span>
                            </div>

                            <div class="form-group">
                                <label for="phone">
                                    Telefone (opcional)
                                </label>

                                <input
                                    id="phone"
                                    type="tel"
                                    placeholder="(00) 00000-0000"
                                    autocomplete="tel"
                                >

                                <span
                                    class="error"
                                    id="err-phone"
                                ></span>
                            </div>

                            <div class="form-group full">
                                <label for="event-select">
                                    Evento *
                                </label>

                                <select
                                    id="event-select"
                                    required
                                >
                                    <option value="">
                                        Selecione um evento
                                    </option>

                                    ${events.map(x => `
                                        <option
                                            value="${x.id}"
                                            ${x.id === e?.id
                                                ? 'selected'
                                                : ''}
                                        >
                                            ${esc(x.nome)}
                                        </option>
                                    `).join('')}
                                </select>

                                <span
                                    class="error"
                                    id="err-event"
                                ></span>
                            </div>
                        </div>

                        <div class="form-actions">
                            <button
                                class="primary"
                                type="submit"
                            >
                                Validar dados
                            </button>

                            <a
                                class="secondary"
                                href="#eventos"
                            >
                                Voltar aos eventos
                            </a>
                        </div>
                    </form>

                    <div
                        id="form-result"
                        role="status"
                        aria-live="polite"
                    ></div>
                </section>
            </div>
        `;


    // Validação dos dados informados.

    document.getElementById('enroll-form').onsubmit = ev => {
        ev.preventDefault();

        const name = document
            .getElementById('full-name')
            .value.trim();

        const email = document
            .getElementById('email')
            .value.trim();

        const phone = document
            .getElementById('phone')
            .value.trim();

        const selected = document
            .getElementById('event-select')
            .value;


        // Limpa mensagens de erro anteriores.

        ['name', 'email', 'phone', 'event'].forEach(k => {
            document.getElementById(
                'err-' + k
            ).textContent = '';
        });

        let valid = true;


        // Exibe o erro correspondente ao campo.

        const err = (key, msg) => {
            document.getElementById(
                'err-' + key
            ).textContent = msg;

            valid = false;
        };


        // Validação do nome.

        if (!name) {
            err(
                'name',
                'Informe seu nome.'
            );
        }


        // Validação do e-mail.

        if (!/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email)) {
            err(
                'email',
                'Informe um e-mail válido.'
            );
        }


        // Validação do telefone.

        if (
            phone &&
            phone.replace(/\D/g, '').length < 10
        ) {
            err(
                'phone',
                'Informe telefone com DDD.'
            );
        }


        // Validação do evento selecionado.

        if (!selected) {
            err(
                'event',
                'Escolha um evento.'
            );
        }


        // Exibe o resultado da validação.
        // Nenhuma inscrição é registrada nesta etapa.

        const result = document.getElementById(
            'form-result'
        );

        result.innerHTML = valid
            ? `
                <div
                    class="info"
                    style="margin-top:20px"
                >
                    ✓ Dados validados com sucesso.
                </div>
            `
            : '';
    };
}


/* =========================================================
   11. MINHAS INSCRIÇÕES
   ========================================================= */

function registrations() {
    app.innerHTML =
        pageHeader(
            'Minhas inscrições',
            'Acompanhe suas inscrições e seus respectivos status.'
        ) + `
            <div class="container">
                <section
                    class="panel"
                    style="margin-top:24px"
                >
                    <div class="empty">
                        <h2>
                            Nenhuma inscrição disponível
                        </h2>

                        <a
                            class="primary"
                            href="#eventos"
                        >
                            Explorar eventos
                        </a>
                    </div>
                </section>
            </div>
        `;
}


/* =========================================================
   12. AVALIAÇÕES
   ========================================================= */

function reviews() {
    app.innerHTML =
        pageHeader(
            'Avaliações',
            'Avalie as atividades das quais você participou.'
        ) + `
            <div class="container">
                <section
                    class="panel"
                    style="margin-top:24px"
                >
                    <div class="empty">
                        <h2>
                            Nenhuma avaliação disponível
                        </h2>

                        <a
                            class="secondary"
                            href="#programacao"
                        >
                            Consultar programação
                        </a>
                    </div>
                </section>
            </div>
        `;
}


/* =========================================================
   13. LOGIN E CADASTRO
   ========================================================= */

function login() {
    app.innerHTML =
        pageHeader(
            'Entrar ou cadastrar',
            'Acesso à área pessoal do participante.'
        ) + `
            <div
                class="container"
                style="max-width:750px"
            >
                <section
                    class="panel"
                    style="margin-top:25px"
                >
                    <h2>
                        Acesso do participante
                    </h2>

                    <div class="form-grid">
                        <div class="form-group">
                            <label for="login-email">
                                E-mail
                            </label>

                            <input
                                type="email"
                                id="login-email"
                                disabled
                                placeholder="seu@email.com"
                            >
                        </div>

                        <div class="form-group">
                            <label for="login-password">
                                Senha
                            </label>

                            <input
                                type="password"
                                id="login-password"
                                disabled
                                placeholder="••••••••"
                            >
                        </div>
                    </div>

                    <div class="form-actions">
                        <button
                            class="primary"
                            disabled
                            style="opacity:.55;cursor:not-allowed"
                        >
                            Entrar
                        </button>

                        <a
                            class="secondary"
                            href="#eventos"
                        >
                            Explorar eventos
                        </a>
                    </div>
                </section>
            </div>
        `;
}


/* =========================================================
   14. NAVEGAÇÃO ENTRE AS PÁGINAS
   ========================================================= */

function route() {
    const [section, id] = location.hash
        .replace(/^#/, '')
        .split('/');


    // Destaca o item ativo do menu.

    nav.querySelectorAll('a').forEach(a => {
        a.classList.toggle(
            'active',
            a.dataset.nav === (section || 'inicio')
        );
    });


    // Define qual página será exibida.

    if (section === 'eventos') {
        catalog();

    } else if (section === 'evento') {
        detail(id);

    } else if (section === 'programacao') {
        programming();

    } else if (section === 'minha-agenda') {
        myAgenda();

    } else if (section === 'minhas-inscricoes') {
        registrations();

    } else if (section === 'inscricao') {
        enrollment(id);

    } else if (section === 'avaliacoes') {
        reviews();

    } else if (section === 'login') {
        login();

    } else {
        home();
    }


    // Retorna ao topo da página.

    window.scrollTo(0, 0);


    // Fecha o menu de navegação em telas menores.

    nav.classList.remove('open');

    document
        .getElementById('menu-toggle')
        .setAttribute(
            'aria-expanded',
            'false'
        );
}


/* =========================================================
   15. EVENTOS E INICIALIZAÇÃO
   ========================================================= */

// Atualiza a página quando o endereço muda.

window.addEventListener(
    'hashchange',
    route
);


// Botão de login e cadastro.

document.getElementById('login-btn').onclick = () => {
    location.hash = '#login';
};


// Menu para dispositivos móveis.

document.getElementById('menu-toggle').onclick = () => {
    let open = nav.classList.toggle('open');

    document
        .getElementById('menu-toggle')
        .setAttribute(
            'aria-expanded',
            String(open)
        );
};


// Inicializa a plataforma.

route();
