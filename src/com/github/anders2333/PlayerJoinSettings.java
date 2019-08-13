package com.github.anders2333;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.ConsoleCommandSender;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerFormRespondedEvent;
import cn.nukkit.event.server.DataPacketReceiveEvent;
import cn.nukkit.form.response.FormResponse;
import cn.nukkit.form.response.FormResponseCustom;
import cn.nukkit.form.response.FormResponseModal;
import cn.nukkit.form.response.FormResponseSimple;
import cn.nukkit.network.protocol.SetLocalPlayerAsInitializedPacket;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.utils.Config;
import com.github.anders2333.lib.ui.ModalForm;

public class PlayerJoinSettings extends PluginBase implements Listener {

    private static int DATE = 20190813;

    @Override
    public void onLoad() {
        super.onLoad();
        saveDefaultConfig();
    }

    @Override
    public void onEnable() {
        super.onEnable();
        getServer().getPluginManager().registerEvents(this, this);
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }

    @Override
    public Config getConfig() {
        return super.getConfig();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equals("uiset")) {
            if (args.length > 0) {
                if (sender.hasPermission("PlayerJoinSettings.Command.UISet")) {
                    if (args.length >= 2){
                        switch (args[0]) {
                            case "title":
                                getConfig().set("标题内容", args[1]);
                                getConfig().save();
                                sender.sendMessage("成功将标题内容修改为" + args[1]);
                                break;
                            case "suntitle":
                                getConfig().set("公告内容", args[1]);
                                getConfig().save();
                                sender.sendMessage("成功将公告内容修改为" + args[1]);
                                break;
                            case "button1":
                                getConfig().set("按钮1显示", args[1]);
                                getConfig().save();
                                sender.sendMessage("成功将按钮1显示修改为" + args[1]);
                                break;
                            case "button2":
                                getConfig().set("按钮2显示", args[1]);
                                getConfig().save();
                                sender.sendMessage("成功将按钮2显示修改为" + args[1]);
                                break;
                            case "cmd1":
                                getConfig().set("按钮1命令", args[1]);
                                getConfig().save();
                                sender.sendMessage("成功将按钮1命令修改为" + args[1]);
                                break;
                            case "cmd2":
                                getConfig().set("按钮2命令", args[1]);
                                getConfig().save();
                                sender.sendMessage("成功将按钮2命令修改为" + args[1]);
                                break;
                            default:
                                sendHelp(sender);
                                break;
                        }
                    } else {
                        if (args[0].equals("switch")){
                            if (getConfig().getBoolean("UI总开关")){
                                getConfig().set("UI总开关", false);
                                getConfig().save();
                                sender.sendMessage("关闭进服提示");
                            } else {
                                getConfig().set("UI总开关", true);
                                getConfig().save();
                                sender.sendMessage("开启进服提示");
                            }
                        } else if (args[0].equals("reload")){
                                getConfig().reload();
                                sender.sendMessage("配置文件重载完成");
                        } else {
                            sendHelp(sender);
                        }
                    }
                } else {
                    sender.sendMessage("§c你没有权限使用这个命令");
                }
            } else {
                sendHelp(sender);
            }
        }
        return super.onCommand(sender, command, label, args);
    }
    
    public static void sendHelp(CommandSender sender){
        sender.sendMessage("§6=================§a[UI设置]§6=================");
        sender.sendMessage("§a/uiset§b title §d<标题内容>      §e设置标题内容");
        sender.sendMessage("§a/uiset§b subtitle §d<副标题内容> §e设置副标题内容");
        sender.sendMessage("§a/uiset§b button1 §d<按钮1内容>   §e设置按钮1内容");
        sender.sendMessage("§a/uiset§b button2 §d<按钮2内容>   §e设置按钮2内容");
        sender.sendMessage("§a/uiset§b cmd1 §d<命令1>          §e设置命令1内容");
        sender.sendMessage("§a/uiset§b cmd2 §d<命令2>          §e设置命令2内容");
        sender.sendMessage("§a/uiset§b reload                §e重新加载配置文件");
        sender.sendMessage("§a/uiset§b switch                §e管理进服提示开关");
        sender.sendMessage("§e使用 @p 作为点击按钮的玩家名称");
        sender.sendMessage("§6=================§a[UI设置]§6=================");
    }

    public void onPlayerJoin(Player player) {
        if (getConfig().getBoolean("UI总开关")) new ModalForm(DATE, getConfig().getString("标题内容"), getConfig().getString("公告内容"), getConfig().getString("按钮1显示"), getConfig().getString("按钮2显示")).sendPlayer(player);
    }

    public void onPlayerFormResponded(PlayerFormRespondedEvent event) {
        FormResponse data = event.getResponse();
        int ID = event.getFormID();
        Player player = event.getPlayer();
        if (player == null || event.wasClosed() || event.getResponse() == null
                || (!(event.getResponse() instanceof FormResponseCustom)
                && !(event.getResponse() instanceof FormResponseSimple)
                && !(event.getResponse() instanceof FormResponseModal)))
            return;
        if (ID == DATE) {
            int ClickedButtonId = ((FormResponseModal) data).getClickedButtonId();
            if (ClickedButtonId ==  0){
                Server.getInstance().getCommandMap().dispatch(new ConsoleCommandSender(), getConfig().getString("按钮1命令").replace("@p", player.getName()));
            } else {
                Server.getInstance().getCommandMap().dispatch(new ConsoleCommandSender(), getConfig().getString("按钮2命令").replace("@p", player.getName()));
            }
        }
    }

    @EventHandler
    public void onDataPacketReceive(DataPacketReceiveEvent event) {
        if (event.getPacket() instanceof SetLocalPlayerAsInitializedPacket) onPlayerJoin(event.getPlayer());
    }
}
