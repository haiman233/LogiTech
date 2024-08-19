package me.matl114.logitech;


import me.matl114.logitech.Utils.UtilClass.CargoClass.CargoConfigs;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

public class Tests {
    public static void log(String message) {
        System.out.println("Test: "+message);
    }


    //@Test
    public void test_init(){
        TestStack[] testStacks = new TestStack[]{
                new TestStack(1),
                new TestStack(2),
                new TestStack(3),
        };
        long a =System.nanoTime();
        TestCounter is=null;
        for(int i=0;i<3;++i){
            is=new TestCounter (testStacks[i]);
        }
        long b =System.nanoTime();

        log("time cost "+(b-a)+" ns");
        long c =System.nanoTime();
        TestCounter is2=null;
        for(int i=0;i<3;++i){
            is2=is.clone();
            is2.amount=testStacks[i].getAmount();
            is2.dirty=false;
            is2.it=testStacks[i];
        }
        long d =System.nanoTime();

        log("time cost "+(d-c)+" ns");
        log("check amount "+is2.amount);
        log("check if equal "+((is2==is)?1:0));
        TestConsumer s=new TestConsumer(testStacks[1]);
        TestConsumer s2=s.clone();
        log("check amounnt"+s2.amount);
        s.amount=10;
        log("check if equal "+s2.amount);
        TestSlotPusher itp=new TestSlotPusher(testStacks[1],3);
        TestSlotPusher itp2=itp.clone();
        itp2.init(testStacks[2],5);
        log(itp2.amount+" "+itp2.maxCnt+" "+itp2.slot);
        //ItemStack
    }
    @Test
    public void test_configCode(){
       boolean symm=false;
       boolean isnull=true;
       boolean lazy=false;
       boolean bklst=true;
       boolean fromInput=true;
       boolean toOutput=false;
       boolean reverse=false;
       int trans=647;
       int configCode= CargoConfigs.setAllConfig(symm,isnull,lazy,bklst,fromInput,toOutput,reverse,trans);

       log("[TEST CONFIG CODE] "+configCode);
       assert CargoConfigs.IS_SYMM.getConfig(configCode)==symm;
       assert CargoConfigs.IS_NULL.getConfig(configCode)==isnull;
       assert CargoConfigs.IS_LAZY.getConfig(configCode)==lazy;
       assert CargoConfigs.TO_OUTPUT.getConfig(configCode)==toOutput;
       assert CargoConfigs.REVERSE.getConfig(configCode)==reverse;
       assert CargoConfigs.IS_WHITELST.getConfig(configCode)==bklst;
       assert CargoConfigs.FROM_INPUT.getConfig(configCode)==fromInput;
       assert CargoConfigs.TRANSLIMIT.getConfigInt(configCode)==trans;
       log("[TEST CONFIG CODE] TEST SUCCESS");
        ArrayList<Integer> a=new ArrayList<>(){{
            for(int i=0;i<16;++i){
                add(i);
            }
        }};
        log(a.size()+"");
        int size=a.size();
        a.subList(size-8,size).clear();
        log(a.size()+"");
    }

}
