package br.pucminas.aluguelcarros.service;

import at.favre.lib.crypto.bcrypt.BCrypt;
import br.pucminas.aluguelcarros.config.JwtConfig;
import br.pucminas.aluguelcarros.model.Cliente;
import br.pucminas.aluguelcarros.repository.ClienteRepository;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

@Singleton
public class AuthService {

    private final ClienteRepository clienteRepository;
    private final JwtConfig jwtConfig;

    @Inject
    public AuthService(ClienteRepository clienteRepository, JwtConfig jwtConfig) {
        this.clienteRepository = clienteRepository;
        this.jwtConfig = jwtConfig;
    }

    public String autenticar(String login, String senha) {
        Cliente cliente = clienteRepository.findByCpf(login)
                .or(() -> clienteRepository.findByLogin(login))
                .orElseThrow(() -> new IllegalArgumentException("credenciais"));
        if (!BCrypt.verifyer().verify(senha.toCharArray(), cliente.getSenha()).verified) {
            throw new IllegalArgumentException("credenciais");
        }
        return jwtConfig.gerarToken(cliente);
    }

    public boolean validarToken(String token) {
        return jwtConfig.validarToken(token);
    }
}
