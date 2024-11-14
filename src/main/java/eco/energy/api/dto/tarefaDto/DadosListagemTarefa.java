package eco.energy.api.dto.tarefaDto;

import eco.energy.api.model.Tarefa;

public record DadosListagemTarefa(String nome, String descricao, Boolean status) {
    public DadosListagemTarefa(Tarefa tarefa) {
        this(tarefa.getNome(), tarefa.getDescricao(), tarefa.getStatus());
    }
}
