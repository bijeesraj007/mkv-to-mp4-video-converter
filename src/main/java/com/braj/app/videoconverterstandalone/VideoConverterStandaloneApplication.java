package com.braj.app.videoconverterstandalone;

import ws.schild.jave.*;

import java.io.File;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class VideoConverterStandaloneApplication{

    public static void main(String[] args) {
        {
            System.out.println("[Video Converter] Version 1.0");
            System.out.println("[Developer] Bijees Raj");
            String currentDir = System.getProperty("user.dir");
            System.out.println("Current Directory : " + currentDir);
            DecimalFormat df = new DecimalFormat("0.00");
            String[] allFiles = new File(currentDir).list();
            List<String> allMkvFiles = Arrays.asList(allFiles).stream().filter(a->a.toLowerCase().endsWith(".mkv")).collect(Collectors.toList());
            System.out.println("Total MKV Files in the Folder : " + allMkvFiles.size());

            int count = 0;
            for(String eachMkvFile : allMkvFiles){

                System.out.println("\n\n ===============================  " + count + "  ===============================");
                long startTime = System.currentTimeMillis();
                count++;
                System.out.println("File Name : " + eachMkvFile);
                System.out.println("File Size  : " + new File(eachMkvFile).length());
                System.out.println(eachMkvFile + " is being converted");
                File source = new File(eachMkvFile);
                File target = new File(eachMkvFile.substring(0,eachMkvFile.indexOf(".")) + "_Converted_"+System.currentTimeMillis()+".mp4");

                /* Step 2. Set Audio Attrributes for conversion*/
                AudioAttributes audio = new AudioAttributes();
                audio.setCodec("aac");
                // here 64kbit/s is 64000
                audio.setBitRate(64000);
                audio.setChannels(2);
                audio.setSamplingRate(44100);

                /* Step 3. Set Video Attributes for conversion*/
                VideoAttributes video = new VideoAttributes();
                video.setCodec("h264");
                video.setX264Profile(VideoAttributes.X264_PROFILE.HIGH444);
                // Here 160 kbps video is 160000
                video.setBitRate(1048000);
                // More the frames more quality and size, but keep it low based on devices like mobile
                video.setFrameRate(60);
                video.setSize(new VideoSize(1280, 720));

                /* Step 4. Set Encoding Attributes*/
                EncodingAttributes attrs = new EncodingAttributes();
                attrs.setFormat("mp4");
                attrs.setAudioAttributes(audio);
                attrs.setVideoAttributes(video);

                /* Step 5. Do the Encoding*/
                try {
                    System.out.println("MKV to MP4 Conversion has been started. Execution time dependends on your hardware.");
                    System.out.println("Video Convertion is a Processor intense operation");
                    System.out.println("Example : i5 Processor may take upto : 20 - 25 mins for 100 MB conversion");
                    System.out.println("Please do not interrupt the process once its started.");
                    Encoder encoder = new Encoder();
                    encoder.encode(new MultimediaObject(source), target, attrs);
                    System.out.println("\nVideo Conversion completed successfully");
                    System.out.println("\nConverted File : " + target.getAbsolutePath());
                    long endTime = System.currentTimeMillis();
                    System.out.println("\nTotal Time taken :" +  df.format(((endTime-startTime)/1000)/60) +" Minutes");

                    if(allMkvFiles.size()>1  && count != allMkvFiles.size()) {
                        Thread.sleep(3000);
                        System.out.println("\n\n ******* Starting to convert the next file  ********");
                    }

                } catch (Exception e) {
                    /*Handle here the video failure*/
                    System.out.println("Sorry there was an error : " + e.getMessage());
                    e.printStackTrace();
                }
            }

            System.out.println(" Process Ended.");
            System.exit(0);
        }
    }
}
