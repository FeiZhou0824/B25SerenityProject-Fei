package b25.spartan.editor;

import io.cucumber.java.af.En;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.CsvSource;
import utilities.SpartanNewBase;



import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import net.serenitybdd.junit5.SerenityTest;
import net.serenitybdd.rest.Ensure;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import utilities.SpartanUtil;


import java.util.LinkedHashMap;
import java.util.Map;

import static net.serenitybdd.rest.SerenityRest.given;
import static net.serenitybdd.rest.SerenityRest.lastResponse;
import static org.hamcrest.Matchers.*;

@SerenityTest
public class SpartanEditorPostTest extends SpartanNewBase {

    @Disabled
    @DisplayName("Editor should be able to POST")
    @Test
    public void postSpartanAsEditor(){

        Map<String, Object> randomSpartanMap = SpartanUtil.getRandomSpartanMap();
        System.out.println("randomSpartanMap = " + randomSpartanMap);

        //send a post REQUEST AS EDITOR
        given()
                .auth().basic("editor","editor")
                .and()
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .body(randomSpartanMap)
                .log().body()
                .when()
                .post("/spartans")
                .then().log().all();


        /*
                status code is 201
                content type is Json
                success message is A Spartan is Born!
                id is not null
                name is correct
                gender is correct
                phone is correct
                check location header ends with newly generated id
         */

        //status code is 201
         Ensure.that("Status code is 201",vRes ->vRes.statusCode(201));

         //content type is Json
        Ensure.that("Content -Type is JSON", vR -> vR.contentType(ContentType.JSON));

        // success message is A Spartan is Born!
        Ensure.that("success message is A Spartan is Born!", vR-> vR.body("success", is("A Spartan is Born!")));

        //id is not null
        Ensure.that("ID is not null", vR-> vR.body("data.id", notNullValue()));

        // name is correct
        Ensure.that("Name is correct", vR->vR.body("data.name",is(randomSpartanMap.get("name"))));

        //gender is correct
        Ensure.that("Gender is correct", vR ->vR.body("data.gender",is(randomSpartanMap.get("gender"))));

        //phone is correct
        Ensure.that("Phone is correct", vR ->vR.body("data.phone",is(randomSpartanMap.get("phone"))));

        //check location header ends with newly generated id
        String id = lastResponse().jsonPath().getString("data.id");

        Ensure.that("check location header ends with newly generated id",
                     vR-> vR.header("Location",endsWith(id)));



    }

    /*
    so if we want to provide custom test name for each execution
    we can use name  = "some message" structure. if we want to include index
    we will use {index}and for using parameter values we use order of parameter index
    just like {0} - name {1} - gender{2} - phone.
     */

    @ParameterizedTest(name = "New Spartan {index} - name - {0}")
    @CsvFileSource(resources = "/SpartanData.csv",numLinesToSkip = 1)
    public void postSpartanWithCsvFile(String nameArg, String gender,long phone){

        System.out.println("nameArg = " + nameArg);
        System.out.println("gender = " + gender);
        System.out.println("phone = " + phone);
        Map<String,Object> spartanMap = new LinkedHashMap<>();
        spartanMap.put("name",nameArg);
        spartanMap.put("gender",gender);
        spartanMap.put("phone",phone);


        //send a post REQUEST AS EDITOR
        given()
                .auth().basic("editor","editor")
                .and()
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .body(spartanMap)
                .log().body()
                .when()
                .post("/spartans")
                .then().log().all();


        //status code is 201
        Ensure.that("Status code is 201",vRes ->vRes.statusCode(201));

        //content type is Json
        Ensure.that("Content -Type is JSON", vR -> vR.contentType(ContentType.JSON));

        // success message is A Spartan is Born!
        Ensure.that("success message is A Spartan is Born!", vR-> vR.body("success", is("A Spartan is Born!")));


    }

}
