package com.teliolabs.tejas.gpon.service.client;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.teliolabs.tejas.gpon.config.ApplicationConfig;
import com.teliolabs.tejas.gpon.config.Endpoint;
import com.teliolabs.tejas.gpon.config.NetworkManagerConfig;
import com.teliolabs.tejas.gpon.context.ApplicationContext;
import com.teliolabs.tejas.gpon.util.AdditionalInformation;
import com.teliolabs.tejas.gpon.util.EndpointConstants;
import com.teliolabs.tejas.gpon.util.NodeEdgePoint;
import com.teliolabs.tejas.gpon.util.Ont;
import com.teliolabs.tejas.gpon.util.Root;
import com.teliolabs.tejas.gpon.util.TapiCommonNameAndValue;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ApiClientInventoryService extends BaseApiClientService {

    private final ObjectMapper objectMapper;

    @Autowired
    public ApiClientInventoryService(ApplicationContext applicationContext, WebClient.Builder webClientBuilder, ApplicationConfig applicationConfig, ObjectMapper objectMapper) {
        super(applicationContext, webClientBuilder, applicationConfig);
        this.objectMapper = objectMapper;
    }
    
    
    public List<TopologyNodeDetail> getPdDetails() {
    	List<Root> nodeList = getNodeList();
    	List<TopologyNodeDetail> physicalDataList = new ArrayList<>();
    	for (Root node : nodeList) {
    		String pdUuid = node.getUuid();
        NetworkManagerConfig networkManager = applicationConfig.getNetworkManager();
        Endpoint endpoint = networkManager.getEndpoints().stream()
                .filter(e -> e.getName().equals(EndpointConstants.GET_NODE_DETAILS))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Endpoint not found"));
            List<TopologyNodeDetail> physicalNodeList = webClientBuilder
                .baseUrl(getEndpointHost(endpoint))
                .build()
                .method(resolveMethod(endpoint))
                .uri(uriBuilder -> uriBuilder.path(getEndpointPath(endpoint))
                        .build(pdUuid))
                .headers(headers -> headers.setBearerAuth(applicationContext.getAuthContext().getAccessToken()))
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<TopologyNodeDetail>>() {})
                .block(); // Blocking call, consider using async if possible
            
            if (physicalNodeList != null) {
            	physicalDataList.addAll(physicalNodeList);
            }
    	}
        return physicalDataList;
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
        List<String[]> csvData = new ArrayList<>(); // List to hold all CSV rows

        // Process each Root object and collect data
        for (Root list : circuitDataList) {
            String trailId = null, userLabel = null, circuitId = null, rate = null;
            String aEndDropPort = null, zEndDropPort = null, topology = null, aEndDropNode = null;
            String zEndDropNode = null, channel = null, aEndNode = null, zEndNode = null;
            String aEndPort = null, zEndPort = null, topologyType = null, circle = null;

            if (list.getRemoteUnit() != null && list.getRemoteUnit().getOnt() != null) {
                Ont ont = list.getRemoteUnit().getOnt();
                String[] split = ont.getUuid().split("\\|");
            	String pdUuid = split[0]+"|"+split[1];
//            	System.out.println("pdUuid"+pdUuid);
//                List<TopologyNodeDetail> pdDetailList = getPdDetails();
//                for(TopologyNodeDetail pdDetails :pdDetailList) {
//                if(pdUuid.equals(pdDetails.getUuid())) {
//                ArrayList<AdditionalInformation> pdAddinfo = pdDetails.getAdditionalIinformation();
//                for (AdditionalInformation addInfo : pdAddinfo) {
//                    if (addInfo.getValueName().equalsIgnoreCase("nativeEMSName")) {
//                    	System.out.println(addInfo.getValue());
//                    	aEndDropNode = addInfo.getValue();
//                    	zEndDropNode = addInfo.getValue();
//                    } 
//                }
//                }
//                }
                ArrayList<AdditionalInformation> additionalInformation = ont.getAdditionalInformation();

                // Get trailId and circuitId
                for (AdditionalInformation addInfo : additionalInformation) {
                    if (addInfo.getValueName().equalsIgnoreCase("CustomerId")) {
                        circuitId = addInfo.getValue();
                    } else if (addInfo.getValueName().equalsIgnoreCase("id")) {
                        trailId = addInfo.getValue();
                    }
                }
            }
//                for (Root list1 : circuitDataList) {
                // Process NodeEdgePoint entries
                ArrayList<NodeEdgePoint> nodeEdgePointList = list.getNodeEdgePoint();
                System.out.println("nodeEdgePointList"+nodeEdgePointList.size());
                    for (NodeEdgePoint endPortList : nodeEdgePointList) {
                        ArrayList<AdditionalInformation> additionalInformation2 = endPortList.getAdditionalInformation();

                        // Process AdditionalInformation for NodeEdgePoint
                        for (AdditionalInformation addInfo1 : additionalInformation2) {
                            if (addInfo1.getValueName().equalsIgnoreCase("TejasKey")) {
                                userLabel = addInfo1.getValue();
                            } else if (addInfo1.getValueName().equalsIgnoreCase("LCTName")) {
                                zEndDropPort = addInfo1.getValue();
                            } else if (addInfo1.getValueName().equalsIgnoreCase("layer-rate")) {
                                rate = addInfo1.getValue();
                            }
                        }
                        System.out.println("userLabel====="+userLabel+"zEndDropPort==="+zEndDropPort+"rate===="+rate);

                        ArrayList<TapiCommonNameAndValue> name = endPortList.getName();
                        for (TapiCommonNameAndValue addInfo2 : name) {
                            if (addInfo2.getValueName().equalsIgnoreCase("name")) {
                                String[] splitUuid = addInfo2.getValue().split("\\|");
                                if (splitUuid.length > 2) {
                                    String splitPort = splitUuid[2];
                                    String[] segments = splitPort.split("-");
                                    String result = segments.length > 3
                                            ? String.join("-", segments[0], segments[1], segments[2])
                                            : splitPort;
                                    aEndDropPort = "Port_GPON-" + result;
                                }
                            }
                        }
						


                    }

                String technology = "Ethernet";
		      	String specification = "INNI Connectivity";
		      	String pathType= "MAIN";
		      	String vendor = "GPON";
		      	String sequence = "1";
		      	LocalDateTime currentDateTime = LocalDateTime.now();
		      	String lastModified = currentDateTime.toString();
		      	channel ="";
                // Collect all the data for this entry
		      	String[] row = {trailId, userLabel, circuitId, rate,technology,specification, pathType,vendor,topology, aEndDropPort, zEndDropPort,
                     aEndDropNode, zEndDropNode,sequence, channel, aEndNode, zEndNode,
                    aEndPort, zEndPort, topologyType, circle,lastModified};
                csvData.add(row);
        }

        // Write all collected data to the CSV file
        writeCsv(csvData);
    }

    private void writeCsv(List<String[]> csvData) {
        String fileName = "PACKET_TOPOLOGY_GPON_ALL.csv";

        try (FileWriter writer = new FileWriter(fileName)) {
            // Write the header
			writer.append("TRAIL_ID,USER_LABEL,CIRCUIT_ID,RATE,TECHNOLOGY,SPECIFICATION,PATH_TYPE,VENDOR,TOPOLOGY,A_END_DROP_NODE,Z_END_DROP_NODE,A_END_DROP_PORT,Z_END_DROP_PORT,SEQUENCE,CHANNEL,A_END_NODE,Z_END_NODE,A_END_PORT,Z_END_PORT,TOPOLOGY_TYPE,CIRCLE,LAST_MODIFIED\n");

            // Write all rows
            for (String[] row : csvData) {
                writer.append(String.join(",", formatValues(row))).append("\n");
            }

            System.out.println("CSV file created successfully: " + fileName);
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

}