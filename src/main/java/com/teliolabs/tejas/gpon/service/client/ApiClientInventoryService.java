package com.teliolabs.tejas.gpon.service.client;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.teliolabs.tejas.gpon.config.ApplicationConfig;
import com.teliolabs.tejas.gpon.config.Endpoint;
import com.teliolabs.tejas.gpon.config.NetworkManagerConfig;
import com.teliolabs.tejas.gpon.context.ApplicationContext;
import com.teliolabs.tejas.gpon.dto.inventory.TopologyService;
import com.teliolabs.tejas.gpon.dto.inventory.TunnelService;
import com.teliolabs.tejas.gpon.util.AdditionalInformation;
import com.teliolabs.tejas.gpon.util.EndpointConstants;
import com.teliolabs.tejas.gpon.util.NodeEdgePoint;
import com.teliolabs.tejas.gpon.util.Ont;
import com.teliolabs.tejas.gpon.util.Root;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ApiClientInventoryService extends BaseApiClientService {

    private final ObjectMapper objectMapper;
    private final ApiClientAuthService apiClientAuthService;
    private final TopologyService topologyService;
    private final TunnelService tunnelService;

    @Autowired
    public ApiClientInventoryService(ApplicationContext applicationContext, WebClient.Builder webClientBuilder, ApplicationConfig applicationConfig, ObjectMapper objectMapper,TopologyService topologyService, TunnelService tunnelService,ApiClientAuthService apiClientAuthService) {
        super(applicationContext, webClientBuilder, applicationConfig);
        this.objectMapper = objectMapper;
        this.topologyService = topologyService;
        this.tunnelService = tunnelService;
        this.apiClientAuthService=apiClientAuthService;
    }
    
    
    public TopologyNodeDetail getPdDetails(String pdUuid) {
        NetworkManagerConfig networkManager = applicationConfig.getNetworkManager();
        Endpoint endpoint = networkManager.getEndpoints().stream()
                .filter(e -> e.getName().equals(EndpointConstants.GET_NODE_DETAILS))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Endpoint not found"));
        
        try {
            TopologyNodeDetail physicalNodeList = webClientBuilder
                    .baseUrl(getEndpointHost(endpoint))
                    .build()
                    .method(resolveMethod(endpoint))
                    .uri(uriBuilder -> uriBuilder.path(getEndpointPath(endpoint))
                            .build(pdUuid))
                    .headers(headers -> headers.setBearerAuth(applicationContext.getAuthContext().getAccessToken()))
                    .retrieve()
                    .bodyToMono(new ParameterizedTypeReference<TopologyNodeDetail>() {})
                    .block(); // Blocking call, consider using async if possible
                
            return physicalNodeList;
        } catch (WebClientResponseException.Unauthorized e) {
            // Handle token expiry (401 Unauthorized)
        	apiClientAuthService.authenticate();
            TopologyNodeDetail physicalNodeList = webClientBuilder
            .baseUrl(getEndpointHost(endpoint))
            .build()
            .method(resolveMethod(endpoint))
            .uri(uriBuilder -> uriBuilder.path(getEndpointPath(endpoint)).build(pdUuid))
            .headers(headers -> headers.setBearerAuth(applicationContext.getAuthContext().getAccessToken()))
            .retrieve()
            .bodyToMono(new ParameterizedTypeReference<TopologyNodeDetail>() {})
            .block();
            return physicalNodeList;
        }

    }
    

    public List<Root> getNodeList() {
        // Get Network Manager Config
        NetworkManagerConfig networkManager = applicationConfig.getNetworkManager();
        
        // Fetch the correct endpoint for getting node list
        Endpoint endpoint = networkManager.getEndpoints().stream()
                .filter(e -> e.getName().equals(EndpointConstants.GET_NODE_LIST))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Endpoint not found"));

        // Build the WebClient and make the request
        List<Root> nodeList = webClientBuilder
                .baseUrl(getEndpointHost(endpoint))
                .build()
                .method(resolveMethod(endpoint))
                .uri(uriBuilder -> uriBuilder.path(getEndpointPath(endpoint))
                        .build("TTLEMS-GPON-1"))
                .headers(headers -> headers.setBearerAuth(applicationContext.getAuthContext().getAccessToken()))
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<Root>>() {})
                .block(); // Blocking call, consider using async if possible
//        // Log the response
//        log.info("nodes: {}", nodeList);
   
        return nodeList;
    }

    public List<Root> getCircuitList() {
        List<Root> nodeList = getNodeList();
        List<Root> circuitDataList = new ArrayList<>();
        
        for (Root node : nodeList) {
            String pdUuid = node.getUuid();
            NetworkManagerConfig networkManager = applicationConfig.getNetworkManager();
            Optional<Endpoint> optionalEndpoint = networkManager.getEndpoints().stream()
                .filter(e -> e.getName().equals(EndpointConstants.GET_CIRCUIT_DATA))
                .findFirst();
            
            if (optionalEndpoint.isPresent()) {
                Endpoint endpoint = optionalEndpoint.get();
                
                List<Root> circuitData = webClientBuilder
                    .baseUrl(getEndpointHost(endpoint))
                    .build()
                    .method(resolveMethod(endpoint))
                    .uri(uriBuilder -> uriBuilder
                        .path(getEndpointPath(endpoint))
                        .queryParam("category", "ont")
                        .queryParam("size", "500")
                        .build(pdUuid))
                    .headers(headers -> headers.setBearerAuth(applicationContext.getAuthContext().getAccessToken()))
                    .retrieve()
                    .bodyToMono(new ParameterizedTypeReference<List<Root>>() {})
                    .block();
                
                if (circuitData != null) {
                    circuitDataList.addAll(circuitData);
                }
            } else {
                // Handle the case where the endpoint is not found
                System.err.println("Endpoint not found for GET_CIRCUIT_DATA");
            }
        }   
        
        return circuitDataList;
    }


    public void getCompleteCircuitData() {
        List<Root> circuitDataList = getCircuitList();
        List<String[]> topologyData = new ArrayList<>();
        List<String[]> tunnelData = new ArrayList<>();
        String excelFilePath = "GPON ONT Data.xlsx"; // Update with the actual path
        Map<String, Map<String, String>> dataMap = ExcelReader.readExcelFile(excelFilePath);

        for (Root list : circuitDataList) {
            String trailId = "null", userLabel = "null", circuitId = "null", rate = "null";
            String aEndDropPort = "null", zEndDropPort = "null", topology = "null";
            String aEndDropNode = "null", zEndDropNode = "null", channel = "null";
            String aEndNode = "null", zEndNode = "null", aEndPort = "null";
            String zEndPort = "null", topologyType = "null", circle = "null";
            String uuid = "null";

            // Handle RemoteUnit and Ont
            if (list.getRemoteUnit() != null && list.getRemoteUnit().getOnt() != null) {
                Ont ont = list.getRemoteUnit().getOnt();
                String[] split = ont.getUuid().split("\\|");
//                String pdUuid = split.length > 1 ? split[1] : "null";
//                uuid = pdUuid;
            	String pdUuid = split[0]+"|"+split[1];
//            	System.out.println("pdUuid"+pdUuid);
            	if(!pdUuid.contains("10.76.200.137")&&!pdUuid.contains("10.76.198.217")&&!pdUuid.contains("10.76.197.81")&&!pdUuid.contains("10.76.199.25")
            			&&!pdUuid.contains("10.76.199.225")&&!pdUuid.contains("10.76.198.57")&&!pdUuid.contains("10.76.199.65")&&!pdUuid.contains("10.76.199.249")
            			&&!pdUuid.contains("10.76.200.1")&&!pdUuid.contains("10.76.200.1")&&!pdUuid.contains("10.76.198.105") &&!pdUuid.contains("10.76.197.113") 
//            			&&!pdUuid.contains("10.76.200.241")&&!pdUuid.contains("10.76.198.33")
            			) {
                TopologyNodeDetail pdDetailList = getPdDetails(pdUuid);
                ArrayList<AdditionalInformation> pdAddinfo = pdDetailList.getAdditionalIinformation();
                for (AdditionalInformation addInfo : pdAddinfo) {
                    if (addInfo.getValueName().equalsIgnoreCase("nativeEMSName")) {
//                    	System.out.println(addInfo.getValue());
                    	aEndDropNode = addInfo.getValue();
                    	zEndDropNode = addInfo.getValue();
                    	aEndNode = aEndDropNode;
                        zEndNode = aEndDropNode;
                        circle = addInfo.getValue().split("_")[0];
                    } 
                }
            }

//                // Lookup data from the map
//                Map<String, String> innerMap = dataMap.get(pdUuid);
//                if (innerMap != null) {
//                    String oltName = innerMap.get("OLT Name");
//                    if (oltName != null) {
//                        aEndNode = oltName;
//                        aEndDropNode = oltName;
//                        zEndNode = oltName;
//                        zEndDropNode = oltName;
//                        circle = oltName.split("_")[0];
//                    } else {
//                        log.warn("OLT Name not found for OLT IP: {}", pdUuid);
//                    }
//                }

                // Process Additional Information
                ArrayList<AdditionalInformation> additionalInformation = ont.getAdditionalInformation();
                for (AdditionalInformation addInfo : additionalInformation) {
                    switch (addInfo.getValueName().toLowerCase()) {
                        case "customerid":
                            circuitId = addInfo.getValue();
                            break;
                        case "id":
                            trailId = addInfo.getValue();
                            break;
                        // Add other cases if needed
                    }
                }
            }

            // Process NodeEdgePoint entries
            if (list.getRemoteUnit() != null && list.getRemoteUnit().getNodeEdgePoint() != null) {
                ArrayList<NodeEdgePoint> nodeEdgePointList = list.getRemoteUnit().getNodeEdgePoint();
                for (NodeEdgePoint endPortList : nodeEdgePointList) {
                    ArrayList<AdditionalInformation> additionalInformation2 = endPortList.getAdditionalInformation();
                    for (AdditionalInformation addInfo1 : additionalInformation2) {
                        switch (addInfo1.getValueName().toLowerCase()) {
                            case "tejaskey":
                                userLabel = addInfo1.getValue();
                                break;
                            case "lctname":
                                zEndPort = addInfo1.getValue();
                                zEndDropPort = zEndPort;
                                String[] parts = zEndPort.split("-");
                                if (parts.length >= 6) {
                                    String result = parts[2] + "-" + parts[3] + "-" + parts[4];
                                    aEndPort = "Port_GPON-" + result;
                                    aEndDropPort = aEndPort;
                                }
                                break;
                            case "layer-rate":
                                if (addInfo1.getValue().contains("96")) {
                                    rate = "1GigE";
                                }
                                break;
                            // Add other cases if needed
                        }
                    }
                }
            }

            // Set default values if necessary
            LocalDateTime currentDateTime = LocalDateTime.now();
            String lastModified = currentDateTime.toString();
            String topologyUserLabel = String.format("%s-%s-%s-%s", aEndNode, aEndPort, zEndNode, zEndPort);

            // Collect data for topology
            String[] row = {topologyUserLabel, rate, "Ethernet", "INNI Connectivity", "GPON", "GPON", "GPON", aEndNode, zEndNode,
                            aEndPort, zEndPort, circle, lastModified};
            topologyData.add(row);

            // Collect data for tunnel
            String[] row2 = {trailId, userLabel, circuitId, rate, "Ethernet", "INNI Connectivity", "MAIN", "GPON", topologyUserLabel,
                             aEndDropNode, zEndDropNode, aEndDropPort, zEndDropPort, aEndNode, zEndNode, aEndPort, zEndPort,
                             circle, "NE2NE", lastModified};
            tunnelData.add(row2);
        }

        // Save data and write to CSV
        tunnelService.saveTunnelData(tunnelData);
        topologyService.saveTopologyData(topologyData);
        writeCsv(topologyData, tunnelData);
    }


    private void writeCsv(List<String[]> topologyData, List<String[]> tunnelData) {
        String fileName = "PACKET_TOPOLOGY_GPON_ALL.csv";
        String fileName2 = "PACKET_TUNNEL_GPON_ALL.csv";

        try (FileWriter writer = new FileWriter(fileName)) {
            // Write the header
			writer.append("USER_LABEL,RATE,TECHNOLOGY,SPECIFICATION,VENDOR,Z_END_VENDOR,A_END_VENDOR,A_END_NODE,Z_END_NODE,A_END_PORT,Z_END_PORT,CIRCLE,LAST_MODIFIED\n");

            // Write all rows
            for (String[] row : topologyData) {
                writer.append(String.join(",", formatValues(row))).append("\n");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        try (FileWriter writer = new FileWriter(fileName2)) {
            // Write the header
			writer.append("TRAIL_ID,USER_LABEL,CIRCUIT_ID,RATE,TECHNOLOGY,SPECIFICATION,PATH_TYPE,VENDOR,TOPOLOGY,A_END_DROP_NODE,Z_END_DROP_NODE,A_END_DROP_PORT,Z_END_DROP_PORT,A_END_NODE,Z_END_NODE,A_END_PORT,Z_END_PORT,CIRCLE,TOPOLOGY_TYPE,LAST_MODIFIED\n");

            // Write all rows
            for (String[] row : tunnelData) {
                writer.append(String.join(",", formatValues(row))).append("\n");
            }

            System.out.println("CSV file created successfully: " + fileName+ "and " +fileName2);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private List<String> formatValues(String[] values) {
        List<String> formattedValues = new ArrayList<>();
        for (String value : values) {
            // Escape double quotes and commas in the value
            if (value != null) {
                formattedValues.add("\"" + value.replace("\"", "\"\"").replace(",", "") + "\"");
            } else {
                formattedValues.add("");
            }
        }
        return formattedValues;
    }


	public void getTxOltData() {
		// TODO Auto-generated method stub
		
	}

}