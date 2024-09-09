package me.matl114.logitech;

import io.github.thebusybiscuit.slimefun4.api.SlimefunAddon;
import me.matl114.logitech.Depends.DependencyInfinity;
import me.matl114.logitech.Depends.DependencyNetwork;
import me.matl114.logitech.Listeners.ListenerManager;
import me.matl114.logitech.Schedule.PersistentEffects.CustomEffects;
import me.matl114.logitech.Schedule.PersistentEffects.RadiationRegion;
import me.matl114.logitech.Schedule.Schedules;
import me.matl114.logitech.SlimefunItem.AddDepends;
import me.matl114.logitech.SlimefunItem.AddGroups;
import me.matl114.logitech.SlimefunItem.AddItem;
import me.matl114.logitech.SlimefunItem.AddSlimefunItems;
import me.matl114.logitech.SlimefunItem.Blocks.MultiBlockTypes;
import me.matl114.logitech.SlimefunItem.Cargo.Storages;
import me.matl114.logitech.Utils.CraftUtils;
import me.matl114.logitech.Utils.DataCache;
import me.matl114.logitech.Utils.Debug;
import me.matl114.logitech.Utils.UtilClass.MultiBlockClass.MultiBlockService;
import net.guizhanss.guizhanlibplugin.updater.GuizhanUpdater;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import io.github.thebusybiscuit.slimefun4.libraries.dough.config.Config;

public class MyAddon extends JavaPlugin implements SlimefunAddon {
    public static boolean testmod=false;
    public static boolean testmode(){
        return testmod;
    }
    private static MyAddon instance;
    private static PluginManager manager;

    public static MyAddon getInstance() {
        return instance;
    }
    public static PluginManager getManager() {
        return manager;
    }
    public static String username;
    public static String repo;
    public static String branch;
    static{
        username="m1919810";
        repo="LogiTech";
        branch="master";
    }
    @Override
    public void onEnable() {
        instance =this;

        manager=getServer().getPluginManager();
        // 从 config.yml 中读取插件配置
        Config cfg = new Config(this);

        if (cfg.getBoolean("options.auto-update")) {
            tryUpdate();
        }
            // 你可以在这里添加自动更新功能
//        }if(cfg.getBoolean("options.test")||testmod) {
//            testmod = true;
//            Debug.logger("Addon is running on TEST MODE");
//        }
        ConfigLoader.load(this);
        Language.loadConfig(ConfigLoader.LANGUAGE);
        try{
            DependencyNetwork.init();
        }catch (Throwable e){
            Debug.logger("在加载软依赖 NETWORKS时出现错误! 出现版本不匹配的附属,禁用附属相应内容");
        }
        try{
            DependencyInfinity.init();
        }catch (Throwable e){
            Debug.logger("在加载软依赖 INFINITY时出现错误! 出现版本不匹配的附属,禁用附属相应内容");
        }
        Debug.logger("软依赖检测完毕");
        AddGroups.registerGroups(this);
        Debug.logger("物品组加载完毕");
        Debug.logger("自定义物品加载完毕");
        AddItem.registerItemStack();
        Debug.logger("物品模板加载完毕");
        AddSlimefunItems.registerSlimefunItems();
        Debug.logger("粘液物品注册完毕");
        //注册关于依赖的相关内容
        AddDepends.setup(this);
        Debug.logger("依赖注册完毕");
        Schedules.setupSchedules(this);
        Debug.logger("计划线程设立完毕");
        ListenerManager.registerListeners(getInstance(),getManager());
        Debug.logger("监听器注册完毕");
        //加载bs工具
        DataCache.setup();
        //注册存储类型
        Storages.setup();
        //注册多方块服务
        MultiBlockService.setup();
        //注册多方块类型
        MultiBlockTypes.setup();
        //加载自定义效果机制
        CustomEffects.setup();
        //加载辐射机制
        RadiationRegion.setup();
        //加载配方工具
        CraftUtils.setup();


        //注册
        Debug.logger("附属特性注册完毕");
    }
    public void tryUpdate() {
        if ( getDescription().getVersion().startsWith("Build")) {
            GuizhanUpdater.start(this, getFile(), username, repo, branch);
        }
    }
    @Override
    public void onDisable() {
        // 禁用插件的逻辑...
        Schedules.onDisableSchedules(this);
        Bukkit.getScheduler().cancelTasks(this);
    }

    @Override
    public String getBugTrackerURL() {
        // 你可以在这里返回你的问题追踪器的网址，而不是 null
        return null;
    }

    @Override
    public JavaPlugin getJavaPlugin() {
        /*
         * 你需要返回对你插件的引用。
         * 如果这是你插件的主类，只需要返回 "this" 即可。
         */
        return this;
    }

}
