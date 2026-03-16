package gtanks.users.missions.weekly;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.List;
import java.util.Random;

import gtanks.battles.BattlefieldPlayerController;
import gtanks.lobby.shop.GiveItemService;
import gtanks.logger.RemoteDatabaseLogger;
import gtanks.main.database.DatabaseManager;
import gtanks.main.database.impl.DatabaseManagerImpl;
import gtanks.services.LobbysServices;
import gtanks.services.annotations.ServicesInject;
import gtanks.services.hibernate.HibernateService;
import gtanks.users.User;
import gtanks.users.karma.Karma;

public class WeeklyMissionsServices {
    private static final WeeklyMissionsServices INSTANCE = new WeeklyMissionsServices();

    @ServicesInject(target = DatabaseManagerImpl.class)
    private DatabaseManager database = DatabaseManagerImpl.instance();

    public static WeeklyMissionsServices instance() {
        return INSTANCE;
    }

    public String parseUserMissions(User user) {
        WeeklyMissionUser userMissions = getUserMissions(user.getId());
        WeeklyMissionFactory weeklyMissionFactory = WeeklyMissionFactory.instance();
        WeeklyMissionInfo mission1 = weeklyMissionFactory.getMissionById(userMissions.missionId1);
        Integer mission1Progr = userMissions.missionProgr1;
        WeeklyMissionInfo mission2 = weeklyMissionFactory.getMissionById(userMissions.missionId2);
        Integer mission2Progr = userMissions.missionProgr2;
        WeeklyMissionInfo mission3 = weeklyMissionFactory.getMissionById(userMissions.missionId3);
        Integer mission3Progr = userMissions.missionProgr3;

        JSONObject quests = new JSONObject();
        quests.put("changeCost", userMissions.changePrice);

        JSONObject quest1 = new JSONObject();
        quest1.put("description", mission1.description);
        quest1.put("id", mission1.id);
        quest1.put("target_progress", mission1.target_progress);
        quest1.put("progress", userMissions.missionProgr1);

        JSONArray quest1Prizes = new JSONArray();
        for (String prizeString : mission1.prizes) {
            quest1Prizes.add(prizeString);
        }
        quest1.put("prizes", quest1Prizes);
        quests.put("quest1", quest1);

        JSONObject quest2 = new JSONObject();
        quest2.put("description", mission2.description);
        quest2.put("id", mission2.id);
        quest2.put("target_progress", mission2.target_progress);
        quest2.put("progress", userMissions.missionProgr2);

        JSONArray quest2Prizes = new JSONArray();
        for (String prizeString : mission2.prizes) {
            quest2Prizes.add(prizeString);
        }
        quest2.put("prizes", quest2Prizes);
        quests.put("quest2", quest2);

        JSONObject quest3 = new JSONObject();
        quest3.put("description", mission3.description);
        quest3.put("id", mission3.id);
        quest3.put("target_progress", mission3.target_progress);
        quest3.put("progress", userMissions.missionProgr3);

        JSONArray quest3Prizes = new JSONArray();
        for (String prizeString : mission3.prizes) {
            quest3Prizes.add(prizeString);
        }
        quest3.put("prizes", quest3Prizes);
        quests.put("quest3", quest3);

        return quests.toString();
    }

    public WeeklyMissionUser getUserMissions(Long userId) {
        Transaction tx = null;
        WeeklyMissionUser result;
        try (Session session = HibernateService.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            Query query = session.createQuery("FROM WeeklyMissionUser UC WHERE UC.userId = :userId");
            query.setParameter("userId", userId);
            result = (WeeklyMissionUser) query.uniqueResult();
            tx.commit();
            return result;
        } catch (Exception e) {
            try{
                if (tx != null && tx.isActive()) {
                    tx.rollback();
                }
            }catch(IllegalStateException ex){}
            RemoteDatabaseLogger.error(e);
            throw new RuntimeException("Error while getting user missions", e);
        }
    }

    public void userJoin(User user) {
        WeeklyMissionUser userMissions = getUserMissions(user.getId());

        if (userMissions == null) {
            WeeklyMissionFactory missionFactory = WeeklyMissionFactory.instance();
            userMissions = new WeeklyMissionUser();
            userMissions.userId = user.getId();
            userMissions.missionId1 = missionFactory.getRandomMission();
            userMissions.missionProgr1 = 0;
            userMissions.missionId2 = "complete_daily";
            userMissions.missionProgr2 = 0;
            userMissions.missionId3 = "complete_daily1";
            userMissions.missionProgr3 = 0;
            userMissions.changePrice = 0;

            // Save the user missions to the database
            saveUserMissions(userMissions);
        }
    }

    public void userRefresh(User user) {
        userJoin(user);

        WeeklyMissionUser userMissions = getUserMissions(user.getId());
        WeeklyMissionFactory missionFactory = WeeklyMissionFactory.instance();

        if (userMissions.missionProgr1 == -1) {
            userMissions.missionId1 = getNewMissionId(userMissions.missionId2, userMissions.missionId3, userMissions.missionId1);
            userMissions.missionProgr1 = 0;
        }
        if (userMissions.missionProgr2 == -1) {
            userMissions.missionId2 = "complete_daily";
            userMissions.missionProgr2 = 0;
        }
        if (userMissions.missionProgr3 == -1) {
            userMissions.missionId3 = "complete_daily1";
            userMissions.missionProgr3 = 0;
        }

        userMissions.changePrice = 0;

        // Update the user missions in the database
        saveUserMissions(userMissions);
    }

