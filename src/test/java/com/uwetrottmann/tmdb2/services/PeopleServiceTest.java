package com.uwetrottmann.tmdb2.services;

import com.uwetrottmann.tmdb2.BaseTestCase;
import com.uwetrottmann.tmdb2.TestData;
import com.uwetrottmann.tmdb2.entities.Image;
import com.uwetrottmann.tmdb2.entities.Person;
import com.uwetrottmann.tmdb2.entities.PersonCastCredit;
import com.uwetrottmann.tmdb2.entities.PersonCredits;
import com.uwetrottmann.tmdb2.entities.PersonCrewCredit;
import com.uwetrottmann.tmdb2.entities.PersonIds;
import com.uwetrottmann.tmdb2.entities.PersonImages;
import com.uwetrottmann.tmdb2.entities.PersonResultsPage;
import com.uwetrottmann.tmdb2.entities.TaggedImagesResultsPage;
import org.junit.Test;
import retrofit2.Call;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import static org.assertj.core.api.Assertions.assertThat;

public class PeopleServiceTest extends BaseTestCase {

    private static final SimpleDateFormat JSON_STRING_DATE = new SimpleDateFormat("yyy-MM-dd");

    @Test
    public void test_summary() throws IOException, ParseException {
        Call<Person> call = getManager().personService().summary(TestData.PERSON_ID);
        Person person = call.execute().body();
        assertThat(person).isNotNull();
        assertThat(person.id).isEqualTo(TestData.PERSON_ID);
        assertThat(TestData.PERSON_NAME).isEqualTo(TestData.PERSON_NAME);
        assertThat(person.birthday).isEqualTo(JSON_STRING_DATE.parse("1944-05-14"));
        assertThat(person.deathday).isNull();
        assertThat(person.profile_path).startsWith("/").endsWith(".jpg");
        assertThat(person.biography).isNotEmpty();
    }

    @Test
    public void test_movie_credits() throws IOException {
        Call<PersonCredits> call = getManager().personService().movieCredits(TestData.PERSON_ID, null);
        PersonCredits credits = call.execute().body();
        assertThat(credits.id).isEqualTo(TestData.PERSON_ID);
        assertCastCredits(credits, false);
        assertCrewCredits(credits, false);

        for (PersonCastCredit credit : credits.cast) {
            assertThat(credit.title).isNotEmpty();
        }
    }

    @Test
    public void test_tv_credits() throws IOException {
        Call<PersonCredits> call = getManager().personService().tvCredits(TestData.PERSON_ID, null);
        PersonCredits credits = call.execute().body();
        assertThat(credits.id).isEqualTo(TestData.PERSON_ID);
        assertCastCredits(credits, false);

        for (PersonCastCredit credit : credits.cast) {
            assertThat(credit.episode_count).isGreaterThanOrEqualTo(0);
            assertThat(credit.name).isNotEmpty();
        }
    }

    @Test
    public void test_combined_credits() throws IOException {
        Call<PersonCredits> call = getManager().personService().combinedCredits(TestData.PERSON_ID, null);
        PersonCredits credits = call.execute().body();
        assertThat(credits.id).isEqualTo(TestData.PERSON_ID);
        assertCastCredits(credits, true);
        assertCrewCredits(credits, true);
    }

    @Test
    public void test_external_ids() throws IOException {
        Call<PersonIds> call = getManager().personService().externalIds(TestData.PERSON_ID);
        PersonIds ids = call.execute().body();
        assertThat(ids.id).isEqualTo(TestData.PERSON_ID);
        assertThat(ids.imdb_id).isEqualTo("nm0000184");
        assertThat(ids.freebase_id).isEqualTo("/en/george_lucas");
        assertThat(ids.freebase_mid).isEqualTo("/m/0343h");
        assertThat(ids.tvrage_id).isEqualTo(6490);
    }

    @Test
    public void test_images() throws IOException {
        Call<PersonImages> call = getManager().personService().images(TestData.PERSON_ID);
        PersonImages images = call.execute().body();
        assertThat(images.id).isEqualTo(TestData.PERSON_ID);

        for (Image image : images.profiles) {
            assertThat(image.file_path).isNotEmpty();
            assertThat(image.width).isNotNull();
            assertThat(image.height).isNotNull();
            assertThat(image.aspect_ratio).isGreaterThan(0);
        }
    }

    @Test
    public void test_tagged_images() throws IOException {
        Call<TaggedImagesResultsPage> call = getManager().personService().taggedImages(TestData.PERSON_ID, null, null);
        TaggedImagesResultsPage images = call.execute().body();
        assertThat(images.id).isEqualTo(TestData.PERSON_ID);

        for (TaggedImagesResultsPage.TaggedImage image : images.results) {
            assertThat(image.file_path).isNotEmpty();
            assertThat(image.width).isNotNull();
            assertThat(image.width).isGreaterThan(0);
            assertThat(image.height).isNotNull();
            assertThat(image.height).isGreaterThan(0);
            assertThat(image.aspect_ratio).isGreaterThan(0);
            assertThat(image.media).isNotNull();
            assertThat(image.id).isNotNull();
            assertThat(image.media_type).isNotNull();
            assertThat(image.image_type).isNotNull();
        }
    }

    @Test
    public void test_popular() throws IOException {
        Call<PersonResultsPage> call = getManager().personService().popular(null);
        PersonResultsPage popular = call.execute().body();

        assertThat(popular.results.get(0).id).isNotNull();
        assertThat(popular.results.get(0).name).isNotNull();
        assertThat(popular.results.get(0).popularity).isPositive();
        assertThat(popular.results.get(0).profile_path).isNotEmpty();
        assertThat(popular.results.get(0).adult).isNotNull();
        assertMedia(popular.results.get(0).known_for);
    }

    @Test
    public void test_latest() throws IOException {
        Call<Person> call = getManager().personService().latest();
        Person person = call.execute().body();
        // Latest person might not have a complete TMDb entry, but at should least some basic properties.
        assertThat(person).isNotNull();
        assertThat(person.name).isNotEmpty();
        assertThat(person.id).isPositive();
    }

    private void assertCastCredits(PersonCredits credits, boolean hasMediaType) {
        // assert cast credits
        assertThat(credits.cast).isNotEmpty();
        for (PersonCastCredit credit : credits.cast) {
            assertThat(credit.character).isNotNull(); // may be empty

            if (hasMediaType) {
                assertThat(credit.media_type).isNotEmpty();
            }
        }
    }

    private void assertCrewCredits(PersonCredits credits, boolean hasMediaType) {
        // assert crew credits
        assertThat(credits.crew).isNotEmpty();
        for (PersonCrewCredit credit : credits.crew) {
            // may be empty, but should exist
            assertThat(credit.department).isNotNull();
            assertThat(credit.job).isNotNull();

            if (hasMediaType) {
                assertThat(credit.media_type).isNotEmpty();
            }
        }
    }

}
