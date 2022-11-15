package it.prova.gestionesatelliti.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

@Entity
@Table(name = "satellite")

public class Satellite {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	@NotBlank(message = "{satellite.denominazione.notblank}")
	@Column(name = "denominazione")
	private String denominazione;

	@NotBlank(message = "{satellite.codice.notblank}")
	@Column(name = "codice")
	private String codice;

	@Column(name = "datalancio")
	private Date dataDiLancio;

	@Column(name = "datarientro")
	private Date dataDiRientro;
	
	@Column(name = "stato")
	@Enumerated(EnumType.STRING)
	private StatoSatellite stato;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDenominazione() {
		return denominazione;
	}

	public void setDenominazione(String denominazione) {
		this.denominazione = denominazione;
	}

	public String getCodice() {
		return codice;
	}

	public void setCodice(String codice) {
		this.codice = codice;
	}

	public Date getDataDiLancio() {
		return dataDiLancio;
	}

	public void setDataDiLancio(Date dataDiLancio) {
		this.dataDiLancio = dataDiLancio;
	}

	public Date getDataDiRientro() {
		return dataDiRientro;
	}

	public void setDataDiRientro(Date dataDiRientro) {
		this.dataDiRientro = dataDiRientro;
	}

	public StatoSatellite getStato() {
		return stato;
	}

	public void setStato(StatoSatellite stato) {
		this.stato = stato;
	}
	

}
