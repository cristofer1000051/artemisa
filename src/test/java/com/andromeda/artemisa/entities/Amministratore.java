package com.andromeda.artemisa.entities;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;


@Entity
@DiscriminatorValue("amministratori")
public class Amministratore extends Utente{
}   
