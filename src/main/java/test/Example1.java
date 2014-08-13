package test;


import com.atlassian.jira.rest.client.JiraRestClient;
import com.atlassian.jira.rest.client.NullProgressMonitor;
import com.atlassian.jira.rest.client.domain.BasicIssue;
import com.atlassian.jira.rest.client.domain.BasicProject;
import com.atlassian.jira.rest.client.domain.Comment;
import com.atlassian.jira.rest.client.domain.Issue;
import com.atlassian.jira.rest.client.domain.SearchResult;
import com.atlassian.jira.rest.client.domain.Transition;
import com.atlassian.jira.rest.client.domain.input.FieldInput;
import com.atlassian.jira.rest.client.domain.input.TransitionInput;
import com.atlassian.jira.rest.client.internal.jersey.JerseyJiraRestClientFactory;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
  /**
Jira Client*
*/

  /**
Please dont run the code or if you wish to run it (Change credentials)*

Thank you
Fatma
*/
public class Example1 {
	
	// I will define the logger
	static final Logger LOG = LoggerFactory.getLogger(Example1.class);

public static void main(String[] args) throws URISyntaxException {
System.out.println("TESSSSSSTSS 1 ");

final JiraRestClientFactory factory = new AsynchronousJiraRestClientFactory();
final URI jiraServerUri = new URI("http://10.26.6.47/");
final JiraRestClient restClient = factory.createWithBasicHttpAuthentication(jiraServerUri, "Fatma.AlMukhaini", "Fatma.AlMukhaini");
final Issue issue = restClient.getIssueClient().getIssue("MO-1251").claim();
System.out.println(issue);

System.out.println("----------here logger will apear-----------");
LOG.trace("Hello I am Fatma");
LOG.debug("Test LogBack");
LOG.warn("I love programming.");
//restClient.getIssueClient().vote(issue.getVotesUri()).claim();
restClient.getIssueClient().watch(issue.getWatchers().getSelf()).claim();
// progressing the issue
final Iterable<Transition> transitions = restClient.getIssueClient().getTransitions(issue.getTransitionsUri()).claim();
final Transition startProgressTransition = getTransitionByName(transitions, "Reopen Issue");

System.out.println("TESSSSSSTSS 2 ");

restClient.getIssueClient().transition(issue.getTransitionsUri(), new TransitionInput(startProgressTransition.getId())).claim();

System.out.println("TESSSSSSTSS 3 ");

final Transition resolveIssueTransition = getTransitionByName(transitions, "Resolve Issue");
Collection<FieldInput> fieldInputs = Arrays.asList(new FieldInput("resolution", "Incomplete"));
final TransitionInput transitionInput = new TransitionInput(resolveIssueTransition.getId(), fieldInputs, Comment.valueOf("My comment"));
restClient.getIssueClient().transition(issue.getTransitionsUri(), transitionInput).claim();

System.out.println("Jira task is done");
}
private static Transition getTransitionByName(Iterable<Transition> transitions, String transitionName)
{
for (Transition transition : transitions) {
if (transition.getName().equals(transitionName)) {
return transition;
}
}
return null;
}
}


