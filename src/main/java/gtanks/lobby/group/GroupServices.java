package gtanks.lobby.group;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import gtanks.StringUtils;
import gtanks.commands.Type;
import gtanks.lobby.LobbyManager;
import gtanks.services.LobbysServices;

public class GroupServices {

    private static GroupServices instance;

    // Map where key is group owner's username, and value is a list of LobbyManagers (the group members)
    public Map<String, List<LobbyManager>> groups = new HashMap<>();

    public static GroupServices getInstance() {
        if (instance == null) {
            instance = new GroupServices();
        }
        return instance;
    }

    public void userJoinGroup(LobbyManager userLobby, String groupUsername) {
        // If the group exists, add the userLobby to the group's list of LobbyManagers
        List<LobbyManager> group = groups.get(groupUsername);
        if (group == null) {
            // If group doesn't exist, create a new one and add the userLobby
            group = new ArrayList<>();
            groups.put(groupUsername, group);
        }

        // Check if group has more than 3 users and handle accordingly
        if (group.size() >= 3) {
            // Custom logic for handling when a group has more than 3 users
            userLobby.send(Type.LOBBY, "group_full");
            return;
        }

        group.add(userLobby);

        // Notify the group owner about the new user joining
        LobbyManager groupOwner = LobbysServices.getInstance().getLobbyByNick(groupUsername);
        String userweapon = StringUtils.concatStrings(userLobby.getLocalUser().getGarage().mountTurret.name.localizatedString(userLobby.getLocalUser().getLocalization()), "_", String.valueOf(userLobby.getLocalUser().getGarage().mountTurret.modificationIndex));
        String userarmor = StringUtils.concatStrings(userLobby.getLocalUser().getGarage().mountHull.name.localizatedString(userLobby.getLocalUser().getLocalization()), "_", String.valueOf(userLobby.getLocalUser().getGarage().mountHull.modificationIndex));
        groupOwner.send(Type.LOBBY, "user_join_group", userLobby.getLocalUser().getNickname(), String.valueOf(userLobby.getLocalUser().getRang() + 1), userweapon, userarmor);

        // Notify all other group members
        for (LobbyManager lobbyInGroup : group) {
            if(lobbyInGroup != userLobby){
                String lobbyweapon = StringUtils.concatStrings(lobbyInGroup.getLocalUser().getGarage().mountTurret.name.localizatedString(lobbyInGroup.getLocalUser().getLocalization()), "_", String.valueOf(lobbyInGroup.getLocalUser().getGarage().mountTurret.modificationIndex));
                String lobbyarmor = StringUtils.concatStrings(lobbyInGroup.getLocalUser().getGarage().mountHull.name.localizatedString(lobbyInGroup.getLocalUser().getLocalization()), "_", String.valueOf(lobbyInGroup.getLocalUser().getGarage().mountHull.modificationIndex));
                lobbyInGroup.send(Type.LOBBY, "user_join_group", userLobby.getLocalUser().getNickname(), String.valueOf(userLobby.getLocalUser().getRang() + 1), lobbyweapon, lobbyarmor);
            }
        }


        JSONArray groupUsersArray = new JSONArray();

        // Add group owner to JSON first
        JSONObject ownerJson = new JSONObject();
        ownerJson.put("username", groupOwner.getLocalUser().getNickname());
        ownerJson.put("rank", groupOwner.getLocalUser().getRang() + 1);
        String ownerweapon = StringUtils.concatStrings(groupOwner.getLocalUser().getGarage().mountTurret.name.localizatedString(groupOwner.getLocalUser().getLocalization()), "_", String.valueOf(groupOwner.getLocalUser().getGarage().mountTurret.modificationIndex));
        String ownerarmor = StringUtils.concatStrings(groupOwner.getLocalUser().getGarage().mountHull.name.localizatedString(groupOwner.getLocalUser().getLocalization()), "_", String.valueOf(groupOwner.getLocalUser().getGarage().mountHull.modificationIndex));
        ownerJson.put("weapon", ownerweapon);
        ownerJson.put("armor", ownerarmor);
        groupUsersArray.put(ownerJson);

        // Add all group members to the JSON
        for (LobbyManager lobbyInGroup : group) {
            JSONObject userJson = new JSONObject();
            userJson.put("username", lobbyInGroup.getLocalUser().getNickname());
            userJson.put("rank", lobbyInGroup.getLocalUser().getRang() + 1);
            String weapon = StringUtils.concatStrings(lobbyInGroup.getLocalUser().getGarage().mountTurret.name.localizatedString(lobbyInGroup.getLocalUser().getLocalization()), "_", String.valueOf(lobbyInGroup.getLocalUser().getGarage().mountTurret.modificationIndex));
            String armor = StringUtils.concatStrings(lobbyInGroup.getLocalUser().getGarage().mountHull.name.localizatedString(lobbyInGroup.getLocalUser().getLocalization()), "_", String.valueOf(lobbyInGroup.getLocalUser().getGarage().mountHull.modificationIndex));
            userJson.put("weapon", weapon);
            userJson.put("armor", armor);
            groupUsersArray.put(userJson);
        }

        // Convert the JSON array to a string
        String jsonGroupUsers = groupUsersArray.toString();

        // Send the group user info back to the user
        userLobby.send(Type.LOBBY, "join_group;" + jsonGroupUsers);
    }

