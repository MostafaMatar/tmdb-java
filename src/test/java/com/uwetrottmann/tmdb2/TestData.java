package com.uwetrottmann.tmdb2;

import com.uwetrottmann.tmdb2.enumerations.Status;

public interface TestData {

    int MOVIE_ID = 550;
    String MOVIE_TITLE = "Fight Club";
    String MOVIE_IMDB = "tt0137523";
    Status STATUS = Status.RELEASED;
    
    //Test Data for ReviewsService
    String REVIEW_ID = "581bbdbbc3a36805c60001f1";
    String REVIEW_AUTHOR = "iheardthatmoviewas";
    String REVIEW_URL = "https://www.themoviedb.org/review/581bbdbbc3a36805c60001f1";
    String REVIEW_ISO_639_1 = "en";
    Integer REVIEW_MEDIA_ID = 284052;
    String REVIEW_MEDIA_TITLE = "Doctor Strange";
    String REVIEW_MEDIA_TYPE = "Movie";
    

    int MOVIE_WITH_COLLECTION_ID = 671;
    String MOVIE_WITH_COLLECTION_TITLE = "Harry Potter and the Philosopher\'s Stone";

    int MOVIE_COLLECTION_ID = 1241;
    String MOVIE_COLLECTION_TITLE = "Harry Potter Collection";

    String TVSHOW_TITLE = "Breaking Bad";
    int TVSHOW_ID = 1396;
    String TVSHOW_IMDB_ID = "tt0903747";
    int TVSHOW_SEASON1_ID = 30272;
    String EPISODE_IDMB_ID = "tt0959621";
    int PERSON_ID = 1;
    String PERSON_NAME = "George Lucas";
    String PERSON_IMDB_ID = "nm0000184";
}
