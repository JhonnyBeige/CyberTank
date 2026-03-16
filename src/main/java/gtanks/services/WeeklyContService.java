/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package gtanks.services;

import gtanks.commands.Type;
import gtanks.containers.ContainerSystem;
import gtanks.json.ContainerItemsFactory;
import gtanks.json.WeeklyContFactory;
import gtanks.lobby.LobbyManager;
import gtanks.lobby.shop.GiveItemService;
import gtanks.main.database.DatabaseManager;
import gtanks.main.database.impl.DatabaseManagerImpl;
import gtanks.services.annotations.ServicesInject;
import gtanks.users.User;
import gtanks.users.garage.GarageItemsLoader;
import gtanks.users.garage.enums.ItemType;
import gtanks.users.garage.items.Item;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class WeeklyContService {
    private static final WeeklyContService INSTANCE = new WeeklyContService();
    @ServicesInject(target = DatabaseManagerImpl.class)
    private DatabaseManager database = DatabaseManagerImpl.instance();
    private List<String> loadedGifts = new ArrayList<String>(Arrays.asList(WeeklyContFactory.getData()));
    
    private static int[] repairKit = {1, 1, 2, 3, 3, 4, 5, 5, 6, 7, 7, 7, 8, 9, 10, 10, 11, 11, 12, 13, 14, 14, 15, 16, 17, 18, 19, 20, 20};
    private static int[] supply = {2, 3, 5, 7, 9, 11, 13, 15, 17, 19, 21, 23, 25, 28, 30, 31, 33, 35, 38, 40, 42, 44, 46, 48, 50, 52, 54, 56, 58, 60};
    private static int[] crystals = {70, 100, 210, 320, 420, 530, 640, 710, 820, 920, 1030, 1140, 1250, 1350, 1460, 1530, 1640, 1750, 1850, 1960, 2070, 2170, 2280, 2350, 2460, 2570, 2670, 2780, 2890, 3000};

    public static WeeklyContService instance() {
        return INSTANCE;
    }

    public void userOnGiftsWindowOpen(LobbyManager lobby) {
        String userContainers = ContainerSystem.getInstance()
                .getUserContainersResponse(lobby.getLocalUser().getId());
        JSONParser parser = new JSONParser();
        try {
            JSONObject jsonObject = (JSONObject) parser.parse(userContainers);
            JSONArray list = (JSONArray) jsonObject.get("list");
            JSONObject weeklyContainer = (JSONObject) list.get(1);
            long count = (long) weeklyContainer.get("count");

            lobby.send(Type.LOBBY, "show_weekly_gifts_window", String.valueOf(this.loadedGifts), Long.toString(count));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public void tryRollItem(LobbyManager lobby) {
    }

    public void rollItems(LobbyManager lobby, int rollCount) {
        String userContainers = ContainerSystem.getInstance().getUserContainersResponse(lobby.getLocalUser().getId());
        JSONParser parser = new JSONParser();
        long containerCount = 0;
        try {
            JSONObject jsonObject = (JSONObject) parser.parse(userContainers);
            JSONArray list = (JSONArray) jsonObject.get("list");
            JSONObject weeklyContainer = (JSONObject) list.get(1);
            containerCount = (long) weeklyContainer.get("count");
        } catch (ParseException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < containerCount; i++) {
            ContainerSystem.getInstance().openContainer(lobby.getLocalUser(), "container_1");
        }

        JSONArray jsonArray = null;
        try{
            jsonArray = WeeklyContService.parseJsonArray(String.valueOf(this.loadedGifts));
        }catch(Exception e){
            e.printStackTrace();
            return;
        }
        JSONArray jsonArrayGift = new JSONArray();

        for (int i = 0; i < 6; ++i) {
            int currentRang = lobby.getLocalUser().getRang() >= 29 ? 28 : lobby.getLocalUser().getRang();
            JSONObject chosenItem = WeeklyContService.pickItem(i, currentRang);

            Object itemName;
            String itemId = (String)chosenItem.get("item_id");
            int rarity = (int)chosenItem.get("rarity");
            int countItems = (int)chosenItem.get("count");
            countItems *= containerCount;
            JSONArray numInventoryCounts = new JSONArray();

            if (itemId.equals("crystalls")) {
                itemName = "Crystalls x" + countItems;
                lobby.addCrystall(countItems);
            } else {
                itemName = this.getItemNameWithCount(lobby, itemId, countItems);
                if(GarageItemsLoader.getInstance().items.get(itemId) == null){
                    System.out.println(itemId + " is null");
                }
                this.rewardGiftItemToUser(lobby, GarageItemsLoader.getInstance().items.get(itemId), countItems);
            }

            JSONObject newItem = new JSONObject();
            newItem.put("itemId", itemId);
            newItem.put("visualItemName", itemName);
            newItem.put("rarity", rarity);
            newItem.put("offsetCrystalls", 0);
            newItem.put("numInventoryCounts", numInventoryCounts);
            jsonArrayGift.add(newItem);
        }

        lobby.send(Type.LOBBY, "items_weekly_rolled", String.valueOf(jsonArrayGift));
    }

    private String getItemNameWithCount(LobbyManager lobby, String itemId, int countItems) {
        if (GarageItemsLoader.getInstance().items.get((Object) itemId) == null) {
            return itemId;
        }
        String itemName = GarageItemsLoader.getInstance().items.get((Object) itemId).name
                .localizatedString(lobby.getLocalUser().getLocalization());
        List<String> specialItemIds = Arrays.asList("mine", "n2o", "health", "armor", "double_damage");
        if (specialItemIds.contains(itemId)) {
            Optional<Item> bonusItem = lobby.getLocalUser().getGarage().getItemById(itemId);
            if (bonusItem == null) {
                lobby.getLocalUser().getGarage().giveItem(itemId, countItems, () -> {
                }, () -> {
                });
            }
            itemName = (String) itemName + " x" + countItems;
        }
        return itemName;
    }

    public static JSONArray parseJsonArray(String jsonArrayString) throws ParseException {
        JSONParser jsonParser = new JSONParser();
        return (JSONArray) jsonParser.parse(jsonArrayString);
    }

    private void addBonusItemsToGarage(User localUser, int setItemCount) {
        List<String> bonusItemIds = Arrays.asList("n2o", "double_damage", "armor", "mine", "health");
        for (String bonusItemId : bonusItemIds) {
            Optional<Item> bonusItem = localUser.getGarage().getItemById(bonusItemId);
            if (bonusItem == null) {
                localUser.getGarage().giveItem(bonusItemId, setItemCount, () -> {
                }, () -> {
                });
            }
        }
    }

    private void rewardGiftItemToUser(LobbyManager lobby, Item item, int count) {
        boolean containsItem = lobby.getLocalUser().getGarage().containsItem(item.id);
        if (containsItem) {
            if (item.itemType != ItemType.INVENTORY) {
                lobby.addCrystall(item.price / 2);
            }else{
                lobby.getLocalUser().getGarage().giveItem(item.id + "_m" + item.modificationIndex, count, () -> {}, () -> {});
            }
        } else {
            lobby.getLocalUser().getGarage().giveItem(item.id + "_m" + item.modificationIndex, count, () -> {}, () -> {});
        }
    }

    public static JSONObject pickItem(int index, int rank) {
        JSONObject item = new JSONObject();
        switch (index) {
            case 0:
                item.put("item_id", "health");
                item.put("rarity", 1);
                item.put("count", repairKit[rank]);
                break;
            case 1:
                item.put("item_id", "armor");
                item.put("rarity", 0);
                item.put("count", supply[rank]);
                break;
            case 2:
                item.put("item_id", "double_damage");
                item.put("rarity", 0);
                item.put("count", supply[rank]);
                break;
            case 3:
                item.put("item_id", "n2o");
                item.put("rarity", 0);
                item.put("count", supply[rank]);
                break;
            case 4:
                item.put("item_id", "mine");
                item.put("rarity", 0);
                item.put("count", supply[rank]);
                break;
            case 5:
                item.put("item_id", "crystalls");
                item.put("rarity", 2);
                item.put("count", crystals[rank]);
                break;
        }
        return item;
    }
}
