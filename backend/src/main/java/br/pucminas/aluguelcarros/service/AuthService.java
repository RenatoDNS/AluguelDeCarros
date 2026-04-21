package br.pucminas.aluguelcarros.service;

import at.favre.lib.crypto.bcrypt.BCrypt;
import br.pucminas.aluguelcarros.config.JwtConfig;
import br.pucminas.aluguelcarros.dto.response.AuthMeResponseDTO;
import br.pucminas.aluguelcarros.enums.UserType;
import br.pucminas.aluguelcarros.model.Banco;
import br.pucminas.aluguelcarros.model.Cliente;
import br.pucminas.aluguelcarros.model.Empresa;
import br.pucminas.aluguelcarros.model.Usuario;
import br.pucminas.aluguelcarros.repository.BancoRepository;
import br.pucminas.aluguelcarros.repository.ClienteRepository;
import br.pucminas.aluguelcarros.repository.EmpresaRepository;
import io.jsonwebtoken.Claims;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

import java.util.Objects;

@Singleton
public class AuthService {

    private final ClienteRepository clienteRepository;
    private final EmpresaRepository empresaRepository;
    private final BancoRepository bancoRepository;
    private final JwtConfig jwtConfig;

    @Inject
    public AuthService(ClienteRepository clienteRepository,
                       EmpresaRepository empresaRepository,
                       BancoRepository bancoRepository,
                       JwtConfig jwtConfig) {
        this.clienteRepository = clienteRepository;
        this.empresaRepository = empresaRepository;
        this.bancoRepository = bancoRepository;
        this.jwtConfig = jwtConfig;
    }

    public AuthResult autenticar(String login, String senha) {
        String loginNormalizado = normalizarTexto(login);
        String senhaInformada = normalizarTexto(senha);
        String documentoNormalizado = normalizarDocumento(loginNormalizado);

        Cliente cliente = clienteRepository.findByCpf(documentoNormalizado).orElse(null);
        if (cliente != null && senhaConfere(senhaInformada, cliente.getSenha())) {
            return gerarResultado(cliente, UserType.CLIENTE, documentoNormalizado);
        }

        Empresa empresa = empresaRepository.findByCnpj(documentoNormalizado).orElse(null);
        if (empresa != null && senhaConfere(senhaInformada, empresa.getSenha())) {
            return gerarResultado(empresa, UserType.EMPRESA, documentoNormalizado);
        }

        Banco banco = bancoRepository.findByCnpj(documentoNormalizado).orElse(null);
        if (banco != null && senhaConfere(senhaInformada, banco.getSenha())) {
            return gerarResultado(banco, UserType.BANCO, documentoNormalizado);
        }

        throw new IllegalArgumentException("credenciais");
    }

    public AuthMeResponseDTO obterPerfilAutenticado(String authorizationHeader) {
        String token = extrairBearerToken(authorizationHeader);
        Claims claims = jwtConfig.extrairClaims(token);

        Long id = Long.parseLong(normalizarTexto(claims.getSubject()));
        String login = normalizarTexto(claims.get("login", String.class));
        UserType userType = UserType.fromValue(claims.get("userType", String.class));

        return new AuthMeResponseDTO(id, login, userType);
    }

    private AuthResult gerarResultado(Usuario usuario, UserType userType, String login) {
        String token = jwtConfig.gerarToken(usuario, userType, login);
        return new AuthResult(token, userType);
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

    private static String normalizarDocumento(String documento) {
        return normalizarTexto(documento).replaceAll("\\D", "");
    }

    private static String normalizarTexto(String valor) {
        return valor == null ? "" : valor.trim();
    }

    private static String extrairBearerToken(String authorizationHeader) {
        String header = normalizarTexto(authorizationHeader);
        if (!header.regionMatches(true, 0, "Bearer ", 0, "Bearer ".length())) {
            throw new IllegalArgumentException("token");
        }
        String token = header.substring("Bearer ".length()).trim();
        if (token.isBlank()) {
            throw new IllegalArgumentException("token");
        }
        return token;
    }

    public record AuthResult(String token, UserType userType) {
    }
}
