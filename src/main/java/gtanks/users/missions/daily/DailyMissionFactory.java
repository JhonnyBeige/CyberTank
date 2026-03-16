package gtanks.users.missions.daily;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class DailyMissionFactory {
    private static final DailyMissionFactory INSTANCE = new DailyMissionFactory();

    ArrayList<DailyMissionInfo> dailyMissions = new ArrayList<>();

    public static DailyMissionFactory instance() {
        return INSTANCE;
    }

    public void load(String filename) {
        JSONParser parser = new JSONParser();
        try (FileReader reader = new FileReader(filename)) {
            JSONArray missionsArray = (JSONArray) parser.parse(reader);

            for (Object obj : missionsArray) {
                JSONObject missionObject = (JSONObject) obj;

                DailyMissionInfo missionInfo = new DailyMissionInfo();
                missionInfo.description = (String) missionObject.get("description");
                missionInfo.id = (String) missionObject.get("id");
                missionInfo.target_progress = ((Long) missionObject.get("target_progress")).intValue();
                
                missionInfo.prizes = (List<String>) missionObject.get("prizes");
                missionInfo.prizesId = (List<String>) missionObject.get("prizesId");
                missionInfo.prizesCount = new ArrayList<>();
                for (Object count : (JSONArray) missionObject.get("prizesCount")) {
                    missionInfo.prizesCount.add(((Long) count).intValue());
                }

                dailyMissions.add(missionInfo);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error while loading missions from file", e);
        }
    }

    public String getRandomMission() {
        Random random = new Random();
        return dailyMissions.get(random.nextInt(dailyMissions.size())).id;
    }

    public DailyMissionInfo getMissionById(String id) {
        return dailyMissions.stream().filter(m -> m.id.equals(id)).findFirst().orElse(null);
    }
}
