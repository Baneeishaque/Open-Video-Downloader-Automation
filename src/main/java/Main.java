import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.Select;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.time.Duration;
import java.util.List;
import java.util.stream.Stream;

public class Main {
    public static void main(String[] args) {

//        ChromeDriver 87.0.4280.88
//        ---------------------
//        Supports Chrome v87
//        URL : https://chromedriver.storage.googleapis.com/index.html?path=87.0.4280.88/
        ChromeDriverService service = new ChromeDriverService.Builder()
                .usingDriverExecutable(new File("C:\\Programs\\chromedriver_win32-87\\chromedriver.exe"))
                .usingAnyFreePort()
                .build();
        ChromeOptions options = new ChromeOptions();

//        Open Video Downloader (youtube-dl-gui)
//        ---------------------
//        URL : https://github.com/jely2002/youtube-dl-gui
//        Version : 2.3.1
//
//        process.versions
//        ----------------------
//        chrome: "87.0.4280.88"
//        electron: "11.0.5"
//        node: "12.18.3"
        options.setBinary("C:\\Users\\muhsi\\AppData\\Local\\Programs\\youtube-dl-gui\\Open Video Downloader.exe");
        options.addArguments("--remote-debugging-port=9222");
        WebDriver driver = new ChromeDriver(service, options);

        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
        if (!driver.findElements(By.id("tasklist-btn")).isEmpty()) {
            driver.findElement(By.cssSelector("[class=\"ml-2 mb-1 close\"]")).click();
        }

        new Select(driver.findElement(By.id("download-quality"))).selectByIndex(1);

        try (Stream<String> lines = Files.lines(getFileFromResource("urls.txt").toPath())) {
            List<String> result = lines.toList();
            if (!result.isEmpty()) {
                result.forEach(resultItem -> {

                    driver.findElement(By.id("add-url")).sendKeys(resultItem);
                    driver.findElement(By.id("add-url-btn")).click();
                });
            }
//            WebDriverWait webDriverWait = new WebDriverWait(driver, Duration.ofMinutes(result.size() * 3L));
//            webDriverWait.until(ExpectedConditions.invisibilityOf(driver.findElement(By.cssSelector("[class=\"progress-bar\"]"))));
//            driver.findElement(By.id("downloadBtn")).click();
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
    }

    /*
        The resource URL is not working in the JAR
        If we try to access a file that is inside a JAR,
        It throws NoSuchFileException (linux), InvalidPathException (Windows)

        Resource URL Sample: file:java-io.jar!/json/file1.json
     */
    private static File getFileFromResource(String fileName) throws URISyntaxException {

        ClassLoader classLoader = Main.class.getClassLoader();
        URL resource = classLoader.getResource(fileName);
        if (resource == null) {
            throw new IllegalArgumentException("file not found! " + fileName);
        } else {

            // failed if files have whitespaces or special characters
            //return new File(resource.getFile());

            return new File(resource.toURI());
        }
    }
}
