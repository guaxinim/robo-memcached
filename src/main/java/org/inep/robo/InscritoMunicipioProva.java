package org.inep.robo;

import javax.persistence.Column;
import javax.persistence.Entity;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

@Entity
public class InscritoMunicipioProva implements Serializable {

    @Column(name = "co_pessoa_fisica")
    private Long codigoPessoaFisica;

    @Column(name = "co_projeto")
    private Integer codigoProjeto;

    @Column(name = "co_inscricao")
    private Long codigoInscricao;

    @Column(name = "no_inscrito")
    private String nomeInscrito;

    @Column(name = "nu_cpf")
    private String cpf;

    @Column(name = "tp_sexo")
    private String sexo;

    @Column(name = "nu_rg")
    private String numeroRG;

    @Column(name = "co_uf_rg")
    private Integer codigoUFRG;

    @Column(name = "sg_uf_rg")
    private String siglaUFRG;

    @Column(name = "no_orgao_emissor")
    private String nomeOrgaoEmissor;

    @Column(name = "sg_orgao_emissor")
    private String siglaOrgaoEmissor;

    @Column(name = "dt_nascimento")
    private Date dataNascimento;

    @Column(name = "co_municipio_prova")
    private Long codigoMunicipioProva;

    @Column(name = "co_uf")
    private Long codigoUF;

    @Column(name = "sg_uf")
    private String siglaUF;

    @Column(name = "tp_lingua_estrangeira")
    private Integer tipoLinguaEstrangeira;

    @Column(name = "no_lingua_estrangeira")
    private String nomeLinguaEstrangeira;

    @Column(name = "id_kit_prova")
    private Integer kitProva;

    @Column(name = "no_social")
    private String nomeSocial;

    @Column(name = "tp_ambiente_sanitario")
    private Integer tipoAmbienteSanitario;


    public Long getCodigoPessoaFisica() {
        return codigoPessoaFisica;
    }

    public void setCodigoPessoaFisica(Long codigoPessoaFisica) {
        this.codigoPessoaFisica = codigoPessoaFisica;
    }

    public Integer getCodigoProjeto() {
        return codigoProjeto;
    }

    public void setCodigoProjeto(Integer codigoProjeto) {
        this.codigoProjeto = codigoProjeto;
    }

    public Long getCodigoInscricao() {
        return codigoInscricao;
    }

    public void setCodigoInscricao(Long codigoInscricao) {
        this.codigoInscricao = codigoInscricao;
    }

    public String getNomeInscrito() {
        return nomeInscrito;
    }

