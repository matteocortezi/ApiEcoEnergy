package eco.energy.api.model;

import eco.energy.api.dto.dispositivoDto.DadosAtualizacaoDispositivo;
import eco.energy.api.dto.dispositivoDto.DadosCadastroDispositivo;
import jakarta.persistence.*;
import lombok.*;

@Table(name = "dispositivo")
@Entity(name = "Dispositivo")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Dispositivo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")  // Nome da coluna id no banco de dados
    private Long id;

    @Column(name = "nome_dispositivo")
    private String nomeDispositivo;

    @Column(name = "tipo_dispositivo")
    private String tipoDispositivo;

    @Column(name = "consumo_watts")
    private Number consumoWatts;

    @Column(name = "estado_dispositivo")
    private String estadoDispositivo;

    @ManyToOne
    @JoinColumn(name = "usuario_id_usuario", nullable = false)
    private Usuario usuario;

    public Dispositivo(DadosCadastroDispositivo dados){
        this.nomeDispositivo = dados.nomeDispositivo();
        this.tipoDispositivo = dados.tipoDispositivo();
        this.consumoWatts = dados.consumoWatts();
        this.estadoDispositivo = dados.estadoDispositivo();

    }
    public void atualizarInformacoes(DadosAtualizacaoDispositivo dados){
        if (dados.nomeDispositivo() != null){
            this.nomeDispositivo = dados.nomeDispositivo();
        }
        if (dados.tipoDispositivo() != null){
            this.tipoDispositivo = dados.tipoDispositivo();
        }
        if (dados.consumoWatts() != null){
            this.consumoWatts = dados.consumoWatts();
        }
        if (dados.estadoDispositivo() != null){
            this.estadoDispositivo = dados.estadoDispositivo();
        }
    }


}
