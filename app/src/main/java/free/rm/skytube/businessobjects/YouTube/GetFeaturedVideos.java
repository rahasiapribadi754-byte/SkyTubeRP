package free.rm.skytube.businessobjects.YouTube;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import free.rm.skytube.businessobjects.YouTube.POJOs.CardData;
import free.rm.skytube.businessobjects.YouTube.POJOs.YouTubeVideo;
import free.rm.skytube.businessobjects.YouTube.newpipe.NewPipeService;
import free.rm.skytube.businessobjects.YouTube.newpipe.VideoPager;

/**
 * Menampilkan 6 kartu acara anak pilihan di halaman awal (Featured).
 */
public class GetFeaturedVideos extends GetYouTubeVideos {

    private static final String[] KIDS_SHOWS = {
        "Polisi Labrador Indonesia",
        "Upin Ipin",
        "BabyBus Indonesia",
        "Leo si penjaga alam",
        "Like nastya Indonesia",
        "Sesame English"
    };

    @Override
    public void init() {
        noMoreVideoPages = false;
    }

    @Override
    public List<CardData> getNextVideos() {
        setLastException(null);

        if (!noMoreVideoPages) {
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

    @Override
    public boolean hasMoreVideoPages() {
        return !noMoreVideoPages;
    }
}
