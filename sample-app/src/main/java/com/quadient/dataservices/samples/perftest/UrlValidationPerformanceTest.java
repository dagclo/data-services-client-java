package com.quadient.dataservices.samples.perftest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.quadient.dataservices.ClientFactory;
import com.quadient.dataservices.api.Client;
import com.quadient.dataservices.api.Credentials;
import com.quadient.dataservices.api.Request;
import com.quadient.dataservices.samples.utils.CommandLineArgs;
import com.quadient.dataservices.url.UrlValidationRequest;
import com.quadient.dataservices.url.model.UrlValidationDepth;

/**
 * Example performance test class for URL validation.
 * 
 * Beware: Running this class may cost you Quadient Cloud credits.
 */
public class UrlValidationPerformanceTest extends AbstractPerformanceTest {

    private static final Logger logger = LoggerFactory.getLogger(UrlValidationPerformanceTest.class);

    public static void main(String[] args) {
        final Credentials credentials = CommandLineArgs.getCredentials(args);
        final Client client = ClientFactory.createClient(credentials);

        final UrlValidationDepth depth = UrlValidationDepth.MODERATE;
        final int numThreads = 1;
        final boolean createJob = true;
        final int numRequests = 100;
        final int numRecordsPerRequest = 10;

        final UrlValidationPerformanceTest perfTest = new UrlValidationPerformanceTest(client, numThreads, createJob,
                numRequests, numRecordsPerRequest, depth);
        final PerformanceTestState testState = perfTest.run();

        if (testState.isCancelled()) {
            System.exit(500);
        }

        logger.info("Config:\nThreads: {}\nRequests: {}\nRecords/Request: {}\nRecords total: {}", numThreads,
                numRequests, numRecordsPerRequest, numRecordsPerRequest * numRequests);
    }

    private final int numRequests;
    private final int numRecordsPerRequest;
    private final UrlValidationDepth depth;

    public UrlValidationPerformanceTest(Client client, int numThreads, boolean createJob, int numRequests,
            int numRecordsPerRequest, UrlValidationDepth depth) {
        super(client, numThreads, createJob);
        this.numRequests = numRequests;
        this.numRecordsPerRequest = numRecordsPerRequest;
        this.depth = depth;
    }

    @Override
    protected Integer getExpectedRecordCount() {
        return numRequests * numRecordsPerRequest;
    }

    @Override
    protected String getServiceId() {
        return "url-validation";
    }

    @Override
    protected Queue<Request<?>> createRequestQueue() {
        final List<String> randomUrls = createDomainList();

        final ConcurrentLinkedQueue<Request<?>> queue = new ConcurrentLinkedQueue<>();
        for (int i = 0; i < numRequests; i++) {
            queue.add(createRequest(randomUrls));
        }
        return queue;
    }

    private Request<?> createRequest(List<String> domains) {
        final List<String> urls = new ArrayList<>();
        final Random r = new Random();
        for (int i = 0; i < numRecordsPerRequest; i++) {
            final String domainName = domains.get(r.nextInt(domains.size()));
            final String url = createUrlFromDomain(r, domainName);
            urls.add(url);
        }
        return new UrlValidationRequest(depth, urls);
    }

    private String createUrlFromDomain(final Random r, final String domainName) {
        final int random = r.nextInt(100);
        final String url;
        if (random > 90) {
            url = "http://" + domainName;
        } else if (random > 80) {
            url = "www." + domainName;
        } else if (random > 70) {
            url = "http://www." + domainName;
        } else if (random > 60) {
            url = "http://www." + domainName + "/hello";
        } else if (random > 50) {
            url = "http://www." + domainName + "/world";
        } else if (random > 40) {
            url = "https://" + domainName;
        } else if (random > 30) {
            url = "https://www." + domainName;
        } else if (random > 20) {
            url = "https://www." + domainName + "/hello";
        } else if (random > 10) {
            url = "https://www." + domainName + "/world";
        } else {
            url = domainName;
        }
        return url;
    }

