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
    @DeleteMapping("/remove-formation/{formation-id}")
    public void removeFormation(@PathVariable("formation-id") Integer fid) {
        formationService.deleteFormation(fid);
    }
    @PutMapping("/modify-formation")
    public Formation modifyFormation(@RequestBody Formation f) {
        return formationService.modifyFormation(f);
    }

    @PostMapping("/{idFormation}/affecter-category")
    public Formation affecterCategoryAFormation(
            @PathVariable Integer idFormation, // ID de la Formation (passé dans l'URL)
            @RequestParam String categoryName) { // Nom de la Category (passé en paramètre de requête)
        return formationService.affecterCategoryAFormationByName(idFormation, categoryName);
    }


}
