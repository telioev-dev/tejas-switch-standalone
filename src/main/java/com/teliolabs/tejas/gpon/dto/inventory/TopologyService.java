package com.teliolabs.tejas.gpon.dto.inventory;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.teliolabs.tejas.gpon.domain.Connection;
import com.teliolabs.tejas.gpon.repository.TopologyRepo;

@Service
public class TopologyService {

    private final TopologyRepo topologyRepo;

    @Autowired
    public TopologyService(TopologyRepo topologyRepo) {
        this.topologyRepo = topologyRepo;
    }

    @Transactional
    public List<Connection> saveTopologyData(List<String[]> topologyData) {
        if (topologyData == null) {
            throw new IllegalArgumentException("Topology data cannot be null");
        }

        List<Connection> connections = new ArrayList<>();
        for (String[] row : topologyData) {
            if (row == null || row.length < 12) {
                // log.warn("Skipping invalid row: {}", Arrays.toString(row));
                continue;
            }

            Connection connection = new Connection();
            connection.setUserLabel(row[0]);
            connection.setRate(row[1]);
            connection.setTechnology(row[2]);
            connection.setSpecification(row[3]);
            connection.setVendor(row[4]);
            connection.setZEndVendor(row[5]);
            connection.setAEndVendor(row[6]);
            connection.setAEndNode(row[7]);
            connection.setZEndNode(row[8]);
            connection.setAEndPort(row[9]);
            connection.setZEndPort(row[10]);
            connection.setCircle(row[11]);

            connections.add(connection);
        }
        return topologyRepo.saveAll(connections);
    }
}
