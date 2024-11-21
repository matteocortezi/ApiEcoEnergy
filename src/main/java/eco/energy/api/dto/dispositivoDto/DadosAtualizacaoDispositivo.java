package eco.energy.api.dto.dispositivoDto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record DadosAtualizacaoDispositivo(

        @NotBlank
        Long idDispositivo,

        @NotBlank
        String nomeDispositivo,

        @NotBlank
        String tipoDispositivo,

        @NotNull
        @Positive
        Number consumoWatts,

        @NotBlank
        String estadoDispositivo
) {
}