    private List<String> createDomainList() {
        return Arrays.asList("quadient.com", "neopost.com", "quadientcloud.com", "quadientcloud.eu", "facebook.com",
                "apple.com", "amazon.com", "netflix.com", "google.com", "twitter.com", "instagram.com",
                "mailinator.com", "myspace.com", "10086.cn", "127.0.0.1:8080", "163.com", "192.168.99.100:3180",
                "360.cn", "403", "58.com", "abc.xyz", "accenture.com", "account.similarweb.com", "accuweather.com",
                "acer.com", "adobe.com", "advantest.com", "akamai.com", "alipay.com", "amazon.com", "amazon.de",
                "amd.com", "amdocs.com", "ampproject.org", "analog.com", "analytics.similarweb.com",
                "annual-report.thomsonreuters.com", "app-data.gcs.trstatic.net", "apple.com", "appliedmaterials.com",
                "applytracking.com", "arris.com", "aseglobal.com", "asmpacific.com", "asus.com", "atianqi.com",
                "atos.net", "autodesk.com", "bahn.de", "baidu.com", "baiducontent.com", "bild.de", "bilibili.com",
                "bing.com", "blogs.thomsonreuters.com", "booking.com", "ca.com", "canon.com", "capgemini.com",
                "celestica.com", "cgi.com", "chaturbate.com", "chip.de", "chrome.google.com", "cisco.com",
                "cognizant.com", "commscope.com", "computacenter.com", "computershare.com", "csdn.net",
                "developers.thomsonreuters.com", "dormakaba.com", "douban.com", "dxc.technology", "eastday.com",
                "ebay.de", "ebayinc.com", "ebay-kleinanzeigen.de", "en.wikipedia.org", "ericsson.com", "facebook.com",
                "flaksjdfljasdfljasdlfjsadlkfjsdal", "focus.de", "fonts.googleapis.com", "fujifilmholdings.com",
                "fujikura.co.jp", "fujitsu.com", "gemalto.com", "github.com", "global.epson.com", "gmx.net",
                "google.co.id", "google.com", "google.com.br", "google.com.hk", "google.de", "google.fr",
                "googletagmanager.com", "hao123.com", "hcltech.com", "hotmail.com", "hp.com", "hpe.com", "ibm.com",
                "idealo.de", "ifeng.com", "immobilienscout24.de", "infineon.com", "infosys.com",
                "innovation.thomsonreuters.com", "instagram.com", "intel.com", "intuit.com", "iqiyi.com",
                "ir.thomsonreuters.com", "jd.com", "jesgoo.com", "jobs.thomsonreuters.com", "lamresearch.com",
                "leidos.com", "lenovo.com", "lg.com", "linkedin.com", "liteon.com", "live.com", "mail.ru",
                "mantech.com", "map.baidu.com", "mastercard.us", "mi.com", "micron.com", "microsoft.com", "miui.com",
                "mobile.de", "motorolasolutions.com", "nanya.com", "ncr.com", "nec.com", "net.educause.edu",
                "netflix.com", "ni.com", "nokia.com", "nvidia.com", "nxp.com", "ok.ru", "oki.com", "onsemi.com",
                "oracle.com", "otto.de", "paypal.com", "pegatroncorp.com", "pinterest.de", "plus.google.com",
                "pti.com.tw", "qisda.com", "qq.com", "qualcomm.com", "quantatw.com", "reddit.com", "renesas.com",
                "rohm.com", "salesforce.com", "samsung.com", "sap.com", "satorisoftware.com", "schema.org",
                "seattle.cn", "seattle.gov", "sharp-world.com", "similarweb.com", "sina.cn", "sina.com.cn",
                "site-images.similarcdn.com", "site-images.similarcdn.com", "skhynix.com", "sm.cn", "snap.licdn.com",
                "so.com", "sogou.com", "sohu.com", "sony.com", "soprasteria.com", "spiegel.de", "spil.com.tw", "st.com",
                "support.similarweb.com", "symantec.com", "taobao.com", "tcs.com", "techmahindra.com", "tel.com",
                "telekom.com", "tencent.com", "teradata.com", "thomsonreuters.com", "ti.com", "tieto.com", "tmall.com",
                "t-online.de", "toutiao.com", "tsmc.com", "tsys.com", "tumblr.com", "twitch.tv", "twitter.com",
                "uczzd.cn", "umc.com", "unisys.com", "vk.com", "vmware.com", "vodafone.de", "w3.org/2000/svg", "web.de",
                "weibo.cn", "weibo.com", "welt.de", "wetter.com", "wetteronline.de", "wikipedia.org", "wipro.com",
                "wistron.com", "wjx.cn", "workday.com", "xerox.com", "xiaomi.com", "xnxx.com", "yahoo.com", "yandex.ru",
                "yidianzixun.com", "youku.com", "youtube.com", "zdf.de", "zhihu.com", "zte.com.cn");
    }
}
