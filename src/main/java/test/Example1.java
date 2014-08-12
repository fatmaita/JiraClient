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
37   * A sample code how to use JRJC library 
38   *
39   * @since v0.1
40   */
  public class Example1 {
	  
	  	static final Logger LOG = LoggerFactory.getLogger(Example1.class);

  	public static void main(String[] args) throws URISyntaxException {
  		final JerseyJiraRestClientFactory factory = new JerseyJiraRestClientFactory();
        final URI jiraServerUri = new URI("http://10.26.6.47/");
  		final JiraRestClient restClient = factory.createWithBasicHttpAuthentication(jiraServerUri, "Fatma.AlMukhaini", "Fatma.AlMukhaini");
  		final NullProgressMonitor pm = new NullProgressMonitor();
  		LOG.trace("Hello I am Fatma");
  		LOG.debug("Test LogBack");
  		LOG.warn("I love programming.");
  		
  		
  		// first let's get and print all visible projects
  		final Iterable<BasicProject> allProjects = restClient.getProjectClient().getAllProjects(pm);
  		
  		for (BasicProject project : allProjects) {
  			System.out.println(project); 
  		}
  
  		// let's now print all issues matching a JQL string (here: all assigned issues)
  		final SearchResult searchResult = restClient.getSearchClient().searchJql("assignee is not EMPTY", pm);
  		for (BasicIssue issue : searchResult.getIssues()) {
  			System.out.println(issue.getKey());
  		}
  
  		final Issue issue = restClient.getIssueClient().getIssue("TST-1", pm);
  
  		System.out.println(issue);
  
  		// now let's vote for it
  		restClient.getIssueClient().vote(issue.getVotesUri(), pm);
  
  		// now let's watch it
  		restClient.getIssueClient().watch(issue.getWatchers().getSelf(), pm);
  
  		// now let's start progress on this issue
  		final Iterable<Transition> transitions = restClient.getIssueClient().getTransitions(issue.getTransitionsUri(), pm);
 		final Transition startProgressTransition = getTransitionByName(transitions, "Start Progress");
 		restClient.getIssueClient().transition(issue.getTransitionsUri(), new TransitionInput(startProgressTransition.getId()), pm);

  		// and now let's resolve it as Incomplete
  		final Transition resolveIssueTransition = getTransitionByName(transitions, "Resolve Issue");
  		Collection<FieldInput> fieldInputs = Arrays.asList(new FieldInput("resolution", "Incomplete"));
  		final TransitionInput transitionInput = new TransitionInput(resolveIssueTransition.getId(), fieldInputs, Comment.valueOf("Fatma"));
  		restClient.getIssueClient().transition(issue.getTransitionsUri(), transitionInput, pm);
  
  	}
  
  	private static Transition getTransitionByName(Iterable<Transition> transitions, String transitionName) {
  		for (Transition transition : transitions) {
  			if (transition.getName().equals(transitionName)) {
  				return transition;
  			}
  		}
  		return null;
  	}
  	
  	
  
  }


