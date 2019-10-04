package io.github.stephanomehawej.gitinteractions;

import io.github.stephanomehawej.gitinteractions.exceptions.attributeNotFoundException;
import io.github.stephanomehawej.gitinteractions.exceptions.invalidValueException;
import org.json.*;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Main {
    public static void main(String [] args) {
       Issue issue = null;
       String data = "";

        try {

            URI uri = new URI("file:///D:/Code/gitInteractions/src/main/resources/config.json");
            JSONTokener tokener = new JSONTokener(uri.toURL().openStream());
            JSONObject issueInfo = new JSONObject(tokener);


            issue = new Issue(issueInfo.get("repo").toString(),issueInfo.get("login").toString(),issueInfo.get("OAuth").toString());
            issue.JSonIssue(issueInfo);
            issue.sendIssue();
        } catch (attributeNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (invalidValueException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }
}

