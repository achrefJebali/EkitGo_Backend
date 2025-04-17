package tn.esprit.examenspring.services;

import tn.esprit.examenspring.entities.Club;


import java.util.List;

public interface IClubService {
    Club addClub(Club club);
    List<Club> getClubs();
    Club modifyClub(Club club);
    void deleteClub(Integer id);

}
