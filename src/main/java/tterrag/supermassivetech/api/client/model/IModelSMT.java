package tterrag.supermassivetech.api.client.model;

import net.minecraft.client.model.ModelRenderer;

public interface IModelSMT
{
    public void render(float size);

    public void setRotation(ModelRenderer model, float x, float y, float z);
}
