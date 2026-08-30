const eventosFingidos = [
    { nome: "Semana da Computação", data: "2026-09-15", remoto: false },
    { nome: "Workshop de Java", data: "2026-09-20", remoto: true }
];

const listaEventos = document.getElementById("lista-eventos");

listaEventos.innerHTML = "";

eventosFingidos.forEach(evento => {
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

const formInscricao = document.getElementById("form-inscricao");
const erroNome = document.getElementById("erro-nome");
const erroEmail = document.getElementById("erro-email");

function emailValido(email) {
    return email.includes("@") && email.includes(".");
}

formInscricao.addEventListener("submit", function(evento) {
    evento.preventDefault();

    const nome = document.getElementById("nome").value;
    const email = document.getElementById("email").value;

    erroNome.textContent = "";
    erroEmail.textContent = "";

    let temErro = false;

    if (nome.trim() === "") {
        erroNome.textContent = "O nome não pode ficar vazio.";
        temErro = true;
    }

    if (!emailValido(email)) {
        erroEmail.textContent = "Digite um e-mail válido.";
        temErro = true;
    }

    if (temErro) {
        return;
    }

    console.log("Inscrição recebida:");
    console.log("Nome: " + nome);
    console.log("E-mail: " + email);

    const mensagemSucesso = document.getElementById("mensagem-sucesso");
    mensagemSucesso.classList.remove("sucesso-escondido");

    formInscricao.reset();
});