package eco.energy.api.model;
import eco.energy.api.dto.tarefaDto.DadosAtualizacaoTarefa;
import eco.energy.api.dto.tarefaDto.DadosCadastroTarefa;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Table(name = "Tarefa")
@Entity(name = "Tarefa")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "idTarefa")
public class Tarefa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_tarefa", nullable = false)
    private Long idTarefa;

    @NotBlank(message = "O nome da tarefa não pode ser vazio")
    @Size(min = 3, max = 100, message = "O nome da tarefa deve ter entre 3 e 100 caracteres")
    @Column(name = "nome_tarefa", nullable = false)
    private String nome;

    @NotBlank(message = "A descrição não pode ser vazia")
    @Size(min = 5, max = 255, message = "A descrição deve ter entre 5 e 255 caracteres")
    @Column(name = "descricao_tarefa", nullable = false)
    private String descricao;

    @NotNull(message = "O status não pode ser nulo")
    @Column(name = "status_tarefa", nullable = true)
    private Boolean status; // true para completada, false para não completada

    @ManyToOne
    @JoinColumn(name = "Usuario_id_usuario", nullable = false)
    private Usuario usuario;

    public Tarefa(DadosCadastroTarefa dados){
        this.nome = dados.nome();
        this.descricao = dados.descricao();
        this.status = dados.status();
    }
    public void atualizarInformacoes(@Valid DadosAtualizacaoTarefa dados) {
        if (dados.nome() != null) {
            this.nome = dados.nome();
        }
        if (dados.descricao() != null) {
            this.descricao = dados.descricao();
        }
        if (dados.status() != null) {
            this.status = dados.status();

        }}}