    public void setNomeInscrito(String nomeInscrito) {
        this.nomeInscrito = nomeInscrito;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public String getNumeroRG() {
        return numeroRG;
    }

    public void setNumeroRG(String numeroRG) {
        this.numeroRG = numeroRG;
    }

    public Integer getCodigoUFRG() {
        return codigoUFRG;
    }

    public void setCodigoUFRG(Integer codigoUFRG) {
        this.codigoUFRG = codigoUFRG;
    }

    public String getSiglaUFRG() {
        return siglaUFRG;
    }

    public void setSiglaUFRG(String siglaUFRG) {
        this.siglaUFRG = siglaUFRG;
    }

    public String getNomeOrgaoEmissor() {
        return nomeOrgaoEmissor;
    }

    public void setNomeOrgaoEmissor(String nomeOrgaoEmissor) {
        this.nomeOrgaoEmissor = nomeOrgaoEmissor;
    }

    public String getSiglaOrgaoEmissor() {
        return siglaOrgaoEmissor;
    }

    public void setSiglaOrgaoEmissor(String siglaOrgaoEmissor) {
        this.siglaOrgaoEmissor = siglaOrgaoEmissor;
    }

    public Date getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(Date dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public Long getCodigoMunicipioProva() {
        return codigoMunicipioProva;
    }

    public void setCodigoMunicipioProva(Long codigoMunicipioProva) {
        this.codigoMunicipioProva = codigoMunicipioProva;
    }

    public Long getCodigoUF() {
        return codigoUF;
    }

    public void setCodigoUF(Long codigoUF) {
        this.codigoUF = codigoUF;
    }

    public String getSiglaUF() {
        return siglaUF;
    }

    public void setSiglaUF(String siglaUF) {
        this.siglaUF = siglaUF;
    }

    public Integer getTipoLinguaEstrangeira() {
        return tipoLinguaEstrangeira;
    }

    public void setTipoLinguaEstrangeira(Integer tipoLinguaEstrangeira) {
        this.tipoLinguaEstrangeira = tipoLinguaEstrangeira;
    }

    public String getNomeLinguaEstrangeira() {
        return nomeLinguaEstrangeira;
    }

    public void setNomeLinguaEstrangeira(String nomeLinguaEstrangeira) {
        this.nomeLinguaEstrangeira = nomeLinguaEstrangeira;
    }

    public Integer getKitProva() {
        return kitProva;
    }

    public void setKitProva(Integer kitProva) {
        this.kitProva = kitProva;
    }

    public String getNomeSocial() {
        return nomeSocial;
    }

    public void setNomeSocial(String nomeSocial) {
        this.nomeSocial = nomeSocial;
    }

    public Integer getTipoAmbienteSanitario() {
        return tipoAmbienteSanitario;
    }

    public void setTipoAmbienteSanitario(Integer tipoAmbienteSanitario) {
        this.tipoAmbienteSanitario = tipoAmbienteSanitario;
    }

    public InscritoMunicipioProva(Long codigoPessoaFisica, Integer codigoProjeto, Long codigoInscricao,
                                  String nomeInscrito, String cpf, String sexo, String numeroRG,
                                  Integer codigoUFRG, String siglaUFRG, String nomeOrgaoEmissor,
                                  String siglaOrgaoEmissor, Date dataNascimento, Long codigoMunicipioProva,
                                  Long codigoUF, String siglaUF, Integer tipoLinguaEstrangeira,
                                  String nomeLinguaEstrangeira, Integer kitProva, String nomeSocial,
                                  Integer tipoAmbienteSanitario) {
        this.codigoPessoaFisica = codigoPessoaFisica;
        this.codigoProjeto = codigoProjeto;
        this.codigoInscricao = codigoInscricao;
        this.nomeInscrito = nomeInscrito;
        this.cpf = cpf;
        this.sexo = sexo;
        this.numeroRG = numeroRG;
        this.codigoUFRG = codigoUFRG;
        this.siglaUFRG = siglaUFRG;
        this.nomeOrgaoEmissor = nomeOrgaoEmissor;
        this.siglaOrgaoEmissor = siglaOrgaoEmissor;
        this.dataNascimento = dataNascimento;
        this.codigoMunicipioProva = codigoMunicipioProva;
        this.codigoUF = codigoUF;
        this.siglaUF = siglaUF;
        this.tipoLinguaEstrangeira = tipoLinguaEstrangeira;
        this.nomeLinguaEstrangeira = nomeLinguaEstrangeira;
        this.kitProva = kitProva;
        this.nomeSocial = nomeSocial;
        this.tipoAmbienteSanitario = tipoAmbienteSanitario;
    }

    public String[] toArray() {
        return new String[]{String.valueOf(this.getCodigoPessoaFisica()), String.valueOf(this.getCodigoProjeto()), String.valueOf(this.getCodigoInscricao()),
        this.getNomeInscrito(), this.getCpf(), this.getSexo(), this.getNumeroRG(), String.valueOf(this.getCodigoUFRG()),
        this.getSiglaUF(), this.getNomeOrgaoEmissor(), this.getSiglaOrgaoEmissor(), new SimpleDateFormat("dd/MM/yyyy").format(this.getDataNascimento()),
                String.valueOf(this.getCodigoMunicipioProva()), String.valueOf(this.getCodigoUF()), this.getSiglaUF(),
                String.valueOf(this.getTipoLinguaEstrangeira()), this.getNomeLinguaEstrangeira(), String.valueOf(this.getKitProva()),
                String.valueOf(this.getTipoAmbienteSanitario()), String.valueOf(this.getTipoAmbienteSanitario())};
    }

    public String valorString() {
        return String.valueOf(this.getCodigoPessoaFisica()).trim() + ", " + String.valueOf(this.getCodigoProjeto()).trim() + ", " + String.valueOf(this.getCodigoInscricao()).trim() + ", " +
                this.getNomeInscrito().trim() + ", " + this.getCpf().trim() + ", " + this.getSexo().trim() + ", " + this.getNumeroRG().trim() + ", " + String.valueOf(this.getCodigoUFRG()).trim() + ", " +
                this.getSiglaUF().trim() + ", " + this.getNomeOrgaoEmissor().trim() + ", " + this.getSiglaOrgaoEmissor().trim() + ", " + new SimpleDateFormat("dd/MM/yyyy").format(this.getDataNascimento()).trim() + ", " +
                String.valueOf(this.getCodigoMunicipioProva()).trim() + ", " + String.valueOf(this.getCodigoUF()).trim() + ", " + this.getSiglaUF().trim() + ", " +
                String.valueOf(this.getTipoLinguaEstrangeira()).trim() + ", " + this.getNomeLinguaEstrangeira().trim() + ", " + String.valueOf(this.getKitProva()).trim() + ", " +
                String.valueOf(this.getTipoAmbienteSanitario()).trim() + ", " + String.valueOf(this.getTipoAmbienteSanitario());
    }
}
