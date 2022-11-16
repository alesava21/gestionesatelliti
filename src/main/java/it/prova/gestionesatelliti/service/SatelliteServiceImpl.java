package it.prova.gestionesatelliti.service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.criteria.Predicate;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import it.prova.gestionesatelliti.model.Satellite;
import it.prova.gestionesatelliti.model.StatoSatellite;
import it.prova.gestionesatelliti.repository.SatelliteRepository;

@Service
public class SatelliteServiceImpl implements SatelliteService {

	@Autowired
	private SatelliteRepository repository;

	@Override
	@Transactional(readOnly = true)
	public List<Satellite> listAllElements() {
		return (List<Satellite>) repository.findAll();
	}

	@Override
	@Transactional(readOnly = true)
	public Satellite caricaSingoloElemento(Long id) {
		return repository.findById(id).orElse(null);
	}

	@Override
	@Transactional
	public void aggiorna(Satellite satelliteInstance) {
		repository.save(satelliteInstance);

	}

	@Override
	@Transactional
	public void inserisciNuovo(Satellite satelliteInstance) {
		repository.save(satelliteInstance);

	}

	@Override
	@Transactional
	public void rimuovi(Long idSatellite) {
		repository.deleteById(idSatellite);

	}

	@Override
	@Transactional(readOnly = true)
	public List<Satellite> findByExample(Satellite example) {
		Specification<Satellite> specificationCriteria = (root, query, cb) -> {

			List<Predicate> predicates = new ArrayList<Predicate>();

			if (StringUtils.isNotEmpty(example.getDenominazione()))
				predicates.add(cb.like(cb.upper(root.get("denominazione")),
						"%" + example.getDenominazione().toUpperCase() + "%"));

			if (StringUtils.isNotEmpty(example.getCodice()))
				predicates.add(cb.like(cb.upper(root.get("codice")), "%" + example.getCodice().toUpperCase() + "%"));

			if (example.getDataDiLancio() != null)
				predicates.add(cb.greaterThanOrEqualTo(root.get("dataDiNascita"), example.getDataDiLancio()));
			if (example.getDataDiRientro() != null)
				predicates.add(cb.greaterThanOrEqualTo(root.get("dataDiNascita"), example.getDataDiRientro()));

			if (example.getStato() != null)
				predicates.add(cb.equal(root.get("stato"), example.getStato()));

			return cb.and(predicates.toArray(new Predicate[predicates.size()]));
		};

		return repository.findAll(specificationCriteria);
	}

	@Override
	@Transactional(readOnly = true)
	public List<Satellite> listAllLaunchMoreThanTwoYears() {
		// TODO Auto-generated method stub
		StatoSatellite stato = StatoSatellite.DISATTIVATO;
		LocalDate tueYearsAgo = LocalDate.now().minusYears(2);
		Date tuweYearsAgoDate = Date.from(tueYearsAgo.atStartOfDay(ZoneId.systemDefault()).toInstant());
		return repository.findByStatoLikeAndDataDiLancioBefore(stato, tuweYearsAgoDate);

	}

	@Override
	@Transactional(readOnly = true)
	public List<Satellite> listAllDeactivatedButNotReEntered() {
		// TODO Auto-generated method stub
		StatoSatellite stato = StatoSatellite.DISATTIVATO;
		return repository.findByStatoLikeAndDataDiRientroIsNull(stato);
	}

	@Override
	@Transactional(readOnly = true)
	public List<Satellite> listAllinOrbitButFixed() {
		// TODO Auto-generated method stub
		StatoSatellite stato = StatoSatellite.FISSO;
		LocalDate tenYearsAgo1 = LocalDate.now().minusYears(10);
		Date tenYearsAgoDate1 = Date.from(tenYearsAgo1.atStartOfDay(ZoneId.systemDefault()).toInstant());
		return repository.findByStatoLikeAndDataDiLancioBefore(stato, tenYearsAgoDate1);
	}

}
