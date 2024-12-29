package com.tstartup.tserver.service;

import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.google.common.base.Strings;
import com.tstartup.tserver.common.response.ApiResponse;
import com.tstartup.tserver.persistence.dataobject.TArticle;
import com.tstartup.tserver.persistence.dataobject.TArticleTypeRelation;
import com.tstartup.tserver.persistence.service.TArticleService;
import com.tstartup.tserver.persistence.service.TArticleTypeRelationService;
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

                String source = tArticle.getSource();

                SourceItemVo sourceItemVo = JSON.parseObject(source, SourceItemVo.class);
                if (Objects.nonNull(sourceItemVo) && !Strings.isNullOrEmpty(sourceItemVo.getUrl())) {
                    String articleURL = sourceItemVo.getUrl();

                    if (Strings.isNullOrEmpty(articleURL)) {
                        continue;
                    }
                    String jsonBody = "{\"model\":\"moonshot-v1-8k\",\"messages\":[{\"role\":\"system\",\"content\":\"你是 Kimi，由 Moonshot AI 提供的人工智能助手，你更擅长中文和英文的对话，你需要对旅游网站的网址内容进行总结，将详细内容介绍给用户\"},{\"role\":\"user\",\"content\":\"请用英文改下一下网址的内容，给出总结，改写总结内容不能太短，太短了请根据内容自行补充下，不少于300单词，不需要类似Certainly, here's a summary of the content from the provided URL in English的这部分 "+articleURL+"\"}],\"temperature\":0.3}";
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

            }
        }

        return ApiResponse.newSuccess();
    }


    public ApiResponse uploadArticle() {
        String json ="[\n" +
                "{\"headline\":\"5 Most Beautiful Islands To Discover In Terengganu\",\"url\":\"https://www.malaysia.travel/explore/5-most-beautiful-islands-to-discover-in-terengganu\"},\n" +
                "{\"headline\":\"5 reasons to visit Kuala Lumpur\",\"url\":\"https://www.jetstar.com/au/en/inspiration/articles/malaysia-kuala-lumpur-sights\"},\n" +
                "{\"headline\":\"The Real Reason I Don’t Want to Visit Kuala Lumpur Again\",\"url\":\"https://www.skyetravels.com/visit-kuala-lumpur/\"},\n" +
                "{\"headline\":\"Kuala Lumpur: The Asian City Surrounded by Waterfalls\",\"url\":\"https://www.going.com/guides/kuala-lumpur\"},\n" +
                "{\"headline\":\"History of Kuala Lumpur\",\"url\":\"https://en.wikipedia.org/wiki/History_of_Kuala_Lumpur\"},\n" +
                "{\"headline\":\"Travel Diary: Malaysia\",\"url\":\"https://www.amaniiphotography.co.uk/blog/travel-malaysia/\"},\n" +
                "{\"headline\":\"Backpacking diary - Wandering a round Kuala Lumpur – Malaysia\",\"url\":\"https://www.linkedin.com/pulse/backpacking-diary-wandering-round-kuala-lumpur-malaysia-haru-akina?trk=mp-reader-card\"},\n" +
                "{\"headline\":\"History & Culture in Kuala Lumpur\",\"url\":\"https://www.flamingotravels.net/world-travel-guide/malaysia-travel-guide/kuala-lumpur-travel-guide/history-and-culture-of-kuala-lumpur\"},\n" +
                "{\"headline\":\"Exploring Kuala Lumpur: A Dance with History and Culture\",\"url\":\"https://www.peek.com/kuala-lumpur-federal-territory-of-kuala-lumpur-malaysia/r0pxyy/exploring-kuala-lumpur-a-dance-with-history-and-culture/ar04rgm5\"},\n" +
                "{\"headline\":\"WHAT TO EAT IN KUALA LUMPUR: KUALA LUMPUR FOOD DIARY!\",\"url\":\"https://eatandtreats.blogspot.com/2016/06/what-to-eat-in-kuala-lumpur-kuala.html\"},\n" +
                "{\"headline\":\"KL Food Trip: The Amazing Local Food of Kuala Lumpur\",\"url\":\"https://www.zoytotheworld.com/blog/malaysia-kuala-lumpur-food-trip-what-to-eat\"},\n" +
                "{\"headline\":\"What to Buy in Kuala Lumpur for a Souvenir of Your Trip\",\"url\":\"https://www.traveloka.com/en-en/explore/destination/what-to-buy-in-kuala-lumpur-trp/375682\"},\n" +
                "{\"headline\":\"A shimmering Pearl of the Orient\",\"url\":\"https://www.trailsofindochina.com/destination/malaysia/penang/\"},\n" +
                "{\"headline\":\"Try stopping at one visit: why Penang is irresistible\",\"url\":\"https://www.exploretravel.com.au/story/8117585/try-stopping-at-one-visit-why-penang-is-irresistible/\"},\n" +
                "{\"headline\":\"The Charm of George Town, Penang\",\"url\":\"https://travelmermaid.com/travel/malaysia/the-charm-of-george-town-penang/\"},\n" +
                "{\"headline\":\"What to do in Penang, Malaysia: the island of cultural tapestry\",\"url\":\"https://feastoftravel.com/what-to-do-in-penang/\"},\n" +
                "{\"headline\":\"Why I Loved Penang\",\"url\":\"https://www.thiswildlifeofmine.com/why-i-loved-penang/\"},\n" +
                "{\"headline\":\"Uncover Penang's Peranakan Culture in a Weekend\",\"url\":\"https://guide.michelin.com/th/en/article/travel/uncover-penang-s-peranakan-culture-in-a-weekend\"},\n" +
                "{\"headline\":\"History of Penang\",\"url\":\"https://en.wikipedia.org/wiki/History_of_Penang\"},\n" +
                "{\"headline\":\"Arts and Culture\",\"url\":\"https://en.wikipedia.org/wiki/Culture_of_Penang\"},\n" +
                "{\"headline\":\"Penang’s rich heritage: A journey through time and culture\",\"url\":\"https://thebruneian.news/2024/09/02/penangs-rich-heritage-a-journey-through-time-and-culture/\"},\n" +
                "{\"headline\":\"Kellie’s Castle: Where Ghosts Linger\",\"url\":\"https://www.youngpioneertours.com/kellies-castle-where-ghosts-linger/\"},\n" +
                "{\"headline\":\"Penang Travel Journal : Exotic Historical Charm From Asia\",\"url\":\"https://wanderingdejavu.com/travel-journal/penang-travel-journal-exotic-historical-charm-from-asia/\"},\n" +
                "{\"headline\":\"Penang Travel Journal : Exotic Historical Charm of Asia – Discover the Timeless Beauty of Malaysia\",\"url\":\"https://pushpitha.livejournal.com/1515195.html\"},\n" +
                "{\"headline\":\"Eating in George Town, Penang\",\"url\":\"https://two-together.com/penang-food/\"},\n" +
                "{\"headline\":\"Exploring the History and Modern Charm of the Johor Bahru's Thaipusam: From Ancient to Present\",\"url\":\"https://downtownjb.my/en/article_details/27/exploring-the-history-and-modern-charm-of-the-johor-bahru-s-thaipusam-from-ancient-to-present\"},\n" +
                "{\"headline\":\"Johor: A vibrant city of cultural treasures and family-fun adventures\",\"url\":\"https://thebruneian.news/2023/12/14/johor-a-vibrant-city-of-cultural-treasures-and-family-fun-adventures/\"},\n" +
                "{\"headline\":\"15 Cultural Heritage Sites of Johor Bahru You Should Visit\",\"url\":\"https://www.sg2jbtaxi.com/15-cultural-heritage-sites-of-johor-bahru-you-should-visit/\"},\n" +
                "{\"headline\":\"Johor Bahru\",\"url\":\"https://davedoesthetravelthing.com/johor-bahru/\"},\n" +
                "{\"headline\":\"25 Things to Do in JB: Your Ultimate Causeway Bucket List\",\"url\":\"https://www.pelago.com/en/articles/best-things-to-do-in-jb/\"},\n" +
                "{\"headline\":\"Johor Bahru Food Trip: Eating My Way in Peninsular Malaysia’s Southernmost City\",\"url\":\"https://www.zoytotheworld.com/blog/malaysia-johor-bahru-food-trip-what-to-eat\"},\n" +
                "{\"headline\":\"\",\"url\":\"https://www.lemon8-app.com/itstoasty/7324922038831825409?region=sg\"},\n" +
                "{\"headline\":\"10 Awesome Things To Do During your Next Trip to Johor Bahru\",\"url\":\"https://www.drivelah.sg/blog/10-awesome-things-to-do-during-your-next-trip-to-johor-bahru-cllyu87xy2928383upbbal5igjo/\"},\n" +
                "{\"headline\":\"Top 10 Must Do in Langkawi Experiences\",\"url\":\"https://www.langkawi.com/10-must-do-langkawi-experiences/\"},\n" +
                "{\"headline\":\"Nature Experiences\",\"url\":\"https://www.thedatai.com/experiences/nature/nature-experiences/\"},\n" +
                "{\"headline\":\"Land of Legends: Discover myths and Malaysian wildlife in the archipelago of Langkawi\",\"url\":\"https://www.nationalgeographic.com/travel/article/paid-content-langkawi-malaysia-reasons-to-visit\"},\n" +
                "{\"headline\":\"Protect This Place: The Langkawi Archipelago, an Ancient Jewel\",\"url\":\"https://therevelator.org/protect-langkawi-archipelago/\"},\n" +
                "{\"headline\":\"History of Langkawi\",\"url\":\"https://www.langkawi.com/history-of-langkawi/\"},\n" +
                "{\"headline\":\"Langkawi History and Lifestyle\",\"url\":\"https://junglewalla.com/langkawi-history-lifestyle/\"},\n" +
                "{\"headline\":\"The History and Culture of Langkawi: Road Trip Insights\",\"url\":\"https://www.langkawibook.my/blogs/the-history-and-culture-of-langkawi-road-trip-insights\"},\n" +
                "{\"headline\":\"Resort life in Langkawi\",\"url\":\"https://www.traveldiariesapp.com/en/diary/0f0cda7a-d1c5-49cf-8ae6-3d3e64025150/chapter/b2313c83-d66c-46a9-a50c-c363540f202d\"},\n" +
                "{\"headline\":\"Food and drink in Langkawi\",\"url\":\"https://www.travelfish.org/eatandmeet/malaysia/peninsular_malaysia/perlis/langkawi/eat\"},\n" +
                "{\"headline\":\"Malacca: World Heritage in Malaysia\",\"url\":\"https://www.europeana.eu/en/stories/malacca-world-heritage-in-malaysia\"},\n" +
                "{\"headline\":\"Exploring Malacca: A State Rich of Historical Background in Malaysia\",\"url\":\"https://www.lomography.com/magazine/230378-exploring-malacca-a-state-rich-of-historical-background-in-malaysia\"},\n" +
                "{\"headline\":\"The cultural and historical significance of Old Melaka\",\"url\":\"https://theedgemalaysia.com/article/cultural-and-historical-significance-old-melaka\"},\n" +
                "{\"headline\":\"Malacca Travel Guide: Top 20 Things to Do in Melaka, Malaysia\",\"url\":\"https://nomadicsamuel.com/city-guides/malacca\"},\n" +
                "{\"headline\":\"Melaka Travel Journal : East, West, and Inbetween\",\"url\":\"https://wanderingdejavu.com/travel-journal/melaka-travel-journal/\"},\n" +
                "{\"headline\":\"A Weekend in Malacca\",\"url\":\"https://pyqaboo.com/2021/08/14/the-diary-a-weekend-in-malacca/\"},\n" +
                "{\"headline\":\"Melaka, not my favorite city at all\",\"url\":\"https://wnfdiary.com/malacca-not-my-favorite-city-at-all/\"},\n" +
                "{\"headline\":\"Malacca Travel Blog\",\"url\":\"https://worldtravelfamily.com/malacca-family-travel-blog-malacca-malaysia/\"},\n" +
                "{\"headline\":\"Malacca Food Trip: Eating My Way in Malaysia's Historic City\",\"url\":\"https://www.zoytotheworld.com/blog/malaysia-malacca-food-trip-what-to-eat\"},\n" +
                "{\"headline\":\"Experiences in Melaka\",\"url\":\"https://inafricaandbeyond.com/experiences-in-melaka-malaysia/\"},\n" +
                "{\"headline\":\"Malacca: City steeped in history\",\"url\":\"https://www.youngpioneertours.com/malacca-city-steeped-in-history/\"},\n" +
                "{\"headline\":\"Eight things you can't miss in Ipoh\",\"url\":\"https://www.nationalgeographic.com/travel/slideshow/partner-content-eight-things-you-cannot-miss-in-Ipoh\"},\n" +
                "{\"headline\":\"5 Stunning Cave Temples to Visit in Ipoh\",\"url\":\"https://www.malaysia.travel/explore/5-stunning-cave-temples-to-visit-in-ipoh\"},\n" +
                "{\"headline\":\"Exploring The Historical Streets of Ipoh Old Town\",\"url\":\"https://trevo.my/stories/exploring-the-historical-streets-of-ipoh-old-town-2/\"},\n" +
                "{\"headline\":\"Why Ipoh, Malaysia, Should Be on Your Travel Radar\",\"url\":\"https://www.nytimes.com/2018/02/28/travel/ipoh-malaysia-tourism.html\"},\n" +
                "{\"headline\":\"Ipoh's Cultural & Historical Heritage\",\"url\":\"https://www.thewanderingjuan.net/2014/09/ipohs-cultural-historical-heritage.html\"},\n" +
                "{\"headline\":\"Welcome in Ipoh\",\"url\":\"https://theglobalwizards.com/travel-diary-3-welcome-in-ipoh/\"},\n" +
                "{\"headline\":\"food adventuring ipoh malaysia\",\"url\":\"https://www.anitasfeast.com/blog/2018/01/food-adventuring-ipoh-malaysia/\"},\n" +
                "{\"headline\":\"poh Attraction: From The Eyes of a Local\",\"url\":\"https://www.holidaytourstravel.com/ipoh-attraction/\"},\n" +
                "{\"headline\":\"Ipoh's Cultural Scene: Festivals, Events\",\"url\":\"http://peraktourism.com.my/about-ipoh/ipoh-s-cultural-scene-festivals-events.html\"},\n" +
                "{\"headline\":\"8 Must-Buy Souvenirs in Ipoh Before You Leave\",\"url\":\"https://theipohguide.com/souvenirs-in-ipoh/\"},\n" +
                "{\"headline\":\"Exploring the islands and rainforest of Kota Kinabalu\",\"url\":\"https://www.wheresidewalksend.com/islands-rainforest-kota-kinabalu-borneo/?srsltid=AfmBOor8Eh49xZRoiQ8_DAZb57iSKY5eG1GU_hkMOOtzKo0bl2UPhJ9w\"},\n" +
                "{\"headline\":\"10 Reasons why I love Kota Kinabalu, Sabah, Malaysia\",\"url\":\"https://blog.staah.com/featured/10-reasons-why-i-love-kota-kinabalu-sabah-malaysia\"},\n" +
                "{\"headline\":\"Living Like a Local in Kota Kinabalu, Malaysia\",\"url\":\"https://www.nytimes.com/2015/02/15/travel/living-like-a-local-in-kota-kinabalu-malaysia.html\"},\n" +
                "{\"headline\":\"My Experience in Kota Kinabalu, Sabah, Malaysian Borneo                                                    \",\"url\":\"https://www.borneoecotours.com/blog/my-experience-in-kota-kinabalu-sabah-malaysian-borneo/\"},\n" +
                "{\"headline\":\"Things to do in Kota Kinabalu, Borneo Sabah – a Travel Guide\",\"url\":\"https://taleof2backpackers.com/things-to-do-in-kota-kinabalu-sabah/\"},\n" +
                "{\"headline\":\"Discovering Kota Kinabalu’s heritage\",\"url\":\"https://theedgemalaysia.com/article/discovering-kota-kinabalus-heritage\"},\n" +
                "{\"headline\":\"Travel Journal | Climbing Mt. Kinabalu\",\"url\":\"https://laurencefoo.com/blog/travel-journal-climbing-mt-kinabalu\"},\n" +
                "{\"headline\":\"Black Girl Travel Diaries: Kota Kinabalu\",\"url\":\"https://wanderlustcalls.com/bgtd-kota-kinabalu/\"},\n" +
                "{\"headline\":\"Food in Kota Kinabalu - 12 Mouth-Watering Items & Where to Find It\",\"url\":\"https://www.holidify.com/pages/food-in-kota-kinabalu-2537.html\"},\n" +
                "{\"headline\":\"Kota Kinabalu Food Tour - Secret Food Tours\",\"url\":\"https://www.secretfoodtours.com/kota-kinabalu/\"},\n" +
                "{\"headline\":\"The Long Way Up: Experience Climbing Mt. Kinabalu\",\"url\":\"https://www.adventureinyou.com/malaysia/the-long-way-up-climbing-mt-kinabalu-review/\"},\n" +
                "{\"headline\":\"Taste of Sarawak: a culinary journey through Kuching\",\"url\":\"https://www.nationalgeographic.com/travel/article/paid-content-taste-of-sarawak-a-culinary-journey-through-kuching\"},\n" +
                "{\"headline\":\"The Asian city obsessed with cats\",\"url\":\"https://www.bbc.com/travel/article/20170531-the-asian-city-obsessed-with-cats\"},\n" +
                "{\"headline\":\"WHAT TO DO IN KUCHING, BORNEO\",\"url\":\"https://www.wander-lust.nl/what-to-do-in-kuching-borneo/\"},\n" +
                "{\"headline\":\"Kuching History\",\"url\":\"http://asiaforvisitors.com/malaysia/sarawak/kuching/history.php\"},\n" +
                "{\"headline\":\"Kuching Sarawak. Nine days in the City of Cats.\",\"url\":\"https://lesscarmorelife.com/2024/03/03/kuching-sarawak-nine-days-in-the-city-of-cats/\"},\n" +
                "{\"headline\":\"7 Reasons Why You’ll Definitely Fall In Love With Kuching\",\"url\":\"https://sunriseodyssey.com/things-do-in-kuching\"},\n" +
                "{\"headline\":\"Cat City and a purr-fect storm\",\"url\":\"http://rtwman.co.uk/SM2014/Diary/day07_Dec01.html\"},\n" +
                "{\"headline\":\"Can’t-Miss-Won’t-Miss experiences in Kuching, Sarawak\",\"url\":\"https://www.traveloka.com/en-sg/explore/activities/cant-miss-wont-miss-experiences-in-kuching-sarawak/48364\"},\n" +
                "{\"headline\":\"Things to do in Kuching for Events and Festivals\",\"url\":\"https://paradesaborneo.com/things-to-do-in-kuching-for-events-and-festivals/\"},\n" +
                "{\"headline\":\"Discover Petaling\",\"url\":\"https://selangor.travel/discover-petaling/\"},\n" +
                "{\"headline\":\"History of Petaling Jaya\",\"url\":\"https://www.propsocial.my/topic/1973/history-of-petaling-jaya-posted-by-propsocial-editor\"},\n" +
                "{\"headline\":\"Is Petaling Jaya Safe? Crime Rates & Safety Report\",\"url\":\"https://www.travelsafe-abroad.com/malaysia/petaling-jaya/\"},\n" +
                "{\"headline\":\"Petaling Jaya travel guide\",\"url\":\"https://www.kayak.sg/Petaling-Jaya.23450.guide\"},\n" +
                "{\"headline\":\"\",\"url\":\"https://en.wikipedia.org/wiki/Petaling_Jaya\"},\n" +
                "{\"headline\":\"A Culinary Adventure in Petaling Jaya\",\"url\":\"https://www.peek.com/petaling-jaya-selangor-malaysia/r0nb78/savoring-malaysia-a-culinary-adventure-in-petaling-jaya/ar0vj5rex\"},\n" +
                "{\"headline\":\"Petaling Jaya\",\"url\":\"https://www.asiakingtravel.com/attraction/petaling-jaya\"},\n" +
                "{\"headline\":\"Local Events and Festivals around Petaling Jaya\",\"url\":\"https://singaporetripguide.com/interests/local-events-and-festivals-around-petaling-jaya\"},\n" +
                "{\"headline\":\"best food tours in petaling jaya\",\"url\":\"https://travel-buddies.com/best-food-tours-in-petaling-jaya/\"},\n" +
                "{\"headline\":\"Shah Alam\",\"url\":\"https://www.asiakingtravel.com/attraction/shah-alam\"},\n" +
                "{\"headline\":\"About Shah Alam\",\"url\":\"https://www.malaxi.com/about_shah_alam.html\"},\n" +
                "{\"headline\":\"Things To Do In Shah Alam\",\"url\":\"https://eatdrinkhilton.com/things-to-do-in-shah-alam/\"},\n" +
                "{\"headline\":\"Top 5 Things To Do In Shah Alam Malaysia For A Fun Getaway This Year!\",\"url\":\"https://traveltriangle.com/blog/things-to-do-in-shah-alam-malaysia-bdp/\"},\n" +
                "{\"headline\":\"A Journey Through Shah Alam’s Hidden Gems\",\"url\":\"https://www.peek.com/shah-alam-selangor-malaysia/r0kn4pz/discovering-tranquility-a-journey-through-shah-alams-hidden-gems/ar0664v4\"},\n" +
                "{\"headline\":\"6 Must-Do Activities for Unforgettable Fun\",\"url\":\"https://www.sgtomalaysia.com/i-city-shah-alam-activities/\"},\n" +
                "{\"headline\":\"Unveiling Shah Alam’s Secrets: Discover the City’s Hidden Gems\",\"url\":\"https://www.agoda.com/travel-guides/malaysia/shah-alam/unveiling-shah-alams-secrets-discover-the-citys-hidden-gems/\"},\n" +
                "{\"headline\":\"Shah Alam’s distinctive flavour\",\"url\":\"https://best.starproperty.my/best/2018/born-and-bred/shah-alams-distinctive-flavour/\"},\n" +
                "{\"headline\":\"Shah Alam introduction\",\"url\":\"https://www.m-city.com.my/cities-of-malaysia/shah-alam.html\"},\n" +
                "{\"headline\":\"Cameron Highlands: Malaysia’s Famous Tea Plantations\",\"url\":\"https://thehappyjetlagger.com/en/cameron-highlands-teaplantations-hiking-malaysia/\"},\n" +
                "{\"headline\":\"Cameron Highlands: Jungle Journey to the Best Cup of Tea in Malaysia\",\"url\":\"https://www.remotelands.com/travelogues/cameron-highlands-jungle-journey-to-the-best-cup-of-tea-in-malaysia/\"},\n" +
                "{\"headline\":\"Cameron Highlands: Discover Nature’s Serenity\",\"url\":\"https://www.youngpioneertours.com/cameron-highlands-discover-natures-serenity/\"},\n" +
                "{\"headline\":\"Cameron Highlands – Tea Country\",\"url\":\"https://www.biggerlifeadventures.com/cameron-highlands-tea-country/\"},\n" +
                "{\"headline\":\"Explore Cameron Highlands’ Historical Sites & Legacies\",\"url\":\"https://tripjive.com/explore-cameron-highlands-historical-sites-legacies/\"},\n" +
                "{\"headline\":\"Cameron Highlands\",\"url\":\"https://www.asiakingtravel.com/attraction/cameron-highlands\"},\n" +
                "{\"headline\":\"Cameron Highlands - trekking and tea plantations\",\"url\":\"https://www.traveldiariesapp.com/en/diary/672cc9b2-7664-42e3-a507-2deea15bf4c8/chapter/1ec3777a-8c5a-44f2-b197-b0870ab77350\"},\n" +
                "{\"headline\":\"HIKING THE HIGHLANDS OF CAMERON\",\"url\":\"https://magtheweekly.com/detail/3277-hiking-the-highlands-of-cameron\"},\n" +
                "{\"headline\":\"Cameron Highlands Itinerary: the best things to do\",\"url\":\"https://wheretogoin.net/cameron-highlands-itinerary/\"},\n" +
                "{\"headline\":\"The Cameron Highlands: What to See and Do\",\"url\":\"https://seeyousoon.ca/the-cameron-highlands/\"},\n" +
                "{\"headline\":\"Explore Cameron Highlands: Local Food & Culture\",\"url\":\"https://tripjive.com/explore-cameron-highlands-local-food-culture/\"},\n" +
                "{\"headline\":\"A dizzying day of merging cultures in Cameron Highlands\",\"url\":\"https://www.t1dwanderer.com/travelogues/se-asia/dizzying-day-merging-culture-cameron-highlands/\"},\n" +
                "{\"headline\":\"Why George Town, Penang Is The Coolest Place in Malaysia\",\"url\":\"https://www.travelcolorfully.com/george-town-penang-best-place-malaysia/\"},\n" +
                "{\"headline\":\"This is why I love George Town\",\"url\":\"https://wnfdiary.com/this-is-why-i-love-penang/\"},\n" +
                "{\"headline\":\"History of George Town, Penang\",\"url\":\"https://en.wikipedia.org/wiki/History_of_George_Town,_Penang\"},\n" +
                "{\"headline\":\"George Town, Malaysia: A Fusion of Cultures, Food, and Art\",\"url\":\"https://thespunkycurl.com/george-town-malaysia/\"},\n" +
                "{\"headline\":\"Travel Diary #5: George Town\",\"url\":\"https://theglobalwizards.com/travel-diary-5-george-town/\"},\n" +
                "{\"headline\":\"Discovering Georgetown: Our Penang Adventure\",\"url\":\"https://www.sunkisseddiaries.com/amazing-two-days-in-george-town-penang/\"},\n" +
                "{\"headline\":\"Penang Diary – For the Street Lovers\",\"url\":\"https://happyfeet.mangalika.com/penang-diary/\"},\n" +
                "{\"headline\":\"How George Town lived up to the hype\",\"url\":\"https://theimaginationtrail.com/georgetown-penang-malaysia-solo-female-travel/\"},\n" +
                "{\"headline\":\"best food in penang\",\"url\":\"https://www.timetravelturtle.com/malaysia/best-food-in-penang/\"},\n" +
                "{\"headline\":\"How George Town lived up to the hype\",\"url\":\"https://taxresidents.com/penang-festivals/\"},\n" +
                "{\"headline\":\"George Town Heritage Celebrations: Penang’s Rich Cultural Tapestry\",\"url\":\"https://ourheritagehomes.com/george-town-heritage-celebrations-penangs-rich-cultural-tapestry/\"},\n" +
                "{\"headline\":\"Exploring Alor Setar: A Journey Through Cultural Treasures and Historical Legacies\",\"url\":\"https://www.malaysia.travel/explore/exploring-alor-setar-a-journey-through-cultural-treasures-and-historical-legacies\"},\n" +
                "{\"headline\":\"Weekend with star of Kedah aka Alor Setar\",\"url\":\"https://travelmishmash.com/index.php/2021/02/14/weekend-with-star-of-kedah-aka-alor-setar/\"},\n" +
                "{\"headline\":\"Explore Alor Setar | Ancient City Charm and Modern Allure\",\"url\":\"https://sg.trip.com/moments/detail/alor-setar-14834-124393298/\"},\n" +
                "{\"headline\":\"A Day of Encounters: Exploring Alor Setar\",\"url\":\"https://medium.com/@hiro.the.world/a-day-of-encounters-exploring-alor-setar-world-journey-diary-72-11a640c059fd\"},\n" +
                "{\"headline\":\"3 Days in Alor Setar: Cultural and Natural Explorations\",\"url\":\"https://www.agoda.com/travel-guides/malaysia/alor-setar/3-days-in-alor-setar-cultural-and-natural-explorations/\"},\n" +
                "{\"headline\":\"Check off these must eat food in Alor Setar, Kedah\",\"url\":\"https://www.traveloka.com/en-sg/explore/culinary/must-eat-food-in-alor-setar/48654\"},\n" +
                "{\"headline\":\"Alor Setar and Penang: First Steps in Malaysia\",\"url\":\"https://blog.oliver-meili.name/2014/11/alor-setar-and-penang-first-steps-in-malaysia/\"},\n" +
                "{\"headline\":\"Local Events and Festivals around Alor Setar\",\"url\":\"https://singaporetripguide.com/interests/local-events-and-festivals-around-alor-setar\"},\n" +
                "{\"headline\":\"Exploring Alor Setar: Top Things to Do in Malaysia’s Cultural Gem\",\"url\":\"https://www.agoda.com/travel-guides/malaysia/alor-setar/exploring-alor-setar-top-things-to-do-in-malaysias-cultural-gem/\"}\n" +
                "]";



        List<ArticleUploadInfoVo> uploadInfoVos = JSON.parseArray(json, ArticleUploadInfoVo.class);

        for (ArticleUploadInfoVo uploadInfoVo : uploadInfoVos) {
            TArticle tArticle = new TArticle();
            tArticle.setHeadline(Objects.isNull(uploadInfoVo.getHeadline()) ? "" : uploadInfoVo.getHeadline());
            tArticle.setIsDelete(0);
            tArticle.setStatus(4);
            tArticle.setCreateTime(DateUtil.getNowSeconds());
            tArticle.setDestCountry(17);
            tArticle.setDestCity(17);

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
