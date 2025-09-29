let movimentacoes = [];
let saldo = 0;

function mostrarTela(idTela) {
    const telas = document.querySelectorAll('.tela');
    telas.forEach(t => t.classList.remove('ativa'));
    document.getElementById(idTela).classList.add('ativa');
    
    if (idTela === 'telaEntrada') {
        document.getElementById('dataEntrada').valueAsDate = new Date();
    } else if (idTela === 'telaSaida') {
        document.getElementById('dataSaida').valueAsDate = new Date();
    }
}

function formatarMoeda(valor) {
    return valor.toLocaleString('pt-BR', {
        style: 'currency',
        currency: 'BRL'
    });
}

function formatarData(data) {
    const d = new Date(data + 'T00:00:00');
    return d.toLocaleDateString('pt-BR');
}

function calcularTotais() {
    let totalEntradas = 0;
    let totalSaidas = 0;
    
    movimentacoes.forEach(mov => {
        if (mov.tipo === 'entrada') {
            totalEntradas += mov.valor;
        } else {
            totalSaidas += mov.valor;
        }
    });
    
    saldo = totalEntradas - totalSaidas;
    
    document.getElementById('saldoTotal').textContent = formatarMoeda(saldo);
    document.getElementById('totalEntradas').textContent = formatarMoeda(totalEntradas);
    document.getElementById('totalSaidas').textContent = formatarMoeda(totalSaidas);
}

function atualizarLista() {
    const lista = document.getElementById('listaMovimentacoes');
    
    if (movimentacoes.length === 0) {
        lista.innerHTML = '<p class="lista-vazia">Nenhuma movimentação registrada</p>';
        return;
    }
    
    const movOrdenadas = [...movimentacoes].sort((a, b) => {
        return new Date(b.data) - new Date(a.data);
    });
    
    lista.innerHTML = '';
    
    movOrdenadas.forEach((mov, idx) => {
        const item = document.createElement('div');
        item.className = 'item-movimentacao';
        
        const valorClass = mov.tipo === 'entrada' ? 'positivo' : 'negativo';
        const sinal = mov.tipo === 'entrada' ? '+' : '-';
        
        item.innerHTML = `
            <div class="info-movimentacao">
                <div class="desc-movimentacao">${mov.descricao}</div>
                <div class="data-movimentacao">${formatarData(mov.data)}</div>
            </div>
            <span class="valor-movimentacao ${valorClass}">${sinal} ${formatarMoeda(mov.valor)}</span>
            <button class="btn-deletar" onclick="deletarMovimentacao(${mov.id})">×</button>
        `;
        
        lista.appendChild(item);
    });
}

function adicionarEntrada(e) {
    e.preventDefault();
    
    const descricao = document.getElementById('descricaoEntrada').value;
    const valor = parseFloat(document.getElementById('valorEntrada').value);
    const data = document.getElementById('dataEntrada').value;
    
    const novaMovimentacao = {
        id: Date.now(),
        tipo: 'entrada',
        descricao: descricao,
        valor: valor,
        data: data
    };
    
    movimentacoes.push(novaMovimentacao);
    
    calcularTotais();
    atualizarLista();
    
    document.getElementById('descricaoEntrada').value = '';
    document.getElementById('valorEntrada').value = '';
    
    mostrarTela('telaInicial');
}

function adicionarSaida(e) {
    e.preventDefault();
    
    const descricao = document.getElementById('descricaoSaida').value;
    const valor = parseFloat(document.getElementById('valorSaida').value);
    const data = document.getElementById('dataSaida').value;
    
    const novaMovimentacao = {
        id: Date.now(),
        tipo: 'saida',
        descricao: descricao,
        valor: valor,
        data: data
    };
    
    movimentacoes.push(novaMovimentacao);
    
    calcularTotais();
    atualizarLista();
    
    document.getElementById('descricaoSaida').value = '';
    document.getElementById('valorSaida').value = '';
    
    mostrarTela('telaInicial');
}

function deletarMovimentacao(id) {
    movimentacoes = movimentacoes.filter(mov => mov.id !== id);
    calcularTotais();
    atualizarLista();
}

calcularTotais();
atualizarLista();
