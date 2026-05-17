/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.islam.cliptool.util;

import com.islam.cliptool.model.Subtitle;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author i3akk
 */
public class SrtParser {
    private static final Pattern TIME_PATTERN = Pattern.compile(
        "(\\d{2}):(\\d{2}):(\\d{2}),(\\d{3}) --> (\\d{2}):(\\d{2}):(\\d{2}),(\\d{3})"
    );

    public static List<Subtitle> parse(File file) throws IOException {

        List<Subtitle> list = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), java.nio.charset.StandardCharsets.UTF_8))) {

            String line;
            int index = 0;

            while ((line = br.readLine()) != null) {

                line = line.trim();
                if (line.isEmpty()) continue;

                // subtitle index
                line = line.replace("\uFEFF", "").trim();

                if (!line.matches("\\d+")) {
                    continue;
                }

                index = Integer.parseInt(line);

                // time line
                String timeLine = br.readLine();
                Matcher m = TIME_PATTERN.matcher(timeLine);

                if (!m.find()) continue;

                long start = toMillis(m, 1);
                long end = toMillis(m, 5);

                // subtitle text (multi-line)
                StringBuilder text = new StringBuilder();
                while ((line = br.readLine()) != null && !line.trim().isEmpty()) {
                    text.append(line).append("\n");
                }

                list.add(new Subtitle(index, start, end, text.toString().trim()));
            }
        }

        return list;
    }

    private static long toMillis(Matcher m, int offset) {
        return (
            Integer.parseInt(m.group(offset)) * 3600000L +
            Integer.parseInt(m.group(offset + 1)) * 60000L +
            Integer.parseInt(m.group(offset + 2)) * 1000L +
            Integer.parseInt(m.group(offset + 3))
        );
    }

}
