package eco.energy.api.model;
import eco.energy.api.dto.contaDto.DadosAtualizacaoConta;
import eco.energy.api.dto.contaDto.DadosCadastroConta;
import eco.energy.api.enums.Mes;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Table(name = "Conta")
@Entity(name = "Conta")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "idConta")
public class Conta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_conta", nullable = false)
    private Long idConta;

    @NotNull(message = "O valor total não pode ser nulo")
    @DecimalMin(value = "0.01", message = "O valor total deve ser maior que 0")
    @Column(name = "valor_conta", nullable = false)
    private Double valorTotal;

    @NotNull(message = "O consumo de kWh não pode ser nulo")
    @DecimalMin(value = "0.01", message = "O consumo de kWh deve ser maior que 0")
    @Column(name = "consumo_kwt_conta", nullable = false)
    private Double consumoKwh;

    @NotNull(message = "O mês não pode ser nulo")
    @Enumerated(EnumType.STRING)
    @Column(name = "mes_conta", nullable = false)
    private Mes mes;

    @ManyToOne
    @JoinColumn(name = "Usuario_id_usuario", nullable = false)
    private Usuario usuario;



    public Conta(DadosCadastroConta dados) {
        this.valorTotal = dados.valorTotal();
        this.consumoKwh = dados.consumoKwh();
        this.mes = dados.mes();
    }

    public void atualizarInformacoes(@Valid DadosAtualizacaoConta dados) {
        if (dados.valorTotal() != null) {
            this.valorTotal = dados.valorTotal();
        }
        if (dados.consumoKwh() != null) {
            this.consumoKwh = dados.consumoKwh();
        }
        if (dados.mes() != null) {
            this.mes = dados.mes();
        }
    }
}
