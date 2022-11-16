package it.prova.gestionesatelliti.web.controller;

import java.util.Date;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import it.prova.gestionesatelliti.model.Satellite;
import it.prova.gestionesatelliti.model.StatoSatellite;
import it.prova.gestionesatelliti.service.SatelliteService;

@Controller
@RequestMapping(value = "/satellite")
public class SatelliteController {

	@Autowired
	private SatelliteService satelliteService;

	@GetMapping
	public ModelAndView listAll() {
		ModelAndView mv = new ModelAndView();
		List<Satellite> results = satelliteService.listAllElements();
		mv.addObject("satellite_list_attribute", results);
		mv.setViewName("satellite/list");
		return mv;
	}

	@GetMapping("/search")
	public String search() {
		return "satellite/search";
	}

	@PostMapping("/list")
	public String listByExample(Satellite example, ModelMap model) {
		List<Satellite> results = satelliteService.findByExample(example);
		model.addAttribute("satellite_list_attribute", results);
		return "satellite/list";
	}

	@GetMapping("/insert")
	public String create(Model model) {
		model.addAttribute("insert_satellite_attr", new Satellite());
		return "satellite/insert";
	}

	@PostMapping("/save")
	public String save(@Valid @ModelAttribute("insert_satellite_attr") Satellite satellite, BindingResult result,
			RedirectAttributes redirectAttrs, Model model) {

		/*
		 * controllo se sono state inserite entrambe le date e sono precedenti ad adesso
		 * ma lo stato è diverso da disabilitato
		 */
		if (satellite.getDataDiLancio() != null && satellite.getDataDiRientro() != null
				&& satellite.getDataDiLancio().before(new Date()) && satellite.getDataDiRientro().before(new Date())
				&& (satellite.getStato() == null || satellite.getStato() != StatoSatellite.DISATTIVATO)) {
			model.addAttribute("errorMessage",
					"ATTENZIONE, lo stato deve essere \"DISABILITATO\" se si inserisce sia una data di lancio che una di rientro");
//			result.rejectValue("DataDiLancio", "satellite.dataDiLancio.mustBe.piuPiccolo");
			return "satellite/insert";
		}

		/* controllo se non ho inserito date ma ho inserito uno stato */
		if (satellite.getDataDiLancio() == null && satellite.getDataDiRientro() == null
				&& satellite.getStato() != null) {
			model.addAttribute("errorMessage",
					"ATTENZIONE, non si puo inserire uno stato se non si inserisce prima almeno una data di lancio");
			return "satellite/insert";
		}

		if (satellite.getDataDiLancio() != null && satellite.getDataDiRientro() != null
				&& satellite.getDataDiLancio().after(satellite.getDataDiRientro())) {
			model.addAttribute("errorMessage",
					"ATTENZIONE, non si puo inserire una data di rientro precedente a quella di lancio");
			return "satellite/insert";
		}

		/*
		 * controllo se la data di lancio inserita è dopo adesso ma lo stato è diverso
		 * da null
		 */
		if (satellite.getDataDiLancio() != null && satellite.getDataDiLancio().after(new Date())
				&& satellite.getStato() != null) {
			model.addAttribute("errorMessage",
					"ATTENZIONE, non si puo inserire uno stato se la data di lancio deve avvenire ancora");
			return "satellite/insert";
		}

		/* controllo se la data di rientro e stata inserita ma quella di lancio no */
		if (satellite.getDataDiRientro() != null && satellite.getDataDiLancio() == null) {
			model.addAttribute("errorMessage",
					"ATTENZIONE, non si puo inserire una data di rientro senza inserire quella si lancio prima");
			return "satellite/insert";
		}

		/*
		 * controllo se ho inserito una data di lancio che è precedente ad adesso ma non
		 * ho inserito uno stato
		 */
		if (satellite.getDataDiLancio() != null && satellite.getDataDiLancio().before(new Date())
				&& satellite.getStato() == null) {
			model.addAttribute("errorMessage",
					"ATTENZIONE, devi inserire uno stato se il satellite e gia stato lanciato");
			return "satellite/insert";
		}

		if (result.hasErrors())
			return "satellite/insert";

		satelliteService.inserisciNuovo(satellite);

		redirectAttrs.addFlashAttribute("successMessage", "Operazione eseguita correttamente");
		return "redirect:/satellite";
	}

	@GetMapping("/delete/{idSatellite}")
	public String delete(@PathVariable(required = true) Long idSatellite, Model model, Satellite satellite) {

		model.addAttribute("delete_satellite_attr", satelliteService.caricaSingoloElemento(idSatellite));
		return "satellite/delete";
	}

	@PostMapping("/executeDelete")
	public String confirm(@RequestParam Long idSatellite, Satellite satellite, Model model,
			RedirectAttributes redirectAttrs) {
		
		Satellite satellite2= satelliteService.caricaSingoloElemento(idSatellite);
		
		if (satellite.getStato() == StatoSatellite.FISSO && satellite.getStato() == StatoSatellite.IN_MOVIMENTO) {
			model.addAttribute("errorMessage",
					"ATTENZIONE, devi inserire uno stato se il satellite e gia stato lanciato");
			return "satellite/delete";
		}

		satelliteService.rimuovi(idSatellite);
		redirectAttrs.addFlashAttribute("successMessage", "Operazione eseguita correttamente");

		return "redirect:/satellite";
	}

	@GetMapping("/show/{idSatellite}")
	public String show(@PathVariable(required = true) Long idSatellite, Model model) {
		model.addAttribute("show_satellite_attr", satelliteService.caricaSingoloElemento(idSatellite));
		return "satellite/show";
	}

