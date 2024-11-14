package eco.energy.api.dto.tarefaDto;

import jakarta.validation.constraints.NotNull;

public record DadosAtualizacaoTarefa(@NotNull Long idTarefa, String nome, String descricao, Boolean status) {
}
