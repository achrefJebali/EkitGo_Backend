package tn.esprit.examenspring.services;

import tn.esprit.examenspring.entities.Ressource;

import java.util.List;

public interface IRessourceService {
    Ressource addRessource(Ressource ressource);
    List<Ressource> getRessources();
    Ressource modifyRessource(Integer id, Ressource ressource);
    void deleteRessource(Integer id);
    public List<Ressource> getRessourcesByFormation(Integer formationId) ;


    }
