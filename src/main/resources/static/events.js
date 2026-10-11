const app = document.getElementById('app');
const nav = document.getElementById('nav');
let token = sessionStorage.getItem('participantToken');
let user = null;
let version = 0;
let nextEvent = null;
const esc = value => String(value ?? '').replace(/[&<>"']/g, c => ({'&':'&amp;','<':'&lt;','>':'&gt;','"':'&quot;',"'":'&#39;'})[c]);
const date = value => value ? new Date(`${String(value).slice(0,10)}T12:00:00`).toLocaleDateString('pt-BR') : 'Data não informada';

function clearSession() {
    token = null;
    user = null;
    sessionStorage.removeItem('participantToken');
    navigation();
}

async function api(path, { method = 'GET', body, authenticated = false } = {}) {
    const headers = { Accept: 'application/json' };
    if (body) headers['Content-Type'] = 'application/json';
    if (authenticated && token) headers.Authorization = `Bearer ${token}`;
    let response;
    try { response = await fetch(path, { method, headers, body: body ? JSON.stringify(body) : undefined }); }
    catch { throw new Error('Não foi possível conectar ao servidor. Tente novamente.'); }
    if (!response.ok) {
        if (response.status === 401 && authenticated) {
            clearSession();
            throw new Error('Sua sessão expirou. Entre novamente.');
        }
        const messages = {
            400: 'Confira os dados informados. No cadastro, o e-mail pode já estar em uso.',
            401: 'E-mail ou senha incorretos.',
            403: 'Acesso não autorizado. Contas de administradores e organizadores devem usar o aplicativo desktop.',
            404: 'Evento ou inscrição não encontrado.',
            409: 'Você já está inscrito neste evento ou as vagas se esgotaram.'
        };
        throw new Error(messages[response.status] || 'Não foi possível concluir a operação. Tente novamente.');
    }
    const text = await response.text();
    if (!text) return null;
    if (text.startsWith('data:image/png;base64,')) return text;
    return response.headers.get('Content-Type')?.includes('application/json') ? JSON.parse(text) : text;
}

function navigation() {
    nav.innerHTML = `<a href="#eventos">Eventos</a>${user
        ? `<a href="#inscricoes">Minhas inscrições</a><span>${esc(user.name)}</span><button id="logout" class="secondary" type="button">Sair</button>`
        : '<a href="#login">Entrar</a><a href="#cadastro">Criar conta</a>'}`;
    document.getElementById('logout')?.addEventListener('click', () => { clearSession(); nextEvent = null; location.hash = '#eventos'; route(); });
}

function shell(title, content) {
    app.innerHTML = `<section class="page-head"><div class="container"><span class="eyebrow">PORTAL ACADÊMICO</span><h1>${esc(title)}</h1></div></section><section class="container section">${content}</section>`;
}

function eventCard(event) {
    return `<article class="card">${event.imageUrl ? `<img class="event-cover" src="/events/${encodeURIComponent(event.id)}/image" alt="${esc(event.name)}" loading="lazy">` : `<div class="card-art" style="--c1:#15365c;--c2:#316eb8" aria-hidden="true">✦</div>`}<div class="card-body"><h3>${esc(event.name)}</h3><p>${esc(event.description)}</p><p class="muted">${esc(date(event.date))} · ${esc(event.hour)}</p><a class="link" href="#evento/${encodeURIComponent(event.id)}">Ver detalhes →</a></div></article>`;
}

async function catalog(currentVersion) {
    shell('Eventos disponíveis', '<form id="search" class="filters"><label for="query">Buscar por nome</label><input class="field search" id="query" type="search"><button class="primary" type="submit">Buscar</button></form><div id="results" aria-live="polite"></div>');
    const results = document.getElementById('results');
    let searchVersion = 0;
    async function load() {
        const request = ++searchVersion;
        results.textContent = 'Carregando eventos…';
        try {
            const name = document.getElementById('query').value.trim();
            const events = await api(`/events${name ? `?nome=${encodeURIComponent(name)}` : ''}`);
            if (currentVersion !== version || request !== searchVersion) return;
            results.innerHTML = events.length ? `<div class="cards">${events.map(eventCard).join('')}</div>` : '<p class="empty">Nenhum evento encontrado.</p>';
        } catch (error) { if (currentVersion === version && request === searchVersion) results.textContent = error.message; }
    }
    document.getElementById('search').onsubmit = event => { event.preventDefault(); load(); };
    await load();
}

async function detail(id, currentVersion) {
    shell('Detalhes do evento', '<p>Carregando…</p>');
    const event = await api(`/events/${encodeURIComponent(id)}`);
    if (currentVersion !== version) return;
    shell(event.name, `<a class="link" href="#eventos">← Voltar aos eventos</a><article class="panel">${event.imageUrl ? `<img class="event-detail-image" src="/events/${encodeURIComponent(event.id)}/image" alt="${esc(event.name)}">` : ''}<p class="event-description">${esc(event.description)}</p><p>Data: ${esc(date(event.date))}</p><p>Horário: ${esc(event.hour)}</p><p>Duração: ${esc(event.duration)}</p><p>Capacidade: ${esc(event.capacity)}</p><button id="enroll" class="primary" type="button">${user ? 'Inscrever-me' : 'Entrar para se inscrever'}</button><p id="enroll-result" role="status"></p></article>`);
    document.getElementById('enroll').onclick = async () => {
        if (!user) { nextEvent = id; location.hash = '#login'; return; }
        const button = document.getElementById('enroll');
        const result = document.getElementById('enroll-result');
        button.disabled = true;
        try {
            await api(`/registrations/events/${encodeURIComponent(id)}`, { method: 'POST', authenticated: true });
            result.innerHTML = 'Inscrição realizada! <a class="link" href="#inscricoes">Ver minhas inscrições</a>';
            button.textContent = 'Inscrição confirmada';
        } catch (error) { result.textContent = error.message; button.disabled = false; }
    };
}

async function registerUser(name, email, password) {
    const normalizedName = name.trim();
    if (!normalizedName) throw new Error('Informe seu nome.');
    return api('/auth/register', {
        method: 'POST',
        body: { name: normalizedName, email: email.trim(), password }
    });
}

function authForm(register) {
    shell(register ? 'Criar conta de participante' : 'Entrar como participante', `<div class="panel auth-panel"><form id="auth-form">${register ? '<label for="name">Nome</label><input id="name" name="name" autocomplete="name" required maxlength="150">' : ''}<label for="email">E-mail</label><input id="email" name="email" type="email" autocomplete="email" required><label for="password">Senha${register ? ' (mínimo de 8 caracteres)' : ''}</label><input id="password" name="password" type="password" autocomplete="${register ? 'new-password' : 'current-password'}" ${register ? 'minlength="8" maxlength="72"' : ''} required>${register ? '<label for="confirm">Confirmar senha</label><input id="confirm" type="password" autocomplete="new-password" required>' : ''}<button class="primary" type="submit">${register ? 'Criar conta' : 'Entrar'}</button><p id="auth-result" role="status"></p></form><a class="link" href="#${register ? 'login' : 'cadastro'}">${register ? 'Já tenho conta' : 'Ainda não tenho conta'}</a></div>`);
    document.getElementById('auth-form').onsubmit = async event => {
        event.preventDefault();
        const form = event.currentTarget;
        const result = document.getElementById('auth-result');
        const button = form.querySelector('button');
        const credentials = { email: form.elements.email.value.trim(), password: form.elements.password.value };
        if (register && credentials.password !== document.getElementById('confirm').value) { result.textContent = 'As senhas não coincidem.'; return; }
        button.disabled = true;
        result.textContent = register ? 'Criando conta…' : 'Entrando…';
        let created = false;
        try {
            if (register) {
                await registerUser(form.elements.name.value, credentials.email, credentials.password);
                created = true;
            }
            const session = await api('/auth/participant/login', { method: 'POST', body: credentials });
            token = session.token;
            user = await api('/auth/me', { authenticated: true });
            sessionStorage.setItem('participantToken', token);
            navigation();
            location.hash = nextEvent ? `#evento/${nextEvent}` : '#eventos';
            nextEvent = null;
        } catch (error) { clearSession(); result.textContent = `${created ? 'Conta criada. Entre novamente para continuar. ' : ''}${error.message}`; button.disabled = false; }
    };
}

async function registrations(currentVersion) {
    if (!user) { location.hash = '#login'; return; }
    shell('Minhas inscrições', '<p>Carregando…</p>');
    const [items, events] = await Promise.all([api('/registrations/me', { authenticated: true }), api('/events')]);
    if (currentVersion !== version) return;
    const names = new Map(events.map(event => [event.id, event.name]));
    shell('Minhas inscrições', items.length ? items.map(item => `<article class="panel"><h2>${esc(names.get(item.eventId) || 'Evento')}</h2><p>${item.present ? 'Presença confirmada' : 'Inscrição confirmada'}</p><a class="link" href="#evento/${encodeURIComponent(item.eventId)}">Ver evento</a> <button class="secondary qr-button" type="button" data-id="${esc(item.id)}">Mostrar QR Code</button><div class="qr-result" role="status"></div></article>`).join('') : '<p class="empty">Você ainda não possui inscrições.</p>');
    document.querySelectorAll('.qr-button').forEach(button => button.onclick = async () => {
        const result = button.nextElementSibling;
        button.disabled = true;
        try {
            const qr = await api(`/registrations/${encodeURIComponent(button.dataset.id)}/qrcode`, { authenticated: true });
            const image = document.createElement('img');
            if (!/^data:image\/png;base64,[A-Za-z0-9+/=]+$/.test(qr)) throw new Error('QR Code inválido.');
            image.src = qr;
            image.alt = 'QR Code da inscrição para apresentar na entrada do evento';
            result.replaceChildren(image);
        } catch (error) { result.textContent = error.message; }
        finally { button.disabled = false; }
    });
}

async function route() {
    const currentVersion = ++version;
    const [section, id] = location.hash.slice(1).split('/');
    try {
        if (section === 'login' || section === 'cadastro') authForm(section === 'cadastro');
        else if (section === 'evento' && id) await detail(id, currentVersion);
        else if (section === 'inscricoes') await registrations(currentVersion);
        else await catalog(currentVersion);
    } catch (error) { if (currentVersion === version) shell('Não foi possível carregar', `<p role="alert">${esc(error.message)}</p><a class="link" href="#eventos">Voltar aos eventos</a>`); }
}

async function initialize() {
    if (token) {
        try { user = await api('/auth/me', { authenticated: true }); if (user.role !== 'USER') clearSession(); }
        catch { clearSession(); }
    }
    navigation();
    window.addEventListener('hashchange', route);
    await route();
}
initialize();