    public void claimMission(User user, int idPanel) {
        GiveItemService giveItemService = GiveItemService.getInstance();
        long giveUserId = user.getId();
        WeeklyMissionFactory missionFactory = WeeklyMissionFactory.instance();
        WeeklyMissionUser userMissions = getUserMissions(user.getId());

        WeeklyMissionInfo missionInfo = null;
        switch (idPanel) {
            case 1:
                if(userMissions.missionProgr1 == -1){
                    return;
                }
                missionInfo = missionFactory.getMissionById(userMissions.missionId1);
                userMissions.missionProgr1 = -1;
                break;
            case 2:
                if(userMissions.missionProgr2 == -1){
                    return;
                }
                missionInfo = missionFactory.getMissionById(userMissions.missionId2);
                userMissions.missionProgr2 = -1;
                break;
            case 3:
                if(userMissions.missionProgr3 == -1){
                    return;
                }
                missionInfo = missionFactory.getMissionById(userMissions.missionId3);
                userMissions.missionProgr3 = -1;
                break;
            default:
                throw new IllegalArgumentException("Invalid mission panel ID");
        }

        if (missionInfo != null) {
            for (int i = 0; i < missionInfo.prizesId.size(); i++) {
                String giveItemId = missionInfo.prizesId.get(i);
                int giveItemCount = missionInfo.prizesCount.get(i);
                String jsonRequest = "{\"userId\":" + giveUserId + ",\"itemId\":\"" + giveItemId + "\",\"count\":" + giveItemCount + "}";
                giveItemService.onReceive(jsonRequest);
            }
        }

        // Update the user missions in the database
        saveUserMissions(userMissions);
    }

    public void gainScore(User user, int count) {
        updateMissionProgress(user, "gainScore", count);
    }

    public void takeGold(User user, int count) {
        updateMissionProgress(user, "takeBonusGold", count);
    }

    public void takeBonus(User user, int count) {
        updateMissionProgress(user, "takeBonus", count);
    }

    public void doDamage(User user, int count) {
        updateMissionProgress(user, "damage", count);
    }

    public void destroyTank(User user, int count) {
        updateMissionProgress(user, "killTank", count);
    }

    public void captureFlag(User user, int count) {
        updateMissionProgress(user, "captureTheFlag", count);
    }

    public void returnFlag(User user, int count) {
        updateMissionProgress(user, "flag_return", count);
    }

    public void winCrystalls(User user, int count) {
        updateMissionProgress(user, "win_cry", count);
    }

    public void firstPlace(User user, int count) {
        updateMissionProgress(user, "first_place", count);
    }

    public void completeDailyMission(User user, int count) {
        updateMissionProgress(user, "complete_daily", count);
        updateMissionProgress(user, "complete_daily1", count);
    }

    private void updateMissionProgress(User user, String missionId, int count) {
        if(LobbysServices.getInstance().getLobbyByUser(user).battle != null){
            if(LobbysServices.getInstance().getLobbyByUser(user).battle.battle.players.size() < 4 ||
                LobbysServices.getInstance().getLobbyByUser(user).battle.battle.battleInfo.battleFormat == 4){
                return;
            }
        }
        
        WeeklyMissionUser userMissions = getUserMissions(user.getId());
        WeeklyMissionFactory missionFactory = WeeklyMissionFactory.instance();

        if (userMissions.missionId1.equals(missionId) && userMissions.missionProgr1 != -1) {
            userMissions.missionProgr1 += count;
            if (userMissions.missionProgr1 > missionFactory.getMissionById(missionId).target_progress) {
                userMissions.missionProgr1 = missionFactory.getMissionById(missionId).target_progress;
            }
        }

        if (userMissions.missionId2.equals(missionId) && userMissions.missionProgr2 != -1) {
            userMissions.missionProgr2 += count;
            if (userMissions.missionProgr2 > missionFactory.getMissionById(missionId).target_progress) {
                userMissions.missionProgr2 = missionFactory.getMissionById(missionId).target_progress;
            }
        }

        if (userMissions.missionId3.equals(missionId) && userMissions.missionProgr3 != -1) {
            userMissions.missionProgr3 += count;
            if (userMissions.missionProgr3 > missionFactory.getMissionById(missionId).target_progress) {
                userMissions.missionProgr3 = missionFactory.getMissionById(missionId).target_progress;
            }
        }

        // Update the user missions in the database
        saveUserMissions(userMissions);
    }

    private void saveUserMissions(WeeklyMissionUser userMissions) {
        Transaction tx = null;
        try (Session session = HibernateService.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.saveOrUpdate(userMissions);
            tx.commit();
        } catch (Exception e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            RemoteDatabaseLogger.error(e);
            throw new RuntimeException("Error while saving user missions", e);
        }
    }

    private String getNewMissionId(String missionId1, String missionId2, String oldMissionId) {
        WeeklyMissionFactory missionFactory = WeeklyMissionFactory.instance();
        String newMissionId = missionFactory.getRandomMission();
        while(newMissionId.equals(missionId1) || newMissionId.equals(missionId2) || newMissionId.equals(oldMissionId)){
            newMissionId = missionFactory.getRandomMission();
        }
        return newMissionId;
    }
}

