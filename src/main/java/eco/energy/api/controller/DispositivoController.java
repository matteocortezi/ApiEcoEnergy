package eco.energy.api.controller;

import eco.energy.api.dto.dispositivoDto.DadosAtualizacaoDispositivo;
import eco.energy.api.dto.dispositivoDto.DadosDetalhamentoDispositivo;
import eco.energy.api.dto.dispositivoDto.DadosListagemDispositivo;
import eco.energy.api.model.Dispositivo;
import eco.energy.api.model.Usuario;
import eco.energy.api.repository.DispositivoRepository;
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


@RestController
@RequestMapping("dispositivos")
public class DispositivoController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private DispositivoRepository dispositivoRepository;

    @PostMapping
    public ResponseEntity<Dispositivo> criarDispositivo(@RequestBody Dispositivo dispositivo) {
        if (dispositivo.getUsuario().getId() != null) {
            Usuario usuario = usuarioRepository.findById(dispositivo.getUsuario().getId())
                    .orElseThrow(() -> new RuntimeException("Usuário não encontrado."));
            dispositivo.setUsuario(usuario);
        }
        Dispositivo novoDispositivo = dispositivoRepository.save(dispositivo);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoDispositivo);
    }

    @Autowired
    private DispositivoRepository repository;

    @GetMapping
    public ResponseEntity<Page<DadosListagemDispositivo>> listar(@PageableDefault(size = 10) Pageable paginacao) {
        var page = repository.findAll(paginacao).map(DadosListagemDispositivo::new);
        return ResponseEntity.ok(page);
    }

    @PutMapping
    public ResponseEntity atualizar(@RequestBody @Valid DadosAtualizacaoDispositivo dados){
        var dispositivo = repository.getReferenceById(dados.idDispositivo());
        dispositivo.atualizarInformacoes(dados);
        return ResponseEntity.ok(new DadosDetalhamentoDispositivo(dispositivo));
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity excluir(@PathVariable Long id){
        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
    @GetMapping("/{id}")
    public ResponseEntity detalhar(@PathVariable Long id){
        var dispositivo = repository.getReferenceById(id);
        return ResponseEntity.ok(new DadosDetalhamentoDispositivo(dispositivo));
    }


}
