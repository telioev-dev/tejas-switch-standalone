package com.teliolabs.tejas.l2swt.start;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Component;

import com.teliolabs.tejas.l2swt.service.data.DataImportService;
import com.teliolabs.tejas.l2swt.util.ArgumentLogger;

@Slf4j
@Component
@RequiredArgsConstructor
@ConditionalOnExpression("'${job}'.contains('import')")
public class ImportRunner implements ApplicationRunner {

    private final DataImportService dataImportService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        ArgumentLogger.log(args);
        dataImportService.importInventory();
    }
}
