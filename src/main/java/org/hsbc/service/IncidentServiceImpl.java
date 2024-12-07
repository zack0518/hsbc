package org.hsbc.service;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.hsbc.cache.CacheTemplate;
import org.hsbc.entity.Incident;
import org.hsbc.mapper.IncidentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class IncidentServiceImpl implements IncidentService {

    @Autowired
    private IncidentMapper incidentMapper;
    @Autowired
    private CacheTemplate cacheTemplate;

    private static final String CACHE_PREFIX = "incident:";

    @Override
    public int createIncident(Incident incident) {
        int result = incidentMapper.insert(incident);
        if (result > 0) {
            String cacheKey = CACHE_PREFIX + incident.getId();
            cacheTemplate.put(cacheKey, incident, 1, TimeUnit.HOURS);
        }
        return result;
    }

    @Override
    public int updateIncident(Long id, Incident incident) {
        if (incidentMapper.selectById(id) != null) {
            incident.setId(id);
            int result = incidentMapper.update(incident);
            if (result > 0) {
                String cacheKey = CACHE_PREFIX + id;
                cacheTemplate.put(cacheKey, incident, 1, TimeUnit.HOURS);
            }
            return result;
        }
        return -1;
    }

    @Override
    public void deleteIncident(Long id) {
        incidentMapper.delete(id);
        String cacheKey = CACHE_PREFIX + id;
        cacheTemplate.remove(cacheKey);
    }

    @Override
    public Incident getIncident(Long id) {
        String cacheKey = CACHE_PREFIX + id;
        Incident cachedIncident = (Incident) cacheTemplate.get(cacheKey);
        if (cachedIncident != null) {
            return cachedIncident;
        }
        Incident incident = incidentMapper.selectById(id);
        if (incident != null) {
            cacheTemplate.put(cacheKey, incident, 1, TimeUnit.HOURS);
        }
        return incident;
    }

    @Override
    public List<Incident> getAllIncidents() {
        return incidentMapper.selectAll();
    }
}