	@GetMapping("/update/{idSatellite}")
	public String update(@PathVariable(required = true) Long idSatellite, Model model) {
		model.addAttribute("update_satellite_attr", satelliteService.caricaSingoloElemento(idSatellite));
		return "satellite/update";
	}

	@PostMapping("/executeUpdate")
	public String update(@Valid @ModelAttribute("update_satellite_attr") Satellite satellite, BindingResult result,
			RedirectAttributes redirectAttrs, Model model) {

		/* controllo se la data di lancio e dopo quella di ritorno */
		if (satellite.getDataDiLancio() != null && satellite.getDataDiRientro() != null
				&& satellite.getDataDiLancio().after(satellite.getDataDiRientro())) {
			model.addAttribute("errorMessage",
					"ATTENZIONE, NON SI PUO INSERIRE UNA DATA DI RIENTRO PRECEDENTE A QUELLA DI LANCIO");
			return "satellite/update";
		}

		/* controllo se la data di rientro e stata inserita ma quella di lancio no */
		if (satellite.getDataDiRientro() != null && satellite.getDataDiLancio() == null) {
			model.addAttribute("errorMessage",
					"ATTENZIONE, NON SI PUO INSERIRE UNA DATA DI RIENTRO SENZA PRIMA INSERIRE QUELLA DI LANCIO");
			return "satellite/update";
		}

		/*
		 * controllo se sono state inserite entrambe le date e sono precedenti ad adesso
		 * ma lo stato è diverso da disabilitato
		 */
		if (satellite.getDataDiLancio() != null && satellite.getDataDiRientro() != null
				&& satellite.getDataDiLancio().before(new Date()) && satellite.getDataDiRientro().before(new Date())
				&& (satellite.getStato() == null || satellite.getStato() != StatoSatellite.DISATTIVATO)) {
			model.addAttribute("errorMessage",
					"ATTENZIONE, LO STATO DEVE ESSERE \"DISABILITATO\" SE SI INSERISCE SIA LA DATA DI LANCIO CHE DI RIENTRO");
			return "satellite/update";
		}

		/* controllo se non ho inserito date ma ho inserito uno stato */
		if (satellite.getDataDiLancio() == null && satellite.getDataDiRientro() == null
				&& satellite.getStato() != null) {
			model.addAttribute("errorMessage",
					"ATTENZIONE, NON SI PUO INSERIRE UNO STATO SE NON SI INSERISCE PRIMA ALMENO LA DATA DI LANCIO");
			return "satellite/update";
		}

		/*
		 * controllo se ho inserito una data di lancio che è precedente ad adesso ma non
		 * ho inserito uno stato
		 */
		if (satellite.getDataDiLancio() != null && satellite.getDataDiLancio().before(new Date())
				&& satellite.getStato() == null) {
			model.addAttribute("errorMessage",
					"ATTENZIONE, BISOGNA INSERIRE UNO STATO SE IL SATELLITE E' GIA STATO LANCIATO");
			return "satellite/update";
		}

		/*
		 * controllo se la data di lancio inserita è dopo adesso ma lo stato è diverso
		 * da null
		 */
		if (satellite.getDataDiLancio() != null && satellite.getDataDiLancio().after(new Date())
				&& satellite.getStato() != null) {
			model.addAttribute("errorMessage",
					"ATTENZIONE, NON SI PUO INSERIRE UNO STATO SE LA DATA DI LANCIO DEVE ANCORA VENIRE");
			return "satellite/update";
		}

		if (result.hasErrors())
			return "satellite/update";

		satelliteService.aggiorna(satellite);

		redirectAttrs.addFlashAttribute("successMessage", "Operazione eseguita correttamente");
		return "redirect:/satellite";
	}

	@GetMapping("/launch/{idSatellite}")
	public String launch(@PathVariable(required = true) Long idSatellite, Model model) {
		Satellite satellite = satelliteService.caricaSingoloElemento(idSatellite);
		satellite.setDataDiLancio(new Date());
		satellite.setStato(StatoSatellite.IN_MOVIMENTO);
		satelliteService.aggiorna(satellite);
		return "redirect:/satellite";
	}

	@GetMapping("/rientro/{idSatellite}")
	public String reentry(@PathVariable(required = true) Long idSatellite, Model model) {
		Satellite satellite = satelliteService.caricaSingoloElemento(idSatellite);
		if (satellite.getDataDiLancio() != null) {
			satellite.setDataDiRientro(new Date());
			satellite.setStato(StatoSatellite.DISATTIVATO);
			satelliteService.aggiorna(satellite);
		}
		return "redirect:/satellite";
	}
	@GetMapping("/launchMoreThanTwoYears")
	public ModelAndView launchMoreThanTwoYears() {
		ModelAndView mv = new ModelAndView();
		List<Satellite> results = satelliteService.listAllLaunchMoreThanTwoYears();
		mv.addObject("satellite_list_attribute", results);
		mv.setViewName("satellite/list");
		return mv;
	}

	@GetMapping("/deactivatedButNotReEntered")
	public ModelAndView deactivatedButNotReEntered() {
		ModelAndView mv = new ModelAndView();
		List<Satellite> results = satelliteService.listAllDeactivatedButNotReEntered();
		mv.addObject("satellite_list_attribute", results);
		mv.setViewName("satellite/list");
		return mv;
	}

	@GetMapping("/inOrbitButFixed")
	public ModelAndView inOrbitButFixed() {
		ModelAndView mv = new ModelAndView();
		List<Satellite> results = satelliteService.listAllinOrbitButFixed();
		mv.addObject("satellite_list_attribute", results);
		mv.setViewName("satellite/list");
		return mv;
	}

}
