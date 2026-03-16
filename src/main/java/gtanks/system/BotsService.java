package gtanks.system;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

import gtanks.lobby.LobbyManager;
import gtanks.lobby.battles.BattleInfo;

public class BotsService {
    private static BotsService instance;

    private HashMap<String, BattleInfo> botsMap;

    private BotsService() {
        botsMap = new HashMap<>();
    }

    public static BotsService getInstance() {
        if (instance == null) {
            instance = new BotsService();
        }
        return instance;
    }

    public void addBot(String botName, BattleInfo battle) {
        try {
            String url = "http://localhost:3777/" + botName.toLowerCase();

            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod("GET");

            int responseCode = con.getResponseCode();
            System.out.println("Response Code : " + responseCode);

            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            System.out.println("Server Response: " + response.toString());

            botsMap.put(botName.toLowerCase(), battle);
            System.out.println("Bot " + botName + " added to battle " + battle.battleId);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void startBot(String botName) {
        try {
            String url = "http://localhost:3777/" + botName.toLowerCase();

            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod("GET");

            int responseCode = con.getResponseCode();
            System.out.println("Response Code : " + responseCode);

            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            System.out.println("Server Response: " + response.toString());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean containsBot(String botName) {
        return botsMap.containsKey(botName.toLowerCase());
    }

    public BattleInfo getBotBattleInfo(String botName) {
        return botsMap.get(botName.toLowerCase());
    }

    public void addUserToBattle(LobbyManager userLobby, String battleId) {
        userLobby.onEnterInBattle(battleId);
    }

    public void addUserToTeamBattle(LobbyManager userLobby, String battleId, boolean red) {
        userLobby.onEnterInTeamBattle(battleId, red);
    }
}
