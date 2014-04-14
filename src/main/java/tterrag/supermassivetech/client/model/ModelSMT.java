package tterrag.supermassivetech.client.model;

import net.minecraft.client.model.ModelRenderer;

public interface ModelSMT
{
    public void render(float size);

    public void setRotation(ModelRenderer model, float x, float y, float z);
}
