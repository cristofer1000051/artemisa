package com.andromeda.artemisa.entities.dtos;

public class AdminDto {

    private Long id;
    private String nome;
    private String codFiscale;
    private String cellulare;
    private String email;
    private String cognome;
    private String rol;
    private String password;

    public AdminDto(Long id, String nome, String codFiscale, String cellulare, String email, String cognome,
            String rol, String password) {
        this.id = id;
        this.nome = nome;
        this.codFiscale = codFiscale;
        this.cellulare = cellulare;
        this.email = email;
        this.cognome = cognome;
        this.rol = rol;
        this.password = password;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCodFiscale() {
        return codFiscale;
    }

    public void setCodFiscale(String codFiscale) {
        this.codFiscale = codFiscale;
    }

    public String getCellulare() {
        return cellulare;
    }

    public void setCellulare(String cellulare) {
        this.cellulare = cellulare;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCognome() {
        return cognome;
    }

    public void setCognome(String cognome) {
        this.cognome = cognome;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
