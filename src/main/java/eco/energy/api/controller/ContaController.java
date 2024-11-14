package eco.energy.api.controller;
import eco.energy.api.dto.contaDto.DadosAtualizacaoConta;
import eco.energy.api.dto.contaDto.DadosCadastroConta;
import eco.energy.api.dto.contaDto.DadosDetalhamentoConta;
import eco.energy.api.dto.contaDto.DadosListagemConta;
import eco.energy.api.model.Conta;
import eco.energy.api.repository.ContaRepository;
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
@RequestMapping("conta")
public class ContaController {
    @Autowired
    private ContaRepository contaRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @PostMapping
    @Transactional
    public ResponseEntity cadastrar(@RequestBody @Valid DadosCadastroConta dados, UriComponentsBuilder uriBuilder) {
        var usuario = usuarioRepository.findById(dados.idUsuario())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado"));
        var conta = new Conta(dados);
        conta.setUsuario(usuario);
        contaRepository.save(conta);

        var uri = uriBuilder.path("/conta/{id}").buildAndExpand(conta.getIdConta()).toUri();
        return ResponseEntity.created(uri).body(new DadosDetalhamentoConta(conta));
    }

    @GetMapping
    public ResponseEntity<Page<DadosListagemConta>> listar(@PageableDefault(size = 10, page = 0) Pageable paginacao){
        var page = contaRepository.findAll(paginacao).map(DadosListagemConta::new);
        return ResponseEntity.ok(page);
    }
    @PutMapping
    @Transactional
    public ResponseEntity atualizar(@RequestBody @Valid DadosAtualizacaoConta dados){
        var conta = contaRepository.getReferenceById(dados.idConta());
        conta.atualizarInformacoes(dados);
        return ResponseEntity.ok(new DadosDetalhamentoConta(conta));
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity excluir(@PathVariable Long id){
        contaRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
    @GetMapping("/{id}")
    public ResponseEntity detalhar(@PathVariable Long id){
        var conta = contaRepository.getReferenceById(id);

        return ResponseEntity.ok(new DadosDetalhamentoConta(conta));
    }



}
