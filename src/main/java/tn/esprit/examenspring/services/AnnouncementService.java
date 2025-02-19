package tn.esprit.examenspring.services;

import tn.esprit.examenspring.entities.Announcement;

import java.util.List;

public interface AnnouncementService {
    Announcement addAnnouncement(Announcement announcement);
    List<Announcement> getAnnouncements();
    Announcement modifyAnnouncement(Announcement announcement);
    void deleteAnnouncement(Integer id);
}
