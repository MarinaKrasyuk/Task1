package utils;

import io.qameta.allure.Allure;
import org.junit.jupiter.api.extension.AfterTestExecutionCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

import java.text.SimpleDateFormat;
import java.util.Date;

public class RunnerExtension implements AfterTestExecutionCallback {

    @Override
    public void afterTestExecution(ExtensionContext extensionContext) throws Exception {
        Boolean testResult = extensionContext.getExecutionException().isPresent();
        if (testResult){//false - SUCCESS, true - FAILED
            Utils.saveScreenshotPNG();
            Date date=new Date();
            SimpleDateFormat formatForDateNow = new SimpleDateFormat("yyyy.MM.dd_hh.mm.ss");
            String date_str=formatForDateNow.format(date);
            Allure.description("Date and Time: "+date_str);
           // Allure.description("Test is Failed on " + ConstantEnv.BROWSER+" and environment: "+ConstantEnv.ENVIRONMENT);
        }

    }
}
