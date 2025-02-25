package tn.esprit.examenspring.services;

import tn.esprit.examenspring.entities.Ressource;

import java.util.List;

public interface IRessourceService {
    Ressource addRessource(Ressource ressource);
    List<Ressource> getRessources();
    Ressource modifyRessource(Ressource ressource);
    void deleteRessource(Integer id);
}
