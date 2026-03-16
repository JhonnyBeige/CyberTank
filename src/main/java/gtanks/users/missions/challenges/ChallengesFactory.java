package gtanks.users.missions.challenges;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.FileReader;
import java.time.LocalDate;
import java.util.ArrayList;

public class ChallengesFactory {

    private static ChallengesFactory INSTANCE = new ChallengesFactory();
    public ArrayList<ChallengeTierInfo> challengeInfos = new ArrayList<>();
    public String endingDate; // Store ending date as string to parse later

    public static ChallengesFactory instance() {
        return INSTANCE;
    }

    public void load(String filename) {
        JSONParser parser = new JSONParser();
        try (FileReader reader = new FileReader(filename)) {
            JSONObject challengesObject = (JSONObject) parser.parse(reader);

            // Parse endDate
            endingDate = (String) challengesObject.get("endDate");

            // Parse challenge tiers
            JSONArray challengesArray = (JSONArray) challengesObject.get("tiers");
            for (Object obj : challengesArray) {
                JSONObject tierObject = (JSONObject) obj;

                ChallengeTierInfo challengeInfo = new ChallengeTierInfo();
                challengeInfo.stars = ((Long) tierObject.get("stars")).intValue();
                
                // Base reward details
                JSONObject base = (JSONObject) tierObject.get("base");
                challengeInfo.baseId = (String) base.get("itemId");
                challengeInfo.baseName = (String) base.get("itemName");
                challengeInfo.baseCount = Integer.parseInt(base.get("count").toString());

                // BattlePass reward details
                JSONObject battlePass = (JSONObject) tierObject.get("battlePass");
                challengeInfo.battlePassId = (String) battlePass.get("itemId");
                challengeInfo.battlePassName = (String) battlePass.get("itemName");
                challengeInfo.battlePassCount = ((Long) battlePass.get("count")).intValue();

                challengeInfos.add(challengeInfo);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error while loading challenges from file", e);
        }
    }
}
