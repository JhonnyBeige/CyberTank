package gtanks.lobby;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import gtanks.commands.Type;
import gtanks.lobby.shop.GiveItemService;
import gtanks.main.database.DatabaseManager;
import gtanks.main.database.impl.DatabaseManagerImpl;
import gtanks.services.annotations.ServicesInject;
import gtanks.system.localization.strings.LocalizedString;
import gtanks.system.localization.strings.StringsLocalizationBundle;
import gtanks.users.User;
import gtanks.users.garage.GarageItemsLoader;
import gtanks.users.garage.items.Item;
import gtanks.users.missions.challenges.ChallengesServices;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class PromocodeCollector {
    private static final PromocodeCollector INSTANCE = new PromocodeCollector();
    @ServicesInject(
        target = DatabaseManagerImpl.class
    )
    private DatabaseManager database = DatabaseManagerImpl.instance();
    private JSONArray promoCodes;

    public PromocodeCollector() {
        this.loadPromoCodes();
    }

    public void loadPromoCodes() {
        System.out.println("INIT: PromocodeCollector successfully");
        JSONParser parser = new JSONParser();

        try (FileReader reader = new FileReader("promocodes.json")) {
            Object obj = parser.parse(reader);
            this.promoCodes = (JSONArray) obj;
        } catch (ParseException | IOException e) {
            e.printStackTrace();
        }
    }

    public static PromocodeCollector instance() {
        return INSTANCE;
    }

    public void checkPromoCode(LobbyManager lobby, String message) {
        for (Object promoObj : this.promoCodes) {
            JSONObject promo = (JSONObject) promoObj;
            String promoCode = (String) promo.get("code");
            String promoType = (String) promo.get("type");

            if (promoCode.equals(message)) {
                JSONArray usedByPlayers = (JSONArray) promo.get("usedByPlayers");
                String playerName = lobby.getLocalUser().getNickname();

                if (promoType.equals("unique")) {
                    handleUniquePromo(lobby, promo, usedByPlayers, playerName, promoCode, promoType);
                } else if (promoType.equals("single_use") && usedByPlayers.isEmpty()) {
                    handleSingleUsePromo(lobby, promo, usedByPlayers, playerName, promoCode, promoType);
                } else {
                    this.sendTableMessage(lobby, "invalid");
                }
                return;
            }
        }
        this.sendTableMessage(lobby, "invalid");
    }

    private void handleUniquePromo(LobbyManager lobby, JSONObject promo, JSONArray usedByPlayers, String playerName, String promoCode, String promoType) {
        if (!usedByPlayers.contains(playerName)) {
            this.applyPromoCode(lobby, promo);
            usedByPlayers.add(playerName);
            this.savePromoCodes();
            this.sendTableMessage(lobby, "succesful");
            System.out.println("User " + lobby.getLocalUser().getNickname() + " activated promocode " + promoCode + " type " + promoType);
        } else {
            this.sendTableMessage(lobby, "invalid");
        }
    }

    private void handleSingleUsePromo(LobbyManager lobby, JSONObject promo, JSONArray usedByPlayers, String playerName, String promoCode, String promoType) {
        this.applyPromoCode(lobby, promo);
        usedByPlayers.add(playerName);
        this.savePromoCodes();
        this.sendTableMessage(lobby, "succesful");
        System.out.println("User " + lobby.getLocalUser().getNickname() + " activated promocode " + promoCode + " type " + promoType);
    }

    private void applyPromoCode(LobbyManager lobby, JSONObject promo) {
        JSONObject reward = (JSONObject) promo.get("reward");
        this.activatePromocode(lobby, reward);
    }

    private void savePromoCodes() {
        try (FileWriter file = new FileWriter("promocodes.json")) {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String formattedJson = gson.toJson(this.promoCodes);
            file.write(formattedJson);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void activatePromocode(LobbyManager lobby, JSONObject reward) {
        int crystalsToShow = 0;
        int premiumToShow = 0;
        int starsToShow = 0;
        StringBuilder otherItems = new StringBuilder();

        if (reward.containsKey("crystals")) {
            long crystals = (Long) reward.get("crystals");
            lobby.addCrystall((int) crystals);
            crystalsToShow = (int)crystals;
        }

        if (reward.containsKey("stars")) {
            long stars = (Long) reward.get("stars");
            starsToShow = (int)stars;
            ChallengesServices.instance().addStarsToUser(lobby.getLocalUser(), starsToShow);
            otherItems.append("Stars." + starsToShow + "@");
        }

        if (reward.containsKey("premium")) {
            long premium = (Long) reward.get("premium");
            long itemCount = (Long) premium;
            premiumToShow = (int)itemCount;
            itemCount *= 86000;

            User giveToUser = lobby.getLocalUser();
            GiveItemService giveItemService = GiveItemService.getInstance();
            long giveUserId = giveToUser.getId();
            String jsonRequest = "{\"userId\":" + giveUserId + ",\"itemId\":\"premium\",\"count\":" + itemCount + "}";
            giveItemService.onReceive(jsonRequest);
        }

        if (reward.containsKey("bonusItems")) {
            JSONObject bonusItems = (JSONObject) reward.get("bonusItems");

            bonusItems.forEach((itemId, count) -> {
                String bonusItemId = (String) itemId;
                long itemCount = (Long) count;

                List<String> specialItemIds = Arrays.asList("mine", "n2o", "health", "armor", "double_damage");
                if (specialItemIds.contains(bonusItemId)) {
                    bonusItemId += "_m0";
                }

                User giveToUser = lobby.getLocalUser();
                GiveItemService giveItemService = GiveItemService.getInstance();
                long giveUserId = giveToUser.getId();
                String jsonRequest = "{\"userId\":" + giveUserId + ",\"itemId\":\"" + bonusItemId + "\",\"count\":" + itemCount + "}";
                giveItemService.onReceive(jsonRequest);

                String realName = "";
                if(itemId.equals("container_0")){
                    realName = "Container";
                }else{
                    Item item = GarageItemsLoader.getInstance().items
                        .get((String) itemId);
                        realName = item.name.localizatedString(lobby.getLocalUser().getLocalization());
                }

                otherItems.append(realName + "." + itemCount + "@");
            });
        }
        lobby.send(Type.LOBBY, "donate_successfully", String.valueOf(crystalsToShow), otherItems.toString(), String.valueOf(premiumToShow));
        lobby.getLocalUser().getGarage().parseJSONData();
        this.database.update(lobby.getLocalUser().getGarage());
    }

    public void sendTableMessage(LobbyManager lobby, String msg) {
        lobby.send(Type.LOBBY, "promostatus", msg);
    }
}
