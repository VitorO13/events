const eventosFingidos = [
    { nome: "Semana da Computação", data: "2026-09-15", remoto: false },
    { nome: "Workshop de Java", data: "2026-09-20", remoto: true }
];

const listaEventos = document.getElementById("lista-eventos");

listaEventos.innerHTML = "";

const seletorEvento = document.getElementById("evento");

eventosFingidos.forEach(evento => {
    const opcao = document.createElement("option");
    opcao.value = evento.nome;
    opcao.textContent = evento.nome + " - " + evento.data;
    seletorEvento.appendChild(opcao);
});

function renderizarEventos(listaParaMostrar) {
    listaEventos.innerHTML = "";

    listaParaMostrar.forEach(evento => {
        const card = document.createElement("div");
        card.className = "evento-card";

        const nome = document.createElement("h3");
        nome.textContent = evento.nome;

        const data = document.createElement("p");
        data.textContent = "📅 " + evento.data;

        const modalidade = document.createElement("span");
        modalidade.className = evento.remoto ? "tag tag-remoto" : "tag tag-presencial";
        modalidade.textContent = evento.remoto ? "Remoto" : "Presencial";

        card.appendChild(nome);
        card.appendChild(data);
        card.appendChild(modalidade);

        listaEventos.appendChild(card);
    });
}

renderizarEventos(eventosFingidos);

const formInscricao = document.getElementById("form-inscricao");
const erroNome = document.getElementById("erro-nome");
const erroEmail = document.getElementById("erro-email");
const erroEvento = document.getElementById("erro-evento");
const erroTelefone = document.getElementById("erro-telefone");

function emailValido(email) {
    return email.includes("@") && email.includes(".");
}

formInscricao.addEventListener("submit", function(evento) {
    evento.preventDefault();

    const nome = document.getElementById("nome").value;
    const email = document.getElementById("email").value;
    const eventoEscolhido = document.getElementById("evento").value;

    erroNome.textContent = "";
    erroEmail.textContent = "";
    erroEvento.textContent = "";
    erroTelefone.textContent = "";

    let temErro = false;

    if (nome.trim() === "") {
        erroNome.textContent = "O nome não pode ficar vazio.";
        temErro = true;
    }

    if (!emailValido(email)) {
        erroEmail.textContent = "Digite um e-mail válido.";
        temErro = true;
    }

    if (eventoEscolhido === "") {
        erroEvento.textContent = "Selecione um evento.";
        temErro = true;
    }

    const telefone = document.getElementById("telefone").value;
    const apenasNumeros = telefone.replace(/\D/g, "");

    if (telefone.trim() !== "" && apenasNumeros.length < 10) {
        erroTelefone.textContent = "Digite um telefone válido (com DDD).";
        temErro = true;
    }

    if (temErro) {
        return;
    }

    console.log("Inscrição recebida:");
    console.log("Nome: " + nome);
    console.log("E-mail: " + email);
    console.log("Evento: " + eventoEscolhido);

    const mensagemSucesso = document.getElementById("mensagem-sucesso");
    mensagemSucesso.classList.remove("sucesso-escondido");

    formInscricao.reset();
});

const campoBusca = document.getElementById("busca-evento");

campoBusca.addEventListener("input", function() {
    const termoBuscado = campoBusca.value.toLowerCase();

    const eventosFiltrados = eventosFingidos.filter(evento =>
        evento.nome.toLowerCase().includes(termoBuscado)
    );

    renderizarEventos(eventosFiltrados);
});