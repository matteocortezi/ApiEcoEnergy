package eco.energy.api.dto.contaDto;

import eco.energy.api.enums.Mes;


public record DadosCadastroConta(Long idUsuario, Double valorTotal, Double consumoKwh, Mes mes) {
}
