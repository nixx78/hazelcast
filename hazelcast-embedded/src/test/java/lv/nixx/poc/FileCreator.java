package lv.nixx.poc;


import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class FileCreator {

    public static void main(String[] args) throws IOException {

        long c = 100_000_000;

        BufferedWriter writer = new BufferedWriter(new FileWriter("./data/large_file.txt"));

        for (long i = 0; i < c; i++) {
            writer.write("string" + (i % 10) + "\n");
        }

        writer.close();


    }

}
