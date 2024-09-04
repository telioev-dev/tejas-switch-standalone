package com.teliolabs.tejas.gpon.start;


import com.teliolabs.tejas.gpon.service.data.DataImportService;
import com.teliolabs.tejas.gpon.util.ArgumentLogger;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Component;

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
