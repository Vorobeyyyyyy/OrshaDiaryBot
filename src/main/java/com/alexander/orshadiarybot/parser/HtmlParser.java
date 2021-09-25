package com.alexander.orshadiarybot.parser;

import com.alexander.orshadiarybot.model.domain.Mark;
import com.alexander.orshadiarybot.model.domain.Subject;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Log4j2
public class HtmlParser {
    public String parseFullName(String rawPage) {
        Document document = Jsoup.parse(rawPage);
        Element fullNameTag = document.select("div[style]").first();
        if (fullNameTag != null) {
            return fullNameTag.text();
        } else {
            return "Undefined";
        }
    }

    public List<Mark> parseMarks(String rawPage) {
        Document document = Jsoup.parse(rawPage);
        Elements elements = document.select("td[style*=border:none;text-align:center;padding-left:20px;]");
        return elements.stream().map(this::parseMarksFromTable).flatMap(List::stream).collect(Collectors.toList());
    }
    private List<Mark> parseMarksFromTable(Element element) {
        LocalDate date = LocalDate.parse(
                element.select("font[color=\"red\"]").text(),
                DateTimeFormatter.ofPattern("yyyy-M-d"));
        return element.select("tr").stream()
                .map(tr -> tr.select("td"))
                .filter(tds -> tds.size() == 3 && tds.get(2).hasText() && StringUtils.isNumeric(tds.get(2).text()))
                .map(tds -> {
                    Mark mark = new Mark();
                    mark.setDate(date);
                    mark.setSubject(new Subject(tds.get(1).text()));
                    mark.setValue(Integer.parseInt(tds.get(2).text()));
                    return mark;
                })
                .collect(Collectors.toList());
    }
}
