package com.tstartup.tserver.service;

import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.google.common.base.Strings;
import com.tstartup.tserver.common.response.ApiResponse;
import com.tstartup.tserver.persistence.dataobject.TArticle;
import com.tstartup.tserver.persistence.dataobject.TArticleTypeRelation;
import com.tstartup.tserver.persistence.service.TArticleService;
import com.tstartup.tserver.persistence.service.TArticleTypeRelationService;
import com.tstartup.tserver.util.CommonThreadExecutor;
import com.tstartup.tserver.util.DateUtil;
import com.tstartup.tserver.web.vo.ArticleUploadInfoVo;
import com.tstartup.tserver.web.vo.SourceItemVo;
import com.tstartup.tserver.web.vo.kimi.KimiChoicesVo;
import com.tstartup.tserver.web.vo.kimi.KimiChatResponseVo;
import com.tstartup.tserver.web.vo.kimi.KimiMessageVo;
import okhttp3.*;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Service
public class ToolBusService {

    @Resource
    private TArticleService tArticleService;

    @Resource
    private TArticleTypeRelationService tArticleTypeRelationService;

    String apiKey = "";
    // API Endpoint
    String url = "https://api.moonshot.cn/v1/chat/completions";

    public ApiResponse genHighlight() {

        // 创建OkHttpClient实例
        // 创建OkHttpClient.Builder实例
        OkHttpClient.Builder builder = new OkHttpClient.Builder();

        // 设置连接超时时间（单位：秒）
        builder.connectTimeout(180, TimeUnit.SECONDS);

        // 设置读取超时时间（单位：秒）
        builder.readTimeout(180, TimeUnit.SECONDS);

        // 设置写入超时时间（单位：秒）
        builder.writeTimeout(180, TimeUnit.SECONDS);

        // 构建OkHttpClient实例
        OkHttpClient client = builder.build();
        // 构建请求体
        MediaType mediaType = MediaType.parse("application/json; charset=utf-8");

        List<TArticle> articleList = tArticleService.list(Wrappers.<TArticle>lambdaQuery().eq(TArticle::getHighlights, ""));
        if (!CollectionUtils.isEmpty(articleList)) {
            for (TArticle tArticle : articleList) {
                CommonThreadExecutor.getInstance().execute(() -> {

                    String source = tArticle.getSource();

                    SourceItemVo sourceItemVo = JSON.parseObject(source, SourceItemVo.class);
                    if (Objects.nonNull(sourceItemVo) && !Strings.isNullOrEmpty(sourceItemVo.getUrl())) {
                        String articleURL = sourceItemVo.getUrl();

                        if (Strings.isNullOrEmpty(articleURL)) {
                            return;
                        }
                        String jsonBody = "{\"model\":\"moonshot-v1-8k\",\"messages\":[{\"role\":\"system\",\"content\":\"你是 Kimi\"},{\"role\":\"user\",\"content\":\"use seo key words to summarize the content of the url to write a new article and traveller find it useful with at least 10 sentences but less than 1000 words  and no title and others can not question its source "+articleURL+"\"}],\"temperature\":0.3}";
                        RequestBody body = RequestBody.create(jsonBody, mediaType);

                        // 构建请求
                        Request request = new Request.Builder()
                                .url(url)
                                .post(body)
                                .addHeader("Authorization", apiKey)
                                .build();

                        // 发送请求并获取响应
                        try (Response response = client.newCall(request).execute()) {
                            // 检查响应是否成功
                            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

                            // 获取响应体
                            String responseString = response.body().string();
                            if (!Strings.isNullOrEmpty(responseString)) {
                                KimiChatResponseVo responseVo = JSON.parseObject(responseString, KimiChatResponseVo.class);
                                if (Objects.nonNull(responseVo) && !CollectionUtils.isEmpty(responseVo.getChoices())) {
                                    KimiChoicesVo choices = responseVo.getChoices().get(0);
                                    KimiMessageVo kimiMessageVo = choices.getMessage();
                                    String articleContent = kimiMessageVo.getContent();
                                    if (!Strings.isNullOrEmpty(articleContent)) {
                                        tArticle.setHighlights(articleContent);
                                        tArticle.setUpdateTime(DateUtil.getNowSeconds());
                                        tArticleService.updateById(tArticle);
                                    }

                                }

                            }


                            System.out.println(tArticle.getId()+"=========================="+tArticle.getHeadline());
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }

                    }

                });



            }
        }

        return ApiResponse.newSuccess();
    }


