/*
 * Decompiled with CFR 0.150.
 */
package gtanks.main;

import gtanks.RankUtils;
import gtanks.battles.maps.MapsLoaderService;
import gtanks.battles.tanks.loaders.HullsFactory;
import gtanks.battles.tanks.loaders.WeaponsFactory;
import gtanks.challenges.GetChallengeInfoService;
import gtanks.challenges.UpdateStarsService;
import gtanks.containers.OpenContainerService;
import gtanks.containers.PopulateContainerWindowService;
import gtanks.groups.UserGroupsLoader;
import gtanks.kafka.KafkaTemplateService;
import gtanks.lobby.LobbyManager;
import gtanks.lobby.shop.GiveItemService;
import gtanks.logger.RemoteDatabaseLogger;
import gtanks.main.netty.NettyService;

import gtanks.services.AutoEntryServices;
import gtanks.services.ShopServiceFactory;
import gtanks.services.StripeShopServices;
import gtanks.services.hibernate.HibernateService;
import gtanks.system.SystemBattlesHandler;
import gtanks.system.dailybonus.DailyBonusService;
import gtanks.system.quartz.impl.QuartzServiceImpl;
import gtanks.test.server.configuration.ConfigurationsLoader;
import gtanks.users.garage.GarageItemsLoader;
import gtanks.users.garage.items.kit.loader.KitsLoader;
import gtanks.users.missions.challenges.ChallengesFactory;
import gtanks.users.missions.daily.DailyMissionFactory;
import gtanks.users.missions.weekly.WeeklyMissionFactory;

import java.io.IOException;

import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.json.simple.parser.ParseException;

public class Main {
    public static void main(String[] args) {
        try {
            ConfigurationsLoader.load("");
            Main.initFactorys();
            UserGroupsLoader.load("groups/");
            Session session = HibernateService.getSessionFactory().getCurrentSession();
            session.beginTransaction();
            SQLQuery query = session.createSQLQuery("SET NAMES 'utf8' COLLATE 'utf8_general_ci';");
            session.getTransaction().commit();
            QuartzServiceImpl.getInstance();
            DailyBonusService.getInstance();
            AutoEntryServices.getInstance();
            NettyService.getInstance().init();
            GiveItemService.getInstance();
            GetChallengeInfoService.getInstance();
            PopulateContainerWindowService.getInstance();
            OpenContainerService.getInstance();
            ShopServiceFactory.instance().init();
            //KafkaTemplateService.getInstance();
            UpdateStarsService.getInstance();
            SystemBattlesHandler.systemBattlesInit();

        } catch (Exception ex) {
            ex.printStackTrace();
            RemoteDatabaseLogger.error(ex);
        }
    }

    private static void initFactorys() throws IOException, ParseException {
        GarageItemsLoader.getInstance()
                .loadFromConfig("turrets.json",
                        "hulls.json",
                        "colormaps.json",
                        "inventory.json",
                        "modules.json");
        WeaponsFactory.getInstance().init("weapons/");
        HullsFactory.getInstance().init("hulls/");
        KitsLoader.load("config/kits/kits.json");
        DailyMissionFactory.instance().load("dailyMissions.json");
        WeeklyMissionFactory.instance().load("weeklyMissions.json");
        ChallengesFactory.instance().load("challenges.json");
        RankUtils.init();
        MapsLoaderService.initFactoryMaps();

        StripeShopServices.instance().init();
    }
}
