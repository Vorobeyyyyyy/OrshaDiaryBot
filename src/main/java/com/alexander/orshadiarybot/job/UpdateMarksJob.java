package com.alexander.orshadiarybot.job;

import com.alexander.orshadiarybot.config.property.MessageProperty;
import com.alexander.orshadiarybot.model.domain.Account;
import com.alexander.orshadiarybot.model.domain.Mark;
import com.alexander.orshadiarybot.service.AccountService;
import com.alexander.orshadiarybot.service.ChatService;
import com.alexander.orshadiarybot.service.MarkService;
import com.alexander.orshadiarybot.service.SiteDataService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.*;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
@Log4j2
public class UpdateMarksJob implements Runnable {
    private MarkService markService;
    private AccountService accountService;
    private SiteDataService siteDataService;
    private ChatService chatService;
    private MessageProperty messageProperty;

    @Override
    @Transactional
    public void run() {
        log.info("Starting UpdateMarksJob");
        List<Account> accounts = accountService.findAll();
        accounts.forEach(account -> {
            log.info("Updating marks for account {}", account.getId());
            List<Mark> localMarks = markService.findMarksByAccount(account);
            List<Mark> actualMarks = siteDataService.findMarksByAccounts(account);
            Set<Mark> newMarks = excludeSame(actualMarks, localMarks);
            Set<Mark> deletedMarks = excludeSame(localMarks, actualMarks);
            log.info("New marks for account {}: {}", account.getId(), newMarks);
            log.info("Deleted marks from account {}: {}", account.getId(), deletedMarks);
            markService.rebaseMarks(account, actualMarks);
            if (!newMarks.isEmpty()) {
                String message = String.format(messageProperty.getNewMarksMessage(),
                        account.getFullName(),
                        newMarks.stream()
                        .map(mark -> mark.getDate().format(DateTimeFormatter.ofPattern("dd.MM")) + " " + mark.getSubject().getName() + " " + mark.getValue())
                        .collect(Collectors.joining("\n")));
                account.getOwners().forEach(owner -> chatService.sendMessage(owner, message));
            }
        });
    }

    private Set<Mark> excludeSame(List<Mark> base, List<Mark> toRemove) {
        Set<Mark> markSet = new TreeSet<>(Comparator.comparing(Mark::getDate).thenComparing(m -> m.getSubject().getName()));
        markSet.addAll(base);
        toRemove.forEach(markSet::remove);
        return markSet;
    }
}
