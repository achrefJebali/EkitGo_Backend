package tn.esprit.examenspring.controller;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tn.esprit.examenspring.entities.Formation;
import tn.esprit.examenspring.services.IFormationService;

import java.util.List;

@RestController
@RequestMapping("/Formation")
public class FormationController {
    @Autowired
    private IFormationService formationService;

    @GetMapping("/retrieve-all-formation")
    public List<Formation> getFormation() {
        return formationService.getFormations();
    }

    @PostMapping("/add-formation")
    public Formation addFormation(@RequestBody Formation f) {
        return formationService.addFormation(f);
    }
    @DeleteMapping("/delete-formation/{formation-id}")
    public void removeFormation(@PathVariable("formation-id") Integer fid) {
        formationService.deleteFormation(fid);
    }
    @PutMapping("/update-formation/{id}")
    public Formation modifyFormation(@PathVariable int id, @RequestBody Formation formation) {
        return formationService.modifyFormation(id,formation);
    }
    @GetMapping("/retrieve-formation/{id}")
    public Formation getFormationById(@PathVariable("id") Integer Formationid) {
        return formationService.retrieveFormation(Formationid);
    }
    @PostMapping("/{idFormation}/affecter-category")
    public Formation affecterCategoryAFormation(
            @PathVariable Integer idFormation, // ID de la Formation (passé dans l'URL)
            @RequestParam String categoryName) { // Nom de la Category (passé en paramètre de requête)
        return formationService.affecterCategoryAFormationByName(idFormation, categoryName);
    }


}
