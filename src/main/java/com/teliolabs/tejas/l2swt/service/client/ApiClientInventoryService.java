package com.teliolabs.tejas.l2swt.service.client;

import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

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

    // Authenticate once
  

    // Fetch all node UUIDs
    List<Root> nodeLists = getNodeList();

    if (nodeLists == null || nodeLists.isEmpty()) {
        return new ArrayList<>();
    }

    // Fetch endpoint once
    NetworkManagerConfig networkManager =
            applicationConfig.getNetworkManager();

    Endpoint endpoint = networkManager.getEndpoints().stream()
            .filter(e ->
                    e.getName().equals(
                            EndpointConstants.GET_NODE_DETAILS))
            .findFirst()
            .orElseThrow(() ->
                    new IllegalArgumentException(
                            "GET_NODE_DETAILS endpoint not found"));

    List<TopologyNodeDetail> pdDetailsList = new ArrayList<>();

    for (Root node : nodeLists) {

        if (node == null || node.getUuid() == null) {
            continue;
        }

        String uuid = node.getUuid();

        try {

            TopologyNodeDetail nodeDetail = webClientBuilder
                    .baseUrl(getEndpointHost(endpoint))
                    .build()
                    .method(resolveMethod(endpoint))
                    .uri(uriBuilder ->
                            uriBuilder
                                    .path(getEndpointPath(endpoint))
                                    .build(uuid))
                    .headers(headers ->
                            headers.setBearerAuth(  apiClientAuthService.getValidToken()))
                                           
                    .retrieve()
                    .bodyToMono(
                            new ParameterizedTypeReference<TopologyNodeDetail>() {
                            })
                    .block();

            if (nodeDetail != null) {
                pdDetailsList.add(nodeDetail);
            }

        } catch (Exception e) {

            log.error(
                    "Failed to fetch node details for UUID: {}",
                    uuid,
                    e
            );
        }
    }

    log.info(
            "Successfully fetched {} node details",
            pdDetailsList.size()
    );

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

                System.out.println("reauthentiat insdie the pdNames");
        
        // Build the WebClient and make the request
        nodesList = webClientBuilder
                .baseUrl(getEndpointHost(endpoint))
                .build()
                .method(resolveMethod(endpoint))
                .uri(uriBuilder -> uriBuilder.path(getEndpointPath(endpoint))
                        .build(uuid))
                .headers(headers ->  headers.setBearerAuth(  apiClientAuthService.getValidToken()))
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<TopologyNodeDetail>() {
                })
                .block(); // Blocking call, consider using async if possible
        return nodesList;
    }

    public List<Root> getNodeList() {
        // Get Network Manager Config
        NetworkManagerConfig networkManager = applicationConfig.getNetworkManager();
          List<String> topologies = networkManager.getTopologies();

        // Fetch the correct endpoint for getting node list
        Endpoint endpoint = networkManager.getEndpoints().stream()
                .filter(e -> e.getName().equals(EndpointConstants.GET_NODE_LIST))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Endpoint not found"));

                System.out.println("calling nodeList autheticate");
          
        // Build the WebClient and make the request
        List<Root> nodeList = webClientBuilder
                .baseUrl(getEndpointHost(endpoint))
                .build()
                .method(resolveMethod(endpoint))
                .uri(uriBuilder -> uriBuilder.path(getEndpointPath(endpoint))
                        .build())
                .headers(headers ->
    headers.setBearerAuth(
        apiClientAuthService.getValidToken()))
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

                System.out.println("apiClientAuthService.authenticate()");
         
        // Build the WebClient and make the request
        List<Root> nodeList = webClientBuilder
                .baseUrl(getEndpointHost(endpoint))
                .build()
                .method(resolveMethod(endpoint))
                .uri(uriBuilder -> uriBuilder.path(getEndpointPath(endpoint))
                        .build())
                .headers(headers ->
    headers.setBearerAuth(
        apiClientAuthService.getValidToken()))
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
                    .headers(headers ->
    headers.setBearerAuth(
        apiClientAuthService.getValidToken()))
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
               .headers(headers ->
    headers.setBearerAuth(
        apiClientAuthService.getValidToken()))
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<Root>>() {
                })
                .block(); // Blocking call, consider using async if possible
        // // Log the response
        // log.info("nodes: {}", nodeList);

        return nodeList;
    }
