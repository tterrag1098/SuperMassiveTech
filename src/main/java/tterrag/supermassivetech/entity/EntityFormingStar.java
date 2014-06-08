package tterrag.supermassivetech.entity;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import tterrag.supermassivetech.util.BlockCoord;

public class EntityFormingStar extends Entity
{
    private List<BlockCoord> fire;
    private int life;
    
    public EntityFormingStar(World world)
    {
        super(world);
        this.renderDistanceWeight *= 4;
        this.ignoreFrustumCheck = true;
    }

    @Override
    protected void entityInit()
    {
    }
    
    @Override
    public void onUpdate()
    {
        if (life < 1000)
            life++;
        else
            this.setDead();
        
        super.onUpdate();
    }

    @Override
    protected void readEntityFromNBT(NBTTagCompound tag)
    {
        if (fire != null)
            fire.clear();
        else
            fire = new ArrayList<BlockCoord>();
        
        NBTTagList fires = tag.getTagList("blockCoords", Constants.NBT.TAG_COMPOUND);
        
        for (int i = 0; i < fires.tagCount(); i++)
        {
            fire.add(BlockCoord.readFromNBT(fires.getCompoundTagAt(i)));
        }
    }

    @Override
    protected void writeEntityToNBT(NBTTagCompound tag)
    {
        NBTTagList fires = new NBTTagList();
        
        for (BlockCoord b : fire)
        {
            NBTTagCompound block = new NBTTagCompound();
            b.writeToNBT(block);
            fires.appendTag(block);
        }
        
        tag.setTag("blockCoords", fires);
    }
    
    @Override
    public boolean shouldRenderInPass(int pass)
    {
        return pass > 0;
    }
    
    
}
