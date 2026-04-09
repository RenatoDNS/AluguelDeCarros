package br.pucminas.aluguelcarros.service;

import at.favre.lib.crypto.bcrypt.BCrypt;
import br.pucminas.aluguelcarros.config.JwtConfig;
import br.pucminas.aluguelcarros.model.Cliente;
import br.pucminas.aluguelcarros.repository.ClienteRepository;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

import java.util.Objects;

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
        String loginNormalizado = normalizarTexto(login);
        String senhaInformada = normalizarTexto(senha);

        Cliente cliente = clienteRepository.findByCpf(normalizarCpf(loginNormalizado))
                .or(() -> clienteRepository.findByLogin(loginNormalizado))
                .orElseThrow(() -> new IllegalArgumentException("credenciais"));

        if (!senhaConfere(senhaInformada, cliente.getSenha())) {
            throw new IllegalArgumentException("credenciais");
        }
        return jwtConfig.gerarToken(cliente);
    }

    public boolean validarToken(String token) {
        return jwtConfig.validarToken(token);
    }

    private static boolean senhaConfere(String senhaInformada, String senhaPersistida) {
        if (senhaInformada == null || senhaPersistida == null) {
            return false;
        }

        // Compatibilidade com registros legados sem hash.
        if (!senhaPersistida.startsWith("$2")) {
            return Objects.equals(senhaInformada, senhaPersistida);
        }

        try {
            return BCrypt.verifyer().verify(senhaInformada.toCharArray(), senhaPersistida).verified;
        } catch (RuntimeException ignored) {
            return false;
        }
    }

    private static String normalizarCpf(String cpf) {
        return normalizarTexto(cpf).replaceAll("\\D", "");
    }

    private static String normalizarTexto(String valor) {
        return valor == null ? "" : valor.trim();
    }
}
