package gtanks.services;

import java.util.ArrayList;
import java.util.Map;

import gtanks.lobby.LobbyManager;
import gtanks.lobby.shop.GiveItemService;

import java.util.HashMap;

public class ShopServiceFactory {
    private static final ShopServiceFactory INSTANCE = new ShopServiceFactory();

    public Map<String, ShopItemInfo> shopItems = new HashMap<String, ShopItemInfo>();

    public static ShopServiceFactory instance() {
        return INSTANCE;
    }

    public void init() {
        // Initialize crystals1
        ShopItemInfo crystals1 = new ShopItemInfo();
        crystals1.items = new ArrayList<>();
        crystals1.itemsCount = new ArrayList<>();
        crystals1.items.add("crystalls");
        crystals1.itemsCount.add(6000);
        shopItems.put("crystals1", crystals1);

        // Initialize crystals2
        ShopItemInfo crystals2 = new ShopItemInfo();
        crystals2.items = new ArrayList<>();
        crystals2.itemsCount = new ArrayList<>();
        crystals2.items.add("crystalls");
        crystals2.itemsCount.add(19000);
        shopItems.put("crystals2", crystals2);

        // Initialize crystals3
        ShopItemInfo crystals3 = new ShopItemInfo();
        crystals3.items = new ArrayList<>();
        crystals3.itemsCount = new ArrayList<>();
        crystals3.items.add("crystalls");
        crystals3.itemsCount.add(45000);
        shopItems.put("crystals3", crystals3);

        // Initialize crystals4
        ShopItemInfo crystals4 = new ShopItemInfo();
        crystals4.items = new ArrayList<>();
        crystals4.itemsCount = new ArrayList<>();
        crystals4.items.add("crystalls");
        crystals4.itemsCount.add(95000);
        crystals4.items.add("premium");
        crystals4.itemsCount.add(1 * 8600);
        shopItems.put("crystals4", crystals4);

        // Initialize crystals5
        ShopItemInfo crystals5 = new ShopItemInfo();
        crystals5.items = new ArrayList<>();
        crystals5.itemsCount = new ArrayList<>();
        crystals5.items.add("crystalls");
        crystals5.itemsCount.add(200000);
        crystals4.items.add("premium");
        crystals4.itemsCount.add(3 * 8600);
        shopItems.put("crystals5", crystals5);

        // Initialize crystals6
        ShopItemInfo crystals6 = new ShopItemInfo();
        crystals6.items = new ArrayList<>();
        crystals6.itemsCount = new ArrayList<>();
        crystals6.items.add("crystalls");
        crystals6.itemsCount.add(358000);
        crystals4.items.add("premium");
        crystals4.itemsCount.add(7 * 8600);
        shopItems.put("crystals6", crystals6);

        // Initialize premium1
        ShopItemInfo premium1 = new ShopItemInfo();
        premium1.items = new ArrayList<>();
        premium1.itemsCount = new ArrayList<>();
        premium1.items.add("premium");
        premium1.itemsCount.add(1 * 86000);
        shopItems.put("premium1", premium1);

        // Initialize premium2
        ShopItemInfo premium2 = new ShopItemInfo();
        premium2.items = new ArrayList<>();
        premium2.itemsCount = new ArrayList<>();
        premium2.items.add("premium");
        premium2.itemsCount.add(7 * 86000);
        shopItems.put("premium2", premium2);

        // Initialize premium3
        ShopItemInfo premium3 = new ShopItemInfo();
        premium3.items = new ArrayList<>();
        premium3.itemsCount = new ArrayList<>();
        premium3.items.add("premium");
        premium3.itemsCount.add(14 * 86000);
        shopItems.put("premium3", premium3);

        // Initialize premium4
        ShopItemInfo premium4 = new ShopItemInfo();
        premium4.items = new ArrayList<>();
        premium4.itemsCount = new ArrayList<>();
        premium4.items.add("premium");
        premium4.itemsCount.add(30 * 86000);
        shopItems.put("premium4", premium4);

        // Initialize premium5
        ShopItemInfo premium5 = new ShopItemInfo();
        premium5.items = new ArrayList<>();
        premium5.itemsCount = new ArrayList<>();
        premium5.items.add("premium");
        premium5.itemsCount.add(60 * 86000);
        shopItems.put("premium5", premium5);

        // Initialize premium6
        ShopItemInfo premium6 = new ShopItemInfo();
        premium6.items = new ArrayList<>();
        premium6.itemsCount = new ArrayList<>();
        premium6.items.add("premium");
        premium6.itemsCount.add(90 * 86000);
        shopItems.put("premium6", premium6);

        // Initialize container1
        ShopItemInfo container1 = new ShopItemInfo();
        container1.items = new ArrayList<>();
        container1.itemsCount = new ArrayList<>();
        container1.items.add("container_0");
        container1.itemsCount.add(1);
        shopItems.put("container1", container1);

        // Initialize container2
        ShopItemInfo container2 = new ShopItemInfo();
        container2.items = new ArrayList<>();
        container2.itemsCount = new ArrayList<>();
        container2.items.add("container_0");
        container2.itemsCount.add(3);
        shopItems.put("container2", container2);

        // Initialize container3
        ShopItemInfo container3 = new ShopItemInfo();
        container3.items = new ArrayList<>();
        container3.itemsCount = new ArrayList<>();
        container3.items.add("container_0");
        container3.itemsCount.add(10);
        shopItems.put("container3", container3);

        // Initialize container4
        ShopItemInfo container4 = new ShopItemInfo();
        container4.items = new ArrayList<>();
        container4.itemsCount = new ArrayList<>();
        container4.items.add("container_0");
        container4.itemsCount.add(25);
        shopItems.put("container4", container4);

        // Initialize container5
        ShopItemInfo container5 = new ShopItemInfo();
        container5.items = new ArrayList<>();
        container5.itemsCount = new ArrayList<>();
        container5.items.add("container_0");
        container5.itemsCount.add(50);
        shopItems.put("container5", container5);

        // Initialize container6
        ShopItemInfo container6 = new ShopItemInfo();
        container6.items = new ArrayList<>();
        container6.itemsCount = new ArrayList<>();
        container6.items.add("container_0");
        container6.itemsCount.add(100);
        shopItems.put("container6", container6);

        // Initialize olivewidow
        ShopItemInfo olivewidow = new ShopItemInfo();
        olivewidow.items = new ArrayList<>();
        olivewidow.itemsCount = new ArrayList<>();
        olivewidow.items.add("olivewidow_m0");
        olivewidow.itemsCount.add(1);
        shopItems.put("olivewidow", olivewidow);

        // Initialize redstoker
        ShopItemInfo redstoker = new ShopItemInfo();
        redstoker.items = new ArrayList<>();
        redstoker.itemsCount = new ArrayList<>();
        redstoker.items.add("redstoker_m0");
        redstoker.itemsCount.add(1);
        shopItems.put("redstoker", redstoker);

        // Initialize thugsigns
        ShopItemInfo thugsigns = new ShopItemInfo();
        thugsigns.items = new ArrayList<>();
        thugsigns.itemsCount = new ArrayList<>();
        thugsigns.items.add("thugsigns_m0");
        thugsigns.itemsCount.add(1);
        shopItems.put("thugsigns", thugsigns);

        // Initialize boom
        ShopItemInfo boom = new ShopItemInfo();
        boom.items = new ArrayList<>();
        boom.itemsCount = new ArrayList<>();
        boom.items.add("boom_m0");
        boom.itemsCount.add(1);
        shopItems.put("boom", boom);

        // Initialize abstractmaze
        ShopItemInfo abstractmaze = new ShopItemInfo();
        abstractmaze.items = new ArrayList<>();
        abstractmaze.itemsCount = new ArrayList<>();
        abstractmaze.items.add("abstractmaze_m0");
        abstractmaze.itemsCount.add(1);
        shopItems.put("abstractmaze", abstractmaze);

        // Initialize infinity
        ShopItemInfo infinity = new ShopItemInfo();
        infinity.items = new ArrayList<>();
        infinity.itemsCount = new ArrayList<>();
        infinity.items.add("infinity_m0");
        infinity.itemsCount.add(1);
        shopItems.put("infinity", infinity);

        // Initialize poisonousvine
        ShopItemInfo poisonousvine = new ShopItemInfo();
        poisonousvine.items = new ArrayList<>();
        poisonousvine.itemsCount = new ArrayList<>();
        poisonousvine.items.add("poisonousvine_m0");
        poisonousvine.itemsCount.add(1);
        shopItems.put("poisonousvine", poisonousvine);

        // Initialize latex
        ShopItemInfo latex = new ShopItemInfo();
        latex.items = new ArrayList<>();
        latex.itemsCount = new ArrayList<>();
        latex.items.add("latex_m0");
        latex.itemsCount.add(1);
        shopItems.put("latex", latex);

        // Initialize drought
        ShopItemInfo drought = new ShopItemInfo();
        drought.items = new ArrayList<>();
        drought.itemsCount = new ArrayList<>();
        drought.items.add("drought_m0");
        drought.itemsCount.add(1);
        shopItems.put("drought", drought);
        
        // Initialize liquidmetal
        ShopItemInfo liquidmetal = new ShopItemInfo();
        liquidmetal.items = new ArrayList<>();
        liquidmetal.itemsCount = new ArrayList<>();
        liquidmetal.items.add("liquidmetal_m0");
        liquidmetal.itemsCount.add(1);
        shopItems.put("liquidmetal", liquidmetal);
        
        // Initialize arachnid
        ShopItemInfo arachnid = new ShopItemInfo();
        arachnid.items = new ArrayList<>();
        arachnid.itemsCount = new ArrayList<>();
        arachnid.items.add("arachnid_m0");
        arachnid.itemsCount.add(1);
        shopItems.put("arachnid", arachnid);
        
        // Initialize amber
        ShopItemInfo amber = new ShopItemInfo();
        amber.items = new ArrayList<>();
        amber.itemsCount = new ArrayList<>();
        amber.items.add("amber_m0");
        amber.itemsCount.add(1);
        shopItems.put("amber", amber);
        
        // Initialize neuron
        ShopItemInfo neuron = new ShopItemInfo();
        neuron.items = new ArrayList<>();
        neuron.itemsCount = new ArrayList<>();
        neuron.items.add("neuron_m0");
        neuron.itemsCount.add(1);
        shopItems.put("neuron", neuron);
        
        // Initialize lime
        ShopItemInfo lime = new ShopItemInfo();
        lime.items = new ArrayList<>();
        lime.itemsCount = new ArrayList<>();
        lime.items.add("lime_m0");
        lime.itemsCount.add(1);
        shopItems.put("lime", lime);
        
        // Initialize watercolor
        ShopItemInfo watercolor = new ShopItemInfo();
        watercolor.items = new ArrayList<>();
        watercolor.itemsCount = new ArrayList<>();
        watercolor.items.add("watercolor_m0");
        watercolor.itemsCount.add(1);
        shopItems.put("watercolor", watercolor);
        
        // Initialize mint
        ShopItemInfo mint = new ShopItemInfo();
        mint.items = new ArrayList<>();
        mint.itemsCount = new ArrayList<>();
        mint.items.add("mint_m0");
        mint.itemsCount.add(1);
        shopItems.put("mint", mint);
        
        // Initialize domino
        ShopItemInfo domino = new ShopItemInfo();
        domino.items = new ArrayList<>();
        domino.itemsCount = new ArrayList<>();
        domino.items.add("domino_m0");
        domino.itemsCount.add(1);
        shopItems.put("domino", domino);

        // Initialize battlepass
        ShopItemInfo battlepass = new ShopItemInfo();
        battlepass.items = new ArrayList<>();
        battlepass.itemsCount = new ArrayList<>();
        battlepass.items.add("battlepass");
        battlepass.itemsCount.add(1);
        shopItems.put("battlepass", battlepass);

        // Initialize offer1
        ShopItemInfo offer1 = new ShopItemInfo();
        offer1.items = new ArrayList<>();
        offer1.itemsCount = new ArrayList<>();
        offer1.items.add("recruit_m0");
        offer1.itemsCount.add(1);
        offer1.items.add("crystalls");
        offer1.itemsCount.add(25000);
        offer1.items.add("premium");
        offer1.itemsCount.add(3 * 86000);
        offer1.items.add("health_m0");
        offer1.itemsCount.add(100);
        offer1.items.add("armor_m0");
        offer1.itemsCount.add(100);
        offer1.items.add("double_damage_m0");
        offer1.itemsCount.add(100);
        offer1.items.add("n2o_m0");
        offer1.itemsCount.add(100);
        offer1.items.add("mine_m0");
        offer1.itemsCount.add(100);
        shopItems.put("offer1", offer1);

        // Initialize offer3
        ShopItemInfo offer2 = new ShopItemInfo();
        offer2.items = new ArrayList<>();
        offer2.itemsCount = new ArrayList<>();
        offer2.items.add("glow_m0");
        offer2.itemsCount.add(1);
        offer2.items.add("premium");
        offer2.itemsCount.add(30 * 86000);
        offer2.items.add("health_m0");
        offer2.itemsCount.add(300);
        offer2.items.add("armor_m0");
        offer2.itemsCount.add(300);
        offer2.items.add("double_damage_m0");
        offer2.itemsCount.add(300);
        offer2.items.add("n2o_m0");
        offer2.itemsCount.add(300);
        offer2.items.add("mine_m0");
        offer2.itemsCount.add(300);
        offer2.items.add("thunder_tx");
        offer2.itemsCount.add(1);
        offer2.items.add("viking_tx");
        offer2.itemsCount.add(1);
        shopItems.put("offer2", offer2);

        // Initialize offer3
        ShopItemInfo offer3 = new ShopItemInfo();
        offer3.items = new ArrayList<>();
        offer3.itemsCount = new ArrayList<>();
        offer3.items.add("glow_m0");
        offer3.itemsCount.add(1);
        offer3.items.add("premium");
        offer3.itemsCount.add(14 * 86000);
        offer3.items.add("health_m0");
        offer3.itemsCount.add(100);
        offer3.items.add("armor_m0");
        offer3.itemsCount.add(100);
        offer3.items.add("double_damage_m0");
        offer3.itemsCount.add(100);
        offer3.items.add("n2o_m0");
        offer3.itemsCount.add(100);
        offer3.items.add("mine_m0");
        offer3.itemsCount.add(100);
        offer3.items.add("wasp_xt");
        offer3.itemsCount.add(1);
        offer3.items.add("railgun_xt");
        offer3.itemsCount.add(1);
        shopItems.put("offer3", offer3);

        // Initialize offer4
        ShopItemInfo offer4 = new ShopItemInfo();
        offer4.items = new ArrayList<>();
        offer4.itemsCount = new ArrayList<>();
        offer4.items.add("glow_m0");
        offer4.itemsCount.add(1);
        offer4.items.add("premium");
        offer4.itemsCount.add(21 * 86000);
        offer4.items.add("health_m0");
        offer4.itemsCount.add(200);
        offer4.items.add("armor_m0");
        offer4.itemsCount.add(200);
        offer4.items.add("double_damage_m0");
        offer4.itemsCount.add(200);
        offer4.items.add("n2o_m0");
        offer4.itemsCount.add(200);
        offer4.items.add("mine_m0");
        offer4.itemsCount.add(200);
        offer4.items.add("mamont_lc");
        offer4.itemsCount.add(1);
        offer4.items.add("twins_lc");
        offer4.itemsCount.add(1);
        shopItems.put("offer4", offer4);

        // Initialize offer5
        ShopItemInfo offer5 = new ShopItemInfo();
        offer5.items = new ArrayList<>();
        offer5.itemsCount = new ArrayList<>();
        offer5.items.add("battlepass");
        offer5.itemsCount.add(1);
        offer5.items.add("premium");
        offer5.itemsCount.add(3 * 86000);
        shopItems.put("offer5", offer5);
        
        // Initialize offer6
        ShopItemInfo offer6 = new ShopItemInfo();
        offer6.items = new ArrayList<>();
        offer6.itemsCount = new ArrayList<>();
        offer6.items.add("crystalls");
        offer6.itemsCount.add(15000);
        offer6.items.add("premium");
        offer6.itemsCount.add(3 * 86000);
        offer6.items.add("dark_pumpkin_m0");
        offer6.itemsCount.add(1);
        shopItems.put("offer6", offer6);

        // Initialize offer7
        ShopItemInfo offer7 = new ShopItemInfo();
        offer7.items = new ArrayList<>();
        offer7.itemsCount = new ArrayList<>();
        offer7.items.add("premium");
        offer7.itemsCount.add(7 * 86000);
        offer7.items.add("health_m0");
        offer7.itemsCount.add(250);
        offer7.items.add("armor_m0");
        offer7.itemsCount.add(250);
        offer7.items.add("double_damage_m0");
        offer7.itemsCount.add(250);
        offer7.items.add("n2o_m0");
        offer7.itemsCount.add(250);
        offer7.items.add("mine_m0");
        offer7.itemsCount.add(250);
        offer6.items.add("scarecrow_rust_m0");
        offer6.itemsCount.add(1);
        shopItems.put("offer7", offer7);

        // Initialize offer8
        ShopItemInfo offer8 = new ShopItemInfo();
        offer8.items = new ArrayList<>();
        offer8.itemsCount = new ArrayList<>();
        offer8.items.add("crystalls");
        offer8.itemsCount.add(20000);
        offer8.items.add("container_0");
        offer8.itemsCount.add(5);
        offer6.items.add("spiders_web_m0");
        offer6.itemsCount.add(1);
        shopItems.put("offer8", offer8);
    }

    public void purchaseItem(LobbyManager lobby, String itemId) {
        GiveItemService giveItemService = GiveItemService.getInstance();

        ShopItemInfo purchasedItem = shopItems.get(itemId);
        for (int i = 0; i < purchasedItem.items.size(); i++) {
            String giveItemId = purchasedItem.items.get(i);
            int giveItemCount = purchasedItem.itemsCount.get(i);
            String jsonRequest = "{\"userId\":" + lobby.getLocalUser().getId() + ",\"itemId\":\"" + giveItemId
                    + "\",\"count\":" + giveItemCount + "}";
            giveItemService.onReceive(jsonRequest);
        }
    }
}
