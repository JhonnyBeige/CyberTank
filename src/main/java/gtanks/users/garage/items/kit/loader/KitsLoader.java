//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package gtanks.users.garage.items.kit.loader;

import gtanks.system.localization.strings.LocalizedString;
import gtanks.system.localization.strings.StringsLocalizationBundle;
import gtanks.users.garage.GarageItemsLoader;
import gtanks.users.garage.enums.ItemType;
import gtanks.users.garage.items.Item;
import gtanks.users.garage.items.PropertyItem;
import gtanks.users.garage.items.kit.Kit;
import gtanks.users.garage.items.kit.KitItem;
import gtanks.users.garage.items.kit.KitItemType;
import gtanks.users.garage.items.modification.ModificationInfo;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class KitsLoader {
    public static final Map<String, Kit> kits = new HashMap();
    public static final Map<String, Item> kit = new HashMap();

    public KitsLoader() {
    }

    public static int load(String config) throws FileNotFoundException, IOException, ParseException {
        File file = new File(config);
        JSONParser parser = new JSONParser();
        JSONObject json = (JSONObject)parser.parse(new FileReader(file));
        Iterator var4 = ((JSONArray)json.get("kits")).iterator();

        int sew;
        for(sew = GarageItemsLoader.getInstance().items.size(); var4.hasNext(); ++sew) {
            Object _kit = var4.next();
            JSONObject kit = (JSONObject)_kit;
            LocalizedString description = StringsLocalizationBundle.registerString((String)kit.get("description_ru"), (String)kit.get("description_en"));
            LocalizedString name = StringsLocalizationBundle.registerString((String)kit.get("name"), (String)kit.get("name"));
            String kitId = (String)kit.get("kit_id");
            ArrayList<KitItem> kitItems = new ArrayList();
            Iterator var9 = ((JSONArray)kit.get("items")).iterator();

            while(var9.hasNext()) {
                Object _item = var9.next();
                JSONObject item = (JSONObject)_item;
                KitItemType type = KitItemType.valueOf((String)item.get("type"));
                String itemId = (String)item.get("item_id");
                int count = (int)(long)item.get("count");
                int modif = (int)(long)item.get("modif");
                kitItems.add(new KitItem(type, itemId, count, modif));
            }

            int pr = (int)(long)kit.get("price");
            int r = (int)(long)kit.get("ranki");
            ModificationInfo[] var30 = new ModificationInfo[4];
            var30[0] = new ModificationInfo(kitId + "_m0", pr, r);
            var30[0].propertys = null;
            var30[1] = new ModificationInfo(kitId + "_m0", pr, r);
            var30[1].propertys = null;
            var30[2] = new ModificationInfo(kitId + "_m0", pr, r);
            var30[2].propertys = null;
            var30[3] = new ModificationInfo(kitId + "_m0", pr, r);
            var30[3].propertys = null;
            kits.put(kitId, new Kit(kitItems, pr));
            Item jju = new Item(kitId, description, true, sew, (PropertyItem[])null, ItemType.KIT, 0, name, (PropertyItem[])null, pr + 1, r + 1, pr, r, var30, false, 0, 0, 0, 0);
            kit.put(kitId, jju);
            GarageItemsLoader.getInstance().items.put(kitId, jju);
        }

        return sew;
    }
}