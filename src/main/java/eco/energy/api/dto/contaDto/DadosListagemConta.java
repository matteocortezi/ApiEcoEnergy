package eco.energy.api.dto.contaDto;

import eco.energy.api.enums.Mes;
import eco.energy.api.model.Conta;

public record DadosListagemConta(Double valorTotal, Double consumoKwh, Mes mes) {
    public DadosListagemConta (Conta conta){
        this(conta.getValorTotal(), conta.getConsumoKwh(), conta.getMes());
    }
}
