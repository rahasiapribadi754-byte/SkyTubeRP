package free.rm.skytube.businessobjects.YouTube;

import com.google.api.services.youtube.YouTube;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import free.rm.skytube.businessobjects.YouTube.POJOs.CardData;
import free.rm.skytube.businessobjects.YouTube.POJOs.YouTubeAPI;
import free.rm.skytube.businessobjects.YouTube.POJOs.YouTubeAPIKey;
import free.rm.skytube.businessobjects.YouTube.POJOs.YouTubeVideo;
import free.rm.skytube.businessobjects.YouTube.newpipe.NewPipeService;
import free.rm.skytube.businessobjects.YouTube.newpipe.VideoPager;

/**
 * Menampilkan 6 kartu acara anak pilihan di halaman awal (Featured).
 */
public class GetFeaturedVideos extends GetYouTubeVideos {

    protected YouTube.Videos.List videosList = null;

    private static final String[] KIDS_SHOWS = {
        "Polisi Labrador Indonesia",
        "Upin Ipin",
        "BabyBus Indonesia",
        "Leo si penjaga alam",
        "Like nastya Indonesia",
        "Sesame English"
    };

    @Override
    public void init() throws IOException {
        videosList = YouTubeAPI.create().videos().list("snippet, statistics, contentDetails");
        videosList.setFields("items(id, snippet/defaultAudioLanguage, snippet/defaultLanguage, snippet/publishedAt, snippet/title, snippet/channelId, snippet/channelTitle, " +
                "snippet/thumbnails, contentDetails/duration, statistics/viewCount), " +
                "nextPageToken");
        videosList.setKey(YouTubeAPIKey.get().getYouTubeAPIKey());
        videosList.setChart("mostPopular");
        videosList.setMaxResults(getMaxResults());
        nextPageToken = null;
        noMoreVideoPages = false;
    }

    @Override
    public void resetKey() {
        if (videosList != null) {
            videosList.setKey(YouTubeAPIKey.get().getYouTubeAPIKey());
        }
    }

    @Override
    public List<CardData> getNextVideos() {
        setLastException(null);

        if (noMoreVideoPages()) {
            noMoreVideoPages = true;
            List<CardData> result = new ArrayList<>();

            for (String query : KIDS_SHOWS) {
                try {
                    VideoPager pager = NewPipeService.get().getSearchResult(query);
                    List<YouTubeVideo> videos = pager.getNextPageAsVideos();
                    if (videos != null && !videos.isEmpty()) {
                        result.add(videos.get(0));
                    }
                } catch (Exception e) {
                    setLastException(e);
                }
            }
            return result;
        }

        return Collections.emptyList();
    }
}
