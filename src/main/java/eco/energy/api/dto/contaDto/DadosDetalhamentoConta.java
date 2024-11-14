package eco.energy.api.dto.contaDto;

import eco.energy.api.enums.Mes;
import eco.energy.api.model.Conta;

public record DadosDetalhamentoConta(Long idConta, Double valorTotal, Double consumoKwh, Mes mes) {
    public DadosDetalhamentoConta(Conta conta){
        this(conta.getIdConta(), conta.getValorTotal(), conta.getConsumoKwh(), conta.getMes());
    }
}
