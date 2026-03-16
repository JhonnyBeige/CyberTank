package gtanks.services;

import com.stripe.Stripe;
import com.stripe.model.Event;
import com.stripe.model.EventDataObjectDeserializer;
import com.stripe.model.StripeObject;
import com.stripe.model.checkout.Session;
import com.stripe.net.ApiResource;
import com.stripe.net.Webhook;
import com.stripe.param.checkout.SessionRetrieveParams;

import gtanks.json.JSONUtils;
import gtanks.lobby.LobbyManager;
import gtanks.system.SystemBattlesHandler;
import spark.Request;
import spark.Response;
import static spark.Spark.*;

import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

public class StripeShopServices {
    private static final StripeShopServices INSTANCE = new StripeShopServices();

    public static StripeShopServices instance() {
        return INSTANCE;
    }

    private static final String STRIPE_API_KEY = "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";
    private static final String WEBHOOK_SECRET = "we_1PwkB1ICbYo3OxpDVXt6El6k";

    public void init() {
        Stripe.apiKey = STRIPE_API_KEY;

        port(4242); // Set the port for the server

        post("/webhook", this::handleWebhook);

        System.out.println("Payment Server started on port 4242");
    }

    private Object handleWebhook(Request request, Response response) {
        String payload = request.body();
        String sigHeader = request.headers("Stripe-Signature");

        Event event;
        System.out.println(payload);

        // try {
        //     event = Webhook.constructEvent(payload, sigHeader, WEBHOOK_SECRET);
        //     //event = ApiResource.GSON.fromJson(payload, Event.class);
        // } catch (Exception e) {
        //     response.status(400);
        //     System.out.println(e.getMessage());
        //     return "Webhook error: " + e.getMessage();
        // }

        //String eventType = event.getType();
        try{
            String playerUsername = null;
            String jsonResultString = payload;//dataObjectDeserializer.getRawJson();
            JSONObject jsonObject = new JSONObject(jsonResultString);
            String eventType = jsonObject.getString("type");
            if ("checkout.session.completed".equals(eventType)) {
                //EventDataObjectDeserializer dataObjectDeserializer = event.getDataObjectDeserializer();
                JSONArray customFields = jsonObject.getJSONObject("data").getJSONObject("object").getJSONArray("custom_fields");
                for (int i = 0; i < customFields.length(); i++) {
                    JSONObject field = customFields.getJSONObject(i);
                    if (field.getString("key").equals("youringamenickname")) {
                        playerUsername = field.getJSONObject("text").optString("value", null);
                    }
                }
                LobbyManager lobby = LobbysServices.getInstance().getLobbyByNick(playerUsername);
                System.out.println(playerUsername + " checkout session is: "+ lobby.getLocalUser().getCheckoutSession());
                handlePurchase(lobby, lobby.getLocalUser().getCheckoutSession());
            }else{
                System.out.println(eventType);
            }
        }catch(Exception e){
            e.printStackTrace();
        }

        response.status(200);
        return "Webhook received successfully";
    }

    private void handlePurchase(LobbyManager lobby, String session) {
        ShopServiceFactory.instance().purchaseItem(lobby, session);
    }
}