    public void userLeaveGroup(LobbyManager userLobby) {
        String userLeavesName = userLobby.getLocalUser().getNickname();
        // Find the group where the user is present
        String groupOwnerUsername = null;
        List<LobbyManager> groupToRemoveFrom = null;

        // Search for the group that contains the userLobby
        for (Map.Entry<String, List<LobbyManager>> entry : groups.entrySet()) {
            if (entry.getValue().contains(userLobby)) {
                groupOwnerUsername = entry.getKey();
                groupToRemoveFrom = entry.getValue();
                break;
            }
        }
        for (Map.Entry<String, List<LobbyManager>> entry : groups.entrySet()) {
            if (entry.getKey().equals(userLeavesName)) {
                groupOwnerUsername = userLeavesName;
                groupToRemoveFrom = entry.getValue();
                break;
            }
        }

        if (groupOwnerUsername != null) {
            if (groupOwnerUsername.equals(userLeavesName)) {
                // If the user is the group owner, notify all group members about group deletion
                for (LobbyManager lobbyInGroup : groupToRemoveFrom) {
                    lobbyInGroup.send(Type.LOBBY, "delete_group");
                }
                // Remove the group from the map
                groups.remove(groupOwnerUsername);
            } else {
                // Remove the user from the group
                groupToRemoveFrom.remove(userLobby);

                // Notify all group members about the user leaving
                for (LobbyManager lobbyInGroup : groupToRemoveFrom) {
                    lobbyInGroup.send(Type.LOBBY, "user_leave_group", userLeavesName);
                }

                // Notify the group owner about the user leaving
                LobbyManager groupOwner = LobbysServices.getInstance().getLobbyByNick(groupOwnerUsername);
                groupOwner.send(Type.LOBBY, "user_leave_group", userLeavesName);
            }
        }
    }

    public void kickFromGroup(LobbyManager groupOwner, String kickedUserNickname) {
        // check if groupOwner.getLocalUser().getNickname() is owner of any group
        // if true continue with
        LobbysServices.getInstance().getLobbyByNick(kickedUserNickname).send(Type.LOBBY, "kicked_from_group");
    }

    public void inviteUserToGroup(LobbyManager userLobby, String invitedUser) {
        LobbyManager invitedUserLobby = LobbysServices.getInstance().getLobbyByNick(invitedUser);
        String weapon = StringUtils.concatStrings(userLobby.getLocalUser().getGarage().mountTurret.name.localizatedString(userLobby.getLocalUser().getLocalization()), "_", String.valueOf(userLobby.getLocalUser().getGarage().mountTurret.modificationIndex));
        String armor = StringUtils.concatStrings(userLobby.getLocalUser().getGarage().mountHull.name.localizatedString(userLobby.getLocalUser().getLocalization()), "_", String.valueOf(userLobby.getLocalUser().getGarage().mountHull.modificationIndex));
        invitedUserLobby.send(Type.LOBBY, "invitation_to_group", userLobby.getLocalUser().getNickname(),
        String.valueOf(userLobby.getLocalUser().getRang() + 1), weapon, armor);
    }

    public void userDeclineInvitation(LobbyManager userLobby, String groupOwner) {
        LobbyManager groupOwnerLobby = LobbysServices.getInstance().getLobbyByNick(groupOwner);
        groupOwnerLobby.send(Type.LOBBY, "decline_invitation", userLobby.getLocalUser().getNickname(), String.valueOf(userLobby.getLocalUser().getRang() + 1));
    }

}
