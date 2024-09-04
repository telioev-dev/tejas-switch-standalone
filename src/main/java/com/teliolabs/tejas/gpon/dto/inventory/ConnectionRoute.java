package com.teliolabs.tejas.gpon.dto.inventory;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ConnectionRoute {
    private ConnectionData connectionData;
    private List<RouteData> routeData;
}
