package eco.energy.api.controller;
import eco.energy.api.dto.tarefaDto.DadosAtualizacaoTarefa;
import eco.energy.api.dto.tarefaDto.DadosCadastroTarefa;
import eco.energy.api.dto.tarefaDto.DadosDetalhamentoTarefa;
import eco.energy.api.dto.tarefaDto.DadosListagemTarefa;
import eco.energy.api.model.Tarefa;
import eco.energy.api.repository.TarefaRepository;
import eco.energy.api.repository.UsuarioRepository;
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
public class TarefaController {
    @Autowired
    private TarefaRepository tarefaRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @PostMapping
    @Transactional
    public ResponseEntity cadastrar(@RequestBody @Valid DadosCadastroTarefa dados, UriComponentsBuilder uriBuilder) {
        var usuario = usuarioRepository.findById(dados.idUsuario())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado"));

        var tarefa = new Tarefa(dados);
        tarefa.setUsuario(usuario);

        tarefaRepository.save(tarefa);

        var uri = uriBuilder.path("/tarefa/{id}").buildAndExpand(tarefa.getIdTarefa()).toUri();
        return ResponseEntity.created(uri).body(new DadosDetalhamentoTarefa(tarefa));
    }
    @GetMapping
    public ResponseEntity<Page<DadosListagemTarefa>> listar(@PageableDefault(size = 10, page = 0) Pageable paginacao){
        var page = tarefaRepository.findAll(paginacao).map(DadosListagemTarefa::new);
        return ResponseEntity.ok(page);
    }
    @PutMapping
    @Transactional
    public ResponseEntity atualizar(@RequestBody @Valid DadosAtualizacaoTarefa dados){
        var tarefa = tarefaRepository.getReferenceById(dados.idTarefa());
        tarefa.atualizarInformacoes(dados);
        return ResponseEntity.ok(new DadosDetalhamentoTarefa(tarefa));
    }
    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity excluir(@PathVariable Long id){
        tarefaRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }
    @GetMapping("/{id}")
    public ResponseEntity detalhar(@PathVariable Long id){
        var tarefa = tarefaRepository.getReferenceById(id);
        return ResponseEntity.ok(new DadosDetalhamentoTarefa(tarefa));
    }
}
