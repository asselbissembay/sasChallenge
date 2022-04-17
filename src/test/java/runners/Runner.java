package runners;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

    @RunWith(Cucumber.class)
    @CucumberOptions(
            plugin = {"com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter:",
                    "html:target\\default-cucumber_reports.html",
                    "json:target\\json-reports/cucumber.json",
                    "junit:target\\xml-report/cucumber.xml"},
            features = "src/test/resources/features",
            glue = {"stepdefinitions"},
//            tags = "@api_testing",
            dryRun = false
    )
    public class Runner {
}
