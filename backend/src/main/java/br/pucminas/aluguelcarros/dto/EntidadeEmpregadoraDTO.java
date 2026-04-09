package br.pucminas.aluguelcarros.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.micronaut.serde.annotation.Serdeable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Serdeable
public class EntidadeEmpregadoraDTO {

    @NotBlank
    @JsonProperty("nomeEmpresa")
    private String nomeEmpresa;

    @NotBlank
    @JsonProperty("cnpj")
    private String cnpj;

    @NotNull
    @JsonProperty("rendimento")
    private Double rendimento;

    public EntidadeEmpregadoraDTO() {
    }

    public String getNomeEmpresa() {
        return nomeEmpresa;
    }

    public void setNomeEmpresa(String nomeEmpresa) {
        this.nomeEmpresa = nomeEmpresa;
    }

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public Double getRendimento() {
        return rendimento;
    }

    public void setRendimento(Double rendimento) {
        this.rendimento = rendimento;
    }
}
