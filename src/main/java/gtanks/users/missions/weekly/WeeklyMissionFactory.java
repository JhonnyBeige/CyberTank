package gtanks.users.missions.weekly;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class WeeklyMissionFactory {
    private static final WeeklyMissionFactory INSTANCE = new WeeklyMissionFactory();

    ArrayList<WeeklyMissionInfo> weeklyMissions = new ArrayList<>();

    public static WeeklyMissionFactory instance() {
        return INSTANCE;
    }

    public void load(String filename) {
        JSONParser parser = new JSONParser();
        try (FileReader reader = new FileReader(filename)) {
            JSONArray missionsArray = (JSONArray) parser.parse(reader);

            for (Object obj : missionsArray) {
                JSONObject missionObject = (JSONObject) obj;

                WeeklyMissionInfo missionInfo = new WeeklyMissionInfo();
                missionInfo.description = (String) missionObject.get("description");
                missionInfo.id = (String) missionObject.get("id");
                missionInfo.target_progress = ((Long) missionObject.get("target_progress")).intValue();
                
                missionInfo.prizes = (List<String>) missionObject.get("prizes");
                missionInfo.prizesId = (List<String>) missionObject.get("prizesId");
                missionInfo.prizesCount = new ArrayList<>();
                for (Object count : (JSONArray) missionObject.get("prizesCount")) {
                    missionInfo.prizesCount.add(((Long) count).intValue());
                }

                weeklyMissions.add(missionInfo);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error while loading missions from file", e);
        }
    }

    public String getRandomMission() {
        Random random = new Random();
        return weeklyMissions.get(random.nextInt(weeklyMissions.size() - 2)).id;
    }

    public WeeklyMissionInfo getMissionById(String id) {
        return weeklyMissions.stream().filter(m -> m.id.equals(id)).findFirst().orElse(null);
    }
}
