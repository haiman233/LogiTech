package me.matl114.logitech.Depends;

import io.github.sefiraat.networks.network.stackcaches.QuantumCache;
import io.github.sefiraat.networks.utils.Keys;
import io.github.sefiraat.networks.utils.datatypes.DataTypeMethods;
import io.github.sefiraat.networks.utils.datatypes.PersistentQuantumStorageType;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.libraries.commons.lang.NotImplementedException;
import me.matl114.logitech.SlimefunItem.AddDepends;
import me.matl114.logitech.Utils.*;
import me.matl114.logitech.Utils.UtilClass.StorageClass.StorageType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class NetworksAdaptQuantumStorage extends StorageType {
    public NetworksAdaptQuantumStorage() {
        super();
    }
    @Override
    public boolean isStorage(ItemMeta meta) {
        return  (DataTypeMethods.hasCustom(meta, Keys.QUANTUM_STORAGE_INSTANCE, PersistentQuantumStorageType.TYPE));
    }
    //TODO 继续完成对网络存储的反射方法表 脱离依赖
    @Override
    public boolean canStorage(ItemMeta meta) {
        if(AddDepends.NETWORKSQUANTUMSTORAGE==null){
            return false;
        }
        String id=CraftUtils.parseSfId(meta);
        if(id==null){
            return false;
        }
        return AddDepends.NETWORKSQUANTUMSTORAGE.isInstance(SlimefunItem.getById(id));
    }

    @Override
    public boolean canStorage(SlimefunItem item) {
        if(AddDepends.NETWORKSQUANTUMSTORAGE==null){
            return false;
        }
        return AddDepends.NETWORKSQUANTUMSTORAGE.isInstance(item);
    }
    public QuantumCache getQuantumCache(ItemMeta meta) {
       QuantumCache cache= DataTypeMethods.getCustom(meta, AddDepends.NTWQUANTUMKEY, PersistentQuantumStorageType.TYPE);
       if(cache==null){
       }
       return cache;
    }
    @Override
    public int getStorageMaxSize(ItemMeta meta) {
        QuantumCache cache= getQuantumCache(meta);
        if(cache==null){return 0;}
        Method amount=NetWorkQuantumMethod. getLimitMethod(cache);
        try{
            return (Integer)amount.invoke(cache);
        }catch (Throwable e){
            return 0;
        }
    }

    @Override
    public void setStorage(ItemMeta meta, ItemStack content) {
//        DataTypeMethods.setCustom(meta, Keys.QUANTUM_STORAGE_INSTANCE, PersistentQuantumStorageType.TYPE, cache);
//        cache.addMetaLore(itemMeta);
//        itemToDrop.setItemMeta(itemMeta);
        throw new NotImplementedException("NetworkQuantumStorage's content shouldn't be set in this method");
    }

    @Override
    public void setStorageAmount(ItemMeta meta, int amount) {
        QuantumCache cache=getQuantumCache(meta);
        if(cache==null){return;}
        Method set=NetWorkQuantumMethod.getSetAmountMethod(cache);
        try{
            set.invoke(cache, amount);
        }catch (Throwable e){
            return ;
        }
        DataTypeMethods.setCustom(meta,Keys.QUANTUM_STORAGE_INSTANCE, PersistentQuantumStorageType.TYPE,cache);
    }

    @Override
    public int getStorageAmount(ItemMeta meta) {
        QuantumCache cache= getQuantumCache(meta);
        if(cache==null){return 0;}
        Method amount=NetWorkQuantumMethod. getAmountMethod(cache);
        try{
            Object res= amount.invoke(cache);
            return res instanceof Long r? MathUtils.fromLong(r) : (Integer) res;
        }catch (Throwable e){
            return 0;
        }
    }

    @Override
    public ItemStack getStorageContent(ItemMeta meta) {
        QuantumCache cache= getQuantumCache(meta);
        if(cache==null){
            return null;}
        Method getItem=NetWorkQuantumMethod.getItemStackMethod(cache);
        try{
               return (ItemStack) getItem.invoke(cache);
        }catch (Throwable e){
            return null;
        }
    }

    @Override
    public void clearStorage(ItemMeta meta) {
        throw new NotImplementedException("NetworkQuantumStorage's content shouldn't be cleared in this method");
    }

    @Override
    public void updateStorageDisplay(ItemMeta meta, ItemStack item, int amount) {
        var cache=getQuantumCache(meta);
        if(cache==null){return;}
        try{
            Method set=NetWorkQuantumMethod.getSetItemStackMethod(cache);
            set.invoke(cache, item);
            set=NetWorkQuantumMethod.getSetAmountMethod(cache);
            set.invoke(cache, amount);
            set=NetWorkQuantumMethod.getUpdateMetaLore(cache);
            set.invoke(cache, meta);
        }catch (Throwable e){
        }

    }

    @Override
    public void updateStorageAmountDisplay(ItemMeta meta, int amount) {
        var cache=getQuantumCache(meta);
        if(cache==null){return;}
        Method set;
        try{
            set=NetWorkQuantumMethod.getSetAmountMethod(cache);
            set.invoke(cache, amount);
            set=NetWorkQuantumMethod.getUpdateMetaLore(cache);
            set.invoke(cache, meta);
        }catch (Throwable e){
            Debug.debug("failed updateStorageDisplay");
        }
    }
}
