package com.teliolabs.tejas.gpon.service.data;

import com.teliolabs.tejas.gpon.service.client.ApiClientAuthService;
import com.teliolabs.tejas.gpon.service.client.ApiClientInventoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class DataImportService {

    private final ApiClientAuthService apiClientAuthService;
    private final ApiClientInventoryService apiClientInventoryService;

    public void importInventory() {
        apiClientAuthService.authenticate();
        apiClientInventoryService.getCompleteCircuitData();
    }
}
