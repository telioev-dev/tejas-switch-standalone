package com.teliolabs.tejas.l2swt.service.data;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import com.teliolabs.tejas.l2swt.service.client.ApiClientAuthService;
import com.teliolabs.tejas.l2swt.service.client.ApiClientInventoryService;

@Service
@RequiredArgsConstructor
@Slf4j
public class DataImportService {

    private final ApiClientAuthService apiClientAuthService;
    private final ApiClientInventoryService apiClientInventoryService;

    public void importInventory() {
        apiClientAuthService.authenticate();
        apiClientInventoryService.getTopologyData();
        apiClientInventoryService.getTunnelData();
        apiClientInventoryService.getServiceData();
    }
}
