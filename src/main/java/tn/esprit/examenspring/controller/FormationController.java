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
    @DeleteMapping("/remove-bloc/{formation-id}")
    public void removeFormation(@PathVariable("formation-id") Integer fid) {
        formationService.deleteFormation(fid);
    }
    @PutMapping("/modify-formation")
    public Formation modifyFormation(@RequestBody Formation f) {
        return formationService.modifyFormation(f);
    }


}
