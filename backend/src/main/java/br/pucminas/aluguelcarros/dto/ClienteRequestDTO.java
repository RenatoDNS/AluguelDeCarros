package br.pucminas.aluguelcarros.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.micronaut.serde.annotation.Serdeable;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import java.util.List;

@Serdeable
public class ClienteRequestDTO {

    @NotBlank
    @JsonProperty("rg")
    private String rg;

    @NotBlank
    @JsonProperty("cpf")
    private String cpf;

    @NotBlank
    @JsonProperty("nome")
    private String nome;

    @NotBlank
    @JsonProperty("endereco")
    private String endereco;

    @NotBlank
    @JsonProperty("profissao")
    private String profissao;

    @NotBlank
    @JsonProperty("senha")
    private String senha;

    @NotEmpty
    @Size(min = 1, max = 3)
    @Valid
    @JsonProperty("entidadesEmpregadoras")
    private List<EntidadeEmpregadoraDTO> entidadesEmpregadoras;

    public ClienteRequestDTO() {
    }

    public String getRg() {
        return rg;
    }

    public void setRg(String rg) {
        this.rg = rg;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getProfissao() {
        return profissao;
    }

    public void setProfissao(String profissao) {
        this.profissao = profissao;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public List<EntidadeEmpregadoraDTO> getEntidadesEmpregadoras() {
        return entidadesEmpregadoras;
    }

    public void setEntidadesEmpregadoras(List<EntidadeEmpregadoraDTO> entidadesEmpregadoras) {
        this.entidadesEmpregadoras = entidadesEmpregadoras;
    }
}
