package eco.energy.api.dto.dispositivoDto;

import eco.energy.api.model.Dispositivo;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record DadosListagemDispositivo(


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
    public DadosListagemDispositivo(Dispositivo dispositivo){
        this(dispositivo.getNomeDispositivo(), dispositivo.getTipoDispositivo(), dispositivo.getConsumoWatts(), dispositivo.getEstadoDispositivo());
    }}
