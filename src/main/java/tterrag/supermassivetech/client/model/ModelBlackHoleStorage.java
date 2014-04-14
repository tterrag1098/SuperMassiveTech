package tterrag.supermassivetech.client.model;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelBlackHoleStorage extends ModelBase implements ModelSMT
{
    ModelRenderer top, bottom;
    ModelRenderer core;
    List<ModelRenderer> crossbars = new ArrayList<ModelRenderer>();
    List<ModelRenderer> spikes = new ArrayList<ModelRenderer>();

    public ModelBlackHoleStorage()
    {
        textureWidth = 128;
        textureHeight = 64;

        top = new ModelRenderer(this);
        top.addBox(-8F, -8F, -8F, 16, 1, 16);
        top.setRotationPoint(0F, 16F, 0F);
        setRotation(top, 0F, 0F, 0F);

        bottom = new ModelRenderer(this);
        bottom.addBox(-8F, -8F, -8F, 16, 1, 16);
        bottom.setRotationPoint(0F, 16F, 0F);
        setRotation(bottom, 0, 0, (float) Math.PI);

        core = new ModelRenderer(this);
        core.setTextureOffset(0, 38);
        core.addBox(0F, 0F, 0F, 10, 15, 10);
        core.setRotationPoint(-5F, 8.5F, -5F);
        setRotation(core, 0F, 0F, 0F);

        for (int i = 0; i < 4; i++)
        {
            ModelRenderer spike = new ModelRenderer(this);
            spike.setTextureOffset(0, 17);
            spike.addBox(-0.5F, 0.5F, -0.5F, 1, 10, 1);
            spike.setRotationPoint(7.2F, 16F, 0F);
            setRotation(spike, (float) (((Math.PI / 2) * i) + (Math.PI / 4)), 0F, 0F);
            spikes.add(spike);
        }
        for (int i = 0; i < 4; i++)
        {
            ModelRenderer spike = new ModelRenderer(this);
            spike.setTextureOffset(0, 17);
            spike.addBox(-0.5F, 0.5F, -0.5F, 1, 10, 1);
            spike.setRotationPoint(-7.2F, 16F, 0F);
            setRotation(spike, (float) (((Math.PI / 2) * i) + (Math.PI / 4)), 0F, 0F);
            spikes.add(spike);
        }
        for (int i = 0; i < 4; i++)
        {
            ModelRenderer spike = new ModelRenderer(this);
            spike.setTextureOffset(0, 17);
            spike.addBox(-0.5F, 0.5F, -0.5F, 1, 10, 1);
            spike.setRotationPoint(0F, 16F, 7.2F);
            setRotation(spike, 0F, 0F, (float) (((Math.PI / 2) * i) + (Math.PI / 4)));
            spikes.add(spike);
        }
        for (int i = 0; i < 4; i++)
        {
            ModelRenderer spike = new ModelRenderer(this);
            spike.setTextureOffset(0, 17);
            spike.addBox(-0.5F, 0.5F, -0.5F, 1, 10, 1);
            spike.setRotationPoint(0F, 16F, -7.2F);
            setRotation(spike, 0F, 0F, (float) (((Math.PI / 2) * i) + (Math.PI / 4)));
            spikes.add(spike);
        }

        ModelRenderer crossbar = new ModelRenderer(this);
        crossbar.setTextureOffset(0, 17);
        crossbar.addBox(-2.5F, -2.5F, -8F, 5, 5, 16);
        crossbar.setRotationPoint(0F, 16F, 0F);
        crossbars.add(crossbar);

        ModelRenderer crossbar2 = new ModelRenderer(this);
        crossbar2.setTextureOffset(0, 17);
        crossbar2.addBox(-2.5F, -2.5F, -8F, 5, 5, 16);
        crossbar2.setRotationPoint(0F, 16F, 0F);
        setRotation(crossbar2, 0F, (float) (Math.PI / 2), 0F);
        crossbars.add(crossbar2);
    }

    public void render(float size)
    {
        top.render(size);
        bottom.render(size);

        core.render(size);

        for (ModelRenderer mr : crossbars)
            mr.render(size);

        for (ModelRenderer mr : spikes)
            mr.render(size);
    }

    public void setRotation(ModelRenderer model, float x, float y, float z)
    {
        model.rotateAngleX = x;
        model.rotateAngleY = y;
        model.rotateAngleZ = z;
    }

    @Override
    public void setRotationAngles(float f1, float f2, float f3, float f4, float f5, float f6, Entity entity)
    {
        super.setRotationAngles(f1, f2, f3, f4, f5, f6, entity);
    }
}
