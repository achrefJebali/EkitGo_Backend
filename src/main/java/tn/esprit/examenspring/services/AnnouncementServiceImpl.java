package tn.esprit.examenspring.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.examenspring.Repository.AnnouncementRepository;

import tn.esprit.examenspring.entities.Announcement;



import java.util.List;

@Service
@Slf4j
public class AnnouncementServiceImpl implements AnnouncementService {
    @Autowired
    private AnnouncementRepository announcementRepository;

    @Override
    public Announcement addAnnouncement(Announcement announcement) {
        return announcementRepository.save(announcement);
    }

    @Override
    public List<Announcement> getAnnouncements() {
        return announcementRepository.findAll();
    }

    @Override
    public Announcement modifyAnnouncement(Announcement announcement) {
        announcementRepository.save(announcement);
        return announcement;
    }

    @Override
    public void deleteAnnouncement(Integer id) {
        announcementRepository.deleteById(id);

    }
}