    public ApiResponse uploadArticle() {
        String json ="[{\n" +
                "\t\"headline\": \"Travel Guide\",\n" +
                "\t\"url\": \"https://travel.usnews.com/Phuket_Thailand/\"\n" +
                "}, {\n" +
                "\t\"headline\": \"What to eat, drink and do in Chiang Mai\",\n" +
                "\t\"url\": \"https://www.gourmettraveller.com.au/travel/destinations/what-to-eat-drink-and-do-in-chiang-mai-5655/\"\n" +
                "}, {\n" +
                "\t\"headline\": \"Temples, street food, shopping and nightlife: thrills for all tastes\",\n" +
                "\t\"url\": \"https://www.jetstar.com/jp/en/inspiration/destinations/thailand/central-thailand/bangkok?flight-type=2&adults=1&origin=NRT&destination=BKK\"\n" +
                "}, {\n" +
                "\t\"headline\": \"My Perfect 3 – 5 Day Chiang Mai Itinerary\",\n" +
                "\t\"url\": \"https://www.global-gallivanting.com/chiang-mai-itinerary/\"\n" +
                "}, {\n" +
                "\t\"headline\": \"清迈 10 个值得游览的地方及其他当地景点\",\n" +
                "\t\"url\": \"https://www.thaiairways.com/en_IN/lifestyle_zone/ChiangMai.page\"\n" +
                "}, {\n" +
                "\t\"headline\": \"Chiang Mai introduction\",\n" +
                "\t\"url\": \"https://en.wikivoyage.org/wiki/Chiang_Mai#\"\n" +
                "}, {\n" +
                "\t\"headline\": \"Chiang Mai Trip Blog Diaries From My Year Of Travel\",\n" +
                "\t\"url\": \"https://www.ensquaredaired.com/chiang-mai-trip-blog/\"\n" +
                "}, {\n" +
                "\t\"headline\": \"Explore Chiang Mai Customs and Culture\",\n" +
                "\t\"url\": \"https://www.theakyra.com/blog/thai-customs-and-culture/\"\n" +
                "}, {\n" +
                "\t\"headline\": \"Learn About The History Of Chiang Mai\",\n" +
                "\t\"url\": \"https://www.vietnamstay.com/blog/Learn_About_The_History_Of_Chiang%20Mai\"\n" +
                "}, {\n" +
                "\t\"headline\": \"What to do in Chiang Mai, Thailand\",\n" +
                "\t\"url\": \"https://www.travellikeanna.com/expats-guide-to-chiang-mai/\"\n" +
                "}, {\n" +
                "\t\"headline\": \"Visiting Bangkok: My Suggested 3-5 Day Itinerary\",\n" +
                "\t\"url\": \"https://www.nomadicmatt.com/travel-blogs/visit-bangkok-itinerary/\"\n" +
                "}, {\n" +
                "\t\"headline\": \"Bangkok Travel Blog: Showing Unusual places in the usual city.\",\n" +
                "\t\"url\": \"https://vacaywork.com/bangkok-travel-blog/\"\n" +
                "}, {\n" +
                "\t\"headline\": \"Bangkok, Thailand: A City That Inspired Me To Become a Traveler\",\n" +
                "\t\"url\": \"https://caffeinatedexcursions.com/bangkok-thailand-a-city-that-inspired-me-to-became-a-traveler/\"\n" +
                "}, {\n" +
                "\t\"headline\": \"Travel Diaries: A Foodie’s First Visit to Bangkok\",\n" +
                "\t\"url\": \"https://thefoodiediaries.co/2017/06/22/travel-diaries-a-foodies-first-visit-to-bangkok/\"\n" +
                "}, {\n" +
                "\t\"headline\": \"Phuket History and Culture\",\n" +
                "\t\"url\": \"https://www.jamiesphuketblog.com/2014/04/phuket-no-culture-and-no-history-er.html\"\n" +
                "}, {\n" +
                "\t\"headline\": \"My Adventure in Phuket\",\n" +
                "\t\"url\": \"https://medium.com/one-table-one-world/my-adventure-in-phuket-b036ce2b97b9\"\n" +
                "}, {\n" +
                "\t\"headline\": \"Is Phuket worth visiting in 2025? My personal take\",\n" +
                "\t\"url\": \"https://www.helenabradbury.com/blog-1/is-phuket-worth-visiting\"\n" +
                "}, {\n" +
                "\t\"headline\": \"Five Reasons I Choose to Live in Pattaya, Thailand\",\n" +
                "\t\"url\": \"https://internationalliving.com/five-reasons-i-choose-to-live-in-pattaya-thailand/\"\n" +
                "}, {\n" +
                "\t\"headline\": \"A Foodie's Guide to Pattaya's Best Street Food\",\n" +
                "\t\"url\": \"https://pearlpropertythailand.com/news/a-foodies-guide-to-pattayas-best-street-food\"\n" +
                "}, {\n" +
                "\t\"headline\": \"Exploring Pattaya’s Rich Cultural Tapestry\",\n" +
                "\t\"url\": \"https://battleconquerpattaya.com/exploring-pattayas-rich-cultural-tapestry/\"\n" +
                "}, {\n" +
                "\t\"headline\": \"Pattaya Culture: Unveiling the City’s Rich Heritage\",\n" +
                "\t\"url\": \"https://adventurebackpack.com/pattaya-culture/\"\n" +
                "}, {\n" +
                "\t\"headline\": \"Things to do in Ayutthaya\",\n" +
                "\t\"url\": \"https://www.timetravelturtle.com/thailand/things-to-do-in-ayutthaya/\"\n" +
                "}, {\n" +
                "\t\"headline\": \"Historic City of Ayutthaya\",\n" +
                "\t\"url\": \"https://www.bestpricetravel.com/travel-guide/historic-city-of-ayutthaya.html\"\n" +
                "}, {\n" +
                "\t\"headline\": \"Ayutthaya Kingdom- A Journey Through The History Of The Once Great Empire\",\n" +
                "\t\"url\": \"https://passportsymphony.com/ayutthaya-kingdom/\"\n" +
                "}, {\n" +
                "\t\"headline\": \"South-east Asia: An Adventure\",\n" +
                "\t\"url\": \"https://www.traveldiariesapp.com/en/diary/72d4cce9-12b2-4d93-8e22-0bbb9c4f206a/chapter/766c9f3e-233b-4990-8bc9-4b512bc42fc7\"\n" +
                "}, {\n" +
                "\t\"headline\": \"10 Things to Know Before You Go to Chiang Rai\",\n" +
                "\t\"url\": \"https://roadsandkingdoms.com/2024/10-things-to-know-before-you-go-to-chiang-rai/\"\n" +
                "}, {\n" +
                "\t\"headline\": \"Chiang Rai Unveiled: Everything You Need to Know\",\n" +
                "\t\"url\": \"https://www.bohoandsalty.com/blog/chiangraitravelguide/\"\n" +
                "}, {\n" +
                "\t\"headline\": \"About Chiang Rai \",\n" +
                "\t\"url\": \"https://www.cisco.com/web/TH/assets/docs/thpc09_About_Chiang_Rai.pdf\"\n" +
                "}, {\n" +
                "\t\"headline\": \"Chiang Rai Night Market: Food & Souvenirs\",\n" +
                "\t\"url\": \"https://thisgirlabroad.com/chiang-rai-night-market/\"\n" +
                "}, {\n" +
                "\t\"headline\": \"Hua Hin is the best place in Thailand to explore\",\n" +
                "\t\"url\": \"https://www.travelandtourworld.com/news/article/hua-hin-is-best-place-in-thailand-to-explore/\"\n" +
                "}, {\n" +
                "\t\"headline\": \"Our Thailand Family Holiday – Why We Chose Hua Hin\",\n" +
                "\t\"url\": \"https://www.my-travelmonkey.com/thailand-family-holiday/\"\n" +
                "}, {\n" +
                "\t\"headline\": \"Hua Hin's Tasty Tales: Uncovering 13 of Hua Hin Night Market's Unique Street Eats\",\n" +
                "\t\"url\": \"https://www.notquitenigella.com/2024/01/18/hua-hin-night-markets-food/\"\n" +
                "}, {\n" +
                "\t\"headline\": \"Experience Hua Hin’s Rich Cultural Heritage: A Journey Through Time\",\n" +
                "\t\"url\": \"https://star-property-huahin.com/2023/04/11/experience-hua-hins-rich-cultural-heritage-a-journey-through-time/\"\n" +
                "}, {\n" +
                "\t\"headline\": \"Savor the Flavors: A Culinary Journey through Hua Hin\",\n" +
                "\t\"url\": \"https://star-property-huahin.com/2023/04/11/savor-the-flavors-a-culinary-journey-through-hua-hin/\"\n" +
                "}, {\n" +
                "\t\"headline\": \"Thrill-Seekers Unite: Adventure Awaits in Hua Hin\",\n" +
                "\t\"url\": \"https://star-property-huahin.com/2023/04/11/thrill-seekers-unite-adventure-awaits-in-hua-hin/\"\n" +
                "}, {\n" +
                "\t\"headline\": \"Embracing the Charms of Hua Hin: A Paradise for All Ages\",\n" +
                "\t\"url\": \"https://star-property-huahin.com/2023/04/11/embracing-the-charms-of-hua-hin-a-paradise-for-all-ages/\"\n" +
                "}, {\n" +
                "\t\"headline\": \"Koh Samui Unveiled: A Tapestry of Nature and Culture\",\n" +
                "\t\"url\": \"https://press.fourseasons.com/kohsamui/trending-now/interesting-facts-about-koh-samui/\"\n" +
                "}, {\n" +
                "\t\"headline\": \"Delicious Southern Thai Food in Koh Samui\",\n" +
                "\t\"url\": \"https://www.eatingthaifood.com/restaurants/thai-food-in-koh-samui/\"\n" +
                "}, {\n" +
                "\t\"headline\": \"Koh Samui: The heart and soul of Thailand’s Coconut Island\",\n" +
                "\t\"url\": \"https://www.groupbanyan.com/stories/koh-samui-discover-heart-and-soul-thailand-coconut-island\"\n" +
                "}, {\n" +
                "\t\"headline\": \"Cultural Tourism and Creative Explorations in Samui\",\n" +
                "\t\"url\": \"https://www.linkedin.com/pulse/cultural-tourism-creative-explorations-samui-wathsala-thalpe-3yiqc\"\n" +
                "}, {\n" +
                "\t\"headline\": \"Koh Samui Is the Next Setting of ‘The White Lotus’—But It’s Always Been a Special Place for Thai Travelers\",\n" +
                "\t\"url\": \"https://www.cntraveler.com/story/koh-samui-thailand-white-lotus\"\n" +
                "}, {\n" +
                "\t\"headline\": \"Food Market Tour @ Krabi Night Market, Krabi, Thailand\",\n" +
                "\t\"url\": \"https://theoccasionalgourmand.blogspot.com/2017/09/amazing-thai-street-food-at-krabi-night.html\"\n" +
                "}, {\n" +
                "\t\"headline\": \"History & Culture in Krabi\",\n" +
                "\t\"url\": \"https://www.flamingotravels.net/world-travel-guide/thailand-travel-guide/krabi-travel-guide/history-and-culture-of-krabi\"\n" +
                "}, {\n" +
                "\t\"headline\": \"Crappy in Krabi - How We Didn't Enjoy The Thai Paradise\",\n" +
                "\t\"url\": \"https://www.tuljak.com/blog/crappy-in-krabi-how-we-didnt-enjoy-the-thai-paradise\"\n" +
                "}, {\n" +
                "\t\"headline\": \"Is Krabi Worth Visiting? 10 Reasons to Visit Krabi in 2024\",\n" +
                "\t\"url\": \"https://feelfreetravel.com/blog/thailand/is-krabi-worth-visiting/\"\n" +
                "}, {\n" +
                "\t\"headline\": \"Krabi, Thailand-Stranded Around The World\",\n" +
                "\t\"url\": \"https://strandedaroundtheworld.com/2013/12/07/krabi/\"\n" +
                "}, {\n" +
                "\t\"headline\": \"Travel to Kanchanaburi: A Nostalgic Day Trip to Death Railway\",\n" +
                "\t\"url\": \"https://thebear.travel/84/Travel-to-Kanchanaburi:-A-Nostalgic-Day-Trip-to-Death-Railway\"\n" +
                "}, {\n" +
                "\t\"headline\": \"Exploring the beauty and history of Kanchanaburi\",\n" +
                "\t\"url\": \"https://www.lopesan.com/blog/en/destinos/exploring-the-beauty-and-history-of-kanchanaburi/\"\n" +
                "}, {\n" +
                "\t\"headline\": \"Discover the best of Kanchanaburi: waterfalls, history, and culture\",\n" +
                "\t\"url\": \"https://thethaiger.com/guides/discover-the-best-of-kanchanaburi-waterfalls-history-and-culture\"\n" +
                "}, {\n" +
                "\t\"headline\": \"Kanchanaburi - travel journey\",\n" +
                "\t\"url\": \"https://jimsea13.substack.com/p/kanchanaburi-part-2?utm_campaign=post&utm_medium=web\"\n" +
                "}, {\n" +
                "\t\"headline\": \"Travel to the Past at Sukhothai – the Land of History\",\n" +
                "\t\"url\": \"https://www.tourismthailand.org/Articles/https-www-tourismthailand-org-articles-travel-to-the-past-at-sukhothai-the-land-of-history\"\n" +
                "}, {\n" +
                "\t\"headline\": \"The Dawn of Happiness: The Historic Town of Sukhothai\",\n" +
                "\t\"url\": \"https://www.kf.or.kr/achNewsletter/mgzinSubViewPage.do?mgzinSn=15821&mgzinSubSn=26067&langTy=ENG\"\n" +
                "}, {\n" +
                "\t\"headline\": \"Sukhothai Travel Diary\",\n" +
                "\t\"url\": \"https://www.hithaonthego.com/travel-diary-sukhothai/\"\n" +
                "}, {\n" +
                "\t\"headline\": \"Exploring the Timeless Wonders of Sukhothai, Thailand: A Comprehensive Travel Guide\",\n" +
                "\t\"url\": \"https://www.explorenique.com/exploring-the-timeless-wonders-of-sukhothai-thailand-a-comprehensive-travel-guide/\"\n" +
                "}, {\n" +
                "\t\"headline\": \"TRAVEL DIARY OF SUKHOTHAI\",\n" +
                "\t\"url\": \"https://travelwithuj.com/2022/08/12/travel-diary-of-sukhothai-day-1/\"\n" +
                "}, {\n" +
                "\t\"headline\": \"The Ancient Echoes of Sukhothai\",\n" +
                "\t\"url\": \"https://jaytindall.asia/story/the-ancient-echoes-of-sukhothai/\"\n" +
                "}, {\n" +
                "\t\"headline\": \"Pai: Thailand’s Mountain Backpacker Paradise (or Hell?)\",\n" +
                "\t\"url\": \"https://www.nomadicmatt.com/travel-blogs/backpacking-pai-thailand/\"\n" +
                "}, {\n" +
                "\t\"headline\": \"This Small Town in Thailand Has Some of the Country's Most Beautiful Landscapes — How to Visit\",\n" +
                "\t\"url\": \"https://www.travelandleisure.com/pai-thailand-small-town-8727308\"\n" +
                "}, {\n" +
                "\t\"headline\": \"Let's live in Goa...\",\n" +
                "\t\"url\": \"https://www.traveldiariesapp.com/en/diary/9b2ebcda-b27b-4e82-9379-9a3c2ac07a8c/chapter/52aecf20-c864-4f8f-af9d-4206df742995\"\n" +
                "}, {\n" +
                "\t\"headline\": \"Thailand Travel Diary\",\n" +
                "\t\"url\": \"https://carinagoesglobal.com/the-backpacker-hotspot-pai/\"\n" +
                "}, {\n" +
                "\t\"headline\": \"Overeating in Pai\",\n" +
                "\t\"url\": \"http://wevemadeahugemistake.com/overeating-in-pai-cakes-pies-coffees/\"\n" +
                "}, {\n" +
                "\t\"headline\": \"Navigating the Twists to Discover Pai’s Hidden Treasures\",\n" +
                "\t\"url\": \"https://www.peek.com/pai-mae-hong-son-thailand/r086wr/navigating-the-twists-to-discover-pais-hidden-treasures/ar0r7wa8\"\n" +
                "}, {\n" +
                "\t\"headline\": \"Pai – The Backpacking Paradise in Thailand\",\n" +
                "\t\"url\": \"https://tripways.com/b/pai-thailand/\"\n" +
                "}, {\n" +
                "\t\"headline\": \"Chiang Khan introduction\",\n" +
                "\t\"url\": \"https://en.wikivoyage.org/wiki/Chiang_Khan\"\n" +
                "}, {\n" +
                "\t\"headline\": \"Nothing But Chill in Chiang Khan, Loei\",\n" +
                "\t\"url\": \"https://www.tourismthailand.org/Articles/nothing-but-chill-in-chiang-khan-loei\"\n" +
                "}, {\n" +
                "\t\"headline\": \"Chiang Khan in 2 days: a welcome dose of serene life in north-east Thailand\",\n" +
                "\t\"url\": \"https://berlintobangkok.com/chiang-khan-travel-guide-and-chiang-khan-skywalk/\"\n" +
                "}, {\n" +
                "\t\"headline\": \"Chiang Khan Walking Street – a great night market\",\n" +
                "\t\"url\": \"https://itsbetterinthailand.com/activities/chiang-khan-walking-street/\"\n" +
                "}, {\n" +
                "\t\"headline\": \"Chiang Khan: Culture, Cuisine, Serenity\",\n" +
                "\t\"url\": \"https://www.bangkokbigears.com/2024/08/24/chiang-khan-culture-cuisine-serenity/\"\n" +
                "}, {\n" +
                "\t\"headline\": \"The Chiang Khan Culture, Countryside and Riverside\",\n" +
                "\t\"url\": \"https://www.thailand-alunrhys.com/chiang-khan-culture-countryside\"\n" +
                "}, {\n" +
                "\t\"headline\": \"Chiang Khan – Wooden Houses and Monk Alms\",\n" +
                "\t\"url\": \"https://walkaboutmonkey.com/destinations/thailand/chiang-khan-wooden-houses-and-monk-alms/\"\n" +
                "}, {\n" +
                "\t\"headline\": \"Food and drink in Chiang Khan\",\n" +
                "\t\"url\": \"https://www.travelfish.org/eatandmeet/thailand/northeast_thailand/loei/chiang_khan/eat\"\n" +
                "}, {\n" +
                "\t\"headline\": \"Culinary Adventures in Rayong: On the Seafood Trail\",\n" +
                "\t\"url\": \"https://guide.michelin.com/th/en/article/features/culinary-adventures-in-rayong-on-the-seafood-trail\"\n" +
                "}, {\n" +
                "\t\"headline\": \"Exploring Star Night Bazaar Rayong, the Largest Food Market in Rayong\",\n" +
                "\t\"url\": \"https://www.feastographyblog.com/blog/star-night-bazaar-market-rayong\"\n" +
                "}, {\n" +
                "\t\"headline\": \"Rayong: A Guide to Visiting the Beauty of the Eastern Seaboard\",\n" +
                "\t\"url\": \"https://thebear.travel/139/Rayong:-A-Guide-to-Visiting-the-Beauty-of-the-Eastern-Seaboard\"\n" +
                "}, {\n" +
                "\t\"headline\": \"Rayong, Its History, and the Exotic Tale of Thai Adventures\",\n" +
                "\t\"url\": \"https://www.centarahotelsresorts.com/journal/rayong-history-and-the-exotic-tale-of-thai-adventures\"\n" +
                "}, {\n" +
                "\t\"headline\": \"From tourist to non-tourist towns (six months in Rayong)\",\n" +
                "\t\"url\": \"https://lanivcox.com/2019/06/14/from-tourist-to-non-tourist-towns-six-months-in-rayong/\"\n" +
                "}, {\n" +
                "\t\"headline\": \"Things to do in Rayong\",\n" +
                "\t\"url\": \"https://www.novotelrayongstarconventioncentre.com/things-to-do/\"\n" +
                "}, {\n" +
                "\t\"headline\": \"Surat Thani, the charming city of hundred islands\",\n" +
                "\t\"url\": \"https://www.oh-hoo.com/articles/surat-thani-the-charming-city-of-hundred-islands\"\n" +
                "}, {\n" +
                "\t\"headline\": \"Surat Thani | Unspoiled Canals and A Vibrant Night Market\",\n" +
                "\t\"url\": \"https://thailandtidbits.com/2021/04/27/things-to-do-in-surat-thani/\"\n" +
                "}, {\n" +
                "\t\"headline\": \"A Traveler’s Guide to Exploring Surat Thani\",\n" +
                "\t\"url\": \"https://houseofcoco.net/a-travelers-guide-to-exploring-surat-thani/\"\n" +
                "}, {\n" +
                "\t\"headline\": \"Chaiya National Museum: Exploring Southern Thai Heritage in Surat Thani\",\n" +
                "\t\"url\": \"https://thebear.travel/455/Chaiya-National-Museum:-Exploring-Southern-Thai-Heritage-in-Surat-Thani\"\n" +
                "}, {\n" +
                "\t\"headline\": \"Impressive Destinations\",\n" +
                "\t\"url\": \"https://flythaismiles.com/Destinations/Surat-Thani\"\n" +
                "}, {\n" +
                "\t\"headline\": \"Food and drink in Surat Thani\",\n" +
                "\t\"url\": \"https://www.travelfish.org/eatandmeet/thailand/southern_thailand/surat_thani/surat_thani/eat\"\n" +
                "}, {\n" +
                "\t\"headline\": \"The Highlights of Surat Thani Province, Thailand\",\n" +
                "\t\"url\": \"https://www.xyzasia.com/home/highlights-of-surat-thani-province-thailand\"\n" +
                "}, {\n" +
                "\t\"headline\": \"Traditional Thai Silk and Pottery from Khorat\",\n" +
                "\t\"url\": \"https://www.wipo.int/en/web/ip-advantage/w/stories/traditional-thai-silk-and-pottery-from-khorat\"\n" +
                "}, {\n" +
                "\t\"headline\": \"Travel to Nakhon Ratchasima (Korat) Guide: Everything You Need to Know\",\n" +
                "\t\"url\": \"https://thebear.travel/522/Travel-to-Nakhon-Ratchasima-%28Korat%29-Guide:-Everything-You-Need-to-Know\"\n" +
                "}, {\n" +
                "\t\"headline\": \"Exploring the rich culture of Korat Thailand\",\n" +
                "\t\"url\": \"https://www.travelauthenticasia.com/blog/thailand-adventures/exploring-the-rich-culture-of-korat-thailand.aspx\"\n" +
                "}, {\n" +
                "\t\"headline\": \"5 Cool Things to Do in Korat (Nakhon Ratchasima), Thailand\",\n" +
                "\t\"url\": \"https://www.centarahotelsresorts.com/centarareserve/journal/5-Cool-Things-to-Do-in-Korat-Nakhon-Ratchasima-Thailand\"\n" +
                "}, {\n" +
                "\t\"headline\": \"Korat’s spectacular Phimai Festival lights up historical park\",\n" +
                "\t\"url\": \"https://www.nationthailand.com/life/travel/40043095\"\n" +
                "}, {\n" +
                "\t\"headline\": \"Exploring Nakhon Ratchasima: Gateway to Isaan\",\n" +
                "\t\"url\": \"https://evendo.com/locations/thailand/nakhon-ratchasima?currency=AUD\"\n" +
                "}, {\n" +
                "\t\"headline\": \"Udon Thani: Living in a Thai Village\",\n" +
                "\t\"url\": \"https://wanderwisdom.com/travel-destinations/ThaiVillage-Greensleeves\"\n" +
                "}, {\n" +
                "\t\"headline\": \"Udon Thani Travel Guide\",\n" +
                "\t\"url\": \"https://jp.hotels.com/go/thailand/udon-thani-travel-guide\"\n" +
                "}, {\n" +
                "\t\"headline\": \"Udon Thani introduction\",\n" +
                "\t\"url\": \"https://en.wikipedia.org/wiki/Udon_Thani\"\n" +
                "}, {\n" +
                "\t\"headline\": \"25 Things to Do in Udon Thani\",\n" +
                "\t\"url\": \"https://nakaravillasandglamping.com/25-things-to-do-in-udon-thani/\"\n" +
                "}, {\n" +
                "\t\"headline\": \"Food and drink in Udon Thani\",\n" +
                "\t\"url\": \"https://www.travelfish.org/eatandmeet/thailand/northeast_thailand/udon_thani/udon_thani/eat\"\n" +
                "}, {\n" +
                "\t\"headline\": \"A 5000-year-old culture thrives in Ban Chiang village at Udon Thani, Thailand\",\n" +
                "\t\"url\": \"http://www.theothersideforever.com/5000-year-old-culture-thrives-ban-chiang-village-udon-thani-thailand/\"\n" +
                "}, {\n" +
                "\t\"headline\": \"Ubon Ratchathani: Two Rivers Run Through It\",\n" +
                "\t\"url\": \"https://thailandinsider.com/destination/ubon-ratchathani/\"\n" +
                "}, {\n" +
                "\t\"headline\": \"Ubon Ratchathani, Thailand’s “Grand” Canyon\",\n" +
                "\t\"url\": \"https://weblogtheworld.com/countries/asia/thailand/ubon-ratchathani\"\n" +
                "}, {\n" +
                "\t\"headline\": \"Tasting Thailand: Vegetarian Festival in Ubon Ratchathani\",\n" +
                "\t\"url\": \"https://www.wanderlustmovement.org/thailand-vegetarian-festival-ubon-ratchathani/\"\n" +
                "}, {\n" +
                "\t\"headline\": \"Experience The Ubon Ratchathani Candle Festival 2024 With VietnamStay Travel\",\n" +
                "\t\"url\": \"https://www.vietnamstay.com/blog/Experience-the-Ubon-Ratchathani-Candle-Festival-2024-with-VietnamStay-Travel\"\n" +
                "}, {\n" +
                "\t\"headline\": \"Ubon Ratchathani Art & Culture Centara UbonDiscovering the Wonders of Ubon Ratchathani, Thailand\",\n" +
                "\t\"url\": \"https://www.centarahotelsresorts.com/journal/the-wonders-of-ubon-ratchathani\"\n" +
                "}, {\n" +
                "\t\"headline\": \"Ubon Ratchathani introduction\",\n" +
                "\t\"url\": \"https://en.wikipedia.org/wiki/Ubon_Ratchathani\"\n" +
                "}, {\n" +
                "\t\"headline\": \"Songkhla: Past and Present\",\n" +
                "\t\"url\": \"https://www.chiangmai-alacarte.com/songkhla-past-and-present/\"\n" +
                "}, {\n" +
                "\t\"headline\": \"Is Songkhla Worth Visiting?\",\n" +
                "\t\"url\": \"https://thailandstartshere.com/2024/03/13/is-songkhla-worth-visiting/\"\n" +
                "}, {\n" +
                "\t\"headline\": \"\",\n" +
                "\t\"url\": \"https://www.lagunagrandsongkhla.com/experiences/\"\n" +
                "}, {\n" +
                "\t\"headline\": \"Songkhla: our favourite Thai city\",\n" +
                "\t\"url\": \"https://www.findingtheuniverse.com/songkhla-our-favourite-thai-city/\"\n" +
                "}, {\n" +
                "\t\"headline\": \"Songkhla | Thailands Lion City\",\n" +
                "\t\"url\": \"https://thailandtidbits.com/2021/03/11/songkhla-the-pearl-of-the-south/\"\n" +
                "}, {\n" +
                "\t\"headline\": \"The Ultimate Guide to Songkhla, Thailand\",\n" +
                "\t\"url\": \"https://www.northofknown.com/songkhla-thailand-travel-guide/\"\n" +
                "}, {\n" +
                "\t\"headline\": \"Sightseeing in Songkhla\",\n" +
                "\t\"url\": \"https://www.bangkokpost.com/life/travel/2855827/sightseeing-in-songkhla\"\n" +
                "}, {\n" +
                "\t\"headline\": \"Exploring the Beauty of Phang Nga Marine National Park\",\n" +
                "\t\"url\": \"https://www.gvi.co.uk/blog/smb-exploring-the-beauty-of-phang-nga-marine-national-park/\"\n" +
                "}, {\n" +
                "\t\"headline\": \"Our ocean: From freedivers to fish, Phang-nga to Puerto Madryn\",\n" +
                "\t\"url\": \"https://dialogue.earth/en/ocean/our-ocean-from-freedivers-to-fish-phang-nga-to-puerto-madryn/\"\n" +
                "}, {\n" +
                "\t\"headline\": \"Phang Nga Beach: An Oasis of Natural Beauty in Thailand\",\n" +
                "\t\"url\": \"https://www.tourismthailand.org/Destinations/Provinces/Phang-Nga/348\"\n" +
                "}, {\n" +
                "\t\"headline\": \"Phang Nga Beach: An Oasis of Natural Beauty in Thailand\",\n" +
                "\t\"url\": \"https://www.gviusa.com/blog/smb-phang-nga-beach-an-oasis-of-natural-beauty-in-thailand/\"\n" +
                "}, {\n" +
                "\t\"headline\": \"Journey through the Stunning Sea Caves of Phang Nga Bay\",\n" +
                "\t\"url\": \"https://www.gviusa.com/blog/smb-journey-through-the-stunning-sea-caves-of-phang-nga-bay/\"\n" +
                "}, {\n" +
                "\t\"headline\": \"Phang Nga – the island of tranquillity in Thailand\",\n" +
                "\t\"url\": \"https://www.budapesttimes.hu/travel/phang-nga-the-island-of-tranquillity-in-thailand/\"\n" +
                "}, {\n" +
                "\t\"headline\": \"Our Thailand Travel Adventure\",\n" +
                "\t\"url\": \"https://www.traveldiariesapp.com/en/diary/1895740f-068a-4c1b-b889-4136d50870c7/chapter/ecd335d1-0aff-4ae0-8884-15ce1ea725a1\"\n" +
                "}, {\n" +
                "\t\"headline\": \"How to Visit the Incredible Phang Nga Bay & James Bond Island\",\n" +
                "\t\"url\": \"https://voyageinstyle.net/thailand/how-to-visit-phang-nga-bay-and-james-bond-island/\"\n" +
                "}, {\n" +
                "\t\"headline\": \"Food and drink in Phang Nga Town\",\n" +
                "\t\"url\": \"https://www.travelfish.org/eatandmeet/thailand/southern_thailand/phang_nga/phang_nga_town/eat\"\n" +
                "}, {\n" +
                "\t\"headline\": \"Discover Thailand’s Spiritual Side at Phang Nga’s Most Beautiful Temples\",\n" +
                "\t\"url\": \"https://www.sarojin.com/blog/discover-phang-nga-temples/\"\n" +
                "}, {\n" +
                "\t\"headline\": \"Ko Phi Phi: The Most Overrated Island in Thailand\",\n" +
                "\t\"url\": \"https://www.nomadicmatt.com/travel-blogs/ko-phi-phi/\"\n" +
                "}, {\n" +
                "\t\"headline\": \"Koh Phi Phi Island is Beautiful (And a Bit Dangerous)\",\n" +
                "\t\"url\": \"https://www.adventurouskate.com/ko-phi-phi-is-very-beautiful-and-may-kill-you/\"\n" +
                "}, {\n" +
                "\t\"headline\": \"First time in Koh Phi Phi\",\n" +
                "\t\"url\": \"https://www.doseoflife.com/first-time-visiting-koh-phi-phi/\"\n" +
                "}, {\n" +
                "\t\"headline\": \"Koh Phi Phi Travel Guide: Discovering Thailand’s Beautiful Beach Paradise\",\n" +
                "\t\"url\": \"https://asiapioneertravel.com/blog/koh-phi-phi-travel-guide/\"\n" +
                "}, {\n" +
                "\t\"headline\": \"Thailand Travel Journal Part 1: An Introduction to Koh Phi Phi\",\n" +
                "\t\"url\": \"https://countryandahalf.com/thailand-travel-journal-part-1-introduction-ko-phi-phi/\"\n" +
                "}, {\n" +
                "\t\"headline\": \"Koh Phi Phi: The One Hit Wonder\",\n" +
                "\t\"url\": \"https://www.addieweller.com/blog/koh-phi-phi-thailand\"\n" +
                "}, {\n" +
                "\t\"headline\": \"Having the Time of My Life in Phi Phi\",\n" +
                "\t\"url\": \"https://www.7continents1passport.com/time-life-phi-phi/\"\n" +
                "}, {\n" +
                "\t\"headline\": \"Food and drink in Ko Phi Phi\",\n" +
                "\t\"url\": \"https://www.travelfish.org/eatandmeet/thailand/southern_thailand/krabi/ko_phi_phi/eat\"\n" +
                "}, {\n" +
                "\t\"headline\": \"Lessons from Hat Yai\",\n" +
                "\t\"url\": \"https://www.nationthailand.com/blogs/life/travel/40042356\"\n" +
                "}, {\n" +
                "\t\"headline\": \"Notes on Hat Yai: Thailand’s laid-back southern city\",\n" +
                "\t\"url\": \"https://www.nomadicnotes.com/notes-on-hat-yai/\"\n" +
                "}, {\n" +
                "\t\"headline\": \"Most Amazing Islands Off the Coast of Hat Yai\",\n" +
                "\t\"url\": \"https://www.centarahotelsresorts.com/journal/most-amazing-islands-off-the-coast-of-hat-yai\"\n" +
                "}, {\n" +
                "\t\"headline\": \"17 Awesome Things to Do in Hat Yai, Thailand in 2024\",\n" +
                "\t\"url\": \"https://barefootcaribou.com/things-to-do-in-hat-yai/\"\n" +
                "}, {\n" +
                "\t\"headline\": \"Hat Yai Introduction\",\n" +
                "\t\"url\": \"https://bangkokattractions.com/hat-yai/\"\n" +
                "}, {\n" +
                "\t\"headline\": \"Food and drink in Hat Yai\",\n" +
                "\t\"url\": \"https://www.travelfish.org/eatandmeet/thailand/southern_thailand/songkhla/hat_yai/eat\"\n" +
                "}, {\n" +
                "\t\"headline\": \"Songkran Water Festival Hat Yai Style\",\n" +
                "\t\"url\": \"https://www.theislanddrum.com/songkran-in-hat-yai-thailand/\"\n" +
                "}, {\n" +
                "\t\"headline\": \"Why Is Hat Yai Famous?\",\n" +
                "\t\"url\": \"https://www.drivecarrental.com/blog/2024/09/25/why-is-hat-yai-famous/\"\n" +
                "}, {\n" +
                "\t\"headline\": \"The Vegetarian Festival in Hat-Yai, Thailand\",\n" +
                "\t\"url\": \"https://ibc.ac.th/en/node/3277\"\n" +
                "}, {\n" +
                "\t\"headline\": \"Ratchaburi’s Nasatta Park is all lit up for the cool season\",\n" +
                "\t\"url\": \"https://www.nationthailand.com/blogs/life/art-culture/40043474\"\n" +
                "}, {\n" +
                "\t\"headline\": \"Unveiling Ratchaburi: A Hidden Gem Beyond Bangkok’s Bustle\",\n" +
                "\t\"url\": \"https://www.peek.com/bangkok-bangkok-thailand/r06gb47/unveiling-ratchaburi-a-hidden-gem-beyond-bangkoks-bustle/ar0jxry5n\"\n" +
                "}, {\n" +
                "\t\"headline\": \"Nasatta Light Festival 2025 in Ratchaburi, Thailand: A New Spectacle of Lights, Culture, and Cuisine for Global Travellers\",\n" +
                "\t\"url\": \"https://www.travelandtourworld.cn/news/article/nasatta-light-festival-2025-in-ratchaburi-thailand-a-new-spectacle-of-lights-culture-and-cuisine-for-global-travellers/\"\n" +
                "}, {\n" +
                "\t\"headline\": \"Top Things to Do in Ratchaburi, Thailand\",\n" +
                "\t\"url\": \"https://www.takemetour.com/amazing-thailand-go-local/things-to-do-in-ratchaburi-thailand/\"\n" +
                "}, {\n" +
                "\t\"headline\": \"The great temples and culture of Ratchaburi\",\n" +
                "\t\"url\": \"https://blogthaitourismguide.wordpress.com/2013/02/15/the-great-temples-and-culture-of-ratchaburi/\"\n" +
                "}, {\n" +
                "\t\"headline\": \"A Day In Ratchaburi: Savour The Flavours In The Land Of The Thai-Chinese-Mon\",\n" +
                "\t\"url\": \"https://guide.michelin.com/th/en/article/features/a-day-in-ratchaburi-savour-the-flavours-in-the-land-of-the-thai-chinese-mon\"\n" +
                "}, {\n" +
                "\t\"headline\": \"Precious Ratchaburi\",\n" +
                "\t\"url\": \"https://www.bangkokpost.com/life/travel/417398/precious-ratchaburi\"\n" +
                "}, {\n" +
                "\t\"headline\": \"The colorful Damnoen Saduak floating market in Ratchaburi\",\n" +
                "\t\"url\": \"https://www.livetheworld.com/post/the-colorful-damnoen-saduak-floating-market-in-ratchaburi-79st\"\n" +
                "}, {\n" +
                "\t\"headline\": \"13 Fantastic Things to Do in Ratchaburi, Thailand\",\n" +
                "\t\"url\": \"https://www.bucketlistly.blog/posts/ratchaburi-things-to-do-backpacking\"\n" +
                "}, {\n" +
                "\t\"headline\": \"Cultural Exploration around Ratchaburi\",\n" +
                "\t\"url\": \"https://singaporetripguide.com/culture/cultural-exploration-around-ratchaburi\"\n" +
                "}, {\n" +
                "\t\"headline\": \"Notes on Khon Kaen – The Isan city with big ambitions\",\n" +
                "\t\"url\": \"https://www.nomadicnotes.com/notes-on-khon-kaen/\"\n" +
                "}, {\n" +
                "\t\"headline\": \"History & Culture\",\n" +
                "\t\"url\": \"https://www.destinationsapart.com/information/thailand/khon-kaen/history-culture/\"\n" +
                "}, {\n" +
                "\t\"headline\": \"Khon Kaen : The Beautiful City in Isan, Thailand that Is Rich in Culture and History\",\n" +
                "\t\"url\": \"https://en.skyticket.jp/guide/3433\"\n" +
                "}, {\n" +
                "\t\"headline\": \"How to Explore Khon Kaen's Festivals and Traditions: Celebrating Culture and Unity\",\n" +
                "\t\"url\": \"https://vocal.media/styled/how-to-explore-khon-kaen-s-festivals-and-traditions-celebrating-culture-and-unity\"\n" +
                "}, {\n" +
                "\t\"headline\": \"Khon Kaen: A City of Unknown Wonders\",\n" +
                "\t\"url\": \"https://earthdrifter.com/khon-kaen-a-city-of-unknown-wonders/\"\n" +
                "}, {\n" +
                "\t\"headline\": \"Food and drink in Khon Kaen\",\n" +
                "\t\"url\": \"https://www.travelfish.org/eatandmeet/thailand/northeast_thailand/khon_kaen/khon_kaen/eat\"\n" +
                "}, {\n" +
                "\t\"headline\": \"Experience the Cultural Diversity of Khon Kaen’s Festivals\",\n" +
                "\t\"url\": \"https://www.thevacationgateway.com/experience-the-cultural-diversity-of-khon-kaens-festivals.html\"\n" +
                "}, {\n" +
                "\t\"headline\": \"Charmed by a City Off Thailand’s Beaten Path\",\n" +
                "\t\"url\": \"https://www.nytimes.com/2024/03/04/travel/lampang-thailand-vacation.html\"\n" +
                "}, {\n" +
                "\t\"headline\": \"Entdecken Sie die historische Stadt Lampang\",\n" +
                "\t\"url\": \"https://blog.asiaventura.de/entdecken-sie-die-historische-stadt-lampang\"\n" +
                "}, {\n" +
                "\t\"headline\": \"Is Lampang the Last Paradise in Thailand?\",\n" +
                "\t\"url\": \"https://medium.com/nomadicwandering/is-lampang-the-last-paradise-in-thailand-90e068173c8c\"\n" +
                "}, {\n" +
                "\t\"headline\": \"Lampang Guide - All you need to know\",\n" +
                "\t\"url\": \"https://www.bestpricetravel.com/travel-guide/lampang.html\"\n" +
                "}, {\n" +
                "\t\"headline\": \"Living history in Lampang\",\n" +
                "\t\"url\": \"https://www.tatnews.org/2019/09/living-history-in-lampang/\"\n" +
                "}, {\n" +
                "\t\"headline\": \"A province of temples, old teak houses and stunning woodcarvings, Lampang basks in the slow life\",\n" +
                "\t\"url\": \"https://www.nationthailand.com/thai-destination/30306374\"\n" +
                "}, {\n" +
                "\t\"headline\": \"Lampang: A Hidden Gem of Northern Thailand\",\n" +
                "\t\"url\": \"https://www.tripoto.com/thailand/places-to-visit/lampang\"\n" +
                "}, {\n" +
                "\t\"headline\": \"From Lampang With Love\",\n" +
                "\t\"url\": \"https://thailandstartshere.com/2022/01/25/is-lampang-worth-visiting/\"\n" +
                "}, {\n" +
                "\t\"headline\": \"10 Places To Visit In Phitsanulok: Where To Go And What To See When In Thailand!\",\n" +
                "\t\"url\": \"https://traveltriangle.com/blog/places-to-visit-in-phitsanulok/\"\n" +
                "}, {\n" +
                "\t\"headline\": \"The City of Two Rivers, Phitsanulok\",\n" +
                "\t\"url\": \"https://www.scb.co.th/en/personal-banking/stories/life-style/travel-phitsanulok.html\"\n" +
                "}, {\n" +
                "\t\"headline\": \"History of Phitsanulok province\",\n" +
                "\t\"url\": \"https://en.wikipedia.org/wiki/History_of_Phitsanulok_province\"\n" +
                "}, {\n" +
                "\t\"headline\": \"Savor the Spice: A Food Lover’s Guide to Khon Kaen’s Cuisine\",\n" +
                "\t\"url\": \"https://www.agoda.com/travel-guides/thailand/khon-kaen/savor-the-spice-a-food-lovers-guide-to-khon-kaens-cuisine/\"\n" +
                "}, {\n" +
                "\t\"headline\": \"10 THINGS TO DO IN KHON KAEN\",\n" +
                "\t\"url\": \"https://www.tourismthailand.org/Articles/10-things-to-do-in-khon-kaen\"\n" +
                "}, {\n" +
                "\t\"headline\": \"A Guide for Unveiling Khon Kaen - Offbeat Adventures and Local Delights\",\n" +
                "\t\"url\": \"https://www.linkedin.com/pulse/guide-unveiling-khon-kaen-offbeat-adventures-local-delights-thalpe-mknwc\"\n" +
                "}, {\n" +
                "\t\"headline\": \"Lopburi, Thailand: A Guide to the City of Monkeys\",\n" +
                "\t\"url\": \"https://www.placesofjuma.com/lopburi-thailand/\"\n" +
                "}, {\n" +
                "\t\"headline\": \"Fear and Loathing in Lopburi\",\n" +
                "\t\"url\": \"https://allpointseast.com/blog/thailand/fear-and-loathing-in-lopburi/\"\n" +
                "}, {\n" +
                "\t\"headline\": \"No more monkey business: Thai city’s macaques to be put in enclosures\",\n" +
                "\t\"url\": \"https://www.theguardian.com/world/2024/apr/05/no-more-monkey-business-thai-lopburi-macaques-to-be-rounded-up-and-put-in-enclosures\"\n" +
                "}, {\n" +
                "\t\"headline\": \"Lop Buri before the monkeying around\",\n" +
                "\t\"url\": \"https://www.bangkokpost.com/opinion/opinion/2803694/lop-buri-before-the-monkeying-around\"\n" +
                "}, {\n" +
                "\t\"headline\": \"Why You Should Visit Lop Buri And Discover The Legend Of The Culinary Arts In Lawo\",\n" +
                "\t\"url\": \"https://guide.michelin.com/th/en/article/features/why-you-should-visit-lopburi-and-discover-the-legend-of-the-culinary-arts-in-lawo\"\n" +
                "}, {\n" +
                "\t\"headline\": \"10 THINGS TO DO IN LOP BURI\",\n" +
                "\t\"url\": \"https://www.tourismthailand.org/Articles/10-things-to-do-in-lop-buri\"\n" +
                "}, {\n" +
                "\t\"headline\": \"Food and drink in Lopburi\",\n" +
                "\t\"url\": \"https://www.travelfish.org/eatandmeet/thailand/central_thailand/lopburi/lopburi/eat\"\n" +
                "}, {\n" +
                "\t\"headline\": \"Lopburi Monkey Banquet Festival - The Most Unique Festival in Thailand\",\n" +
                "\t\"url\": \"https://www.bestpricetravel.com/travel-guide/lopburi-monkey-banquet-festival-the-most-unique-festival-in-thailand-1277.html\"\n" +
                "}, {\n" +
                "\t\"headline\": \"Unveil the Charm of Lopburi: 7 Exciting Reasons to Explore This Quirky Thai Destination\",\n" +
                "\t\"url\": \"https://www.yim-travel.com/post/unveil-the-charm-of-lopburi-7-exciting-reasons-to-explore-this-quirky-thai-destination\"\n" +
                "}, {\n" +
                "\t\"headline\": \"10 THINGS TO DO IN NAKHON PATHOM\",\n" +
                "\t\"url\": \"https://www.tourismthailand.org/Articles/10-things-to-do-in-nakhon-pathom\"\n" +
                "}, {\n" +
                "\t\"headline\": \"Things To Do For Solo Travelers In Nakhon Pathom, Thailand\",\n" +
                "\t\"url\": \"https://www.linkedin.com/pulse/nakhon-pathom-things-do-backpackers-travel-guide-2024-naman-jaloria-wtbff\"\n" +
                "}, {\n" +
                "\t\"headline\": \"What to Do, See and Eat in Nakhon Pathom, Nakhon Chai Si\",\n" +
                "\t\"url\": \"https://www.ohhappybear.com/2019/12/23/what-to-see-eat-do-in-nakhon-pathom/\"\n" +
                "}, {\n" +
                "\t\"headline\": \"Nakhon Pathom Introduction\",\n" +
                "\t\"url\": \"https://www.thai.lt/thailand-travel/destinations/central-thailand/nakhon-pathom\"\n" +
                "}, {\n" +
                "\t\"headline\": \"Food Walk: Nakhon Pathom, The Full-Flavoured Inspiration Of Chef Thanintorn Noom  Chantharawan\",\n" +
                "\t\"url\": \"https://guide.michelin.com/th/en/article/travel/food-walk-nakhon-pathom-the-full-flavoured-inspiration-of-chef-thanintorn-noom-chantharawan\"\n" +
                "}, {\n" +
                "\t\"headline\": \"Nakhon Pathom – The City of Magnificent Culture and Attractions in Thailand\",\n" +
                "\t\"url\": \"https://thailandlocaltravel.com/nakhonpathom-thailand/\"\n" +
                "}, {\n" +
                "\t\"headline\": \"Nakhon Pathom Temple Festival at Wat Phra Pathom Chedi\",\n" +
                "\t\"url\": \"https://www.eatingthaifood.com/restaurants/nakhon-pathom-temple-festival-at-wat-phra-pathom-chedi/\"\n" +
                "}, {\n" +
                "\t\"headline\": \"Chanthaburi, Treasury of The East\",\n" +
                "\t\"url\": \"https://www.scb.co.th/en/personal-banking/stories/life-style/travel-chanthaburi.html\"\n" +
                "}, {\n" +
                "\t\"headline\": \"Chanthaburi Introduction\",\n" +
                "\t\"url\": \"https://www.asiakingtravel.com/attraction/chanthaburi\"\n" +
                "}, {\n" +
                "\t\"headline\": \"Chanthaburi – Off the Beaten Path In Thailand’s Gem City\",\n" +
                "\t\"url\": \"https://www.chowtraveller.com/chanthaburi-thailand/\"\n" +
                "}, {\n" +
                "\t\"headline\": \"5 Wonders of Chanthaburi\",\n" +
                "\t\"url\": \"https://sawasdee.thaiairways.com/5-wonders-chanthaburi/\"\n" +
                "}, {\n" +
                "\t\"headline\": \"Chanthaburi: a stopover between Bangkok and Koh Chang\",\n" +
                "\t\"url\": \"https://travelynnfamily.com/chanthaburi-the-perfect-pit-stop/\"\n" +
                "}, {\n" +
                "\t\"headline\": \"The charm of Chanthaburi\",\n" +
                "\t\"url\": \"https://www.bangkokpost.com/life/travel/2284362/the-charm-of-chanthaburi\"\n" +
                "}, {\n" +
                "\t\"headline\": \"Food and drink in Chanthaburi\",\n" +
                "\t\"url\": \"https://www.travelfish.org/eatandmeet/thailand/eastern_thailand/chanthaburi/chanthaburi/eat\"\n" +
                "}, {\n" +
                "\t\"headline\": \"\",\n" +
                "\t\"url\": \"https://thaiherbkitchen.co.uk/thailand-festivals-traditions-cultures-and-how-to-experience-them/\"\n" +
                "}, {\n" +
                "\t\"headline\": \"Discover the charms of Chanthaburi\",\n" +
                "\t\"url\": \"https://fanclubthailand.co.uk/discover-the-charms-of-chanthaburi/\"\n" +
                "}, {\n" +
                "\t\"headline\": \"The sleepy little town of Phetchaburi, Thailand.\",\n" +
                "\t\"url\": \"https://ikimasho.net/2015/07/01/the-sleepy-little-town-of-phetchaburi-thailand/\"\n" +
                "}, {\n" +
                "\t\"headline\": \"Explore the unique charms of Phetchaburi\",\n" +
                "\t\"url\": \"https://www.bangkokpost.com/life/travel/2229115/explore-the-unique-charms-of-phetchaburi\"\n" +
                "}, {\n" +
                "\t\"headline\": \"Is Phetchaburi Worth Visiting?\",\n" +
                "\t\"url\": \"https://thailandstartshere.com/2024/01/23/is-phetchaburi-worth-visiting/\"\n" +
                "}, {\n" +
                "\t\"headline\": \"The pleasures of phetchaburi\",\n" +
                "\t\"url\": \"https://www.bangkokpost.com/life/social-and-lifestyle/302508/the-pleasures-of-phetchaburi\"\n" +
                "}, {\n" +
                "\t\"headline\": \"10 THINGS TO DO IN PHETCHABURI\",\n" +
                "\t\"url\": \"https://www.tourismthailand.org/Articles/10-things-to-do-in-phetchaburi\"\n" +
                "}, {\n" +
                "\t\"headline\": \"Explore The Phetchaburi In Thailand For Great Hiking Trails And Best Outdoor Camping Experience\",\n" +
                "\t\"url\": \"https://traveltriangle.com/blog/phetchaburi-in-thailand/\"\n" +
                "}, {\n" +
                "\t\"headline\": \"Festivals - Food - Fruits - Sports\",\n" +
                "\t\"url\": \"https://stocks-search.com/siam2553/center/petburi/pet_cult.htm\"\n" +
                "}, {\n" +
                "\t\"headline\": \"Touring Thailand's quiet magnet: the southern province of Trang\",\n" +
                "\t\"url\": \"https://www.cntraveller.com/article/trang-thailand-guide\"\n" +
                "}, {\n" +
                "\t\"headline\": \"Trang Travel Guide\",\n" +
                "\t\"url\": \"https://www.thaiairways.com/tr_TR/lifestyle_zone/Trang.page?\"\n" +
                "}, {\n" +
                "\t\"headline\": \"7 reasons to add Trang to your Thailand itinerary\",\n" +
                "\t\"url\": \"https://fanclubthailand.co.uk/7-reasons-to-add-trang-to-your-thailand-itinerary/\"\n" +
                "}, {\n" +
                "\t\"headline\": \"Discover the Rich History of Trang Old Town: Southern Thailand's Hidden Gem\",\n" +
                "\t\"url\": \"https://www.yim-travel.com/post/discover-the-rich-history-of-trang-old-town-southern-thailand-s-hidden-gem\"\n" +
                "}, {\n" +
                "\t\"headline\": \"Trang: the Thai city obsessed with breakfast\",\n" +
                "\t\"url\": \"https://www.bbc.com/travel/article/20210301-trang-the-thai-city-obsessed-with-breakfast\"\n" +
                "}, {\n" +
                "\t\"headline\": \"The Enchanting Charms of Trang\",\n" +
                "\t\"url\": \"https://evendo.com/locations/thailand/trang\"\n" +
                "}, {\n" +
                "\t\"headline\": \"Koh Tao Travel Guide – Explore paradise in Thailand\",\n" +
                "\t\"url\": \"https://inbetweentravels.com/koh-tao-travel-guide-explore-paradise-in-thailand/\"\n" +
                "}, {\n" +
                "\t\"headline\": \"Koh Tao’s Best Beaches\",\n" +
                "\t\"url\": \"https://goodtimethailand.com/articles/koh-tao-best-beaches/\"\n" +
                "}, {\n" +
                "\t\"headline\": \"Koh Tao: The Island of Death or a Paradise in Thailand?\",\n" +
                "\t\"url\": \"https://nitrokohtao.com/en/blog-es-en/the-island-of-death-or-a-paradise-in-thailand/\"\n" +
                "}, {\n" +
                "\t\"headline\": \"Koh Tao History\",\n" +
                "\t\"url\": \"https://www.islandtravelkohtao.com/koh-tao-information/koh-tao-history\"\n" +
                "}, {\n" +
                "\t\"headline\": \"A Foodie’s Guide to Koh Tao Restaurants.\",\n" +
                "\t\"url\": \"https://www.midnightblueelephant.com/koh-tao-restaurants/\"\n" +
                "}, {\n" +
                "\t\"headline\": \"Koh Tao Festivals and Events\",\n" +
                "\t\"url\": \"https://www.kohtaocompleteguide.com/fact/koh-tao-festivals-annual-events/\"\n" +
                "}, {\n" +
                "\t\"headline\": \"There’s Something About Koh Tao\",\n" +
                "\t\"url\": \"https://www.koktailmagazine.com/2022/03/02/koh-tao/\"\n" +
                "}, {\n" +
                "\t\"headline\": \"Your Complete Guide to Koh Lanta: Island of a Million Eyes\",\n" +
                "\t\"url\": \"https://www.tatacheers.com/travel/your-complete-guide-to-koh-lanta-island-of-a-million-eyes/\"\n" +
                "}, {\n" +
                "\t\"headline\": \"Why Koh Lanta Surprised Me\",\n" +
                "\t\"url\": \"https://thailandstartshere.com/2024/03/18/is-koh-lanta-worth-visiting/\"\n" +
                "}, {\n" +
                "\t\"headline\": \"13 Amazing Things to Do in Koh Lanta, Thailand\",\n" +
                "\t\"url\": \"https://www.neverendingvoyage.com/koh-lanta-guide/\"\n" +
                "}, {\n" +
                "\t\"headline\": \"An island with local character\",\n" +
                "\t\"url\": \"https://www.inthailand.travel/koh-lanta/\"\n" +
                "}, {\n" +
                "\t\"headline\": \"Top 15 Unique Things to Do in Koh Lanta\",\n" +
                "\t\"url\": \"https://www.placesofjuma.com/koh-lanta-thailand/\"\n" +
                "}, {\n" +
                "\t\"headline\": \"Food and drink in Ko Lanta\",\n" +
                "\t\"url\": \"https://www.travelfish.org/eatandmeet/thailand/southern_thailand/krabi/ko_lanta/eat\"\n" +
                "}, {\n" +
                "\t\"headline\": \"Koh Lanta’s Festivals and Celebrations: Embracing Local Culture and Traditions\",\n" +
                "\t\"url\": \"https://www.eventsandfestivalsblog.com/koh-lantas-festivals-and-celebrations-embracing-local-culture-and-traditions.html\"\n" +
                "}, {\n" +
                "\t\"headline\": \"Koh Chang Travel Guide\",\n" +
                "\t\"url\": \"https://www.inthailand.travel/koh-chang/\"\n" +
                "}, {\n" +
                "\t\"headline\": \"Koh Chang — An Island Worth Many Returns\",\n" +
                "\t\"url\": \"https://medium.com/@adventureswithstrawberry/koh-chang-an-island-worth-many-returns-d38f3954b1f2\"\n" +
                "}, {\n" +
                "\t\"headline\": \"Koh Chang - A beautiful island in the Gulf of Thailand\",\n" +
                "\t\"url\": \"https://www.marionandalfred.de/index.php?option=com_content&view=article&id=224:koh-chang&catid=70&Itemid=497\"\n" +
                "}, {\n" +
                "\t\"headline\": \"10 Awesome Things To Do In Koh Chang\",\n" +
                "\t\"url\": \"https://www.guyontheroad.com/blog/things-to-do-in-koh-chang\"\n" +
                "}, {\n" +
                "\t\"headline\": \"Discover the Historical Significance of Ko Chang Memorial\",\n" +
                "\t\"url\": \"https://evendo.com/locations/thailand/koh-chang/landmark/ko-chang-memorial?currency=AUD\"\n" +
                "}, {\n" +
                "\t\"headline\": \"Food and drink in Ko Chang\",\n" +
                "\t\"url\": \"https://www.travelfish.org/eatandmeet/thailand/eastern_thailand/trat/ko_chang/eat\"\n" +
                "}, {\n" +
                "\t\"headline\": \"Koh Chang, Thailand’s big and beautiful paradise island\",\n" +
                "\t\"url\": \"https://www.travelmag.co.uk/2017/03/koh-chang-thailands-big-beautiful-paradise-island/\"\n" +
                "}, {\n" +
                "\t\"headline\": \"16 Awesome Things to Do on Koh Phangan, Thailand\",\n" +
                "\t\"url\": \"https://type1travelling.com/things-to-do-koh-phangan/\"\n" +
                "}, {\n" +
                "\t\"headline\": \"Koh Pha Ngan, Thailand's party island?\",\n" +
                "\t\"url\": \"https://www.inthailand.travel/koh-phangan/\"\n" +
                "}, {\n" +
                "\t\"headline\": \"The Magic of Koh Phangan Beyond the Full Moon Party\",\n" +
                "\t\"url\": \"https://www.global-gallivanting.com/koh-phangan-beyond-the-full-moon-party-an-island-for-yogis-digital-nomads-party-people/?doing_wp_cron=1734597425.7334890365600585937500\"\n" +
                "}, {\n" +
                "\t\"headline\": \"9 Wonderful Things To Experience In Koh Phangan\",\n" +
                "\t\"url\": \"https://littlewanderblog.com/top-9-experiences-in-koh-phangan/\"\n" +
                "}, {\n" +
                "\t\"headline\": \"Culinary Delights: Top 10 Must-Try Local Dishes in Koh Phangan\",\n" +
                "\t\"url\": \"https://www.carrentalsphangan.com/blog/culinary-delights-top-10-must-try-local-dishes-in-koh-phangan.html\"\n" +
                "}, {\n" +
                "\t\"headline\": \"Loy Krathong Festival on Phangan\",\n" +
                "\t\"url\": \"https://phanganist.com/fr/content/loy-krathong-festival-phangan\"\n" +
                "}, {\n" +
                "\t\"headline\": \"Experience the Vibrant Culture of Koh Phangan\",\n" +
                "\t\"url\": \"https://songserm.com/discover/500/experience-the-vibrant-culture-of-koh-phangan\"\n" +
                "}, {\n" +
                "\t\"headline\": \"Nong Khai: More Than Just a Border Town\",\n" +
                "\t\"url\": \"https://sailingstonetravel.com/nong-khai-more-than-just-a-border-town/\"\n" +
                "}, {\n" +
                "\t\"headline\": \"Exploring Nong Khai: A Hidden Gem of Thailand’s Northeast with Stunning Scenery and Rich Culture\",\n" +
                "\t\"url\": \"https://toptouristplaces.in/exploring-nong-khai-a-hidden-gem-of-thailands-northeast-with-stunning-scenery-and-rich-culture/\"\n" +
                "}, {\n" +
                "\t\"headline\": \"Discover the Rich History of Nong Khai Museum\",\n" +
                "\t\"url\": \"https://evendo.com/locations/laos/vientiane/attraction/nong-khai-museum\"\n" +
                "}, {\n" +
                "\t\"headline\": \"Nong Khai Events: Unforgettable Experiences Await You\",\n" +
                "\t\"url\": \"https://adventurebackpack.com/nong-khai-events/\"\n" +
                "}, {\n" +
                "\t\"headline\": \"Nong Khai Experiences: Top 10 Unique Adventures\",\n" +
                "\t\"url\": \"https://adventurebackpack.com/nong-khai-experiences/\"\n" +
                "}]";



        List<ArticleUploadInfoVo> uploadInfoVos = JSON.parseArray(json, ArticleUploadInfoVo.class);

        for (ArticleUploadInfoVo uploadInfoVo : uploadInfoVos) {
            TArticle tArticle = new TArticle();
            tArticle.setHeadline(Objects.isNull(uploadInfoVo.getHeadline()) ? "" : uploadInfoVo.getHeadline());
            tArticle.setIsDelete(0);
            tArticle.setStatus(4);
            tArticle.setCreateTime(DateUtil.getNowSeconds());
            tArticle.setDestCountry(14);
            tArticle.setDestCity(14);
            tArticle.setIsHot(1);

            SourceItemVo sourceItemVo = new SourceItemVo();
            sourceItemVo.setUrl(uploadInfoVo.getUrl());
            sourceItemVo.setMediaType("1");
            tArticle.setSource(JSON.toJSONString(sourceItemVo));

            tArticleService.save(tArticle);

            TArticleTypeRelation relation = new TArticleTypeRelation();
            relation.setTypeId(330);
            relation.setTypeIdentity("articleType");
            relation.setArticleId(tArticle.getId());
            relation.setCreateTime(DateUtil.getNowSeconds());
            tArticleTypeRelationService.save(relation);
        }



        return ApiResponse.newSuccess();
    }
}
