package spotify.oauth2.test;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.equalTo;

public class PlaylistTests {

    RequestSpecification requestSpecification;
    ResponseSpecification responseSpecification;
    String accessToken = "BQDxEB8OAyshtOV-71tEe8-DgpYhpaYKoV2QSxvwOQwS72a9jsPoMuYyqPNz84DedLaelS1Tq_eU9V0yScOIE6UuSjjYfmLX42x-LNxTmvnH3kkCk2uuMfCJBsMP5qFpORBwGnX8ObARy0EbFYMJPQMon20BR_-hCm1lQ1i8aOGKH1U5y1zz0H7hu8hkFFGGlnHR2NeV9yKQHFPQ5ADtojoCsFO4ttIBEBtoCpW6nv1k9n-MjuE6XmvE1xqDP8b1x01YPcWc7v4bJWah";

    @BeforeClass
    public void setUp() {
        RequestSpecBuilder reqBuilder = new RequestSpecBuilder().
                setBaseUri("https://api.spotify.com").
                setBasePath("/v1").
                addHeader("Authorization", "Bearer " + accessToken).
                setContentType(ContentType.JSON).
                log(LogDetail.ALL);

        requestSpecification = reqBuilder.build();

        ResponseSpecBuilder resBuilder = new ResponseSpecBuilder()
                .log(LogDetail.ALL);
        responseSpecification = resBuilder.build();
    }


    @Test
    public void createPlaylist() {
        String payload = "{\n" +
                "    \"name\": \"Rakesh API Playlist via Automation\",\n" +
                "    \"description\": \"New playlist description for API Testing via Automation\",\n" +
                "    \"public\": false\n" +
                "}";

        given(requestSpecification).
                body(payload).
                when().post("/users/31265aprw5noftw6dwldazqiqr4q/playlists").
                then().spec(responseSpecification).
                assertThat().
                statusCode(201).
                body("name", equalTo("Rakesh API Playlist via Automation"),
                        "description", equalTo("New playlist description for API Testing via Automation"),
                        "public", equalTo(false));
    }

    @Test
    public void getPlaylist() {
        given(requestSpecification).
                when().get("/playlists/3qwU7nWqK7rM5FnOmU3YGu").
                then().spec(responseSpecification).
                assertThat().
                statusCode(200).
                body("name", equalTo("Rakesh API Playlist via Automation"),
                        "description", equalTo("New playlist description for API Testing via Automation"));
    }

    @Test
    public void updatePlaylist() {
        String payload = "{\n" +
                "    \"name\": \"Updated Rakesh Playlist via Automation\",\n" +
                "    \"description\": \"Updated Rakesh playlist description via Automation\",\n" +
                "    \"public\": false\n" +
                "}";

        given(requestSpecification).
                body(payload).
                when().put("/playlists/3qwU7nWqK7rM5FnOmU3YGu").
                then().spec(responseSpecification).
                assertThat().
                statusCode(200);
    }

    @Test
    public void shouldNotCreatePlaylist() {
        String payload = "{\n" +
                "    \"name\": \"\",\n" +
                "    \"description\": \"New playlist description for API Testing via Automation\",\n" +
                "    \"public\": false\n" +
                "}";

        given(requestSpecification).
                body(payload).
                when().post("/users/31265aprw5noftw6dwldazqiqr4q/playlists").
                then().spec(responseSpecification).
                assertThat().
                statusCode(400).
                body("error.status", equalTo(400),
                        "error.message", equalTo("Missing required field: name"));
    }

    @Test
    public void shouldNotCreatePlaylistWithInvalidToken() {
        String payload = "{\n" +
                "    \"name\": \"Rakesh API Playlist via Automation\",\n" +
                "    \"description\": \"New playlist description for API Testing via Automation\",\n" +
                "    \"public\": false\n" +
                "}";

        given().
                baseUri("https://api.spotify.com").
                basePath("/v1").
                header("Authorization", "Bearer " + "12345").
                contentType(ContentType.JSON).
                log().all().
                body(payload).
        when().post("/users/31265aprw5noftw6dwldazqiqr4q/playlists").
        then().spec(responseSpecification).
                assertThat().
                statusCode(401).
                body("error.status", equalTo(401),
                        "error.message", equalTo("Invalid access token"));
    }
}
