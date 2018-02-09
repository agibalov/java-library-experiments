package me.loki2302;

import com.atlassian.jira.rest.client.api.IssueRestClient;
import com.atlassian.jira.rest.client.api.JiraRestClient;
import com.atlassian.jira.rest.client.api.domain.ChangelogGroup;
import com.atlassian.jira.rest.client.api.domain.Issue;
import com.atlassian.jira.rest.client.api.domain.SearchResult;
import com.atlassian.jira.rest.client.api.domain.Status;
import com.atlassian.jira.rest.client.auth.AnonymousAuthenticationHandler;
import com.atlassian.jira.rest.client.internal.async.AsynchronousJiraRestClientFactory;
import net.rcarz.jiraclient.JiraClient;
import net.rcarz.jiraclient.JiraException;
import net.rcarz.jiraclient.greenhopper.GreenHopperClient;
import net.rcarz.jiraclient.greenhopper.RapidView;
import net.rcarz.jiraclient.greenhopper.Sprint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.net.URI;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@SpringBootApplication
public class App implements CommandLineRunner {
    private final static Logger LOGGER = LoggerFactory.getLogger(App.class);

    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }

    @Autowired
    private JiraRestClient jiraRestClient;

    @Autowired
    private JiraClient jiraClient;

    private void fetchOneIssueDetails() throws ExecutionException, InterruptedException {
        final String issueKey = "DATAGRAPH-146";
        Issue issue = jiraRestClient.getIssueClient().getIssue(issueKey, Arrays.asList(
                IssueRestClient.Expandos.CHANGELOG,
                IssueRestClient.Expandos.NAMES,
                IssueRestClient.Expandos.OPERATIONS,
                IssueRestClient.Expandos.TRANSITIONS)).get();
        LOGGER.info("\nkey={}\ntype={}\nsummary={}\nreporter={}\nassignee={}\ncreated={}\nupdated={}\nstatus={}",
                issue.getKey(),
                issue.getIssueType().getName(),
                issue.getSummary(),
                issue.getReporter().getName(),
                issue.getAssignee().getName(),
                issue.getCreationDate(),
                issue.getUpdateDate(),
                issue.getStatus().getName());

        Iterable<Status> statuses = jiraRestClient.getMetadataClient().getStatuses().get();
        Map<String, String> statusByStatusId = StreamSupport.stream(statuses.spliterator(), false)
                .collect(Collectors.toMap(s -> String.valueOf(s.getId()), s -> s.getName()));

        Iterable<ChangelogGroup> changelogGroups = issue.getChangelog();
        changelogGroups.forEach(changelogGroup -> {
            LOGGER.info("ChangelogGroup author={}, created={}",
                    changelogGroup.getAuthor().getName(),
                    changelogGroup.getCreated());

            changelogGroup.getItems().forEach(changelogItem -> {
                LOGGER.info("  ChangelogItem '{}' ({}) has changed from {}({}) to {}({})",
                        changelogItem.getField(),
                        changelogItem.getFieldType(), // Standard JIRA vs. Custom
                        changelogItem.getFrom(),
                        changelogItem.getFromString(),
                        changelogItem.getTo(),
                        changelogItem.getToString());

                if(changelogItem.getField().equals("status")) {
                    String oldStatus = statusByStatusId.get(changelogItem.getFrom());
                    String newStatus = statusByStatusId.get(changelogItem.getTo());
                    LOGGER.info("    Status has changed from '{}' to '{}'", oldStatus, newStatus);
                }
            });
        });
    }

    private void findIssuesByJqlQuery() throws ExecutionException, InterruptedException {
        final String jqlQuery =
                "project = DATAGRAPH and type = Bug and assignee changed from mhunger to \"vince@graphaware.com\"";
        SearchResult searchResult = jiraRestClient
                .getSearchClient()
                .searchJql(jqlQuery, 99999, 0, Collections.emptySet())
                .get();

        searchResult.getIssues().forEach(issue -> {
            LOGGER.info("key={} reporter={} assignee={}",
                    issue.getKey(),
                    issue.getReporter().getName(),
                    issue.getAssignee().getName());
        });
    }

    private void findAllSprints() throws JiraException {
        GreenHopperClient greenHopperClient = new GreenHopperClient(jiraClient);
        List<RapidView> rapidViews = greenHopperClient.getRapidViews();
        for(RapidView rapidView : rapidViews) {
            LOGGER.info("Board={}", rapidView.getName());

            List<Sprint> sprints = rapidView.getSprints();
            for(Sprint sprint : sprints) {
                LOGGER.info("  Sprint={} (start={}, end={}, closed={})",
                        sprint.getName(),
                        sprint.getStartDate(),
                        sprint.getEndDate(),
                        sprint.isClosed());
            }
        }
    }

    @Override
    public void run(String... args) throws Exception {
        fetchOneIssueDetails();
        findIssuesByJqlQuery();
        findAllSprints();
    }

    @Bean(destroyMethod = "close")
    public JiraRestClient jiraRestClient() {
        AsynchronousJiraRestClientFactory restClientFactory = new AsynchronousJiraRestClientFactory();
        return restClientFactory.create(
                URI.create("https://jira.spring.io/"),
                new AnonymousAuthenticationHandler());
    }

    @Bean
    public JiraClient jiraClient() {
        return new JiraClient("http://jira.spring.io");
    }
}
