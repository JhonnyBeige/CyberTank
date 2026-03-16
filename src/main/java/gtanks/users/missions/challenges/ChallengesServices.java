package gtanks.users.missions.challenges;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import gtanks.commands.Type;
import gtanks.lobby.LobbyManager;
import gtanks.lobby.shop.GiveItemService;
import gtanks.logger.RemoteDatabaseLogger;
import gtanks.main.database.impl.DatabaseManagerImpl;
import gtanks.services.LobbysServices;
import gtanks.services.hibernate.HibernateService;
import gtanks.users.User;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class ChallengesServices {
    private static ChallengesServices INSTANCE = new ChallengesServices();

    public static ChallengesServices instance() {
        return INSTANCE;
    }

    // Parses the user's challenge data and returns it as a JSON string
    public String parseUserChallenges(User user) {
        ChallengesUser userChallengesInfo = getUserChallenges(user.getId());
        JSONObject battlePass = new JSONObject();

        // Add stars and BattlePass status to the response
        battlePass.put("stars", userChallengesInfo.getStars());
        battlePass.put("isBattlePassActive", userChallengesInfo.getBattlePass().toString());

        // Calculate remaining time in minutes
        LocalDate endDate = LocalDate.parse(ChallengesFactory.instance().endingDate, DateTimeFormatter.ISO_DATE);
        long leftMinutes = Duration.between(LocalDate.now().atStartOfDay(), endDate.atStartOfDay()).toMinutes();
        battlePass.put("leftMinutes", leftMinutes);

        // Create an array to hold the tiers
        JSONArray tiers = new JSONArray();

        // Iterate through the challenge tiers
        for (ChallengeTierInfo challenge : ChallengesFactory.instance().challengeInfos) {
            JSONObject tier = new JSONObject();
            tier.put("stars", challenge.stars);

            // Base reward details
            JSONObject base = new JSONObject();
            base.put("itemId", challenge.baseId);
            base.put("itemName", challenge.baseName);
            base.put("count", String.valueOf(challenge.baseCount)); // Ensuring the count is in quotes
            tier.put("base", base);

            // BattlePass reward details
            JSONObject battlePassItem = new JSONObject();
            battlePassItem.put("itemId", challenge.battlePassId);
            battlePassItem.put("itemName", challenge.battlePassName);
            battlePassItem.put("count", challenge.battlePassCount); // Integer value as it is
            tier.put("battlePass", battlePassItem);

            // Add tier to the tiers array
            tiers.add(tier);
        }

        // Add the tiers array to the response
        battlePass.put("tiers", tiers);

        // Return the generated JSON string
        return battlePass.toJSONString();
    }

    // Fetches the user's challenges information from the database, creates one if it doesn't exist
    public ChallengesUser getUserChallenges(Long userId) {
        Transaction tx = null;
        ChallengesUser result;
        try (Session session = HibernateService.getSessionFactory().openSession()) {
            tx = session.beginTransaction();

            // Check if the user already has challenges info
            Query query = session.createQuery("FROM ChallengesUser UC WHERE UC.userId = :userId");
            query.setParameter("userId", userId);
            result = (ChallengesUser) query.uniqueResult();

            // If no record exists, create a new one with default values
            if (result == null) {
                result = new ChallengesUser();
                result.setUserId(userId);
                result.setStars(0);  // Default stars to 0
                result.setBattlePass(false);  // Default battlePass to false
                result.setScore(0);
                session.save(result);  // Save new entry
            }

            tx.commit();
            return result;
        } catch (Exception e) {
            try {
                if (tx != null && tx.isActive()) {
                    tx.rollback();
                }
            }catch(Exception ex){}
            RemoteDatabaseLogger.error(e);
            throw new RuntimeException("Error while getting or creating user challenges", e);
        }
    }

    // Assigns a BattlePass to the user and gives them all rewards up to their current stars
    public void giveUserBattlePass(Long userId) {
        Transaction tx = null;
        try (Session session = HibernateService.getSessionFactory().openSession()) {
            tx = session.beginTransaction();

            // Ensure user challenge info exists, creating it if necessary
            ChallengesUser user = getUserChallenges(userId);

            // If the user hasn't bought the BattlePass, activate it
            if (!user.getBattlePass()) {
                user.setBattlePass(true); // Activate the battle pass
                session.update(user);

                // Distribute BattlePass rewards based on user's current stars
                distributeBattlePassRewards(user);
            }

            tx.commit();
        } catch (Exception e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            RemoteDatabaseLogger.error(e);
            throw new RuntimeException("Error while giving user battle pass", e);
        }
    }

    public void initUser(User user) {
        //fetch stars of user
        LobbyManager userLobby = LobbysServices.getInstance().getLobbyByUser(user);
        ChallengesUser userInfo = getUserChallenges(userLobby.getLocalUser().getId());
        userLobby.send(Type.LOBBY, "stars_count", String.valueOf(userInfo.stars));
    }

    public void userEarnScore(User user, int earnedScore) {
        ChallengesUser userChallenges = getUserChallenges(user.getId());

        int currentScore = userChallenges.getScore();
        int newScore = currentScore + earnedScore;
        userChallenges.setScore(newScore);

        updateUserInDatabase(userChallenges);

        int starsToAdd = (newScore / 85) - (currentScore / 85);
        if (starsToAdd > 0) {
            addStarsToUser(user, starsToAdd);
        }
    }

    // Method to give rewards to the user
    public void addStarsToUser(User user, int stars) {
        Long userId = user.getId();
        ChallengesUser userChallenges = getUserChallenges(userId);

        int currentStars = userChallenges.getStars();
        int newStars = currentStars + stars;
        userChallenges.setStars(newStars);

        // Check for rewards
        int currTier = 0;
        for (ChallengeTierInfo tier : ChallengesFactory.instance().challengeInfos) {
            currTier++;
            if (currentStars < tier.stars && newStars >= tier.stars) {
                // Give base reward
                giveItemToUser(user, tier.baseId, tier.baseCount);

                // If user has BattlePass, give BattlePass reward
                if (userChallenges.getBattlePass()) {
                    giveItemToUser(user, tier.battlePassId, tier.battlePassCount);
                }

                LobbysServices.getInstance().getLobbyByUser(user).send(Type.LOBBY, "tier_done", String.valueOf(currTier));
            }
        }

        // Update user progress in the database
        LobbysServices.getInstance().getLobbyByUser(user).send(Type.LOBBY, "stars_count", String.valueOf(newStars));
        updateUserInDatabase(userChallenges);
    }

    public void resetBattlePass(){
        Transaction tx = null;
        try (Session session = HibernateService.getSessionFactory().openSession()) {
            tx = session.beginTransaction();

            String hql = "UPDATE ChallengesUser SET score = 0, stars = 0, battlePass = false";
            Query query = session.createQuery(hql);
            int updatedEntities = query.executeUpdate();

            tx.commit();
            System.out.println("Battle Pass reset for " + updatedEntities + " users.");
        } catch (Exception e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            RemoteDatabaseLogger.error(e);
            throw new RuntimeException("Error while resetting Battle Pass", e);
        }
    }

    // Updates user data in the database
    private void updateUserInDatabase(ChallengesUser user) {
        Transaction tx = null;
        try (Session session = HibernateService.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.update(user);
            tx.commit();
        } catch (Exception e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            RemoteDatabaseLogger.error(e);
        }
    }

    // Method to give items to the user
    public void giveItemToUser(User user, String itemId, int itemCount) {
        if(itemId.equals("supplies_m0")){
            GiveItemService giveItemService = GiveItemService.getInstance();
            long giveUserId = user.getId();
            String jsonRequest0 = "{\"userId\":" + giveUserId + ",\"itemId\":\"health_m0\",\"count\":" + itemCount + "}";
            giveItemService.onReceive(jsonRequest0);
            String jsonRequest1 = "{\"userId\":" + giveUserId + ",\"itemId\":\"armor_m0\",\"count\":" + itemCount + "}";
            giveItemService.onReceive(jsonRequest1);
            String jsonRequest2 = "{\"userId\":" + giveUserId + ",\"itemId\":\"double_damage_m0\",\"count\":" + itemCount + "}";
            giveItemService.onReceive(jsonRequest2);
            String jsonRequest3 = "{\"userId\":" + giveUserId + ",\"itemId\":\"n2o_m0\",\"count\":" + itemCount + "}";
            giveItemService.onReceive(jsonRequest3);
            String jsonRequest4 = "{\"userId\":" + giveUserId + ",\"itemId\":\"mine_m0\",\"count\":" + itemCount + "}";
            giveItemService.onReceive(jsonRequest4);
            return;
        }
        if(itemId.equals("premium")){
            itemCount *= 86000;
        }
        GiveItemService giveItemService = GiveItemService.getInstance();
        long giveUserId = user.getId();
        String jsonRequest = "{\"userId\":" + giveUserId + ",\"itemId\":\"" + itemId + "\",\"count\":" + itemCount + "}";
        giveItemService.onReceive(jsonRequest);
    }

    // Method to distribute BattlePass rewards based on user stars
    private void distributeBattlePassRewards(ChallengesUser user) {
        int userStars = user.getStars();
        User actualUser = getUserById(user.getUserId());
        for (ChallengeTierInfo tier : ChallengesFactory.instance().challengeInfos) {
            if (userStars >= tier.stars) {
                giveItemToUser(actualUser, tier.battlePassId, tier.battlePassCount);
            }
        }
    }

    private User getUserById(Long userId) {
        return DatabaseManagerImpl.instance().getUserById(userId);
    }
}
