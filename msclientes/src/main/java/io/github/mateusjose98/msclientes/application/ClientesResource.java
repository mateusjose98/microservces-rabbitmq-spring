package io.github.mateusjose98.msclientes.application;

import io.github.mateusjose98.msclientes.application.representation.ClienteSaveRequest;
import io.github.mateusjose98.msclientes.domain.Cliente;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController @RequestMapping("clientes") @RequiredArgsConstructor
public class ClientesResource {

    private final ClienteService clienteService;

    @GetMapping("/status")
    public String status(){
        return "ok";
    }

    @GetMapping
    public ResponseEntity dadosCliente(@RequestParam String cpf){
        var cliente = clienteService.getByCPF(cpf);
        if (cliente.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(cliente);
    }

    @PostMapping
    public ResponseEntity save(@RequestBody ClienteSaveRequest request){


        Cliente cliente = request.toModel();
        Cliente clienteSalvo = clienteService.save(cliente);
        URI headerLocation = ServletUriComponentsBuilder.fromCurrentRequest().query("cpf={cpf}").buildAndExpand(clienteSalvo.getCpf()).toUri();
        return ResponseEntity.created(headerLocation).build();
    }
}
