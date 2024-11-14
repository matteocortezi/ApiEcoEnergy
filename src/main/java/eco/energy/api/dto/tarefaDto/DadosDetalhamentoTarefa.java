package eco.energy.api.dto.tarefaDto;
import eco.energy.api.model.Tarefa;

public record DadosDetalhamentoTarefa(Long idTarefa, String nome, String descricao, Boolean status) {
    public DadosDetalhamentoTarefa(Tarefa tarefa) {
        this(tarefa.getIdTarefa(), tarefa.getNome(), tarefa.getDescricao(), tarefa.getStatus());
    }
}
