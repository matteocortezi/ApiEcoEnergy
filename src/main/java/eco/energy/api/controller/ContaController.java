package eco.energy.api.controller;

import eco.energy.api.dto.contaDto.DadosAtualizacaoConta;
import eco.energy.api.dto.contaDto.DadosCadastroConta;
import eco.energy.api.dto.contaDto.DadosDetalhamentoConta;
import eco.energy.api.dto.contaDto.DadosListagemConta;
import eco.energy.api.model.Conta;
import eco.energy.api.model.Usuario;
import eco.energy.api.repository.ContaRepository;
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
@RequestMapping("conta")
@Tag(name = "Contas", description = "Operações relacionadas ao cadastro e gerenciamento de contas")
public class ContaController {

    @Autowired
    private ContaRepository contaRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Operation(summary = "Cadastrar uma nova conta", description = "Registra uma nova conta no sistema vinculada a um usuário existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Conta cadastrada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos")
    })
    @PostMapping
    public ResponseEntity<Conta> criarConta(@RequestBody Conta conta) {
        if (conta.getUsuario().getId() != null) {
            Usuario usuario = usuarioRepository.findById(conta.getUsuario().getId())
                    .orElseThrow(() -> new RuntimeException("Usuário não encontrado."));
            conta.setUsuario(usuario);
        }
        Conta novaConta = contaRepository.save(conta);
        return ResponseEntity.status(HttpStatus.CREATED).body(novaConta);
    }

    @Operation(summary = "Listar contas", description = "Retorna uma lista paginada de todas as contas registradas")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de contas cadastradas"),
            @ApiResponse(responseCode = "400", description = "Requisição inválida")
    })
    @GetMapping
    public ResponseEntity<Page<DadosListagemConta>> listar(
            @PageableDefault(size = 10, page = 0) Pageable paginacao) {
        var page = contaRepository.findAll(paginacao).map(DadosListagemConta::new);
        return ResponseEntity.ok(page);
    }

    @Operation(summary = "Atualizar informações de uma conta", description = "Atualiza os dados de uma conta existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Informações da conta atualizadas com sucesso"),
            @ApiResponse(responseCode = "404", description = "Conta não encontrada"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos")
    })
    @PutMapping
    @Transactional
    public ResponseEntity<DadosDetalhamentoConta> atualizar(
            @RequestBody @Valid @Parameter(description = "Dados atualizados da conta", required = true) DadosAtualizacaoConta dados) {
        var conta = contaRepository.getReferenceById(dados.idConta());
        conta.atualizarInformacoes(dados);
        return ResponseEntity.ok(new DadosDetalhamentoConta(conta));
    }

    @Operation(summary = "Excluir uma conta", description = "Remove uma conta do sistema com base no ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Conta excluída com sucesso"),
            @ApiResponse(responseCode = "404", description = "Conta não encontrada")
    })
    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<Void> excluir(
            @Parameter(description = "ID da conta a ser excluída", required = true, example = "1") @PathVariable Long id) {
        contaRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Detalhar uma conta", description = "Retorna os detalhes de uma conta específica")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Detalhamento da conta"),
            @ApiResponse(responseCode = "404", description = "Conta não encontrada")
    })
    @GetMapping("/{id}")
    public ResponseEntity<DadosDetalhamentoConta> detalhar(
            @PathVariable Long id) {
        var conta = contaRepository.getReferenceById(id);
        return ResponseEntity.ok(new DadosDetalhamentoConta(conta));
    }
}
