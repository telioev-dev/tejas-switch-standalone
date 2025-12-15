package com.teliolabs.tejas.l2swt.service.client;

import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.teliolabs.tejas.l2swt.config.ApplicationConfig;
import com.teliolabs.tejas.l2swt.config.Endpoint;
import com.teliolabs.tejas.l2swt.config.NetworkManagerConfig;
import com.teliolabs.tejas.l2swt.context.ApplicationContext;
import com.teliolabs.tejas.l2swt.dto.inventory.TopologyOltService;
import com.teliolabs.tejas.l2swt.dto.inventory.TopologyService;
import com.teliolabs.tejas.l2swt.dto.inventory.TrailService;
import com.teliolabs.tejas.l2swt.dto.inventory.TunnelService;
import com.teliolabs.tejas.l2swt.repository.TopologyRepo;
import com.teliolabs.tejas.l2swt.repository.TrailRepo;
import com.teliolabs.tejas.l2swt.repository.TunnelRepo;
import com.teliolabs.tejas.l2swt.util.AdditionalInformation;
import com.teliolabs.tejas.l2swt.util.ConnectionEndPoint;
import com.teliolabs.tejas.l2swt.util.ConnectivityService;
import com.teliolabs.tejas.l2swt.util.EndPoint;
import com.teliolabs.tejas.l2swt.util.EndpointConstants;
import com.teliolabs.tejas.l2swt.util.Erp;
import com.teliolabs.tejas.l2swt.util.ErpRinglet;
import com.teliolabs.tejas.l2swt.util.Link;
import com.teliolabs.tejas.l2swt.util.Name;
import com.teliolabs.tejas.l2swt.util.NodeEdgePoint;
import com.teliolabs.tejas.l2swt.util.Ont;
import com.teliolabs.tejas.l2swt.util.Root;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ApiClientInventoryService extends BaseApiClientService {

    private final ObjectMapper objectMapper;
    private final ApiClientAuthService apiClientAuthService;
    private final TopologyService topologyService;
    private final TunnelService tunnelService;
    private final TrailService trailService;
    private final TopologyRepo topologyRepo;
    private final TunnelRepo tunnelRepo;
    private final TrailRepo trailRepo;

    @Autowired
    public ApiClientInventoryService(ApplicationContext applicationContext, WebClient.Builder webClientBuilder,
            ApplicationConfig applicationConfig, ObjectMapper objectMapper, TopologyService topologyService,
            TunnelService tunnelService,
            TrailService trailService, ApiClientAuthService apiClientAuthService,
            TopologyRepo topologyRepo, TunnelRepo tunnelRepo, TrailRepo trailRepo) {
        super(applicationContext, webClientBuilder, applicationConfig);
        this.objectMapper = objectMapper;
        this.topologyService = topologyService;
        this.tunnelService = tunnelService;
        this.trailService = trailService;
        this.apiClientAuthService = apiClientAuthService;
        this.topologyRepo = topologyRepo;
        this.trailRepo = trailRepo;
        this.tunnelRepo = tunnelRepo;
    }

    // Service method with token refresh logic
    public List<TopologyNodeDetail> getPdDetails() {
        // Get Network Manager Config
        List<Root> nodeLists = getNodeList();
        List<TopologyNodeDetail> pdDetailsList = new ArrayList<>();

        TopologyNodeDetail nodesList = null;
        for (Root nodeList : nodeLists) {
            String uuid = nodeList.getUuid();
            NetworkManagerConfig networkManager = applicationConfig.getNetworkManager();
            // Fetch the correct endpoint for getting node list
            Endpoint endpoint = networkManager.getEndpoints().stream()
                    .filter(e -> e.getName().equals(EndpointConstants.GET_NODE_DETAILS))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("Endpoint not found"));

            // Build the WebClient and make the request
            nodesList = webClientBuilder
                    .baseUrl(getEndpointHost(endpoint))
                    .build()
                    .method(resolveMethod(endpoint))
                    .uri(uriBuilder -> uriBuilder.path(getEndpointPath(endpoint))
                            .build(uuid))
                    .headers(headers -> headers.setBearerAuth(applicationContext.getAuthContext().getAccessToken()))
                    .retrieve()
                    .bodyToMono(new ParameterizedTypeReference<TopologyNodeDetail>() {
                    })
                    .block(); // Blocking call, consider using async if possible
            if (nodesList != null) {
                pdDetailsList.add(nodesList);
            }

        }

        return pdDetailsList;
    }

    public TopologyNodeDetail getPdNames(String uuid) {

        // Get Network Manager Config
        TopologyNodeDetail nodesList = null;
        NetworkManagerConfig networkManager = applicationConfig.getNetworkManager();
        // Fetch the correct endpoint for getting node list
        Endpoint endpoint = networkManager.getEndpoints().stream()
                .filter(e -> e.getName().equals(EndpointConstants.GET_NODE_DETAILS))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Endpoint not found"));

        // Build the WebClient and make the request
        nodesList = webClientBuilder
                .baseUrl(getEndpointHost(endpoint))
                .build()
                .method(resolveMethod(endpoint))
                .uri(uriBuilder -> uriBuilder.path(getEndpointPath(endpoint))
                        .build(uuid))
                .headers(headers -> headers.setBearerAuth(applicationContext.getAuthContext().getAccessToken()))
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<TopologyNodeDetail>() {
                })
                .block(); // Blocking call, consider using async if possible
        return nodesList;
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
                        .build("TTLSwitchEms"))
                .headers(headers -> headers.setBearerAuth(applicationContext.getAuthContext().getAccessToken()))
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<Root>>() {
                })
                .block(); // Blocking call, consider using async if possible
        // // Log the response
        // log.info("nodes: {}", nodeList);

        return nodeList;
    }

    public List<Root> getLinkList() {
        // Get Network Manager Config
        NetworkManagerConfig networkManager = applicationConfig.getNetworkManager();

        // Fetch the correct endpoint for getting node list
        Endpoint endpoint = networkManager.getEndpoints().stream()
                .filter(e -> e.getName().equals(EndpointConstants.GET_LINK_LIST))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Endpoint not found"));

        // Build the WebClient and make the request
        List<Root> nodeList = webClientBuilder
                .baseUrl(getEndpointHost(endpoint))
                .build()
                .method(resolveMethod(endpoint))
                .uri(uriBuilder -> uriBuilder.path(getEndpointPath(endpoint))
                        .build("TTLSwitchEms"))
                .headers(headers -> headers.setBearerAuth(applicationContext.getAuthContext().getAccessToken()))
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<Root>>() {
                })
                .block(); // Blocking call, consider using async if possible
        // // Log the response
        // log.info("nodes: {}", nodeList);

        return nodeList;
    }

    public List<Root> getServiceDetails() {
        List<Root> nodeLists = getNodeList();
        List<Root> allServices = new ArrayList<>();

        // Get config and endpoint once
        NetworkManagerConfig networkManager = applicationConfig.getNetworkManager();
        Endpoint endpoint = networkManager.getEndpoints().stream()
                .filter(e -> e.getName().equals(EndpointConstants.GET_SERVICE))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Endpoint not found"));

        for (Root node : nodeLists) {
            String uuid = node.getUuid();

            List<Root> serviceList = webClientBuilder
                    .baseUrl(getEndpointHost(endpoint))
                    .build()
                    .method(resolveMethod(endpoint))
                    .uri(uriBuilder -> uriBuilder
                            .path(getEndpointPath(endpoint))
                            .queryParam("configState", true)
                            .queryParam("csType", "Ethernet")
                            .queryParam("continue", 0)
                            .queryParam("nodeuuid", uuid)
                            .queryParam("size", 500)
                            .build())
                    .headers(headers -> headers.setBearerAuth(applicationContext.getAuthContext().getAccessToken()))
                    .retrieve()
                    .bodyToMono(new ParameterizedTypeReference<List<Root>>() {
                    })
                    .block();

            allServices.addAll(serviceList);
        }
        return allServices;
    }

    public List<Root> getRingDetails() {
        // Get Network Manager Config
        NetworkManagerConfig networkManager = applicationConfig.getNetworkManager();

        // Fetch the correct endpoint for getting node list
        Endpoint endpoint = networkManager.getEndpoints().stream()
                .filter(e -> e.getName().equals(EndpointConstants.GET_RING_DETAILS))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Endpoint not found"));

        // Build the WebClient and make the request
        List<Root> nodeList = webClientBuilder
                .baseUrl(getEndpointHost(endpoint))
                .build()
                .method(resolveMethod(endpoint))
                .uri(uriBuilder -> uriBuilder
                        .path(getEndpointPath(endpoint))
                        .queryParam("size", 500)
                        .build())
                .headers(headers -> headers.setBearerAuth(applicationContext.getAuthContext().getAccessToken()))
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<Root>>() {
                })
                .block(); // Blocking call, consider using async if possible
        // // Log the response
        // log.info("nodes: {}", nodeList);

        return nodeList;
    }

    public List<TopologyNodeDetail> getLinkDetails() {
        // Get Network Manager Config
        List<Root> getLinkList = getLinkList();
        List<TopologyNodeDetail> topologyDetails = new ArrayList<>();

        TopologyNodeDetail linksList = null;
        for (Root linkList : getLinkList) {
            String linkUuid = linkList.getUuid();
            NetworkManagerConfig networkManager = applicationConfig.getNetworkManager();
            // Fetch the correct endpoint for getting node list
            Endpoint endpoint = networkManager.getEndpoints().stream()
                    .filter(e -> e.getName().equals(EndpointConstants.GET_LINK_DETAILS))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("Endpoint not found"));

            // Build the WebClient and make the request
            linksList = webClientBuilder
                    .baseUrl(getEndpointHost(endpoint))
                    .build()
                    .method(resolveMethod(endpoint))
                    .uri(uriBuilder -> uriBuilder.path(getEndpointPath(endpoint))
                            .build(linkUuid))
                    .headers(headers -> headers.setBearerAuth(applicationContext.getAuthContext().getAccessToken()))
                    .retrieve()
                    .bodyToMono(new ParameterizedTypeReference<TopologyNodeDetail>() {
                    })
                    .block(); // Blocking call, consider using async if possible
            if (linksList != null) {
                topologyDetails.add(linksList);
            }

        }

        return topologyDetails;
    }

    public void getTopologyData() {
        List<String[]> topologyData = new ArrayList<>();
        List<String[]> tunnelData = new ArrayList<>();
        List<TopologyNodeDetail> getLinkDetailList = getLinkDetails();
        List<TopologyNodeDetail> getNodeNames = getPdDetails();

        for (TopologyNodeDetail getLinkDetails : getLinkDetailList) {
            String trailId = "null", userLabel = "null", circuitId = "null", rate = "null";
            String aEndDropPort = "null", zEndDropPort = "null", topology = "null";
            String aEndDropNode = "null", zEndDropNode = "null", channel = "null";
            String aEndNode = "null", zEndNode = "null", aEndPort = "null";
            String aEndNodeObj = "null", zEndNodeObj = "null";
            String zEndPort = "null", topologyType = "null", circle = "null";
            String uuid = "null", ZEndCapacity = "null";
            ArrayList<AdditionalInformation> additionalInformations = getLinkDetails.getAdditionalIinformation();

            for (AdditionalInformation topologyaddinfo : additionalInformations) {
                if (topologyaddinfo.valueName.equals("layer-rate")) {
                    String rateCode = topologyaddinfo.value;
                    if (rateCode.contains("19")) {
                        rate = "STM-0";
                    } else if (rateCode.equals("19")) {
                        rate = "STM0";
                    } else if (rateCode.equals("73") || rateCode.equals("25") || rateCode.equals("20")
                            || rateCode.equals("93")) {
                        rate = "STM1";
                    } else if (rateCode.equals("74") || rateCode.equals("21") || rateCode.equals("26")) {
                        rate = "STM4";
                    } else if (rateCode.equals("75") || rateCode.equals("89") || rateCode.equals("88")) {
                        rate = "STM8";
                    } else if (rateCode.equals("76") || rateCode.equals("22") || rateCode.equals("27")) {
                        rate = "STM16";
                    } else if (rateCode.equals("77") || rateCode.equals("28") || rateCode.equals("23")) {
                        rate = "STM64";
                    } else if (rateCode.equals("78") || rateCode.equals("91") || rateCode.equals("90")) {
                        rate = "STM256";
                    }

                } else if (topologyaddinfo.valueName.equals("ZEndCapacity")) {
                    ZEndCapacity = calculateRate(topologyaddinfo.value);
                } else if (topologyaddinfo.valueName.equals("user-label")) {
                    userLabel = topologyaddinfo.value;

                } else if (topologyaddinfo.valueName.equals("src-tp-label")) {
                    aEndPort = topologyaddinfo.value;
                } else if (topologyaddinfo.valueName.equals("dest-tp-label")) {
                    zEndPort = topologyaddinfo.value;
                }

                if (ZEndCapacity.equals("1 GigE")) {
                    rate = "1GigE";
                }

            }

            String nativeEmsName = getLinkDetails.getUuid();
            ArrayList<NodeEdgePoint> nodeEdgePoints = getLinkDetails.getNodeEdgePoint();
            aEndNodeObj = nodeEdgePoints.get(0).getNodeUuid();
            zEndNodeObj = nodeEdgePoints.get(1).getNodeUuid();
            for (TopologyNodeDetail getNodeName : getNodeNames) {
                if (getNodeName.getUuid().equals(aEndNodeObj)) {
                    ArrayList<AdditionalInformation> nodeAdditionalInformations = getNodeName
                            .getAdditionalIinformation();
                    for (AdditionalInformation nodeAdditionalInformation : nodeAdditionalInformations) {
                        if (nodeAdditionalInformation.valueName.equals("nativeEMSName")) {
                            aEndNode = nodeAdditionalInformation.value;
                        }
                    }
                } else if (getNodeName.getUuid().equals(zEndNodeObj)) {
                    ArrayList<AdditionalInformation> nodeAdditionalInformations = getNodeName
                            .getAdditionalIinformation();
                    for (AdditionalInformation nodeAdditionalInformation : nodeAdditionalInformations) {
                        if (nodeAdditionalInformation.valueName.equals("nativeEMSName")) {
                            zEndNode = nodeAdditionalInformation.value;
                        }
                    }
                }
            }

            circle = aEndNode.split("_")[0];

            // Set default values if necessary
            LocalDateTime currentDateTime = LocalDateTime.now();
            String lastModified = currentDateTime.toString();

            // Collect data for topology
            String[] row = { userLabel, rate, "Ethernet", "INNI Connectivity", "SWITCH", "SWITCH", "SWITCH", aEndNode,
                    zEndNode, aEndPort, zEndPort, circle, nativeEmsName, lastModified };
            topologyData.add(row);

            // Collect data for tunnel

        }

        // Save data and write to CSV
        topologyRepo.truncateTable();
        topologyService.saveTopologyData(topologyData);
        // writeCsv(topologyData, tunnelData);
    }

    public void getTunnelData() {
        List<String[]> tunnelData = new ArrayList<>();
        List<TopologyNodeDetail> getNodeNames = getPdDetails();
        String trailId = "null", userLabel = "null", circuitId = "null", rate = "null";
        String aEndDropPort = "null", zEndDropPort = "null", topology = "null";
        String aEndDropNode = "null", zEndDropNode = "null", channel = "null";
        String aEndNode = "null", zEndNode = "null", aEndPort = "null";
        String aEndNodeObj = "null", zEndNodeObj = "null";
        String zEndPort = "null", topologyType = "null", circle = "null";
        String uuid = "null";
        String topologyUserLabel = "";
        LocalDateTime currentDateTime = LocalDateTime.now();
        String lastModified = currentDateTime.toString();

        List<Root> ringDetails = getRingDetails();

        for (Root ringDetail : ringDetails) {
            ArrayList<Link> links = ringDetail.getErp().getLinks();
            for (Link link : links) {
                ArrayList<ErpRinglet> erpRinglets = ringDetail.getErp().getErpRinglet();
                for (ErpRinglet erpRinglet : erpRinglets) {
                    // userLabel = erpRinglet.getErpRingletName();
                    trailId = erpRinglet.getDataVid();
                }
                userLabel = ringDetail.getErp().getUuid();
                circuitId = extractCircuitId(userLabel);
                String topologyUuid = link.getTopologyUuid(); // Full string
                String[] ends = topologyUuid.split("-", 2); // A-end and Z-end

                if (ends.length == 2) {
                    String[] aTokens = ends[0].split("\\|");
                    String[] zTokens = ends[1].split("\\|");

                    if (aTokens.length >= 5 && zTokens.length >= 5) {
                        aEndNodeObj = aTokens[0] + "|" + aTokens[1]; // e.g., TTLSwitchEms|10.129.173.35
                        zEndNodeObj = zTokens[0] + "|" + zTokens[1];

                        String aPortSuffix = aTokens[2] + "-" + aTokens[3] + "-" + aTokens[4];
                        String zPortSuffix = zTokens[2] + "-" + zTokens[3] + "-" + zTokens[4];

                        aEndDropPort = "ETH-" + aPortSuffix;
                        zEndDropPort = "ETH-" + zPortSuffix;

                        for (TopologyNodeDetail getNodeName : getNodeNames) {
                            if (getNodeName.getUuid().equals(aEndNodeObj)) {
                                ArrayList<AdditionalInformation> nodeAdditionalInformations = getNodeName
                                        .getAdditionalIinformation();
                                for (AdditionalInformation info : nodeAdditionalInformations) {
                                    if ("nativeEMSName".equals(info.valueName)) {
                                        aEndDropNode = info.value;
                                    }
                                }
                            } else if (getNodeName.getUuid().equals(zEndNodeObj)) {
                                ArrayList<AdditionalInformation> nodeAdditionalInformations = getNodeName
                                        .getAdditionalIinformation();
                                for (AdditionalInformation info : nodeAdditionalInformations) {
                                    if ("nativeEMSName".equals(info.valueName)) {
                                        zEndDropNode = info.value;
                                    }
                                }
                            }
                        }
                    }
                }

                String[] row2 = { trailId, userLabel, circuitId, rate, "Ethernet", "INNI Connectivity", "MAIN",
                        "SWITCH",
                        topologyUserLabel,
                        aEndDropNode, zEndDropNode, aEndDropPort, zEndDropPort, aEndNode, zEndNode, aEndPort, zEndPort,
                        circle, "NE2NE", lastModified };
                tunnelData.add(row2);
            }

        }
        tunnelRepo.truncateTable();
        tunnelService.saveTunnelData(tunnelData);

    }

    public void getServiceData() {
        List<String[]> tunnelData = new ArrayList<>();

        String trailId = "null", userLabel = "null", circuitId = "null", rate = "null";
        String aEndDropPort = "null", zEndDropPort = "null", topology = "null";
        String aEndDropNode = "null", zEndDropNode = "null", channel = "null";
        String aEndNode = "null", zEndNode = "null", aEndPort = "null";
        String aEndNodeObj = "null", zEndNodeObj = "null";
        String zEndPort = "null", topologyType = "null", circle = "null";
        String uuid = "null";
        String topologyUserLabel = "";
        LocalDateTime currentDateTime = LocalDateTime.now();
        String lastModified = currentDateTime.toString();

        List<Root> serviceDetails = getServiceDetails();
        List<TopologyNodeDetail> getNodeNames = getPdDetails(); // Move outside loop for efficiency

        for (Root serviceDetail : serviceDetails) {
            if (serviceDetail.getConnectivityService() != null) {
                ConnectivityService connectivityService = serviceDetail.getConnectivityService();

                if (connectivityService.getName() != null) {
                    ArrayList<AdditionalInformation> vlanids = connectivityService.getAdditionalInformation();
                    for (AdditionalInformation vlanid : vlanids) {
                        if (vlanid.getValueName().equals("associated-vlans")) {
                            trailId = vlanid.getValue();
                        }
                    }
                    List<Name> serviceNames = connectivityService.getName();
                    for (Name serviceName : serviceNames) {
                        if ("ConnectivityService".equals(serviceName.getValueName())) {
                            userLabel = serviceName.getValue();

                            ArrayList<EndPoint> endPoints = connectivityService.getEndPoint();
                            if (endPoints != null) {
                                for (EndPoint endPoint1 : endPoints) {
                                    if (endPoint1.getConnectionEndPoint() != null) {
                                        for (ConnectionEndPoint connectionEndPoint1 : endPoint1
                                                .getConnectionEndPoint()) {
                                            if (connectionEndPoint1.topologyUuid != null
                                                    && connectionEndPoint1.nodeUuid != null) {
                                                String nodeUuid = connectionEndPoint1.topologyUuid + "|"
                                                        + connectionEndPoint1.nodeUuid;

                                                for (TopologyNodeDetail getNodeName : getNodeNames) {
                                                    if (nodeUuid.equals(getNodeName.getUuid())) {
                                                        ArrayList<AdditionalInformation> nodeAdditionalInformations = getNodeName
                                                                .getAdditionalIinformation();
                                                        if (nodeAdditionalInformations != null) {
                                                            for (AdditionalInformation nodeAdditionalInformation : nodeAdditionalInformations) {
                                                                if ("nativeEMSName"
                                                                        .equals(nodeAdditionalInformation.valueName)) {

                                                                    aEndDropNode = nodeAdditionalInformation.value;

                                                                    // Extract port label from endpoint
                                                                    ArrayList<AdditionalInformation> additionalInformation = endPoint1
                                                                            .getAdditionalInformation();
                                                                    if (additionalInformation != null) {
                                                                        for (AdditionalInformation additionalInformation1 : additionalInformation) {
                                                                            if (additionalInformation1 != null &&
                                                                                    "port-label".equals(
                                                                                            additionalInformation1.valueName)) {

                                                                                // Assign to a_end_port or z_end_port
                                                                                aEndDropPort = additionalInformation1.value;
                                                                            }
                                                                        }
                                                                    }

                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }

                                }
                            }

                            // Extract circuitId and rate
                            circuitId = extractCircuitId(userLabel);
                            rate = extractRate(userLabel);

                            // // Debug output
                            // System.out.println("User Label: " + userLabel);
                            // System.out.println("Node Label: " + nodeLabel);
                            // System.out.println("Port Label: " + portLabels);

                            // Construct row
                            String[] row2 = { trailId, userLabel, circuitId, rate, "Ethernet", "INNI Connectivity",
                                    "MAIN",
                                    "SWITCH",
                                    topologyUserLabel,
                                    aEndDropNode, zEndDropNode, aEndDropPort, zEndDropPort, aEndNode, zEndNode,
                                    aEndPort, zEndPort,
                                    circle, "NE2NE", lastModified };
                            tunnelData.add(row2);
                        }
                    }
                }
            }
        }
        trailRepo.truncateTable();
        trailService.saveTrailData(tunnelData);
    }

    public static String extractRate(String input) {
        Pattern pattern = Pattern.compile("\\b\\d{1,5}(M(?:B|BPS|BPLS)?)(?![A-Z])", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(input);
        if (matcher.find()) {
            return matcher.group(); // Return the first match
        }
        return "1 GigE"; // No match found
    }

    private String calculateRate(String value) {

        if (value.equals("1000")) {
            return "1 GigE";
        } else if (value.equals("10000")) {
            return "10 GigE";
        }
        return "1 GigE";
    }

    public static String extractCircuitId(String input) {
        Pattern pattern = Pattern.compile("(\\d{10,13}[A-Z]{0,3})");
        Matcher matcher = pattern.matcher(input);
        if (matcher.find()) {
            return matcher.group().toUpperCase(); // Return the first match
        }
        return null; // No match found
    }

}