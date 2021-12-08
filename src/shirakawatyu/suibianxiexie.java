package shirakawatyu;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;


class ThreadSend extends Thread {
    @Override
    public void run() {
        try {
            File ip = new File("." + File.separator + "plugins" + File.separator + "Chat2QQ" + File.separator + "ip");
            FileReader readip = new FileReader(ip);
            int tt = 0;
            String address = "";
            while((tt = readip.read()) != -1){
                address += (char)tt;
            }
            Socket client = new Socket(address,23333);
            PrintStream prt = new PrintStream(client.getOutputStream());
            File temp = new File("." + File.separator + "plugins" + File.separator + "Chat2QQ" + File.separator + "temp");
            String time = String.valueOf(temp.lastModified());
            while(true){
                if(!time.equals(String.valueOf(temp.lastModified()))){
                    Thread.sleep(100);
                    String str = "";
                    BufferedReader read = new BufferedReader(new FileReader(temp));
                    str = read.readLine();
                    prt.println(str);
                    time = String.valueOf(temp.lastModified());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

public class suibianxiexie  extends JavaPlugin implements org.bukkit.event.Listener {
    @Override
    public void onEnable(){
        Bukkit.getPluginManager().registerEvents(this, this);
        File temp = new File("." + File.separator + "plugins" + File.separator + "Chat2QQ" + File.separator + "temp");
        if(!temp.getParentFile().exists()){
            temp.getParentFile().mkdirs();
        }
        File ip = new File("." + File.separator + "plugins" + File.separator + "Chat2QQ" + File.separator + "ip");
        if(!ip.exists()){
            try {
                ip.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            getLogger().info("*******首次使用请在./plguin/Chat2QQ/ip文件中填写IP地址，然后重启服务端*******");
        }
        ThreadSend t1 = new ThreadSend();
        t1.start();
        getLogger().info("Trying to Connect...");
    }
    @Override
    public void onDisable(){
        File temp = new File("." + File.separator + "Chat2QQ" + File.separator + "temp");
        if(temp.getParentFile().exists()){
            temp.getParentFile().delete();
        }
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent chat) throws Exception{
        String name = String.valueOf(chat.getPlayer()).trim();
        String str = name + " " + chat.getMessage().trim();
        File temp = new File("." + File.separator + "plugins" + File.separator + "Chat2QQ" + File.separator + "temp");
        Writer write = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(temp),"GBK"));
        write.write(str);
        write.flush();
        write.close();
    }
}
