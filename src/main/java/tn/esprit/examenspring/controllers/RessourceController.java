package tn.esprit.examenspring.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tn.esprit.examenspring.entities.Ressource;
import tn.esprit.examenspring.services.IRessourceService;

import java.util.List;

@RestController
@RequestMapping("/Ressource")
public class RessourceController {
    @Autowired
    private IRessourceService ressourceService;

    @GetMapping("/retrieve-all-ressource")
    public List<Ressource> getRessource() {
        return ressourceService.getRessources();
    }

    @PostMapping("/add-ressource")
    public Ressource addRessource(@RequestBody Ressource r) {
        return ressourceService.addRessource(r);
    }
    @DeleteMapping("/remove-ressource/{ressource-id}")
    public void removeRessource(@PathVariable("ressource-id") Integer fid) {
        ressourceService.deleteRessource(fid);
    }
    @PutMapping("/modify-ressource")
    public Ressource modifyRessource(@RequestBody Ressource r) {
        return ressourceService.modifyRessource(r);
    }


}