// gpon,switch,ptn
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
                    .headers(headers ->
    headers.setBearerAuth(
        apiClientAuthService.getValidToken()))
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

    // Fetch all links once
    List<TopologyNodeDetail> getLinkDetailList = getLinkDetails();

    // Fetch all node details once
    List<TopologyNodeDetail> allNodes = getPdDetails();

    // Create UUID -> Node map
    Map<String, TopologyNodeDetail> nodeMap =
            allNodes.stream()
                    .collect(Collectors.toMap(
                            TopologyNodeDetail::getUuid,
                            node -> node
                    ));

    for (TopologyNodeDetail getLinkDetails : getLinkDetailList) {

        String userLabel = "null";
        String rate = "null";
        String aEndPort = "null";
        String zEndPort = "null";
        String aEndNode = "null";
        String zEndNode = "null";
        String aEndNodeObj;
        String zEndNodeObj;
        String circle = null;
        String ZEndCapacity = "null";

        ArrayList<AdditionalInformation> additionalInformations =
                getLinkDetails.getAdditionalIinformation();

        // Extract link additional info
        for (AdditionalInformation topologyaddinfo : additionalInformations) {

            if ("layer-rate".equals(topologyaddinfo.valueName)) {

                String rateCode = topologyaddinfo.value;

                if (rateCode.contains("19")) {
                    rate = "STM-0";
                } else if (rateCode.equals("73") || rateCode.equals("25")
                        || rateCode.equals("20") || rateCode.equals("93")) {
                    rate = "STM1";
                } else if (rateCode.equals("74")
                        || rateCode.equals("21")
                        || rateCode.equals("26")) {
                    rate = "STM4";
                } else if (rateCode.equals("75")
                        || rateCode.equals("89")
                        || rateCode.equals("88")) {
                    rate = "STM8";
                } else if (rateCode.equals("76")
                        || rateCode.equals("22")
                        || rateCode.equals("27")) {
                    rate = "STM16";
                } else if (rateCode.equals("77")
                        || rateCode.equals("28")
                        || rateCode.equals("23")) {
                    rate = "STM64";
                } else if (rateCode.equals("78")
                        || rateCode.equals("91")
                        || rateCode.equals("90")) {
                    rate = "STM256";
                }

            } else if ("ZEndCapacity".equals(topologyaddinfo.valueName)) {

                ZEndCapacity = calculateRate(topologyaddinfo.value);

            } else if ("user-label".equals(topologyaddinfo.valueName)) {

                userLabel = topologyaddinfo.value;

            } else if ("src-tp-label".equals(topologyaddinfo.valueName)) {

                aEndPort = topologyaddinfo.value;

            } else if ("dest-tp-label".equals(topologyaddinfo.valueName)) {

                zEndPort = topologyaddinfo.value;
            }

            if ("1 GigE".equals(ZEndCapacity)) {
                rate = "1GigE";
            }
        }

        String nativeEmsName = getLinkDetails.getUuid();

        // Node edge points
        ArrayList<NodeEdgePoint> nodeEdgePoints =
                getLinkDetails.getNodeEdgePoint();

        aEndNodeObj = nodeEdgePoints.get(0).getNodeUuid();
        zEndNodeObj = nodeEdgePoints.get(1).getNodeUuid();

        String aVendor = nodeEdgePoints.get(0).getTopologyUuid();
        String zVendor = nodeEdgePoints.get(1).getTopologyUuid();

        // Fetch node details from map instead of API call
        TopologyNodeDetail aNodeDetail = nodeMap.get(aEndNodeObj);
        TopologyNodeDetail zNodeDetail = nodeMap.get(zEndNodeObj);

        // A-End node name
        if (aNodeDetail != null
                && aNodeDetail.getAdditionalIinformation() != null) {

            for (AdditionalInformation info :
                    aNodeDetail.getAdditionalIinformation()) {

                if ("nativeEMSName".equals(info.valueName)) {
                    aEndNode = info.value;
                    break;
                }
            }
        }

        // Z-End node name
        if (zNodeDetail != null
                && zNodeDetail.getAdditionalIinformation() != null) {

            for (AdditionalInformation info :
                    zNodeDetail.getAdditionalIinformation()) {

                if ("nativeEMSName".equals(info.valueName)) {
                    zEndNode = info.value;
                    break;
                }
            }
        }

        // Determine circle/vendor type
        if (aVendor.contains("GPON")) {

            circle = "GPONEms";

        } else if (aVendor.toLowerCase().contains("switch")) {

            circle = "SwitchEms";

        } else if (aVendor.toLowerCase().contains("ptn")) {

            circle = "PtnEms";
        }

        String lastModified = LocalDateTime.now().toString();

        // Final topology row
        String[] row = {
                userLabel,
                rate,
                "Ethernet",
                "INNI Connectivity",
                aVendor,
                zVendor,
                aVendor,
                aEndNode,
                zEndNode,
                aEndPort,
                zEndPort,
                circle,
                nativeEmsName,
                lastModified
        };

        topologyData.add(row);
    }

    topologyRepo.truncateTable();
    topologyService.saveTopologyData(topologyData);
}

    public void getTunnelData() {
apiClientAuthService.authenticate();
    List<String[]> tunnelData = new ArrayList<>();

    String lastModified = LocalDateTime.now().toString();

    // Fetch ring details once
    List<Root> ringDetails = getRingDetails();

    // Fetch all node details once
    List<TopologyNodeDetail> allNodes = getPdDetails();

    // Create UUID -> Node map
    Map<String, TopologyNodeDetail> nodeMap =
            allNodes.stream()
                    .collect(Collectors.toMap(
                            TopologyNodeDetail::getUuid,
                            node -> node,
                            (a, b) -> a
                    ));

    for (Root ringDetail : ringDetails) {

        if (ringDetail.getErp() == null) {
            continue;
        }

        Erp erp = ringDetail.getErp();

        ArrayList<Link> links = erp.getLinks();

        if (links == null) {
            continue;
        }

        for (Link link : links) {

            String trailId = "null";
            String userLabel = "null";
            String circuitId = "null";
            String rate = "1 GigE";

            String aEndDropPort = "null";
            String zEndDropPort = "null";

            String aEndNode = "null";
            String zEndNode = "null";

            String aEndPort = "null";
            String zEndPort = "null";

            String circle = "SwitchEms";

            String topologyUserLabel = "";

            // Extract ERP Ringlet info
            ArrayList<ErpRinglet> erpRinglets = erp.getErpRinglet();

            if (erpRinglets != null) {

                for (ErpRinglet erpRinglet : erpRinglets) {

                    if (erpRinglet.getDataVid() != null) {
                        trailId = erpRinglet.getDataVid();
                    }
                }
            }

            userLabel = erp.getUuid();

            if (userLabel != null) {
                circuitId = extractCircuitId(userLabel);
            }

            String topologyUuid = link.getTopologyUuid();

            if (topologyUuid == null) {
                continue;
            }

            // Split A-end and Z-end
            String[] ends = topologyUuid.split("-", 2);

            if (ends.length != 2) {
                continue;
            }

            String[] aTokens = ends[0].split("\\|");
            String[] zTokens = ends[1].split("\\|");

            if (aTokens.length < 5 || zTokens.length < 5) {
                continue;
            }

            // Build node UUIDs
            String aEndNodeObj = aTokens[0] + "|" + aTokens[1];
            String zEndNodeObj = zTokens[0] + "|" + zTokens[1];

            // Build ports
            aEndDropPort =
                    "ETH-" + aTokens[2] + "-" + aTokens[3] + "-" + aTokens[4];

            zEndDropPort =
                    "ETH-" + zTokens[2] + "-" + zTokens[3] + "-" + zTokens[4];

            // Fetch node details from map
            TopologyNodeDetail aNodeDetail = nodeMap.get(aEndNodeObj);
            TopologyNodeDetail zNodeDetail = nodeMap.get(zEndNodeObj);

            // Resolve A-End node name
            if (aNodeDetail != null
                    && aNodeDetail.getAdditionalIinformation() != null) {

                for (AdditionalInformation info :
                        aNodeDetail.getAdditionalIinformation()) {

                    if ("nativeEMSName".equals(info.getValueName())) {
                        aEndNode = info.getValue();
                        break;
                    }
                }
            }

            // Resolve Z-End node name
            if (zNodeDetail != null
                    && zNodeDetail.getAdditionalIinformation() != null) {

                for (AdditionalInformation info :
                        zNodeDetail.getAdditionalIinformation()) {

                    if ("nativeEMSName".equals(info.getValueName())) {
                        zEndNode = info.getValue();
                        break;
                    }
                }
            }

            // Final row
            String[] row = {
                    trailId,
                    userLabel,
                    circuitId,
                    rate,
                    "Ethernet",
                    "INNI Connectivity",
                    "MAIN",
                    "SWITCH",
                    topologyUserLabel,
                    "null",
                    "null",
                    aEndDropPort,
                    zEndDropPort,
                    aEndNode,
                    zEndNode,
                    aEndPort,
                    zEndPort,
                    circle,
                    "NE2NE",
                    lastModified
            };

            tunnelData.add(row);
        }
    }

    tunnelRepo.truncateTable();

    tunnelService.saveTunnelData(tunnelData);
}

    public void getServiceData() {
        apiClientAuthService.authenticate();
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
        // List<TopologyNodeDetail> getNodeNames = getPdDetails(); // Move outside loop for efficiency

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

                                            TopologyNodeDetail getNodeName = getPdNames(nodeUuid);

                                                    
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