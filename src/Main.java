import com.google.gson.JsonObject;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Copyright (C) 2016 Hadi
 */
public class Main {
    public static void main(String[] args) throws FileNotFoundException {
        Scanner s = new Scanner(new FileInputStream("logs"));
        while (s.hasNext()) {
            String address = s.nextLine();
            checkIdle(address);
        }
    }

    public static final Pattern pattern = Pattern.compile(".*?/(\\d+)/logs/game.log");

    public static void checkIdle(String logFile) throws FileNotFoundException {
        try {
            Scanner s = new Scanner(new FileInputStream(logFile));
            s.useDelimiter("\0");
            Message init = Json.GSON.fromJson(s.next(), Message.class);
            String[] teams = {init.args.get(init.args.size()-2).getAsString(), init.args.get(init.args.size()-1).getAsString()};
            s.next();
            int[] moves = new int[]{0, 0};
            // check for 3 idle steps
            for (int temp = 0; temp < 3; temp++) {
                s.next();
                Message msg = Json.GSON.fromJson(s.next(), Message.class);
                for (int i = 0; i < msg.args.size(); i++) {
                    JsonObject arg = msg.args.get(i).getAsJsonObject();
                    if (arg.getAsJsonPrimitive("name").getAsString().equals("0")) { // moves
                        int id = arg.get("args").getAsJsonArray().get(2).getAsInt();
                        moves[id]++;
                    }
                }
            }
            if (moves[0] == 0 || moves[1] == 0) {
                Matcher matcher = pattern.matcher(logFile);
                matcher.matches();
                String logID = matcher.group(1);
                System.out.printf(
                        "%s:%s:%s%n",
                        logID,
                        moves[0] == 0 ? "(" + teams[0] + ")" : teams[0],
                        moves[1] == 0 ? "(" + teams[1] + ")" : teams[1]
                );
            }
        } catch (Exception e) {
//            System.out.println("Exception occurred while processing " + logFile + ": " + e.getMessage());
//            e.printStackTrace();
        }
    }
}
