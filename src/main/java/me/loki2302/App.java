package me.loki2302;

import net.bramp.ffmpeg.FFmpeg;
import net.bramp.ffmpeg.FFmpegExecutor;
import net.bramp.ffmpeg.FFprobe;
import net.bramp.ffmpeg.builder.FFmpegBuilder;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class App {
    public static void main(String[] args) throws IOException {
        for(int i = 0; i < 10; ++i) {
            String filename = String.format("%03d.png", i);
            renderImage(String.format("Hello World #%d", i), new File(filename));
        }

        FFmpeg fFmpeg = new FFmpeg();
        FFprobe fFprobe = new FFprobe();
        FFmpegExecutor fFmpegExecutor = new FFmpegExecutor(fFmpeg, fFprobe);

        if(true) {
            FFmpegBuilder fFmpegBuilder = new FFmpegBuilder()
                    .setInput("%03d.png")
                    .addExtraArgs("-framerate", "2") // 2 frames per second
                    .overrideOutputFiles(true)
                    .addOutput("out.mp4")
                    .setVideoFrameRate(30) // stream framerate
                    .setVideoResolution(320, 240)
                    .done();
            fFmpegExecutor.createJob(fFmpegBuilder).run();
        }

        if(true) {
            FFmpegBuilder fFmpegBuilder = new FFmpegBuilder()
                    .setInput("out.mp4")
                    .setStartOffset(1, TimeUnit.SECONDS)
                    .overrideOutputFiles(true)
                    .addOutput("thumbnail.jpg")
                    .setVideoResolution(160, 120)
                    .setFrames(1)
                    .done();
            fFmpegExecutor.createJob(fFmpegBuilder).run();
        }
    }

    private static void renderImage(String text, File file) {
        BufferedImage bufferedImage = new BufferedImage(320, 240, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics2D = bufferedImage.createGraphics();

        graphics2D.setColor(Color.LIGHT_GRAY);
        graphics2D.fillRect(0, 0, 320, 240);

        Font font = new Font("Serif", Font.PLAIN, 30);
        graphics2D.setColor(Color.ORANGE);
        graphics2D.setFont(font);
        graphics2D.drawString(text, 0, 60);

        graphics2D.dispose();

        try {
            ImageIO.write(bufferedImage, "png", file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
