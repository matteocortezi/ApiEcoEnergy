package eco.energy.api.controller;

import eco.energy.api.dto.tarefaDto.DadosAtualizacaoTarefa;
import eco.energy.api.dto.tarefaDto.DadosCadastroTarefa;
import eco.energy.api.dto.tarefaDto.DadosDetalhamentoTarefa;
import eco.energy.api.dto.tarefaDto.DadosListagemTarefa;
import eco.energy.api.model.Tarefa;
import eco.energy.api.model.Usuario;
import eco.energy.api.repository.TarefaRepository;
import eco.energy.api.repository.UsuarioRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("tarefa")
@Tag(name = "Tarefas", description = "Operações relacionadas ao cadastro e gerenciamento de tarefas")
public class TarefaController {

    @Autowired
    private TarefaRepository tarefaRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Operation(summary = "Cadastrar uma nova tarefa", description = "Registra uma nova tarefa no sistema vinculada a um usuário existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Tarefa cadastrada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos")
    })
    @PostMapping
    public ResponseEntity<Tarefa> criarTarefa(@RequestBody Tarefa tarefa) {
        if (tarefa.getUsuario().getId() != null) {
            Usuario usuario = usuarioRepository.findById(tarefa.getUsuario().getId())
                    .orElseThrow(() -> new RuntimeException("Usuário não encontrado."));
            tarefa.setUsuario(usuario);
        }
        Tarefa novaTarefa = tarefaRepository.save(tarefa);
        return ResponseEntity.status(HttpStatus.CREATED).body(novaTarefa);
    }

    @Operation(summary = "Listar tarefas", description = "Retorna uma lista paginada de todas as tarefas registradas")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de tarefas cadastradas"),
            @ApiResponse(responseCode = "400", description = "Requisição inválida")
    })
    @GetMapping
    public ResponseEntity<Page<DadosListagemTarefa>> listar(
            @PageableDefault(size = 10, page = 0) Pageable paginacao) {
        var page = tarefaRepository.findAll(paginacao).map(DadosListagemTarefa::new);
        return ResponseEntity.ok(page);
    }

    @Operation(summary = "Atualizar informações de uma tarefa", description = "Atualiza os dados de uma tarefa existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Informações da tarefa atualizadas com sucesso"),
            @ApiResponse(responseCode = "404", description = "Tarefa não encontrada"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos")
    })
    @PutMapping
    @Transactional
    public ResponseEntity<DadosDetalhamentoTarefa> atualizar(
            @RequestBody @Valid @Parameter(description = "Dados atualizados da tarefa", required = true) DadosAtualizacaoTarefa dados) {
        var tarefa = tarefaRepository.getReferenceById(dados.idTarefa());
        tarefa.atualizarInformacoes(dados);
        return ResponseEntity.ok(new DadosDetalhamentoTarefa(tarefa));
    }

    @Operation(summary = "Excluir uma tarefa", description = "Remove uma tarefa do sistema com base no ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tarefa excluída com sucesso"),
            @ApiResponse(responseCode = "404", description = "Tarefa não encontrada")
    })
    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<Void> excluir(
            @Parameter(description = "ID da tarefa a ser excluída", required = true, example = "1") @PathVariable Long id) {
        tarefaRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Detalhar uma tarefa", description = "Retorna os detalhes de uma tarefa específica")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Detalhamento da tarefa"),
            @ApiResponse(responseCode = "404", description = "Tarefa não encontrada")
    })
    @GetMapping("/{id}")
    public ResponseEntity<DadosDetalhamentoTarefa> detalhar(
            @PathVariable Long id) {
        var tarefa = tarefaRepository.getReferenceById(id);
        return ResponseEntity.ok(new DadosDetalhamentoTarefa(tarefa));
    }
}
