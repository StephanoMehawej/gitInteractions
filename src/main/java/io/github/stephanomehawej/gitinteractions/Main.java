package io.github.stephanomehawej.gitinteractions;

import io.github.stephanomehawej.gitinteractions.exceptions.attributeNotFoundException;
import io.github.stephanomehawej.gitinteractions.exceptions.invalidValueException;
import org.json.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Main {
    public static void main(String [] args) {
       Issue issue = null;
       String data = "";

        try {
            JSONObject issueInfo = new JSONObject();
            File file = (new File(args[0]));
            if (file.exists()){
                data =  new String(Files.readAllBytes(Paths.get(args[0])), "UTF-8");
            }else{
                data = args[0];
            }
            JSONObject newIssue = new JSONObject(data);
            issue = new Issue(newIssue.get("repo").toString(),newIssue.get("login").toString(),newIssue.get("OAuth").toString());
            issue.JSonIssue(newIssue);
            issue.sendIssue();
        } catch (attributeNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (invalidValueException e) {
            e.printStackTrace();
        }
    }
}

