package io.github.stephanomehawej.gitinteractions;

import io.github.stephanomehawej.gitinteractions.exceptions.attributeNotFoundException;
import io.github.stephanomehawej.gitinteractions.exceptions.invalidValueException;
import org.json.JSONObject;
import org.kohsuke.github.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class Issue {

    private enum attributes {
            title{
                @Override
                public String toString() {
                    return "title";
                }
            },
            body{
                @Override
                public String toString() {
                    return "body";
                }
            },
            repo{
                @Override
                public String toString() {return  "repo"; }
            },
            labels{
                @Override
                public String toString() {return "labels"; }
            },
            assignees{
                @Override
                public String toString() {return "assignees"; }
            },
            milestone{
                @Override
                public String toString() {return "milestone"; }
            };

    }

    private GHRepository repo;
    private HashMap<String,Object> values = new HashMap<String, Object>();

    public Issue(String repoName,String login, String OAuth) throws IOException {
        GitHub github = GitHub.connect(login,OAuth);
        repo = github.getRepository(repoName);

    }

    public void sendIssue() throws IOException, invalidValueException {


        GHIssueBuilder issue = repo.createIssue(values.get(attributes.title.toString()).toString());
        if (values.get(attributes.assignees.toString()) instanceof String[]) {
            String[] assignees = (String[]) values.get(attributes.assignees.toString());
            for (String assignee:assignees) {
                issue.assignee(assignee);
            }
        }else{
            String assignee = (String) values.get(attributes.assignees.toString());
            issue.assignee(assignee);
        }

        if (values.get(attributes.labels.toString()) instanceof String[]) {
            String[] labels = (String[]) values.get(attributes.labels.toString());
            for (String label:labels) {
                issue.assignee(label);
            }
        }else{
            String label = (String) values.get(attributes.assignees.toString());
            issue.assignee(label);
        }
        issue.milestone(getCurrentMilestone( (String) values.get(attributes.labels.toString())));
        issue.body((String) values.get(attributes.body.toString()));
        issue.create();

    }

    /**
     * Creates an issue on a repo by supplying a JSON (org.json.JSONObject is required to use json)
     * with a <b>title </b> named <i>title</i>, a<b> body </b>(description of the issue) named <i>body</i>
     * and a <b>repo path</b> named <i>repo</i> (repoPath/reponame). You can also specifie <b>labels</b>
     * in and array or a single string named <i>labels</i>, a single or a list  of <B>assignees</B> named <i>assignees</i>, a <b>milestone</b>
     *  named <i>milestone</i> if none is specified the current one will be used.
     *
     * @param issueInfo All the info that is needed to create the issue in a JSON
     */

    public void JSonIssue( JSONObject issueInfo) throws attributeNotFoundException, invalidValueException {
            if (!issueInfo.has("milestone")){
                issueInfo.put("milestone",getCurrentMilestone(""));
            }
            validateJson(issueInfo);
            setValues(issueInfo);
    }

    private GHMilestone getCurrentMilestone(String milestoneName) throws invalidValueException{
        List<GHMilestone> milestones = repo.listMilestones(GHIssueState.OPEN).asList();
        GHMilestone smallest = milestones.get(0);
        for (int i = 1; i < milestones.size() ; i++) {
            if(smallest.getDueOn().before(milestones.get(i).getDueOn()) && milestoneName == null){
                smallest = milestones.get(i);
            }else if(milestones.get(i).getTitle().equalsIgnoreCase(milestoneName)) {
                smallest = milestones.get(i);
            }
        }
        return smallest;
    }

    private void validateJson(JSONObject object) throws attributeNotFoundException {
        for (int i = 0; i < attributes.values().length ; i++) {
            if(!object.has(attributes.values()[i].toString())){
                throw new attributeNotFoundException("\nThe attribute : " + attributes.values()[i].toString()
                        + " was not found. Check if it is included. If it is check the spelling");
            }
        }

    }

    private void setValues(JSONObject object) throws invalidValueException {
        for (int i = 0; i < attributes.values().length ; i++) {
            if(object.has(attributes.values()[i].toString())){
                values.put(attributes.values()[i].toString(),object.get(attributes.values()[i].toString()).toString());
            }
        }
    }

    public void setAttribue(String name,Object value) throws invalidValueException {
        switch (attributes.valueOf(name)) {

            case title:
                if(value instanceof String){
                    values.put(name,value);
                }else {
                    throw new invalidValueException("The value for the name attribute must be a string");
                }
                break;

            case body:
                if(value instanceof String){
                    values.put(name,value);
                }else {
                    throw new invalidValueException("The value for the body attribute must be a string");
                }
                break;

            case repo:
                if(value instanceof String){
                    values.put(name,value);
                }
                else {
                    throw new invalidValueException("The value for the repo attribute must be a string");
                }
                break;

            case labels:
                if(value instanceof String[]){
                    values.put(name,value);
                }else {
                    throw new invalidValueException("The value for the labels attribute must be an array strings");
                }
                break;
            case assignees:
                if(value instanceof String[]){
                    values.put(name,value);
                }else {
                    throw new invalidValueException("The value for the assignees attribute must be an array of strings");
                }
                break;
            case milestone:
                if(value instanceof String){
                    values.put(name,value);
                }else {
                    throw new invalidValueException("The value for the milestone attribute must be a string");
                }
                break;
            default:
                break;
        }
    }
}
