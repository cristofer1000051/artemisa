package com.andromeda.artemisa.entities.dtos;

public class ClienteDto {

    private Long id;
    private String name;
    private String codFiscale;
    private String cellulare;
    private String email;
    private String cognome;
    private String rol;
    private String password;
    private String indirizzo;
    private String cap;
    private String citta;
    private String provincia;

    public ClienteDto(String cap, String cellulare, String citta, String codFiscale, String cognome, String email, Long id, String indirizzo, String name, String password, String provincia, String rol) {
        this.cap = cap;
        this.cellulare = cellulare;
        this.citta = citta;
        this.codFiscale = codFiscale;
        this.cognome = cognome;
        this.email = email;
        this.id = id;
        this.indirizzo = indirizzo;
        this.name = name;
        this.password = password;
        this.provincia = provincia;
        this.rol = rol;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getIndirizzo() {
        return indirizzo;
    }

    public void setIndirizzo(String indirizzo) {
        this.indirizzo = indirizzo;
    }

    public String getCap() {
        return cap;
    }

    public void setCap(String cap) {
        this.cap = cap;
    }

    public String getCitta() {
        return citta;
    }

    public void setCitta(String citta) {
        this.citta = citta;
    }

    public String getProvincia() {
        return provincia;
    }

    public void setProvincia(String provincia) {
        this.provincia = provincia;
    }

}
