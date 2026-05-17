/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.islam.cliptool.util;
import com.islam.cliptool.model.Subtitle;
import java.io.File;
import java.io.FileWriter;
import java.util.List;
/**
 *
 * @author i3akk
 */


public class SrtWriter {

    public static File writeTemp(List<Subtitle> subtitles) throws Exception {

        File temp = File.createTempFile("vlcj_subs_", ".srt");
        temp.deleteOnExit();

        try (FileWriter fw = new FileWriter(temp)) {

            for (Subtitle s : subtitles) {

                fw.write(s.getIndex() + "\n");

                fw.write(format(s.getStartTime())
                        + " --> "
                        + format(s.getEndTime())
                        + "\n");

                fw.write(s.getText() + "\n\n");
            }
        }

        return temp;
    }

    private static String format(long ms) {

        long h = ms / 3600000;
        ms %= 3600000;

        long m = ms / 60000;
        ms %= 60000;

        long s = ms / 1000;
        long millis = ms % 1000;

        return String.format(
                "%02d:%02d:%02d,%03d",
                h, m, s, millis
        );
    }
}
