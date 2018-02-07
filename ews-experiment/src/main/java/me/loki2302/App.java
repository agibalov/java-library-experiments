package me.loki2302;

import microsoft.exchange.webservices.data.core.ExchangeService;
import microsoft.exchange.webservices.data.core.PropertySet;
import microsoft.exchange.webservices.data.core.enumeration.property.WellKnownFolderName;
import microsoft.exchange.webservices.data.core.exception.service.local.ServiceLocalException;
import microsoft.exchange.webservices.data.core.service.folder.CalendarFolder;
import microsoft.exchange.webservices.data.core.service.folder.Folder;
import microsoft.exchange.webservices.data.core.service.item.Appointment;
import microsoft.exchange.webservices.data.core.service.item.Item;
import microsoft.exchange.webservices.data.credential.ExchangeCredentials;
import microsoft.exchange.webservices.data.credential.WebCredentials;
import microsoft.exchange.webservices.data.search.CalendarView;
import microsoft.exchange.webservices.data.search.FindFoldersResults;
import microsoft.exchange.webservices.data.search.FindItemsResults;
import microsoft.exchange.webservices.data.search.FolderView;

import java.net.URI;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.ZoneId;
import java.util.Date;
import java.util.stream.Collectors;

public class App {
    private final static String USERNAME = "some@user.name";
    private final static String PASSWORD = "somepassword";
    private final static String SERVICE_URL = "https://somehost/somewhere/Exchange.asmx";

    public static void main(String[] args) throws Exception {
        ExchangeCredentials exchangeCredentials = new WebCredentials(USERNAME, PASSWORD);
        ExchangeService exchangeService = new ExchangeService();
        exchangeService.setCredentials(exchangeCredentials);
        exchangeService.setUrl(new URI(SERVICE_URL));

        FindFoldersResults findFoldersResults = exchangeService.findFolders(WellKnownFolderName.Inbox, new FolderView(100));
        for(Folder folder : findFoldersResults.getFolders()) {
            System.out.printf("%s\n", folder.getDisplayName());
        }

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date startDate = formatter.parse("2016-05-02 00:00:01");
        Date endDate = formatter.parse("2016-05-06 23:59:59");
        CalendarFolder calendarFolder = CalendarFolder.bind(exchangeService, WellKnownFolderName.Calendar);
        FindItemsResults<Appointment> findResults = calendarFolder.findAppointments(new CalendarView(startDate, endDate));
        exchangeService.loadPropertiesForItems(findResults.getItems().stream().map(i -> (Item)i).collect(Collectors.toList()), PropertySet.FirstClassProperties);

        Instant.ofEpochMilli(new Date().getTime());

        findResults.getItems().stream().collect(Collectors.groupingBy(appt -> {
            try {
                return Instant.ofEpochMilli(appt.getStart().getTime()).atZone(ZoneId.systemDefault()).toLocalDate();
            } catch (ServiceLocalException e) {
                throw new RuntimeException(e);
            }
        })).forEach((date, appointments) -> {
            System.out.printf("%s\n", date);

            appointments.forEach(appointment -> {
                try {
                    System.out.printf("  [%s] %s\n",
                            appointment.getMyResponseType(),
                            appointment.getSubject());
                } catch (ServiceLocalException e) {
                    throw new RuntimeException(e);
                }
            });
        });
    }

    private static URI discoverUrl(
            String email,
            String password) throws Exception {
        ExchangeCredentials exchangeCredentials = new WebCredentials(email, password);
        ExchangeService exchangeService = new ExchangeService();
        exchangeService.setCredentials(exchangeCredentials);
        exchangeService.autodiscoverUrl(email, redirectionUrl -> true);
        return exchangeService.getUrl();
    }
}
