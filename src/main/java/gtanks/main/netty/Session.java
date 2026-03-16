/*
 * Decompiled with CFR 0.150.
 */
package gtanks.main.netty;

import gtanks.StringUtils;
import gtanks.auth.AuthService;
import gtanks.commands.Command;
import gtanks.commands.Commands;
import gtanks.commands.Type;
import gtanks.lobby.LobbyManager;
import gtanks.logger.LogType;
import gtanks.logger.LoggerService;
import gtanks.system.SystemClientMessagesHandler;
import lombok.Getter;
import lombok.SneakyThrows;

import org.eclipse.jetty.util.thread.ScheduledExecutorScheduler;
import org.jboss.netty.channel.Channel;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.net.SocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

public class Session {
    public static final AuthService authService = AuthService.getInstance();
    private static final LoggerService loggerService = LoggerService.getInstance();
    private static final NettyUsersHandlerController nettyUsersHandlerController = NettyUsersHandlerController
            .getInstance();
    private static final SystemClientMessagesHandler systemClientMessagesHandler = SystemClientMessagesHandler
            .getInstance();
    private static final String encryptionKey = "084B255737229811CF454AF2AE99B20E";
    private static final String encryptionIv = "D8BF3DF78364B5CC";
    private static final int MAX_PACKAGE_COUNT_PS = 10;//Integer.parseInt(System.getenv("MAX_PACKAGE_COUNT_PS"));
    public LobbyManager lobby;
    private Channel channel;
    private Map<String, Object> sessionData = new HashMap<>();
    // count pacakge in second
    private int countPackageLastSec = 0;
    @Getter
    private long totalPackageCount = 0;

    public int encryptionEnabled = -1;

    public Session(Channel channel) {
        this.channel = channel;
    }

    public void resetCountPackage() {
        this.countPackageLastSec = 0;
    }

    public void decryptProtocol(String request) {
        if(encryptionEnabled == -1){
            encryptionEnabled = 0;
        }
        if (!request.contains("move;")) {
            // loggerService.log(LogType.INFO,
            //         this.getIP() + " send request: " + request + " " + this.getParam("userId"));
            this.countPackageLastSec++;
            if (this.countPackageLastSec > MAX_PACKAGE_COUNT_PS) {
                loggerService.log(LogType.WARNING,
                        "Session DDOS detected " + this.countPackageLastSec + " " + this.getIP());
                this.closeConnection();
                return;
            }
        }
        Command cmd = Commands.decrypt(request);
        totalPackageCount++;

        switch (cmd.type) {
            case AUTH, PING, REGISTRATON: {
                authService.executeCommand(cmd, this);
                break;
            }
            case CHAT, BATTLE, GARAGE, LOBBY_CHAT, LOBBY: {
                this.lobby.executeCommand(cmd);
                break;
            }
            case HTTP: {
                break;
            }
            case SYSTEM: {
                systemClientMessagesHandler.executeCommand(cmd, this);
                authService.executeCommand(cmd, this);
                if (this.lobby == null)
                    break;
                this.lobby.executeCommand(cmd);
                break;
            }
            case UNKNOWN: {
                loggerService.log(LogType.INFO, "User " + this.channel.toString() +
                        " send unknowed request: " + cmd);
            }
        }
    }

    private StringBuffer inputRequest;
    private StringBuffer badRequest = new StringBuffer();

    public void decryptProtocol(String protocol, int secondaryKey) {
        if(secondaryKey == 0 && encryptionEnabled == -1){
            encryptionEnabled = 1;
        }
        StringBuffer inputRequest;
        this.inputRequest = inputRequest = new StringBuffer(protocol);
        if (inputRequest.length() > 0) {
            if (!this.inputRequest.toString().endsWith("end~")) {
                this.badRequest = new StringBuffer(StringUtils.concatStrings(new String[]{this.badRequest.toString(), this.inputRequest.toString()}));
            } else {
                this.inputRequest = new StringBuffer(StringUtils.concatStrings(new String[]{this.badRequest.toString(), this.inputRequest.toString()}));
                String[] cryptRequests = this.parseCryptRequests();
                int length = cryptRequests.length;

                for(int i = 0; i < length; ++i) {
                    String request = cryptRequests[i];
                    int key;
                    try {
                        key = Integer.parseInt(String.valueOf(request.charAt(0)));
                    } catch (Exception var10) {
                        this.closeConnection();
                        return;
                    }

                    if(key == 0){
                        this.sendRequestToManagers(request.substring(1));
                        return;
                    }
                }
                this.badRequest = new StringBuffer();
            }
        }
    }

    private void sendRequestToManagers(String request) {
        try {
            this.sendCommandToManagers(Commands.decrypt(request));
        } catch (Exception var6) {
            this.closeConnection();
        }
    }

    private void sendCommandToManagers(Command cmd) {
        switch (cmd.type) {
            case AUTH, PING, REGISTRATON: {
                authService.executeCommand(cmd, this);
                break;
            }
            case CHAT, BATTLE, GARAGE, LOBBY_CHAT, LOBBY: {
                this.lobby.executeCommand(cmd);
                break;
            }
            case HTTP: {
                break;
            }
            case SYSTEM: {
                systemClientMessagesHandler.executeCommand(cmd, this);
                authService.executeCommand(cmd, this);
                if (this.lobby == null)
                    break;
                this.lobby.executeCommand(cmd);
                break;
            }
            case UNKNOWN: {
                loggerService.log(LogType.INFO, "User " + this.channel.toString() +
                        " send unknowed request: " + cmd);
            }
        }
    }

    private String[] parseCryptRequests() {
        return this.inputRequest.toString().split("end~");
    }

    public boolean send(Type type, String... args) throws IOException {
        StringBuilder request = new StringBuilder();
        request.append(type.toString());
        request.append(";");
        for (int i = 0; i < args.length - 1; ++i) {
            request.append(StringUtils.concatStrings(args[i], ";"));
        }
        request.append(args[args.length - 1]);
        if (this.channel.isWritable() && this.channel.isConnected() && this.channel.isOpen()) {
            if(encryptionEnabled == 0){
                this.channel.write(encode(request.toString(), encryptionKey, Base64.getEncoder().encodeToString(encryptionIv.getBytes())));
            }else{
                //request.append(StringUtils.concatStrings(new String[]{args[args.length - 1], "end~"}));
                request.append("end~");
                this.channel.write(request.toString());
            }
        }
        request = null;
        return true;
    }

    @SneakyThrows
    private String encode(String message, String key, String initVector) {
        IvParameterSpec iv = new IvParameterSpec(Base64.getDecoder().decode(initVector));
        SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes("UTF-8"), "AES");

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);

        byte[] original = cipher.doFinal(message.getBytes(StandardCharsets.UTF_8));

        return Base64.getEncoder().encodeToString(original);
    }

    protected void onDisconnect() {
        if (this.lobby != null) {
            this.lobby.onDisconnect();
        }
    }

    public void closeConnection() {
        this.channel.close();
    }

    public String getIP() {
        SocketAddress remoteAddress = this.channel.getRemoteAddress();
        return remoteAddress.toString();
    }

    public <T> T getParam(String key) {
        try {
            return (T) sessionData.get(key);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void setParam(String key, Object value) {
        sessionData.put(key, value);
    }

    public void identify(long id) {
        nettyUsersHandlerController.authorizeSession(this, id);
        this.setParam("userId", id);
    }

    protected Channel getChannel() {
        return this.channel;
    }
}
