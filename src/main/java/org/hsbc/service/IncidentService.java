package org.hsbc.service;

import org.hsbc.entity.Incident;

import java.util.List;

public interface IncidentService {
    int createIncident(Incident incident);
    int updateIncident(Long id, Incident incident);
    void deleteIncident(Long id);
    Incident getIncident(Long id);
    List<Incident> getAllIncidents();
}