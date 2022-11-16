package it.prova.gestionesatelliti.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import it.prova.gestionesatelliti.model.Satellite;
import it.prova.gestionesatelliti.model.StatoSatellite;

public interface SatelliteRepository  extends CrudRepository<Satellite, Long>,JpaSpecificationExecutor<Satellite>{

	List<Satellite> findByStatoLikeAndDataDiLancioBefore(StatoSatellite stato, Date tueYearsAgoDate);

	List<Satellite> findByStatoLikeAndDataDiRientroIsNull(StatoSatellite stato);
	
	List<Satellite> findAllByStatoNotLikeAndDataDiRientroIsNull(StatoSatellite stato);
}
