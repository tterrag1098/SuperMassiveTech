package tterrag.supermassivetech.common.tile;

import tterrag.supermassivetech.ModProps;
import tterrag.supermassivetech.common.util.Utils;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.DamageSource;
import net.minecraft.util.IChatComponent;

public class DamageSourceBlackHole extends DamageSource
{
    public DamageSourceBlackHole(String name)
    {
        super(name);
    }

    private static final String DEATH_MSG_BASE = "death.blackHole.";
    private static final int DEATH_MSG_COUNT;
    static
    {
        int num = 1;
        String text = DEATH_MSG_BASE + num;
        while (!Utils.localize(text, true).equals(ModProps.LOCALIZING + "." + text))
        {
            num++;
            text = text.replace("" + (num - 1), "" + num);
        }
        
        DEATH_MSG_COUNT = num - 1;
    }

    @Override
    public boolean isDamageAbsolute()
    {
        return true;
    }
    
    @Override
    public boolean isUnblockable()
    {
        return true;
    }

    @Override
    public IChatComponent func_151519_b(EntityLivingBase entity)
    {
        if (entity instanceof EntityPlayer)
        {
            int num = entity.worldObj.rand.nextInt(DEATH_MSG_COUNT) + 1;
            String text = String.format(Utils.localize(DEATH_MSG_BASE + num, true), ((EntityPlayer) entity).getCommandSenderName());
            return new ChatComponentText(text);
        }
        else
        {
            return null;
        }
    }
}
