/*
 * SkyTube
 * Copyright (C) 2018  Ramon Mifsud
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation (version 3 of the License).
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package free.rm.skytube.businessobjects.YouTube;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import free.rm.skytube.businessobjects.YouTube.POJOs.CardData;
import free.rm.skytube.businessobjects.YouTube.newpipe.NewPipeException;
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
                    List<CardData> items = pager.getNextPage();
                    if (items != null && !items.isEmpty()) {
                        result.add(items.get(0));
                    }
                } catch (NewPipeException e) {
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
